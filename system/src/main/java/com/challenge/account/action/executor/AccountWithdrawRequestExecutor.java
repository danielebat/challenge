package com.challenge.account.action.executor;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.challenge.data.model.Account;
import com.challenge.data.store.AccountDao;

public class AccountWithdrawRequestExecutor implements IAccountRequestExecutor {

	private static final String ID = "id";
	private static final String AMOUNT = "amount";
	private final AccountDao dao;
	
	@Inject
	public AccountWithdrawRequestExecutor(AccountDao dao) {
		this.dao = dao;
	}
	
	public String executeRequest(HttpServletRequest request) {
		
		try {
			String id = request.getParameter(ID);
			String amount = request.getParameter(AMOUNT);
			
			BigDecimal amountToWithdraw = new BigDecimal(amount);
			
			Account account = dao.findById(Integer.valueOf(id));
			if (account == null)
				return "Unable to process request for account. Account not available";
			
			if (account.getAmount().compareTo(BigDecimal.ZERO) < 0)
				return "Unable to process delete request for account. Account amount is less than zero.";
			
			if (account.getAmount().compareTo(amountToWithdraw) < 0)
				return "Unable to process delete request for account. Account amount is lower than withdrawal.";
			
			BigDecimal updatedAmount = dao.withdraw(account, amountToWithdraw);
			
			return "Amount deposited successfully. Updated amount: " + updatedAmount;
		} catch (Exception e) {
			return "Unable to process request for account";
		}
		
	}

}
