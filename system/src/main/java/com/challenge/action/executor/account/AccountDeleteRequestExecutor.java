package com.challenge.action.executor.account;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.challenge.action.executor.AbstractRequestExecutor;
import com.challenge.data.model.Account;
import com.challenge.data.model.JsonObject;
import com.challenge.data.model.Transaction;
import com.challenge.data.store.AccountDao;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;

/**
 * Class to process Account Delete Request
 */
public class AccountDeleteRequestExecutor extends AbstractRequestExecutor {

	private static final String ID = "id";
	private final AccountDao dao;
	private final TransactionDao transactionDao;
	
	@Inject
	public AccountDeleteRequestExecutor(AccountDao dao, TransactionDao transactionDao) {
		this.dao = dao;
		this.transactionDao = transactionDao;
	}
	
	public List<JsonObject> executeRequest(HttpServletRequest request) {
		
		try {
			String id = request.getParameter(ID);
			
			Account account = dao.findById(Integer.valueOf(id));
			if (account == null)
				return generateResponseMessage(true, "Account not available", null);
			
			dao.remove(Integer.valueOf(id));
			
			Transaction transaction = new Transaction(AccountAction.DELETE, Integer.valueOf(id),
					null, null, "Account delete successfully");
			transactionDao.add(transaction);
			
			return generateResponseMessage(false, StringUtils.EMPTY, transaction);
		} catch (Exception e) {
			return generateResponseMessage(true, StringUtils.EMPTY, null);
		}
		
	}

}
