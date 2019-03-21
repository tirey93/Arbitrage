import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.bittrex.BittrexExchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.kucoin.KucoinExchange;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;
import org.python.util.PythonInterpreter;

import Markets.BinanceMarket;
import Markets.BittrexMarket;
import Markets.KucoinMarket;
import Markets.Market;
import Markets.MarketCalculator;

public class Main {

	public static ArrayList<Market> markets = new ArrayList<Market>();
	public static ArrayList<String> numerators = new ArrayList<String>();
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("python.home","C:\\Python37");
		props.put("python.console.encoding", "UTF-8"); // Used to prevent: console: Failed to install '': java.nio.charset.UnsupportedCharsetException: cp0.
		props.put("python.security.respectJavaAccessibility", "false"); //don't respect java accessibility, so that we can access protected members on subclasses
		props.put("python.import.ccxt","false");

		Properties preprops = System.getProperties();
				
		PythonInterpreter.initialize(preprops, props, new String[0]);
		PythonInterpreter interp = new PythonInterpreter();
		interp.execfile("C:\\Users\\Damian Pytkowski\\PycharmProjects\\test1\\Main.py");
		
	}
	private static Exchange prepareExchange(String name) {
		Exchange exchange = null;
		ExchangeSpecification exSpec = null;
		if(name.toLowerCase().equals("kucoin")) {
			exchange = new KucoinExchange();
			exSpec = exchange.getDefaultExchangeSpecification();
			exSpec.setApiKey("5b524142e0abb82d5964ca8e");
			exSpec.setSecretKey("1077f9c2-94c8-4104-8658-be3f541ec538");
		}
		else if(name.toLowerCase().equals("bittrex")) {
			exchange = new BittrexExchange();
			exSpec = exchange.getDefaultExchangeSpecification();
			exSpec.setApiKey("554f587c220647d18bd76cf8f234af67");
			exSpec.setSecretKey("66760c317d034e8a9564825297dc9c3d");
		}
		else if(name.toLowerCase().equals("binance")) {
			exchange = new BinanceExchange();
			exSpec = exchange.getDefaultExchangeSpecification();
			exSpec.setApiKey
			("jgqbGalNGax8Q87yeVNhPcKkl2zeKaGm9uAnFzqlRvdMoD7b1uVQ1s2f6d3rwWDQ");
			exSpec.setSecretKey
			("v6HLZEG9hypzTlZVFiEXZhKxJda0Ym38glWB58BTBM0RvTWUbfsgFBT9OJhe5qYf");
		}
		else return null;
		exchange.applySpecification(exSpec);
		return exchange;
	}
	private static void prepareData(Logger logger) throws IOException, Exception {
		FileHandler fh;
		// This block configure the logger with handler and formatter  
        fh = new FileHandler("MyLogFile.log");  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);
        for(Market market : markets) {
			market.downloadCurrenciesInfo();
		}
	}
	//Exchange exchange = prepareExchange("kucoin");
	

			/*System.out.println(
					exchange.getAccountService().
					requestDepositAddress(new Currency("XLM")));*/
			
			
			//System.out.println(exchange.getMarketDataService().getTicker(new CurrencyPair("XLM", "BTC")));
			/*System.out.println(exchange.getAccountService()
					.withdrawFunds(new Currency("XLM"), new BigDecimal("7.58"), 
					"xrb_1e5xnktuzokk9q14igsoyazgtfqsthxtnxznpktzs8k9a6qwnu3knbjwezh6"));*/
			
			/*System.out.println(
					exchange.getAccountService().
					requestDepositAddress(new Currency("XLM")));*/
			/*markets.add(new BittrexMarket());
			markets.add(new KucoinMarket());
			markets.add(new BinanceMarket());
			String denominator = "BTC";
			Logger logger = Logger.getLogger("MyLog");  
		    FileHandler fh;  
	        prepareData(logger);
	        MarketCalculator mc = new MarketCalculator(markets, numerators);
	        while(true) {
	        	mc.mainLoop(markets, denominator, numerators, logger);
	        }*/
			/*Exchange exchange = prepareExchange("KUCOIN");
			System.out.println(
			exchange.getMarketDataService().getTicker(new CurrencyPair("NANO", "BTC")));
			System.out.println(
			exchange.getTradeService().
			placeMarketOrder(new MarketOrder
					(OrderType.BID, 
							new BigDecimal("1.0"), 
							new CurrencyPair("NANO", "BTC"))));*/
			/*Currency c = new Currency("NANO");
			System.out.println(exchange.getAccountService().withdrawFunds(c, new BigDecimal("1.1"), 
					"xrb_1e5xnktuzokk9q14igsoyazgtfqsthxtnxznpktzs8k9a6qwnu3knbjwezh6"));*/

}
