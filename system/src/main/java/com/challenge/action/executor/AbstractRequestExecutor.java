package com.challenge.action.executor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.challenge.data.model.IJsonObject;
import com.challenge.data.model.Transaction;
import com.google.common.collect.Lists;

public abstract class AbstractRequestExecutor {
	
	public final static String ERROR_MESSAGE = "Unable to process request";
	
	public abstract List<IJsonObject> executeRequest(HttpServletRequest request);
	
	public List<IJsonObject> generateResponseMessage(boolean isError, String message, Transaction transaction) {
		String msg = (isError) ? ERROR_MESSAGE : message;
		if (isError && !message.equals(StringUtils.EMPTY))
			msg = msg + " - " + message;
		Transaction trsc = (isError) ? new Transaction(null, null, null, null, msg) : transaction;
		return Lists.newArrayList(trsc);
	}

}
