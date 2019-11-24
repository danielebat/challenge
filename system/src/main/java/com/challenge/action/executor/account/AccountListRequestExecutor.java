package com.challenge.action.executor.account;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.challenge.action.executor.IRequestExecutor;
import com.challenge.data.model.IJsonObject;
import com.challenge.data.model.Transaction;
import com.challenge.data.store.AccountDao;
import com.google.common.collect.Lists;

public class AccountListRequestExecutor implements IRequestExecutor {

	private static final String USER_ID = "id";
	private final AccountDao accountDao;
	
	@Inject
	public AccountListRequestExecutor(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	public List<IJsonObject> executeRequest(HttpServletRequest request) {
		
		String message = "Unable to process request for account.";
		
		try {
			String userId = request.getParameter(USER_ID);
			return Lists.newArrayList(accountDao.findAllByUserId(Integer.valueOf(userId)));
		} catch (Exception e) {
			return Lists.newArrayList(new Transaction(null, null, null, null, message));
		}
		
	}

}
