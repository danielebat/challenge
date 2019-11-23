package com.challenge.action.executor.account;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.challenge.action.executor.IRequestExecutor;
import com.challenge.data.model.Account;
import com.challenge.data.model.Transaction;
import com.challenge.data.store.AccountDao;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;

public class AccountWithdrawRequestExecutor implements IRequestExecutor {

	private static final String ID = "id";
	private static final String AMOUNT = "amount";
	private final AccountDao dao;
	private final TransactionDao transactionDao;
	
	@Inject
	public AccountWithdrawRequestExecutor(AccountDao dao, TransactionDao transactionDao) {
		this.dao = dao;
		this.transactionDao = transactionDao;
	}
	
	public Transaction executeRequest(HttpServletRequest request) {
		
		String message = "Unable to process request for account";
		
		try {
			String id = request.getParameter(ID);
			String amount = request.getParameter(AMOUNT);
			
			BigDecimal amountToWithdraw = new BigDecimal(amount);
			
			Account account = dao.findById(Integer.valueOf(id));
			if (account == null)
				return new Transaction(null, null, null, null, "Unable to process request for account. Account not available");
			
			if (account.getAmount().compareTo(BigDecimal.ZERO) < 0)
				return new Transaction(null, null, null, null, "Unable to process delete request for account. Account amount is less than zero.");
			
			if (account.getAmount().compareTo(amountToWithdraw) < 0)
				return new Transaction(null, null, null, null, "Unable to process delete request for account. Account amount is lower than withdrawal.");
			
			BigDecimal updatedAmount = dao.withdraw(account, amountToWithdraw);
			
			message = "Amount deposited successfully. Updated amount: " + updatedAmount + ".";
			
			Transaction transaction = new Transaction(AccountAction.WITHDRAW, Integer.valueOf(id), null, amountToWithdraw, message);
			transactionDao.add(transaction);
			
			return transaction;
		} catch (Exception e) {
			return new Transaction(null, null, null, null, message);
		}
		
	}

}
