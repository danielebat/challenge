package com.challenge.handler.transaction;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Request;

import com.challenge.action.executor.transaction.TransactionListRequestExecutor;
import com.challenge.data.model.IdentityObject;
import com.challenge.handler.ActionHandler;

/**
 * Class to handle Transaction HTTP request and process acccording to target value
 */
public class TransactionHandler extends ActionHandler {

	private final TransactionListRequestExecutor listExec;
	
	@Inject
	public TransactionHandler(TransactionListRequestExecutor listExec) {
		this.listExec = listExec;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		TransactionAction action;
		try {
			action = TransactionAction.valueOf(target.substring(1).toUpperCase());
		} catch (IllegalArgumentException e) {
			action = null;
		}
		
		List<IdentityObject> jsonObjects = listExec.generateResponseMessage(true, StringUtils.EMPTY, null);
		if (TransactionAction.LIST.equals(action))
			jsonObjects = listExec.executeRequest(request);
		
		buildHttpResponse(response, baseRequest, jsonObjects);
	}

}
