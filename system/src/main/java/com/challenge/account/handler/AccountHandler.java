package com.challenge.account.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.challenge.data.store.AccountDao;

public class AccountHandler extends AbstractHandler {
	
	private final AccountDao dao;
	
	@Inject
	public AccountHandler(AccountDao dao) {
		this.dao = dao;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		System.out.println("---");
		System.out.println(target);
		System.out.println("---");
		System.out.println(baseRequest);
		System.out.println("---");
		System.out.println(request);
		System.out.println("---");
		System.out.println(response);
		
		response.setContentType("text/plain; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        out.println("pippoPluto");

        baseRequest.setHandled(true);
			

	}

}
