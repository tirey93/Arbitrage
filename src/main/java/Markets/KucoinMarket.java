package Markets;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.kucoin.KucoinExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;

public class KucoinMarket extends Market{
	private Exchange exchange;
	private MarketDataService marketDataService;
	public KucoinMarket() throws Exception {
		this.name = "Kucoin";
		commision = 0.001;
		url = new URL("https://www.kucoin.com/");
		exchange = ExchangeFactory.INSTANCE.createExchange(KucoinExchange.class.getName());
		marketDataService = exchange.getMarketDataService();
		downloadCurrenciesInfo();
	}
	
	public void downloadOrderBook2(String numerator, String denominator) throws Exception {
		String content = getText("https://api.kucoin.com/v1/open/orders?limit=30&symbol="
				+ numerator.toUpperCase() + "-" + denominator.toUpperCase());
		//String content = getText("https://bittrex.com/api/v1.1/public/getmarketsummaries");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(content);
        JSONObject json = (JSONObject) obj;
        JSONObject jsonResult = (JSONObject) json.get("data");
        Set types = jsonResult.keySet();
        ArrayList<Order> buyOrders = new ArrayList<>();
        ArrayList<Order> sellOrders = new ArrayList<>();
        for(Object type : types) {
        	String typeCast = (String) type;
        	JSONArray orders = null;
        	try {
				orders = (JSONArray) jsonResult.get(type.toString());
			} catch (ClassCastException e) {}
        	if(orders != null) {
	        	for(Object order : orders) {
	        		JSONArray orderCast = (JSONArray) order; 
	        		Double rate = (Double) orderCast.get(0);
	        		Double quantity = (Double) orderCast.get(1);
	        		if(typeCast.equals("BUY")) {
	        			buyOrders.add(new Order(quantity, rate));
	        		}
	        		else if(typeCast.equals("SELL")) {
	        			sellOrders.add(new Order(quantity, rate));
	        		}
	        	}
	        }
        }
        prices.put(numerator + "-" + denominator, new Prices(sellOrders, buyOrders));
	}
	@Override
	public void downloadOrderBook(String numerator, String denominator) throws Exception {
		ArrayList<Order> buyOrders = new ArrayList<>();
        ArrayList<Order> sellOrders = new ArrayList<>();
		String[] args = new String[1];
		CurrencyPair pair = new CurrencyPair(numerator, denominator);
		OrderBook ob = marketDataService.getOrderBook(pair, args);
		for(LimitOrder lo : ob.getAsks()) {
			sellOrders.add(
					new Order(
					lo.getOriginalAmount().doubleValue(), 
					lo.getLimitPrice().doubleValue()));
		}
		for(LimitOrder lo : ob.getBids()) {
			buyOrders.add(
					new Order(
					lo.getOriginalAmount().doubleValue(), 
					lo.getLimitPrice().doubleValue()));
		}
	}

	private Double parsePrice(Object genre) {
		Double price = 0.0;
		try {
			String str = (String) genre;
			price = Double.parseDouble(str);
		}
		catch(ClassCastException ex) {
			try {
				price = (Double) genre;
			}
			catch(ClassCastException ex1) {
				Long last = (Long) genre;
				price = last.doubleValue();
			}
		}
		catch(NullPointerException nlEx) {}
		return price;
	}
	
	public void downloadCurrenciesInfo2() throws Exception {
		String content = getText("https://api.kucoin.com/v1/market/open/coins");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(content);
        JSONObject json = (JSONObject) obj;
        JSONArray genreArray = (JSONArray) json.get("data");
        for(Object genreObject : genreArray) {
        	JSONObject genre = (JSONObject) genreObject;
        	String name = (String) genre.get("coin");
        	name = name.toLowerCase();
        	CurrencyInfo info = new CurrencyInfo(name);
        	info.txFee = (Double)genre.get("withdrawMinFee");
        	if((boolean)genre.get("enableWithdraw") && (boolean)genre.get("enableDeposit"))
        		info.payoutState = WithdrawsState.Enabled;
        	else
        		info.payoutState = WithdrawsState.Disabled;
        	
        	Object a = genre.get("confirmationCount");
        	String aStr = a.toString();
        	info.confirmationNumber = Double.parseDouble(aStr); 
        	currenciesInfo.put(name, info);
        }
		
	}
	@Override
	public void downloadCurrenciesInfo() throws Exception {
		Exchange exchange = ExchangeFactory.INSTANCE.createExchange(KucoinExchange.class.getName());
		Map<Currency, CurrencyMetaData> pairsMap =
				exchange.getExchangeMetaData().getCurrencies();
		for(Map.Entry<Currency, CurrencyMetaData> pair : pairsMap.entrySet()) {
			if(pair != null) {
				if(pair.getKey() != null) {
					CurrencyInfo currency = new CurrencyInfo(pair.getKey().toString());
					if (pair.getValue() != null){
						//todo
					}
					currenciesInfo.put(pair.getKey().toString().toLowerCase(), currency);
				}
			}
		}
	}

}
