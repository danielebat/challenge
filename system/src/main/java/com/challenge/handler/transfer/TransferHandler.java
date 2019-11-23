package com.challenge.handler.transfer;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import com.challenge.action.executor.account.TransferRequestExecutor;
import com.challenge.handler.ActionHandler;

public class TransferHandler extends ActionHandler {
	
	private final TransferRequestExecutor transferExecutor;

	@Inject
	public TransferHandler(TransferRequestExecutor transferExecutor) {
		this.transferExecutor = transferExecutor;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String responseStr = "Unable to process request for account.";
		
		responseStr = transferExecutor.executeRequest(request);
		
		buildHttpResponse(response, baseRequest, HttpServletResponse.SC_OK, responseStr);
	}

}
