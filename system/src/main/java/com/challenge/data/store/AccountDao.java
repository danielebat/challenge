package com.challenge.data.store;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.challenge.data.model.Account;
import com.google.common.collect.Lists;

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
	
	public synchronized List<Account> findAllByUserId(Integer userId) {
		List<Account> acccounts = Lists.newArrayList();
		for (Account acccount : entities.values()) {
			if (acccount.getUserId() == userId)
				acccounts.add(acccount);
		}
		return acccounts;
	}

}
