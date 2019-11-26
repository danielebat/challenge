package com.challenge.handler.account;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import com.challenge.action.executor.AbstractRequestExecutor;
import com.challenge.data.model.JsonObject;
import com.challenge.data.model.Transaction;
import com.challenge.handler.ActionHandler;
import com.google.common.collect.Lists;

/**
 * Class to handle Account HTTP request and process acccording to target value
 */
public class AccountHandler extends ActionHandler {
	
	private final AccountRequestExecutorFactory factory;
	
	@Inject
	public AccountHandler(AccountRequestExecutorFactory factory) {
		this.factory = factory;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		AccountAction action;
		try {
			action = AccountAction.valueOf(target.substring(1).toUpperCase());
		} catch (IllegalArgumentException e) {
			action = null;
		}
		AbstractRequestExecutor actionExecutor = factory.create(action);
		List<JsonObject> jsonObjects = Lists.newArrayList(new Transaction(null, null, null, null, AbstractRequestExecutor.ERROR_MESSAGE));
		if(actionExecutor != null)
			jsonObjects = actionExecutor.executeRequest(request);
		
		buildHttpResponse(response, baseRequest, jsonObjects);
	}

}
