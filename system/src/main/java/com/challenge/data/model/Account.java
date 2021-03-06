package com.challenge.data.model;

import java.math.BigDecimal;

import com.challenge.util.account.AccountUtil;

/**
 * Class representing the bank account of a user that a user can request through a HTTP request
 */
public class Account extends JsonObject {

	private String ibanCode;
	private BigDecimal amount;
	private Currency currency;
	private Integer userId;
	
	public Account(BigDecimal amount, Currency currency, Integer userId) {
		this.ibanCode = AccountUtil.generateIbanCode(currency);
		this.amount = amount;
		this.currency = currency;
		this.userId = userId;
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

	public Integer getUserId() {
		return userId;
	}

}
