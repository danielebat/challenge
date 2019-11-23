package com.challenge.action.executor;

import javax.servlet.http.HttpServletRequest;

import com.challenge.data.model.Transaction;

public interface IRequestExecutor {
	
	Transaction executeRequest(HttpServletRequest request);

}
