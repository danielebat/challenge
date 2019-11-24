package com.challenge.handler.transaction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Request;
import org.junit.Before;
import org.junit.Test;

import com.challenge.action.executor.transaction.TransactionListRequestExecutor;
import com.challenge.data.model.Transaction;
import com.challenge.handler.account.AccountAction;
import com.google.common.collect.Lists;

public class TransactionHandlerTest {

	TransactionHandler handler;
	TransactionListRequestExecutor executor;
	
	Request request;
	HttpServletRequest httpRequest;
	HttpServletResponse httpResponse;
	
	PrintWriter writer;
	Transaction tr;
	
	@Before
	public void setup() {
		
		writer = mock(PrintWriter.class);
		
		request = mock(Request.class);
		httpRequest = mock(HttpServletRequest.class);
		httpResponse = mock(HttpServletResponse.class);
		
		executor = mock(TransactionListRequestExecutor.class);
		handler = new TransactionHandler(executor);
		
		tr = new Transaction(AccountAction.CREATE, 1, null, new BigDecimal(300), StringUtils.EMPTY);
	}
	
	@Test
	public void testGivenRequestParamsWhenHandleMethodIsCalledThenInternalMethodsAreCalledWithCorrectParameters() throws Exception {
		
		when(httpResponse.getWriter()).thenReturn(writer);
		
		tr.setId(1);
		when(executor.executeRequest(httpRequest)).thenReturn(Lists.newArrayList(tr));
		
		handler.handle("/list", request, httpRequest, httpResponse);
		
		verify(executor).executeRequest(httpRequest);
	}
	
	@Test
	public void testGivenWrongTargetWhenHandleMethodIsCalledThenExecuteRequestMethodIsNotCalled() throws Exception {
		
		when(httpResponse.getWriter()).thenReturn(writer);
		
		tr.setId(null);
		when(executor.generateResponseMessage(true, StringUtils.EMPTY, null)).thenReturn(Lists.newArrayList(tr));
		
		handler.handle("list", request, httpRequest, httpResponse);
		
		verify(executor, times(0)).executeRequest(httpRequest);
	}
}
