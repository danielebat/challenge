package com.challenge.data.store;

import java.math.BigDecimal;

import javax.inject.Inject;

import com.challenge.data.model.Account;

public class AccountDao extends Dao<Account> {
	
	@Inject
	public AccountDao() {
		super();
	}
	
	public synchronized BigDecimal deposit(Account account, BigDecimal amount) {
		BigDecimal updatedAmount = account.getAmount().add(amount);
		account.setAmount(updatedAmount);
		return updatedAmount;
	}
	
	public synchronized BigDecimal withdraw(Account account, BigDecimal amount) {
		BigDecimal updatedAmount = account.getAmount().subtract(amount);
		account.setAmount(updatedAmount);
		return updatedAmount;
	}

}
