package com.challenge.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.challenge.data.model.IdentityObject;
import com.google.gson.Gson;

public abstract class ActionHandler extends AbstractHandler {

	/**
	 * Method to create Http response to send to requester
	 * @param response Http Response object
	 * @param baseRequest Http Base request
	 * @param jsonObjects List of objects to be put into the HTTP response
	 */
	public void buildHttpResponse(HttpServletResponse response, Request baseRequest, List<IdentityObject> jsonObjects) {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus((jsonObjects.size() == 0 || jsonObjects.get(0).getId() != null) ? HttpServletResponse.SC_OK :
			HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		String jsonString = new Gson().toJson(jsonObjects);

		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			System.err.println("Unable to create HTTP response.");
		}

		out.println(jsonString);
		baseRequest.setHandled(true);
	}
	

}
