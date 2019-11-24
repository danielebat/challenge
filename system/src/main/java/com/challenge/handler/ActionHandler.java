package com.challenge.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.challenge.data.model.IJsonObject;
import com.google.gson.Gson;

public abstract class ActionHandler extends AbstractHandler {

	public void buildHttpResponse(HttpServletResponse response, Request baseRequest, List<IJsonObject> jsonObjects) {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus((jsonObjects.get(0).getId() == null) ? HttpServletResponse.SC_INTERNAL_SERVER_ERROR :
			HttpServletResponse.SC_OK);
		
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
