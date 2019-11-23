package com.challenge.action.executor.account;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.challenge.action.executor.IRequestExecutor;
import com.challenge.data.model.Account;
import com.challenge.data.store.AccountDao;

public class AccountDeleteRequestExecutor implements IRequestExecutor {

	private static final String ID = "id";
	private final AccountDao dao;
	
	@Inject
	public AccountDeleteRequestExecutor(AccountDao dao) {
		this.dao = dao;
	}
	
	public String executeRequest(HttpServletRequest request) {
		
		try {
			String id = request.getParameter(ID);
			
			Account account = dao.findById(Integer.valueOf(id));
			if (account == null)
				return "Unable to process delete request for account. Account not available.";
			
			if (account.getAmount().compareTo(BigDecimal.ZERO) < 0)
				return "Unable to process delete request for account. Account amount is less than zero.";
			
			dao.remove(Integer.valueOf(id));
			
			return "Account delete successfully.";
		} catch (Exception e) {
			return "Unable to process request for account";
		}
		
	}

}
