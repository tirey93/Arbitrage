package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.bittrex.BittrexExchange;
import org.knowm.xchange.kucoin.KucoinExchange;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Common.ArbiLine;
import Common.ArbiLine2;
import Common.JsonClass;
import Common.MarketCalculator;
import Common.Transaction;
import Markets.BinanceMarket;
import Markets.BitfinexMarket;
import Markets.BittrexMarket;
import Markets.KucoinMarket;
import Markets.Market;

public class Main {

	public static ArrayList<Market> markets = new ArrayList<Market>();
	public static ArrayList<String> numerators = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		runLoop();
		

	}
	
	private static void runLoop() throws Exception{
		ArrayList<Market> markets = new ArrayList<>();
		markets.add(new KucoinMarket());
		markets.add(new BinanceMarket());
		markets.add(new BittrexMarket());
		//markets.add(new BitfinexMarket());
		
		MarketCalculator mc = new MarketCalculator(markets, "BTC");
		Logger logger = Logger.getLogger(Main.class.getName()); 
		prepareLogger(logger);
		for(Market market : markets) {
			logger.info(market.name + " -> " + market.currenciesInfo.size());
		}
		while(true) {
			mc.mainLoop(markets, logger);
		}
	}
	private static Exchange prepareExchange(String name) {
		Exchange exchange = null;
		ExchangeSpecification exSpec = null;
		if (name.toLowerCase().equals("kucoin")) {
			exchange = new KucoinExchange();
			exSpec = exchange.getDefaultExchangeSpecification();
			exSpec.setApiKey("5b524142e0abb82d5964ca8e");
			exSpec.setSecretKey("1077f9c2-94c8-4104-8658-be3f541ec538");
		} else if (name.toLowerCase().equals("bittrex")) {
			exchange = new BittrexExchange();
			exSpec = exchange.getDefaultExchangeSpecification();
			exSpec.setApiKey("554f587c220647d18bd76cf8f234af67");
			exSpec.setSecretKey("66760c317d034e8a9564825297dc9c3d");
		} else if (name.toLowerCase().equals("binance")) {
			exchange = new BinanceExchange();
			exSpec = exchange.getDefaultExchangeSpecification();
			exSpec.setApiKey("jgqbGalNGax8Q87yeVNhPcKkl2zeKaGm9uAnFzqlRvdMoD7b1uVQ1s2f6d3rwWDQ");
			exSpec.setSecretKey("v6HLZEG9hypzTlZVFiEXZhKxJda0Ym38glWB58BTBM0RvTWUbfsgFBT9OJhe5qYf");
		} else
			return null;
		exchange.applySpecification(exSpec);
		return exchange;
	}

	private static void prepareLogger(Logger logger) throws IOException, Exception {
		FileHandler fh;
		// This block configure the logger with handler and formatter
		fh = new FileHandler("MyLogFile.log");
		logger.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
	}
	/*json.addArbiLine(new ArbiLine("BTC", "ETH", "Kucoin", "Bittrex"));
	json.getArbiLines().get(new ArbiLine
			("BTC", "ETH", "Binance", "Bittrex").hashCode()).
			addTransaction(new Transaction(0.1, 21.0, 1.15));
	json.getArbiLines().get(new ArbiLine
			("BTC", "ETH", "Binance", "Bittrex").hashCode()).
			addTransaction(new Transaction(0.2, 20.0, 1.15));*/
	/*System.out.println(gson.toJson(json));
	JsonClass testJsonOut = gson.fromJson(gson.toJson(json), JsonClass.class);
	if(testJsonOut != null) {
		System.out.println(testJsonOut.getArbiLines().size());
		
	}*/
	//System.out.println(gson.toJson(testJson));
	
	//TestJson testJsonOut = gson.fromJson(gson.toJson(testJson), TestJson.class);
	
	/*for (Map.Entry<Complex, TestJson> entry : mapa.entrySet()) {
		
	}*/
	/*
	 * Properties props = new Properties(); props.put("python.home","C:\\Python37");
	 * props.put("python.console.encoding", "UTF-8"); // Used to prevent: console:
	 * Failed to install '': java.nio.charset.UnsupportedCharsetException: cp0.
	 * props.put("python.security.respectJavaAccessibility", "false"); //don't
	 * respect java accessibility, so that we can access protected members on
	 * subclasses props.put("python.import.ccxt","false");
	 * 
	 * Properties preprops = System.getProperties();
	 * 
	 * PythonInterpreter.initialize(preprops, props, new String[0]);
	 * PythonInterpreter interp = new PythonInterpreter(); interp.
	 * execfile("C:\\Users\\Damian Pytkowski\\PycharmProjects\\test1\\Main.py");
	 */
	/*
	 * Logger logger = Logger.getLogger(Main.class.getName()); markets.add(new
	 * BinanceMarket()); markets.add(new BittrexMarket()); //markets.add(new
	 * KucoinMarket()); prepareData(logger); MarketCalculator calculator = new
	 * MarketCalculator(markets, numerators); calculator.mainLoop(markets, "BTC",
	 * numerators, logger);
	 */
	
	
	/*MarketDataService marketDataService = bitstamp.getMarketDataService();
	for(CurrencyPair pair : bitstamp.getExchangeSymbols()) {
		System.out.println(pair.base);
	}
	Map<Currency, CurrencyMetaData> pairsMap =
			bitstamp.getExchangeMetaData().getCurrencies();
	for(Map.Entry<Currency, CurrencyMetaData> pair : pairsMap.entrySet()) {
		if(pair != null) {
			if(pair.getValue() != null) {
			System.out.println(pair.getKey().toString() + " -> " + pair.getValue().toString());
			}
			else {
				System.out.println(pair.getKey() + " -> null getValue");
			}
		}
		else {
			System.out.println("null pair");
		}
	}*/
	
	/*CurrencyPair pair = new CurrencyPair("LTC", "BTC");
	OrderBook ob = marketDataService.getOrderBook(pair, ars);
	for(LimitOrder lo : ob.getAsks()) {
		System.out.println(lo.toString());
	}*/
	/*param = new CurrencyPairsParam() {
	
	@Override
	public Collection<CurrencyPair> getCurrencyPairs() {
		ArrayList<CurrencyPair> pairs = new ArrayList<>();
		Map<CurrencyPair, CurrencyPairMetaData> pairsMap =
				bitstamp.getExchangeMetaData().getCurrencyPairs();
		for(Map.Entry<CurrencyPair, CurrencyPairMetaData> pair : pairsMap.entrySet()) {
			pairs.add(pair.getKey());
		}
		return pairs;
	}
	};*/
	//System.out.println(param.getCurrencyPairs());
	/*List<Ticker> listT = marketDataService.getTickers(param);
	for (Ticker tick : listT) {
		System.out.println(tick.toString());
	}*/
	// Exchange exchange = prepareExchange("kucoin");

	/*
	 * System.out.println( exchange.getAccountService(). requestDepositAddress(new
	 * Currency("XLM")));
	 */

	// System.out.println(exchange.getMarketDataService().getTicker(new
	// CurrencyPair("XLM", "BTC")));
	/*
	 * System.out.println(exchange.getAccountService() .withdrawFunds(new
	 * Currency("XLM"), new BigDecimal("7.58"),
	 * "xrb_1e5xnktuzokk9q14igsoyazgtfqsthxtnxznpktzs8k9a6qwnu3knbjwezh6"));
	 */

	/*
	 * System.out.println( exchange.getAccountService(). requestDepositAddress(new
	 * Currency("XLM")));
	 */
	/*
	 * markets.add(new BittrexMarket()); markets.add(new KucoinMarket());
	 * markets.add(new BinanceMarket()); String denominator = "BTC"; Logger logger =
	 * Logger.getLogger("MyLog"); FileHandler fh; prepareData(logger);
	 * MarketCalculator mc = new MarketCalculator(markets, numerators); while(true)
	 * { mc.mainLoop(markets, denominator, numerators, logger); }
	 */
	/*
	 * Exchange exchange = prepareExchange("KUCOIN"); System.out.println(
	 * exchange.getMarketDataService().getTicker(new CurrencyPair("NANO", "BTC")));
	 * System.out.println( exchange.getTradeService(). placeMarketOrder(new
	 * MarketOrder (OrderType.BID, new BigDecimal("1.0"), new CurrencyPair("NANO",
	 * "BTC"))));
	 */
	/*
	 * Currency c = new Currency("NANO");
	 * System.out.println(exchange.getAccountService().withdrawFunds(c, new
	 * BigDecimal("1.1"),
	 * "xrb_1e5xnktuzokk9q14igsoyazgtfqsthxtnxznpktzs8k9a6qwnu3knbjwezh6"));
	 */

}
