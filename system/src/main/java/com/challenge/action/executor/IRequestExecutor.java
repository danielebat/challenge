package com.challenge.action.executor;

import javax.servlet.http.HttpServletRequest;

public interface IRequestExecutor {
	
	String executeRequest(HttpServletRequest request);

}
