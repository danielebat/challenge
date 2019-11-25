package com.challenge.util.transfer;

import java.math.BigDecimal;

import com.challenge.data.model.Currency;
import com.challenge.exchange.rate.ExchangeRateRepository;

public class TransferUtil {
	
	/**
	 * Method to convert an amount of money that must be transferred from an account to another according 
	 * to currencies of accounts
	 * @param amount Amount of money to be transferred
	 * @param from Currency of the Source account
	 * @param to Currency of the target account
	 * @return Converted amount
	 */
	public static BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
		BigDecimal exchangeRate = ExchangeRateRepository.getExchangeRate(from, to);
		BigDecimal convertedAmount = amount.multiply(exchangeRate);
		return convertedAmount;
	}

}
