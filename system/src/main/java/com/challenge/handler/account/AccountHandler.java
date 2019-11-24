package com.challenge.handler.account;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Request;

import com.challenge.action.executor.AbstractRequestExecutor;
import com.challenge.data.model.IJsonObject;
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
		
		AbstractRequestExecutor actionExecutor = factory.create(AccountAction.valueOf(target.substring(1).toUpperCase()));
		List<IJsonObject> jsonObjects = actionExecutor.generateResponseMessage(true, StringUtils.EMPTY, null);
		if(actionExecutor != null)
			jsonObjects = actionExecutor.executeRequest(request);
		
		buildHttpResponse(response, baseRequest, jsonObjects);
	}

}
