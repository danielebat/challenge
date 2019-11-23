package com.challenge.data.store;

import javax.inject.Inject;

import com.challenge.data.model.Transaction;

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

}
