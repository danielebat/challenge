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
import com.challenge.util.transfer.TransferUtil;

public class AccountTransferRequestExecutor implements IRequestExecutor {

	private static final String FROM = "from";
	private static final String TO = "to";
	private static final String AMOUNT = "amount";
	private final AccountDao dao;
	private final TransactionDao transactionDao;
	
	@Inject
	public AccountTransferRequestExecutor(AccountDao dao, TransactionDao transactionDao) {
		this.dao = dao;
		this.transactionDao = transactionDao;
	}
	
	public Transaction executeRequest(HttpServletRequest request) {
		
		String message = "Unable to process request for account.";
		
		try {
			String from = request.getParameter(FROM);
			String to = request.getParameter(TO);
			String amount = request.getParameter(AMOUNT);
			
			BigDecimal amountToTransfer = new BigDecimal(amount);
			
			Account accountFrom = dao.findById(Integer.valueOf(from));
			Account accountTo = dao.findById(Integer.valueOf(to));
			if (accountFrom == null)
				return new Transaction(null, null, null, null, message + " Source Account not available");
			
			if (accountTo == null)
				return new Transaction(null, null, null, null, message + " Target Account not available");
			
			if (accountFrom.getAmount().compareTo(BigDecimal.ZERO) < 0)
				return new Transaction(null, null, null, null, message + ". Source account amount is less than zero.");
			
			if (accountFrom.getAmount().compareTo(amountToTransfer) < 0)
				return new Transaction(null, null, null, null, message + " Source account amount is lower than transfer amount.");
			
			if (accountFrom == accountTo)
				return new Transaction(null, null, null, null, message + " Source and Target Account are equal.");
			
			dao.withdraw(accountFrom, amountToTransfer);
			BigDecimal convertedAmount = TransferUtil.convert(amountToTransfer, accountFrom.getCurrency(), accountTo.getCurrency());
			dao.deposit(accountTo, convertedAmount);
			
			message = "Amount transferred successfully.";
			
			Transaction transaction = new Transaction(AccountAction.TRANSFER, Integer.valueOf(from), Integer.valueOf(to), amountToTransfer, message);
			transactionDao.add(transaction);
			
			return transaction;
		} catch (Exception e) {
			return new Transaction(null, null, null, null, message);
		}
		
	}

}
