package Markets;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BittrexMarket extends Market{

	public BittrexMarket() throws Exception {
		this.name = "Bittrex";
		commision = 0.0025;
		url = new URL("https://bittrex.com/");
		downloadCurrenciesInfo();
	}
	/*@Override
	public void downloadPrices() throws Exception {
		String content = getText("https://bittrex.com/api/v1.1/public/getmarketsummaries");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(content);
        JSONObject json = (JSONObject) obj;
        JSONArray genreArray = (JSONArray) json.get("result");
        for(Object genreObject : genreArray) {
        	JSONObject genre = (JSONObject) genreObject;
        	String[] currencies = genre.get("MarketName").toString().toLowerCase().split("-");
        	if(currenciesInfo.get(currencies[1]).payoutState.equals(WithdrawsState.Enabled)) {
        		//System.out.println(currencies[1] + "-" + currencies[0]);
            	Price price = new Price(
            			(Double)genre.get("Ask"), 
            			(Double)genre.get("Bid"), 
            			(Double)genre.get("BaseVolume"));
            	prices.put(currencies[1] + "-" + currencies[0], price);
        	}
        	
        }
        //System.out.println(prices.get("BCH-BTC"));
	}*/
	@Override
	public void downloadCurrenciesInfo() throws Exception {
		String content = getText("https://bittrex.com/api/v1.1/public/getcurrencies");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(content);
        JSONObject json = (JSONObject) obj;
        JSONArray genreArray = (JSONArray) json.get("result");
        for(Object genreObject : genreArray) {
        	JSONObject genre = (JSONObject) genreObject;
        	String name = (String) genre.get("Currency");
        	name = name.toLowerCase();
        	CurrencyInfo info = new CurrencyInfo(name);
        	info.txFee = (Double)genre.get("TxFee");
        	Object a = genre.get("MinConfirmation");
        	String aStr = a.toString();
        	info.confirmationNumber = Double.parseDouble(aStr); 
        	if((boolean)genre.get("IsActive"))
        		info.payoutState = WithdrawsState.Enabled;
        	else
        		info.payoutState = WithdrawsState.Disabled;
        	currenciesInfo.put(name, info);
        }
	}
	public void downloadOrderBook(String numerator, String denominator) throws Exception {
		String content = getText("https://bittrex.com/api/v1.1/public/getorderbook?market="
				+denominator.toUpperCase() + "-" + numerator.toUpperCase() +"&type=both");
		//String content = getText("https://bittrex.com/api/v1.1/public/getmarketsummaries");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(content);
        JSONObject json = (JSONObject) obj;
        JSONObject jsonResult = (JSONObject) json.get("result");
        Set types = jsonResult.keySet();
        ArrayList<Order> buyOrders = new ArrayList<>();
        ArrayList<Order> sellOrders = new ArrayList<>();
        for(Object type : types) {
        	String typeCast = (String) type;
        	JSONArray orders = (JSONArray) jsonResult.get(type.toString());
        	for(Object order : orders) {
        		JSONObject orderCast = (JSONObject) order;       		
        		Double rate = (Double) orderCast.get("Rate");
        		Double quantity = (Double) orderCast.get("Quantity");
        		if(typeCast.equals("buy")) {
        			buyOrders.add(new Order(quantity, rate));
        		}
        		else if(typeCast.equals("sell")) {
        			sellOrders.add(new Order(quantity, rate));
        		}
        	}
        }
        prices.put(numerator + "-" + denominator, new Prices(sellOrders, buyOrders));
	}

}












