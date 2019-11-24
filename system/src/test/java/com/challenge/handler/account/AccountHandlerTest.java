package com.challenge.handler.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.junit.Before;
import org.junit.Test;

import com.challenge.action.executor.AbstractRequestExecutor;
import com.google.common.collect.Lists;

public class AccountHandlerTest {
	
	AccountHandler handler;
	AccountRequestExecutorFactory factory;
	AbstractRequestExecutor executor;
	
	Request request;
	HttpServletRequest httpRequest;
	HttpServletResponse httpResponse;
	
	PrintWriter writer;
	
	@Before
	public void setup() {
		
		writer = mock(PrintWriter.class);
		
		request = mock(Request.class);
		httpRequest = mock(HttpServletRequest.class);
		httpResponse = mock(HttpServletResponse.class);
		
		executor = mock(AbstractRequestExecutor.class);
		factory = mock(AccountRequestExecutorFactory.class);
		handler = new AccountHandler(factory);
	}
	
	@Test
	public void testGivenRequestParamsWhenHandleMethodIsCalledThenInternalMethodsAreCalledWithCorrectParameters() throws Exception {
		
		when(httpResponse.getWriter()).thenReturn(writer);
		
		when(factory.create(any(AccountAction.class))).thenReturn(executor);
		when(executor.executeRequest(httpRequest)).thenReturn(Lists.newArrayList());
		
		handler.handle("/list", request, httpRequest, httpResponse);
		
		verify(executor).executeRequest(httpRequest);
	}
	
	@Test
	public void testGivenWrongTargetWhenHandleMethodIsCalledThenExecuteRequestMethodIsNotCalled() throws Exception {
		
		when(httpResponse.getWriter()).thenReturn(writer);
		
		when(factory.create(any(AccountAction.class))).thenReturn(executor);
		when(executor.executeRequest(httpRequest)).thenReturn(Lists.newArrayList());
		
		handler.handle("list", request, httpRequest, httpResponse);
		
		verify(executor, times(0)).executeRequest(httpRequest);
	}

}
