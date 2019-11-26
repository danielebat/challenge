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
import com.challenge.data.model.JsonObject;
import com.challenge.data.store.AccountDao;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;
import com.google.common.collect.Lists;

public class AccountDepositRequestExecutorTest extends RequestExecutorTest {

	AccountDepositRequestExecutor executor;
	private AccountDao dao;
	private TransactionDao transactionDao;
	private HttpServletRequest request;
	
	@Before
	public void setup() {
		dao = mock(AccountDao.class);
		transactionDao = mock(TransactionDao.class);
		executor = new AccountDepositRequestExecutor(dao, transactionDao);
	}
	
	@Test
	public void testGivenCorrectParametersWhenExecutingRequestThenAmountIsDepositedAndTransactionIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("amount", "500", "id", "53"));
		
		Account account = new Account(new BigDecimal(1000), Currency.EUR, 3);
		account.setId(53);
		
		when(dao.findById(53)).thenReturn(account);
		when(dao.deposit(account, new BigDecimal(500))).thenReturn(new BigDecimal(1500));
		
		List<JsonObject> trList = executor.executeRequest(request);
		assertEquals(1, trList.size());
		assertReturnedTransaction(trList, transactionDao, AccountAction.DEPOSIT,
				new BigDecimal(500), "Amount deposited successfully - Updated amount: 1500",
				new Integer(53), null);
	}
	
	@Test
	public void testGivenWrongIdParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList("amount", "500"));
		List<JsonObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
	
	@Test
	public void testGivenWrongAmountParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList("id", "53"));
		List<JsonObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
	
	@Test
	public void testGivenNotAvailableAccountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("amount", "500", "id", "53"));
		
		when(dao.findById(53)).thenReturn(null);
		
		List<JsonObject> trList = executor.executeRequest(request);
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Account not available");
	}
	
	@Test
	public void testGivenNegativeAmountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("id", "53", "amount", "-1000"));
		
		List<JsonObject> trList = executor.executeRequest(request);
		
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Amount is less than zero");
	}
	
	
}
