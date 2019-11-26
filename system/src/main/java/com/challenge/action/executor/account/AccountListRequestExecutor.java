package com.challenge.action.executor.account;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.challenge.action.executor.AbstractRequestExecutor;
import com.challenge.data.model.JsonObject;
import com.challenge.data.store.AccountDao;
import com.google.common.collect.Lists;

/**
 * Class to process Account List Request
 */
public class AccountListRequestExecutor extends AbstractRequestExecutor {

	private static final String USER_ID = "id";
	private final AccountDao accountDao;
	
	@Inject
	public AccountListRequestExecutor(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	public List<JsonObject> executeRequest(HttpServletRequest request) {
		
		try {
			String userId = request.getParameter(USER_ID);
			return Lists.newArrayList(accountDao.findAllByUserId(Integer.valueOf(userId)));
		} catch (Exception e) {
			return generateResponseMessage(true, StringUtils.EMPTY, null);
		}
		
	}

}
