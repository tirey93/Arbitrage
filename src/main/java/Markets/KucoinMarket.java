package Markets;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class KucoinMarket extends Market{
	public KucoinMarket() throws Exception {
		this.name = "Kucoin";
		commision = 0.001;
		url = new URL("https://www.kucoin.com/");
	}
	@Override
	public void downloadOrderBook(String numerator, String denominator) throws Exception {
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
	@Override
	public void downloadCurrenciesInfo() throws Exception {
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

}
