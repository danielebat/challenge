package com.challenge.data.model;

import java.math.BigDecimal;

import com.challenge.handler.account.AccountAction;

/**
 * Class used to represent a transaction that a user can request through a HTTP request
 */
public class Transaction extends MessageObject {

	AccountAction action;
	Integer sourceAccountId;
	Integer targetAccountId;
	BigDecimal amount;
	
	public Transaction(AccountAction action, Integer sourceAccountId,
			Integer targetAccountId, BigDecimal amount, String message) {
		super(message);
		this.action = action;
		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
		this.amount = amount;
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

}
