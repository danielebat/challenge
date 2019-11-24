package com.challenge.handler.transaction;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import com.challenge.action.executor.transaction.TransactionListRequestExecutor;
import com.challenge.data.model.IJsonObject;
import com.challenge.data.model.Transaction;
import com.challenge.handler.ActionHandler;
import com.google.common.collect.Lists;

public class TransactionHandler extends ActionHandler {

	private final TransactionListRequestExecutor listExec;
	
	@Inject
	public TransactionHandler(TransactionListRequestExecutor listExec) {
		this.listExec = listExec;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		List<IJsonObject> jsonObjects = Lists.newArrayList(new Transaction(null, null, null, null, "Unable to process request."));
		
		if (TransactionAction.LIST.equals(target.substring(1).toUpperCase()))
			jsonObjects = listExec.executeRequest(request);
		
		buildHttpResponse(response, baseRequest, jsonObjects);
	}

}
