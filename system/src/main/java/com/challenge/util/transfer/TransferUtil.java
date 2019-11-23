package com.challenge.util.transfer;

import java.math.BigDecimal;

import com.challenge.data.model.Currency;
import com.challenge.exchange.rate.ExchangeRateRepository;

public class TransferUtil {
	
	public static BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
		BigDecimal exchangeRate = ExchangeRateRepository.getExchangeRate(from, to);
		BigDecimal convertedAmount = amount.multiply(exchangeRate);
		return convertedAmount;
	}

}
