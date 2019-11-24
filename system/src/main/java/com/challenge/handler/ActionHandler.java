package com.challenge.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.challenge.data.model.Transaction;
import com.google.gson.JsonObject;

public abstract class ActionHandler extends AbstractHandler {

	public void buildHttpResponse(HttpServletResponse response, Request baseRequest, Transaction transaction) {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus((transaction.getId() == null) ? 
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR : HttpServletResponse.SC_OK);
		
		JsonObject message = new JsonObject();
		message.addProperty("transactionId", transaction.getId());
		message.addProperty("sourceId", transaction.getSourceAccountId());
		message.addProperty("targetId", transaction.getTargetAccountId());
		message.addProperty("amount", transaction.getAmount());
		message.addProperty("message", transaction.getMessage());

		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
//			handle exception
		}

		out.println(message.toString());
		baseRequest.setHandled(true);
	}
	

}
