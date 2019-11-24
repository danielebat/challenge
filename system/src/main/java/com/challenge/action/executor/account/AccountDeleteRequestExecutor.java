package com.challenge.action.executor.account;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.challenge.action.executor.IRequestExecutor;
import com.challenge.data.model.Account;
import com.challenge.data.model.IJsonObject;
import com.challenge.data.model.Transaction;
import com.challenge.data.store.AccountDao;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;
import com.google.common.collect.Lists;

public class AccountDeleteRequestExecutor implements IRequestExecutor {

	private static final String ID = "id";
	private final AccountDao dao;
	private final TransactionDao transactionDao;
	
	@Inject
	public AccountDeleteRequestExecutor(AccountDao dao, TransactionDao transactionDao) {
		this.dao = dao;
		this.transactionDao = transactionDao;
	}
	
	public List<IJsonObject> executeRequest(HttpServletRequest request) {
		
		String message = "Unable to process request for account.";
		
		try {
			String id = request.getParameter(ID);
			
			Account account = dao.findById(Integer.valueOf(id));
			if (account == null)
				return Lists.newArrayList(new Transaction(null, null, null, null, message + " Account not available."));
			
			if (account.getAmount().compareTo(BigDecimal.ZERO) < 0)
				return Lists.newArrayList(new Transaction(null, null, null, null, message + " Account amount is less than zero."));
			
			dao.remove(Integer.valueOf(id));
			
			message = "Account delete successfully.";
			
			Transaction transaction = new Transaction(AccountAction.DELETE, Integer.valueOf(id), null, null, message);
			transactionDao.add(transaction);
			
			return Lists.newArrayList(transaction);
		} catch (Exception e) {
			return Lists.newArrayList(new Transaction(null, null, null, null, message));
		}
		
	}

}
