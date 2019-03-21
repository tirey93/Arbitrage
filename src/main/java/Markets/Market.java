package Markets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public abstract class Market {
	String name;
	Double commision;
	URL url;
	Map<String, Prices> prices = new HashMap<>();
	//Map<String, Price> prices = new HashMap<>();
	Dictionary<String, CurrencyInfo> currenciesInfo = new Hashtable<>();
	
	public String getText(String url) throws Exception {
        URL website = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) website.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.76"); 
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;
        inputLine = in.readLine();
            response.append(inputLine);
        in.close();
        return response.toString();
    }
	//public abstract void downloadPrices() throws Exception;
	public abstract void downloadCurrenciesInfo() throws Exception;
	public abstract void downloadOrderBook(String numerator, String denominator) throws Exception;
	public void initMarket() throws Exception{
		downloadCurrenciesInfo();
		//downloadPrices();
	}
	public void sortOrders(String denominator) {
		Enumeration<String> keys = currenciesInfo.keys();
		while(keys.hasMoreElements()) {
			String coin = keys.nextElement();
			try {
				prices.get(coin + "-" + denominator).asks.sort((p1,p2) -> p1.rate.compareTo(p2.rate));
				prices.get(coin + "-" + denominator).bids.sort((p1,p2) -> p1.rate.compareTo(-p2.rate));
			}
			catch(NullPointerException ex) {}
//			for(Order ask : prices2.get(coin + "-" + denominator).asks) {
//				
//			}
//			for(Order bid : prices2.get(coin + "-" + denominator).bids) {
//				
//			}
		}
	}
}







