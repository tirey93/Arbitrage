package Markets;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.v2.BitfinexExchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.kucoin.KucoinExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;

import Common.CurrencyInfo;
import Common.Order;

public class BitfinexMarket extends Market {
	private Exchange exchange;
	private MarketDataService marketDataService;
	public BitfinexMarket() throws Exception {
		this.name = "Bitfinex";
		commision = 0.001;
		url = new URL("https://www.bitfinex.com/");
		exchange = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class.getName());
		marketDataService = exchange.getMarketDataService();
		downloadCurrenciesInfo();
	}
	@Override
	public void downloadCurrenciesInfo() throws Exception {
		Map<Currency, CurrencyMetaData> pairsMap =
				exchange.getExchangeMetaData().getCurrencies();
		for(Map.Entry<Currency, CurrencyMetaData> pair : pairsMap.entrySet()) {
			if(pair != null) {
				if(pair.getKey() != null) {
					CurrencyInfo currency = new CurrencyInfo(pair.getKey().toString());
					if (pair.getValue() != null){
						BigDecimal fee = pair.getValue().getWithdrawalFee();
						if(fee != null) {
							currency.txFee = pair.getValue().getWithdrawalFee().doubleValue();
						}
					}
					currenciesInfo.put(pair.getKey().toString().toLowerCase(), currency);
				}
			}
		}
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

}
