package com.challenge.data.store;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.challenge.data.model.Transaction;

public class TransactionDao extends Dao<Transaction> {
	
	@Inject
	public TransactionDao() {
		super();
	}
	
	public synchronized List<Transaction> findAllByAccountId(Integer accountId) {
		return entities.values()
				.stream()
				.filter(transaction -> transaction.getSourceAccountId() == accountId || transaction.getTargetAccountId() == accountId)
				.collect(Collectors.toList());
	}

}
