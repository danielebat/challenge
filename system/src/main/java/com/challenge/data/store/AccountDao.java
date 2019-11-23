package com.challenge.data.store;

import javax.inject.Inject;

import com.challenge.data.model.Account;

public class AccountDao extends Dao<Account> {
	
	@Inject
	public AccountDao() {
		super();
	}

}
