package com.challenge.handler.account;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import com.challenge.action.executor.IRequestExecutor;
import com.challenge.data.model.Transaction;
import com.challenge.handler.ActionHandler;

public class AccountHandler extends ActionHandler {
	
	private final AccountRequestExecutorFactory factory;
	
	@Inject
	public AccountHandler(AccountRequestExecutorFactory factory) {
		this.factory = factory;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Transaction transaction = new Transaction(null, null, null, null, "Unable to process delete request for account.");
		
		IRequestExecutor actionExecutor = factory.create(AccountAction.valueOf(target.substring(1).toUpperCase()));
		if(actionExecutor != null)
			transaction = actionExecutor.executeRequest(request);
		
		buildHttpResponse(response, baseRequest, transaction);
	}

}
