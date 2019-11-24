package com.challenge.action.executor.transaction;

import static org.mockito.Mockito.mock;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.challenge.action.executor.account.RequestExecutorTest;
import com.challenge.data.model.IJsonObject;
import com.challenge.data.store.TransactionDao;
import com.google.common.collect.Lists;

public class TransactionListRequestExecutorTest extends RequestExecutorTest {
	
	TransactionListRequestExecutor executor;
	private TransactionDao dao;
	private HttpServletRequest request;
	
	@Before
	public void setup() {
		dao = mock(TransactionDao.class);
		executor = new TransactionListRequestExecutor(dao);
	}
	
	@Test
	public void testGivenWrongIdParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList());
		List<IJsonObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}

}
