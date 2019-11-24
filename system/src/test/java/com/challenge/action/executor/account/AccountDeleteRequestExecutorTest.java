package com.challenge.action.executor.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.challenge.data.model.Account;
import com.challenge.data.model.Currency;
import com.challenge.data.model.IJsonObject;
import com.challenge.data.store.AccountDao;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;
import com.google.common.collect.Lists;

public class AccountDeleteRequestExecutorTest extends AccountRequestExecutorTest {
	
	AccountDeleteRequestExecutor executor;
	private AccountDao dao;
	private TransactionDao transactionDao;
	private HttpServletRequest request;
	
	@Before
	public void setup() {
		dao = mock(AccountDao.class);
		transactionDao = mock(TransactionDao.class);
		executor = new AccountDeleteRequestExecutor(dao, transactionDao);
	}
	
	@Test
	public void testGivenCorrectParametersWhenExecutingRequestThenAccountIsDeletedAndTransactionIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("id", "53"));
		
		Account account = new Account(new BigDecimal(1000), Currency.EUR, 3);
		account.setId(53);
		
		when(dao.findById(53)).thenReturn(account);
		
		List<IJsonObject> trList = executor.executeRequest(request);
		
		assertEquals(1, trList.size());
		
		assertReturnedTransaction(trList, transactionDao, AccountAction.DELETE,
				null, "Account delete successfully",
				new Integer(53), null);
	}
	
	@Test
	public void testGivenWrongIdParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList());
		List<IJsonObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
	
	@Test
	public void testGivenNotAvailableAccountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("id", "53"));
		
		when(dao.findById(53)).thenReturn(null);
		
		List<IJsonObject> trList = executor.executeRequest(request);
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Account not available");
	}
	
	@Test
	public void testGivenNegativeAccountAmountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("id", "53"));
		
		Account account = new Account(new BigDecimal(-1000), Currency.EUR, 3);
		account.setId(53);
		
		when(dao.findById(53)).thenReturn(account);
		
		List<IJsonObject> trList = executor.executeRequest(request);
		
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Account amount is less than zero");
	}

}
