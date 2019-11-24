package com.challenge.action.executor.account;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.challenge.action.executor.AbstractRequestExecutor;
import com.challenge.data.model.Account;
import com.challenge.data.model.IJsonObject;
import com.challenge.data.model.Transaction;
import com.challenge.data.store.AccountDao;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;

public class AccountDepositRequestExecutor extends AbstractRequestExecutor {

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
		
		try {
			String id = request.getParameter(ID);
			String amount = request.getParameter(AMOUNT);
			
			BigDecimal depositAmount = new BigDecimal(amount);
			
			Account account = dao.findById(Integer.valueOf(id));
			if (account == null)
				return generateResponseMessage(true, "Account not available", null);
			
			BigDecimal updatedAmount = dao.deposit(account, depositAmount);
			
			Transaction transaction = new Transaction(AccountAction.DEPOSIT, Integer.valueOf(id), null, 
					depositAmount, "Amount deposited successfully - Updated amount: " + updatedAmount);
			transactionDao.add(transaction);
			
			return generateResponseMessage(false, StringUtils.EMPTY, transaction);
		} catch (Exception e) {
			return generateResponseMessage(true, StringUtils.EMPTY, null);
		}
		
	}

}
