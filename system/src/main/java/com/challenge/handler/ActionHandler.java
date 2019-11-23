package com.challenge.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public abstract class ActionHandler extends AbstractHandler {
	
	public void buildHttpResponse(HttpServletResponse response, Request baseRequest,
			int code, String responseStr) {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(code);

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
