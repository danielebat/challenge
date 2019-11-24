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

public class AccountDepositRequestExecutor implements IRequestExecutor {

	private static final String ID = "id";
	private static final String AMOUNT = "amount";
	private final AccountDao dao;
	private TransactionDao transactionDao;
	
	@Inject
	public AccountDepositRequestExecutor(AccountDao dao, TransactionDao transactionDao) {
		this.dao = dao;
		this.transactionDao = transactionDao;
	}
	
	public List<IJsonObject> executeRequest(HttpServletRequest request) {
		
		String message = "Unable to process request for account.";
		
		try {
			String id = request.getParameter(ID);
			String amount = request.getParameter(AMOUNT);
			
			BigDecimal depositAmount = new BigDecimal(amount);
			
			Account account = dao.findById(Integer.valueOf(id));
			if (account == null)
				return Lists.newArrayList(new Transaction(null, null, null, null, message + " Account not available"));
			
			BigDecimal updatedAmount = dao.deposit(account, depositAmount);
			
			message = "Amount deposited successfully. Updated amount: " + updatedAmount + ".";
			
			Transaction transaction = new Transaction(AccountAction.DEPOSIT, Integer.valueOf(id), null, depositAmount, message);
			transactionDao.add(transaction);
			
			return Lists.newArrayList(transaction);
		} catch (Exception e) {
			return Lists.newArrayList(new Transaction(null, null, null, null, message));
		}
		
	}

}
