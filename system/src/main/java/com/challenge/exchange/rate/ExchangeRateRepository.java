package com.challenge.exchange.rate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.challenge.data.model.Currency;

/**
 * Class working as static repository of exchange rates
 */
public class ExchangeRateRepository {
	
	private static Map<String, BigDecimal> exchangeRateMap;
	
	static {
		exchangeRateMap = new HashMap<String, BigDecimal>();
		
		exchangeRateMap.put(Currency.USD.name() + Currency.EUR.name(), new BigDecimal(1.04));
		exchangeRateMap.put(Currency.USD.name() + Currency.GBP.name(), new BigDecimal(1.14));
		exchangeRateMap.put(Currency.USD.name() + Currency.JPY.name(), new BigDecimal(108.64));
		
		exchangeRateMap.put(Currency.EUR.name() + Currency.USD.name(), new BigDecimal(0.96));
		exchangeRateMap.put(Currency.EUR.name() + Currency.GBP.name(), new BigDecimal(0.92));
		exchangeRateMap.put(Currency.EUR.name() + Currency.JPY.name(), new BigDecimal(115.55));
		
		exchangeRateMap.put(Currency.GBP.name() + Currency.USD.name(), new BigDecimal(0.96));
		exchangeRateMap.put(Currency.GBP.name() + Currency.EUR.name(), new BigDecimal(0.92));
		exchangeRateMap.put(Currency.GBP.name() + Currency.JPY.name(), new BigDecimal(115.55));
		
		exchangeRateMap.put(Currency.JPY.name() + Currency.EUR.name(), new BigDecimal(0.0084));
		exchangeRateMap.put(Currency.JPY.name() + Currency.GBP.name(), new BigDecimal(0.0072));
		exchangeRateMap.put(Currency.JPY.name() + Currency.USD.name(), new BigDecimal(0.0092));
	}
	
	/**
	 * Method to get the exchange rate between two currencies
	 * @param from Source Currency
	 * @param to Target Currency
	 * @return Exchange Rate
	 */
	public static BigDecimal getExchangeRate(Currency from, Currency to) {
		if (from.equals(to))
			return new BigDecimal(1.00);
		return exchangeRateMap.get(from.name() + to.name());
	}

}
