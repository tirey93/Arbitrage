package Common;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonReader;

import Markets.Market;

public class MarketCalculator {
	private Double toSpend = 0.02;
	public ArrayList<ArbiLine2> arbiLines = new ArrayList<>();
	public ArrayList<String> numerators;
	private String denominator;
	public MarketCalculator
	(ArrayList<Market> markets, String denominator) throws InterruptedException {
		numerators = downloadCoins(markets);
		this.denominator = denominator;
		
	}
	public ArrayList<String> downloadCoins(ArrayList<Market> markets){
		ArrayList<String> numerators = new ArrayList<>();
		for(Market market : markets) {
			Enumeration<String> keys1 = market.currenciesInfo.keys();
			while(keys1.hasMoreElements()) {
				String key = keys1.nextElement();
				if(!numerators.contains(key)) {
					int i = 0;
					for(Market m : markets) {
						if(m.currenciesInfo.get(key) != null) {
								i++;
						}
					}
					if(i > 1) {
						numerators.add(key);
						//dodac sprawdzanie czy disabled
					}
				}
			}
		}
		return numerators;
	}
	public void mainLoop
	(ArrayList<Market> markets,
			Logger logger) throws InterruptedException {
		downloadOrders(markets, denominator);
		for(Market market : markets) {
			market.sortOrders(denominator);
		}

		JsonClass json = downloadJson("MyLogFile.json");
		
		//PrintWriter out = new PrintWriter("filename.txt");
  
		
		for(String coin : numerators) {
			for(Market marketBid : markets) {
				for(Market marketAsk : markets) { 
					if(!marketAsk.name.equals(marketBid.name) && isCoinEnabled(coin, marketAsk, marketBid)) {
						Double[] result = calcProfitNet(coin, marketBid, marketAsk);
						Double totalROI = result[0];
						Double sumToBuy = result[1];
						Double sumToSell = result[2];
						//out.println("Z: " + marketAsk.name + " Na: " + marketBid.name);
						//if(!totalROI.equals(0.0))
						
						ArbiLine line = new ArbiLine(coin, denominator, marketAsk.name, marketBid.name);
						
						if(json.hasArbiLine(line)) {
							Transaction transaction = new Transaction(totalROI, sumToSell, sumToBuy);
							json.getArbiLines().get(line.hashCode()).addTransaction(transaction);
						}
						else {
							if(totalROI > 0.01) {
								Transaction transaction = new Transaction(totalROI, sumToSell, sumToBuy);
								json.addArbiLine(line, transaction);
			 				}
						}
					}
				}
			}
			
		}
		
		uploadJson(json, "MyLogFile.json");
		
	}
	private void uploadJson(JsonClass json, String path) {
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		createJson(path, gson, json);
	}
	private JsonClass downloadJson(String path) {
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		try {
			return getJson(gson, path);
		} catch (FileNotFoundException e) {
			JsonClass json = new JsonClass();
			createJson(path, gson, json);
			try {
				return getJson(gson, path);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	private JsonClass getJson(Gson gson, String path) throws FileNotFoundException {
		JsonReader reader = new JsonReader(new FileReader(path));
		return gson.fromJson(reader, JsonClass.class);
	}
	private void createJson(String path, Gson gson, JsonClass json) {
		
		try {
			//System.out.println(gson.toJson(json));
			try (FileWriter writer = new FileWriter(path)) {
			    gson.toJson(json, writer);
			}
		} catch (JsonIOException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	private void displayPositiveProfit(Logger logger, String coin, Market marketBid, Market marketAsk,
			Double totalROI) {
		logger.info("-------!\n");
		ArbiLine2 current = new ArbiLine2(System.currentTimeMillis(), coin, marketAsk.name, marketBid.name);
		current.rois.add(totalROI);
		if(!containsArbiLine(current, totalROI)) {
			arbiLines.add(current);
		}
		else {
			System.out.println(coin + " " + totalROI);
		}
	}
	private Double[] calcProfitNet(String coin, Market marketBid, Market marketAsk) {
		Double totalROI = 0.0;
		Double sumToSell = 0.0;
		Double sumToBuy = 0.0;
		try {
			ArrayList<Order> asks = marketAsk.prices.get(coin + "-" + denominator).asks;
			ArrayList<Order> bids = marketBid.prices.get(coin + "-" + denominator).bids;

			int[] result = findLowestPositions(asks, bids);
			int pivotAsk = result[0];
			int pivotBid = result[1];
			if(pivotAsk + pivotBid > 0) {
					Double[] result2 = computeMarketValues(coin, asks, bids, pivotAsk, pivotBid);
					sumToSell = result2[0];
					sumToBuy = result2[1];
					Double profitGross = sumToBuy - sumToSell;
					Double transactionCharge = 
							marketAsk.currenciesInfo.get(coin).txFee *
							marketBid.prices.get(coin + "-" + denominator).bids.get(0).rate;
					Double commissionCharge = 
							sumToSell * marketAsk.commision + 
							(sumToBuy - transactionCharge) * marketBid.commision;
					Double profitNet = profitGross - commissionCharge - transactionCharge;
					totalROI = profitNet / sumToSell * 100.0;
					/*logger.info(String.format("\nSTS: %.8f", sumToSell) +
							String.format("\nSTB: %.8f", sumToBuy) +
							String.format("\nPG: %.8f", profitGross) +
							String.format("\nTC: %.8f", transactionCharge) +
							String.format("\nCC: %.8f", commissionCharge) +
							String.format("\nPN: %.8f", profitNet));*/
			}
		}
		catch(NullPointerException ex) {}
		Double[] result = {totalROI, sumToBuy, sumToSell};
		return result;
	}
	private boolean containsArbiLine(ArbiLine2 current, Double totalROI) {
		for(ArbiLine2 aLi : arbiLines) {
			if(aLi.marketAsk.equals(current.marketAsk) && aLi.marketBid.equals(current.marketBid)
					&& aLi.asset.equals(current.asset)){
				aLi.rois.add(totalROI);
				return true;
			}
		}
		return false;
	}
	private String concatenateRois(ArbiLine2 current) {
		String result = "";
		for(Double roi : current.rois) {
			result += String.format("%.2f%%", roi) + " ";
		}
		return result;
	}
	private boolean displayTimes(ArbiLine2 current, Double roi, Logger logger) {
		Iterator<ArbiLine2> i = arbiLines.iterator();
		while(i.hasNext()) {
			ArbiLine2 aLi = i.next();
			if(aLi.marketAsk.equals(current.marketAsk) && aLi.marketBid.equals(current.marketBid)
					&& aLi.asset.equals(current.asset)) {
				if(roi <= 0 && aLi.nShowNegative > 0) {
					if(aLi.endTime.equals(Long.parseLong("0"))) {
						aLi.endTime = System.currentTimeMillis();
					}
					SimpleDateFormat format = new SimpleDateFormat("mm:ss.SSS"); 
					Long end = aLi.endTime - aLi.startTime;
					String rois = concatenateRois(aLi);
					aLi.rois.add(roi);
					aLi.nShowNegative--;
					if(aLi.nShowNegative <= 0) {
						logger.info("Coin: " + aLi.asset + " Skad: " + aLi.marketAsk + " Do: " + aLi.marketBid +
							"\n" + format.format(end) + "\n" + rois);
						i.remove();
					}
					return true;
				}
			}
		}
		return false;
		
	}
	public void downloadOrders
	(ArrayList<Market> markets, String denominator) throws InterruptedException {
		Long start = System.nanoTime();
		int i = 0;
		ExecutorService es = Executors.newCachedThreadPool();
		for(Market market : markets) {
			es.execute(new Runnable() {
				@Override
				public void run() {
					downloadMarket(denominator, market);
				}
			});
		}
		es.shutdown();
		boolean finshed = es.awaitTermination(2, TimeUnit.MINUTES);
		Long end = (long) ((System.nanoTime() - start)/ 1000000.0);
		System.out.println(i + "\nend: " + end);
	}
	private void downloadMarket(String denominator, Market market) {
		int numCount = 0;
		for(String numerator : numerators) {
				try {
					market.downloadOrderBook(numerator, denominator);
					//System.out.println(numerator);
				}
				catch(Exception ex) {}
				numCount++;
				System.out.println(market.name + "-> " + numerator + "  " + numCount + "/" + numerators.size());
		}
	}
	
	private boolean isCoinEnabled(String coin, Market marketAsk, Market marketBid) {
		try {
		return marketAsk.currenciesInfo.get(coin).payoutState.equals(WithdrawsState.Enabled) &&
				marketBid.currenciesInfo.get(coin).payoutState.equals(WithdrawsState.Enabled);
		}
		catch(NullPointerException ex) {return false;}
	}
	private Double fixSumToSpend(int lowest, ArrayList<Order> orders) {
		Double sumNew = 0.0;
		Double sumToNew = 0.0;
		for(int i = 0; i < lowest; i++) {
			sumToNew += orders.get(i).quantity * orders.get(i).rate;
			if(sumToNew > toSpend) {
				sumNew += orders.get(i).quantity - (sumToNew - toSpend) / orders.get(i).rate;
				break;
			}
			else {
				sumNew += orders.get(i).quantity;
			}
		}
		return sumNew;
	}
	public Double[] computeMarketValues(String coin, ArrayList<Order> asks,
			ArrayList<Order> bids, int lowestAsk, int maxLowestBid) {
		Double sumQuantAsk = 0.0;
 		Double sumQuantBid = 0.0;
		Double sumDenBid = 0.0;
		Double sumDenAsk = 0.0;
 		
 		for(int i = 0; i < lowestAsk; i++) {
 			sumQuantAsk += asks.get(i).quantity;
 		}
 		for(int i = 0; i < maxLowestBid; i++) {
 			sumQuantBid += bids.get(i).quantity;
 		}

 		if(sumQuantAsk < sumQuantBid) {//rozpatruj Ask
 			for(int i = 0; i < lowestAsk; i++) {
 				sumDenAsk += asks.get(i).quantity * asks.get(i).rate;
 				if(sumDenAsk > toSpend) {
 					sumDenAsk = toSpend;
 					sumQuantAsk = fixSumToSpend(lowestAsk, asks);
 					break;
 				}
 			}
 			Double currentQuantBid = 0.0;
 			for(int i = 0; i < maxLowestBid; i++) {
 				if(bids.get(i).quantity + currentQuantBid < sumQuantAsk) {
 					currentQuantBid += bids.get(i).quantity;
 					sumDenBid += bids.get(i).quantity * bids.get(i).rate;
 				}
 				else {
 					sumDenBid += (sumQuantAsk - currentQuantBid) * bids.get(i).rate;
 					currentQuantBid += (sumQuantAsk - currentQuantBid);
 					break;
 				}
 			}
 			sumQuantBid = currentQuantBid;
 		}
 		else {//rozpatruj Bid
 			for(int i = 0; i < maxLowestBid; i++) {
 				sumDenBid += bids.get(i).quantity * bids.get(i).rate;
 				if(sumDenBid > toSpend) {
 					sumDenBid = toSpend;
 					sumQuantBid = fixSumToSpend(maxLowestBid, bids);
 					break;
 				}
 			}
 			Double currentQuantAsk = 0.0;
 			for(int i = 0; i < lowestAsk; i++) {
 				if(asks.get(i).quantity + currentQuantAsk < sumQuantBid) {
 					currentQuantAsk += asks.get(i).quantity;
 					sumDenAsk += asks.get(i).quantity * asks.get(i).rate;
 				}
 				else {
 					sumDenAsk += (sumQuantBid - currentQuantAsk) * asks.get(i).rate;
 					currentQuantAsk += (sumQuantBid - currentQuantAsk);
 					break;
 				}
 			}
 			sumQuantAsk = currentQuantAsk;
 			
 		}
 		Double[] result = {sumDenAsk, sumDenBid, sumQuantAsk, sumQuantBid};
 		return result;
		
	}
	public int[] findLowestPositions(ArrayList<Order> asks, ArrayList<Order> bids) {
		int pivotAsk = 0;
		int pivotBid = 0;
		int lowestBid = 0;
		boolean isAnyResult = true;
		try {
		while(isAnyResult && lowestBid < bids.size() && pivotAsk < asks.size()) {
			lowestBid = 0;
			boolean isBidGrander = true;
			while(isBidGrander) {
				if(bids.get(lowestBid).rate > asks.get(pivotAsk).rate) {
					isBidGrander = true;
				}
				else {
					isBidGrander = false;
				}
				if(isBidGrander)
					lowestBid++;
				if(lowestBid > pivotBid) {
					pivotBid = lowestBid;
				}
			}
			if(lowestBid > 0) {
				isAnyResult = true;
			}
			else {
				isAnyResult = false;
			}
			if(isAnyResult)
				pivotAsk++;
		}
		}
		catch(IndexOutOfBoundsException iex) {
			//System.out.println("IndexOutOfBoundsException");
		}
		int[] result = {pivotAsk, pivotBid};
		return result;
	}
	
}
