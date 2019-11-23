package com.challenge.data.model;

import java.math.BigDecimal;

public class Account {
	
	private String ibanCode;
	private BigDecimal amount;
	private Currency currency;
	
	public Account(String ibanCode, BigDecimal amount, Currency currency) {
		this.ibanCode = ibanCode;
		this.amount = amount;
		this.currency = currency;
	}

	public String getIbanCode() {
		return ibanCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

}
