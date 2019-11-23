package com.challenge.action.executor.account;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.challenge.data.model.Account;
import com.challenge.data.store.AccountDao;

public class AccountDepositRequestExecutor implements IAccountRequestExecutor {

	private static final String ID = "id";
	private static final String AMOUNT = "amount";
	private final AccountDao dao;
	
	@Inject
	public AccountDepositRequestExecutor(AccountDao dao) {
		this.dao = dao;
	}
	
	public String executeRequest(HttpServletRequest request) {
		
		try {
			String id = request.getParameter(ID);
			String amount = request.getParameter(AMOUNT);
			
			BigDecimal depositAmount = new BigDecimal(amount);
			
			Account account = dao.findById(Integer.valueOf(id));
			if (account == null)
				return "Unable to process request for account. Account not available";
			
			BigDecimal updatedAmount = dao.deposit(account, depositAmount);
			
			return "Amount deposited successfully. Updated amount: " + updatedAmount;
		} catch (Exception e) {
			return "Unable to process request for account";
		}
		
	}

}
