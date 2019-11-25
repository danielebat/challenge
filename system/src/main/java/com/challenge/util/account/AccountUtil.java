package com.challenge.util.account;

import java.util.Random;

import com.challenge.data.model.Currency;

public class AccountUtil {
	
	private static final int IBAN_MAX_BOUND = 99999999;
	private static final int IBAN_MIN_BOUND = 10000000;

	/**
	 * Method to generate a random IBAN code consisting of two letters and ten numbers
	 * @param currency Account currency
	 * @return IBAN code
	 */
	public static String generateIbanCode(Currency currency) {
		
		return currency.name().substring(0,2) + (IBAN_MIN_BOUND + new Random().nextInt(IBAN_MAX_BOUND));
	}

}
