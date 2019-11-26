package com.challenge.action.executor.account;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.challenge.action.executor.AbstractRequestExecutor;
import com.challenge.data.model.Account;
import com.challenge.data.model.Currency;
import com.challenge.data.model.JsonObject;
import com.challenge.data.model.Transaction;
import com.challenge.data.store.AccountDao;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;

/**
 * Class to process Account Create Request
 */
public class AccountCreateRequestExecutor extends AbstractRequestExecutor {

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
	
	public List<JsonObject> executeRequest(HttpServletRequest request) {
		
		try {
			String amount = request.getParameter(AMOUNT);
			String currency = request.getParameter(CURRENCY);
			String userId = request.getParameter(USER_ID);
			
			BigDecimal accountAmount = new BigDecimal(amount);
			
			if (accountAmount.compareTo(BigDecimal.ZERO) < 0)
				return generateResponseMessage(true, "Amount is less than zero", null);
			
			Account account = new Account(accountAmount, 
					Currency.valueOf(currency.toUpperCase()), Integer.valueOf(userId));
			Integer id = dao.add(account);
			
			Transaction transaction = new Transaction(AccountAction.CREATE, id, null,
					accountAmount, "Account created successfully. IBAN: " + account.getIbanCode());
			transactionDao.add(transaction);
			
			return generateResponseMessage(false, StringUtils.EMPTY, transaction);
		} catch (Exception e) {
			return generateResponseMessage(true, StringUtils.EMPTY, null);
		}
		
	}

}
