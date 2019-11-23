package com.challenge.data.model;

import java.math.BigDecimal;

import com.challenge.handler.account.AccountAction;

public class Transaction {
	
	Integer id;
	AccountAction action;
	Integer sourceAccountId;
	Integer targetAccountId;
	BigDecimal amount;
	String message;
	
	public Transaction(AccountAction action, Integer sourceAccountId,
			Integer targetAccountId, BigDecimal amount, String message) {
		this.action = action;
		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
		this.amount = amount;
		this.message = message;
	}

	public AccountAction getAction() {
		return action;
	}

	public Integer getSourceAccountId() {
		return sourceAccountId;
	}

	public Integer getTargetAccountId() {
		return targetAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	public String getMessage() {
		return message;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
