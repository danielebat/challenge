package com.challenge.action.executor.account;

import static org.mockito.Mockito.mock;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.challenge.data.model.IJsonObject;
import com.challenge.data.store.AccountDao;
import com.google.common.collect.Lists;

public class AccountListResourceExtractorTest extends AccountRequestExecutorTest {
	
	AccountListRequestExecutor executor;
	private AccountDao dao;
	private HttpServletRequest request;
	
	@Before
	public void setup() {
		dao = mock(AccountDao.class);
		executor = new AccountListRequestExecutor(dao);
	}
	
	@Test
	public void testGivenWrongIdParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList());
		List<IJsonObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}

}
