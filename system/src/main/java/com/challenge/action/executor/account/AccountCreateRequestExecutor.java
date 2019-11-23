package com.challenge.action.executor.account;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.challenge.data.model.Account;
import com.challenge.data.model.Currency;
import com.challenge.data.store.AccountDao;

public class AccountCreateRequestExecutor implements IAccountRequestExecutor {

	private static final String CURRENCY = "currency";
	private static final String AMOUNT = "amount";
	private final AccountDao dao;
	
	@Inject
	public AccountCreateRequestExecutor(AccountDao dao) {
		this.dao = dao;
	}
	
	public String executeRequest(HttpServletRequest request) {
		
		try {
			String amount = request.getParameter(AMOUNT);
			String currency = request.getParameter(CURRENCY);
			
			BigDecimal accountAmount = new BigDecimal(amount);
			
			Account account = new Account(accountAmount, Currency.valueOf(currency.toUpperCase()));
			
			Integer id = dao.add(account);
			
			return "Account created successfully. IBAN: " + account.getIbanCode() +"|"+ String.valueOf(id);
		} catch (Exception e) {
			return "Unable to process request for account";
		}
		
	}

}
