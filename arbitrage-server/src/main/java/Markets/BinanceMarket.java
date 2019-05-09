package Markets;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Common.CurrencyInfo;
import Common.Order;
import Common.Prices;
import Common.WithdrawsState;

public class BinanceMarket extends Market {
	public BinanceMarket() throws Exception {
		this.name = "Binance";
		commision = 0.001;
		url = new URL("https://www.binance.com/");
		downloadCurrenciesInfo();
		//https://api.binance.com//api/v1/depth?symbol=LTCBTC
	}
	/*@Override
	public void downloadPrices() throws Exception {
		String content = getText("https://api.binance.com/api/v1/ticker/24hr");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(content);
        JSONArray json = (JSONArray) obj;
        for(Object genreObject : json) {
        	JSONObject genre = (JSONObject) genreObject;
        	String name = (String) genre.get("symbol");
        	name = name.toLowerCase();
        	String denominator = name.substring(name.length() - 3, name.length());
        	String numerator = name.substring(0, name.length() - 3);
        	String denUsdt = name.substring(name.length() - 4, name.length());
        	if(denUsdt.equals("usdt")) {
        		denominator = "usdt";
        		numerator = name.substring(0, name.length() - 4);
        	}
        	if(currenciesInfo.get(numerator) != null) {
	        	if(currenciesInfo.get(numerator).payoutState.equals(WithdrawsState.Enabled))
	        	{
	        		putPrice(genre, denominator, numerator);
	        	}
        	}
        }
		
	}
	private void putPrice(JSONObject genre, String denominator, String numerator) throws DataFormatException {
		Double ask = 0.0;
		Double bid = 0.0;
		Double volume = 0.0;
		ask = parsePrice(genre.get("askPrice"));
		bid = parsePrice(genre.get("bidPrice"));
		volume = parsePrice(genre.get("quoteVolume"));
		Price price = new Price(ask, bid, volume);
		if(!denominator.equals("usdt") && !denominator.equals("btc") &&
				!denominator.equals("usd") && !denominator.equals("eth") && 
				!denominator.equals("bnb"))
			throw new DataFormatException();
		prices.put(numerator + "-" + denominator, price);
	}*/
	private Double parsePrice(Object genre) {
		Double price = 0.0;
    	try {
			String str = (String) genre;
			price = Double.parseDouble(str);
		} catch (Exception e) {
			e.getMessage();
		}
    	return price;
	}

	@Override
	public void downloadCurrenciesInfo() throws Exception {
		String content = getText("https://www.binance.com/assetWithdraw/getAllAsset.html");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(content);
        JSONArray json = (JSONArray) obj;
        for(Object genreObject : json) {
        	JSONObject genre = (JSONObject) genreObject;
        	String name = (String) genre.get("assetCode");
        	name = name.toLowerCase();
        	//System.out.println(name);
        	CurrencyInfo info = new CurrencyInfo(name);
        	Double fee = (Double) genre.get("transactionFee");
        	info.txFee = fee;
        	setPayoutEnabled(genre.get("enableWithdraw"), info);
        	
        	Object a = genre.get("confirmTimes");
        	String aStr = a.toString();
        	info.confirmationNumber = Double.parseDouble(aStr); 
        	currenciesInfo.put(name, info);
        }
		
	}

	public void downloadOrderBook(String numerator, String denominator) throws Exception {
		String content = getText("https://api.binance.com//api/v1/depth?symbol=" 
									+ numerator.toUpperCase() + denominator.toUpperCase());
		
		//String content = getText("https://bittrex.com/api/v1.1/public/getmarketsummaries");
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(content);
	    JSONObject json = (JSONObject) obj;
	    JSONArray jsonAsks = (JSONArray) json.get("asks");
	    JSONArray jsonBids = (JSONArray) json.get("bids");
	    ArrayList<Order> buyOrders = new ArrayList<>();
	    ArrayList<Order> sellOrders = new ArrayList<>();
	    for(Object asks : jsonAsks) {
	    	JSONArray asksObj = (JSONArray) asks;
	    	Double rateDbl = parsePrice(asksObj.get(0));
			Double quantity = parsePrice(asksObj.get(1));
			sellOrders.add(new Order(quantity, rateDbl));
	    }
	    for(Object bids : jsonBids) {
	    	JSONArray bidsObj = (JSONArray) bids;
	    	Double rateDbl = parsePrice(bidsObj.get(0));
			Double quantity = parsePrice(bidsObj.get(1));
			buyOrders.add(new Order(quantity, rateDbl));
	    }
	    prices.put(numerator + "-" + denominator, new Prices(sellOrders, buyOrders));
	}
	private void setPayoutEnabled(Object genre, CurrencyInfo info) {
		boolean payoutEnabled = (boolean) genre;
		if(payoutEnabled)
			info.payoutState = WithdrawsState.Enabled;
		else
			info.payoutState = WithdrawsState.Disabled;
	}

}
