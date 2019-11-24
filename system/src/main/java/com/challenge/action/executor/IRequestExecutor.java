package com.challenge.action.executor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.challenge.data.model.IJsonObject;

public interface IRequestExecutor {
	
	List<IJsonObject> executeRequest(HttpServletRequest request);

}
