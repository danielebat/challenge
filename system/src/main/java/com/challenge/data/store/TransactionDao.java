package com.challenge.data.store;

import java.util.List;

import javax.inject.Inject;

import com.challenge.data.model.Transaction;
import com.google.common.collect.Lists;

public class TransactionDao extends Dao<Transaction> {
	
	@Inject
	public TransactionDao() {
		super();
	}
	
	@Override
	public synchronized Integer add(Transaction transaction) {
		Integer id = super.add(transaction);
		transaction.setId(id);
		return id;
	}
	
	public synchronized List<Transaction> findAllByAccountId(Integer accountId) {
		List<Transaction> transactions = Lists.newArrayList();
		for (Transaction transaction : entities.values()) {
			if (transaction.getSourceAccountId() == accountId ||
					transaction.getTargetAccountId() == accountId)
				transactions.add(transaction);
		}
		return transactions;
	}

}
