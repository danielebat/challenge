package com.challenge.action.executor.account;

import java.math.BigDecimal;
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
 * Class to process Account Amount Withdraw Request
 */
public class AccountWithdrawRequestExecutor extends AbstractRequestExecutor {

	private static final String ID = "id";
	private static final String AMOUNT = "amount";
	private final AccountDao dao;
	private final TransactionDao transactionDao;
	
	@Inject
	public AccountWithdrawRequestExecutor(AccountDao dao, TransactionDao transactionDao) {
		this.dao = dao;
		this.transactionDao = transactionDao;
	}
	
	public List<JsonObject> executeRequest(HttpServletRequest request) {
		
		try {
			String id = request.getParameter(ID);
			String amount = request.getParameter(AMOUNT);
			
			BigDecimal amountToWithdraw = new BigDecimal(amount);
			
			if (amountToWithdraw.compareTo(BigDecimal.ZERO) < 0)
				return generateResponseMessage(true, "Amount is less than zero", null);
			
			Account account = dao.findById(Integer.valueOf(id));
			if (account == null)
				return generateResponseMessage(true, "Account not available", null);
			
			if (account.getAmount().compareTo(amountToWithdraw) < 0)
				return generateResponseMessage(true, "Account amount is lower than withdrawal", null);
			
			BigDecimal updatedAmount = dao.withdraw(account, amountToWithdraw);
			
			Transaction transaction = new Transaction(AccountAction.WITHDRAW, Integer.valueOf(id), null,
					amountToWithdraw, "Amount withdrawn successfully - Updated amount: " + updatedAmount);
			transactionDao.add(transaction);
			
			return generateResponseMessage(false, StringUtils.EMPTY, transaction);
		} catch (Exception e) {
			return generateResponseMessage(true, StringUtils.EMPTY, null);
		}
		
	}

}
