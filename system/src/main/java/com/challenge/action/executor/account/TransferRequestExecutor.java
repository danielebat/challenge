package com.challenge.action.executor.account;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.challenge.action.executor.IRequestExecutor;
import com.challenge.data.model.Account;
import com.challenge.data.store.AccountDao;
import com.challenge.util.transfer.TransferUtil;

public class TransferRequestExecutor implements IRequestExecutor {

	private static final String FROM = "from";
	private static final String TO = "to";
	private static final String AMOUNT = "amount";
	private final AccountDao dao;
	
	@Inject
	public TransferRequestExecutor(AccountDao dao) {
		this.dao = dao;
	}
	
	public String executeRequest(HttpServletRequest request) {
		
		try {
			String from = request.getParameter(FROM);
			String to = request.getParameter(TO);
			String amount = request.getParameter(AMOUNT);
			
			BigDecimal amountToTransfer = new BigDecimal(amount);
			
			Account accountFrom = dao.findById(Integer.valueOf(from));
			Account accountTo = dao.findById(Integer.valueOf(to));
			if (accountFrom == null)
				return "Unable to process request for account. Source Account not available";
			
			if (accountTo == null)
				return "Unable to process request for account. Target Account not available";
			
			if (accountFrom.getAmount().compareTo(BigDecimal.ZERO) < 0)
				return "Unable to process delete request for account. Source account amount is less than zero.";
			
			if (accountFrom.getAmount().compareTo(amountToTransfer) < 0)
				return "Unable to process delete request for account. Source account amount is lower than transfer amount.";
			
			if (accountFrom == accountTo)
				return "Unable to process request for account. Source and Target Account are equal.";
			
			dao.withdraw(accountFrom, amountToTransfer);
			BigDecimal convertedAmount = TransferUtil.convert(amountToTransfer, accountFrom.getCurrency(), accountTo.getCurrency());
			dao.deposit(accountTo, convertedAmount);
			
			return "Amount transferred successfully.";
		} catch (Exception e) {
			return "Unable to process request for account";
		}
		
	}

}
