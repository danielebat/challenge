package com.challenge.data.store;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.challenge.data.model.Account;

/**
 * Class to store Account data and transactions
 */
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
		return entities.values()
				.stream()
				.filter(account -> account.getUserId() == userId)
				.collect(Collectors.toList());
	}

}
