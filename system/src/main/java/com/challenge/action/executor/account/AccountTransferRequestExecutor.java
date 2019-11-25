package com.challenge.action.executor.account;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.challenge.action.executor.AbstractRequestExecutor;
import com.challenge.data.model.Account;
import com.challenge.data.model.IdentityObject;
import com.challenge.data.model.Transaction;
import com.challenge.data.store.AccountDao;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;
import com.challenge.util.transfer.TransferUtil;

/**
 * Class to process Account Amount Transfer Request
 */
public class AccountTransferRequestExecutor extends AbstractRequestExecutor {

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
	
	public List<IdentityObject> executeRequest(HttpServletRequest request) {
		
		try {
			String from = request.getParameter(FROM);
			String to = request.getParameter(TO);
			String amount = request.getParameter(AMOUNT);
			
			BigDecimal amountToTransfer = new BigDecimal(amount);
			
			if (amountToTransfer.compareTo(BigDecimal.ZERO) < 0)
				return generateResponseMessage(true, "Amount is less than zero", null);
			
			Account accountFrom = dao.findById(Integer.valueOf(from));
			Account accountTo = dao.findById(Integer.valueOf(to));
			if (accountFrom == null)
				return generateResponseMessage(true, "Source Account not available", null);
			
			if (accountTo == null)
				return generateResponseMessage(true, "Target Account not available", null);
			
			if (accountFrom.getAmount().compareTo(amountToTransfer) < 0)
				return generateResponseMessage(true, "Source Account amount is lower than amount to transfer", null);
			
			if (accountFrom == accountTo)
				return generateResponseMessage(true, "Source and Target Account are equal", null);
			
			dao.withdraw(accountFrom, amountToTransfer);
			BigDecimal convertedAmount = TransferUtil.convert(amountToTransfer, accountFrom.getCurrency(), accountTo.getCurrency());
			dao.deposit(accountTo, convertedAmount);
			
			Transaction transaction = new Transaction(AccountAction.TRANSFER, Integer.valueOf(from),
					Integer.valueOf(to), amountToTransfer, "Amount transferred successfully");
			transactionDao.add(transaction);
			
			return generateResponseMessage(false, StringUtils.EMPTY, transaction);
		} catch (Exception e) {
			return generateResponseMessage(true, StringUtils.EMPTY, null);
		}
		
	}

}
