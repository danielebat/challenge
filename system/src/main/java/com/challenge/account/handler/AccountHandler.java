package com.challenge.account.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.challenge.account.action.executor.IAccountRequestExecutor;
import com.challenge.data.store.AccountDao;

public class AccountHandler extends AbstractHandler {
	
	private final AccountRequestExecutorFactory factory;
	
	@Inject
	public AccountHandler(AccountRequestExecutorFactory factory) {
		this.factory = factory;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String responseStr = "Unable to process request for account.";
		
//		System.out.println("---");
//		System.out.println(target);
//		System.out.println("---");
//		System.out.println(baseRequest);
//		System.out.println("---");
//		System.out.println(request);
//		System.out.println("---");
//		System.out.println(response);
		
		IAccountRequestExecutor actionExecutor = factory.create(AccountAction.valueOf(target.substring(1).toUpperCase()));
		if(actionExecutor != null)
			responseStr = actionExecutor.executeRequest(request);
		
		buildHttpResponse(response, baseRequest, responseStr);
			

	}

	private void buildHttpResponse(HttpServletResponse response, Request baseRequest,
			String responseStr) {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
//			handle exception
		}

		out.println(responseStr);
		baseRequest.setHandled(true);
	}

	

}
