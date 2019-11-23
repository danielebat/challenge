package com.challenge.data.model;

import java.math.BigDecimal;

import com.challenge.account.util.AccountUtil;

public class Account {
	
	private String ibanCode;
	private BigDecimal amount;
	private Currency currency;
	
	public Account(BigDecimal amount, Currency currency) {
		this.ibanCode = AccountUtil.generateIbanCode(currency);
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
