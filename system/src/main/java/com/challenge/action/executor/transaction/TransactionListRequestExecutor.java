package com.challenge.action.executor.transaction;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.challenge.action.executor.AbstractRequestExecutor;
import com.challenge.data.model.IJsonObject;
import com.challenge.data.store.TransactionDao;
import com.google.common.collect.Lists;

public class TransactionListRequestExecutor extends AbstractRequestExecutor {

	private static final String ACCOUNT_ID = "id";
	private final TransactionDao transactionDao;
	
	@Inject
	public TransactionListRequestExecutor(TransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}
	
	public List<IJsonObject> executeRequest(HttpServletRequest request) {
		
		try {
			String accountId = request.getParameter(ACCOUNT_ID);
			return Lists.newArrayList(transactionDao.findAllByAccountId(Integer.valueOf(accountId)));
		} catch (Exception e) {
			return generateResponseMessage(true, StringUtils.EMPTY, null);
		}
		
	}

}
