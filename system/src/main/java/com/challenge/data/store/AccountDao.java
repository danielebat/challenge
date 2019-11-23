package com.challenge.data.store;

import javax.inject.Inject;

import com.challenge.data.model.Account;

public class AccountDao extends Dao<Account> {
	
	@Inject
	public AccountDao() {
		super();
	}

	@Override
	protected Account findById(String id) {
		return this.entities.get(id);
	}

}
