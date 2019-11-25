package com.challenge.action.executor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.challenge.data.model.IdentityObject;
import com.challenge.data.model.JsonObject;
import com.google.common.collect.Lists;

public abstract class AbstractRequestExecutor {
	
	public final static String ERROR_MESSAGE = "Unable to process request";
	
	/**
	 * Method to process an HTTP request
	 * @param request HTTP request
	 * @return list of objects to be put into HTTP response
	 */
	public abstract List<IdentityObject> executeRequest(HttpServletRequest request);
	
	/**
	 * Method to create list of objects to be put into HTTP response
	 * @param isError Whether the response is an error response or not
	 * @param message Message to be put into returned object
	 * @param jsonObject object to return as json object into
	 * @return
	 */
	public List<IdentityObject> generateResponseMessage(boolean isError, String message, JsonObject jsonObject) {
		String msg = (isError) ? ERROR_MESSAGE : message;
		if (isError && !message.equals(StringUtils.EMPTY))
			msg = msg + " - " + message;
		JsonObject jsonObj = (isError) ? new JsonObject(msg) : jsonObject;
		return Lists.newArrayList(jsonObj);
	}

}
