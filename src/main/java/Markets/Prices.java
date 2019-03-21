package Markets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Prices {
	public ArrayList<Order> asks;
	public ArrayList<Order> bids;
	public Prices(ArrayList<Order> ask, ArrayList<Order> bid) {
		this.asks = ask;
		this.bids = bid;
	}
}
