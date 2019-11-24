package com.challenge.action.executor.account;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.challenge.action.executor.IRequestExecutor;
import com.challenge.data.model.Account;
import com.challenge.data.model.Currency;
import com.challenge.data.model.Transaction;
import com.challenge.data.store.AccountDao;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;

public class AccountCreateRequestExecutor implements IRequestExecutor {

	private static final String USER_ID = "userId";
	private static final String CURRENCY = "currency";
	private static final String AMOUNT = "amount";
	private final AccountDao dao;
	private final TransactionDao transactionDao;
	
	@Inject
	public AccountCreateRequestExecutor(AccountDao dao, TransactionDao transactionDao) {
		this.dao = dao;
		this.transactionDao = transactionDao;
	}
	
	public Transaction executeRequest(HttpServletRequest request) {
		
		String message = "Unable to process request for account.";
		
		try {
			String amount = request.getParameter(AMOUNT);
			String currency = request.getParameter(CURRENCY);
			String userId = request.getParameter(USER_ID);
			
			BigDecimal accountAmount = new BigDecimal(amount);
			
			Account account = new Account(accountAmount, 
					Currency.valueOf(currency.toUpperCase()), Integer.valueOf(userId));
			Integer id = dao.add(account);
			
			message = "Account created successfully. IBAN: " + account.getIbanCode() +".";
			
			Transaction transaction = new Transaction(AccountAction.CREATE, id, null, accountAmount, message);
			transactionDao.add(transaction);
			
			return transaction;
		} catch (Exception e) {
			return new Transaction(null, null, null, null, message);
		}
		
	}

}
