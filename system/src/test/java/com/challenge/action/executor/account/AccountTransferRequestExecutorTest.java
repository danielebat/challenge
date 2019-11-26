package com.challenge.action.executor.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

public class AccountTransferRequestExecutorTest extends RequestExecutorTest {
	
	AccountTransferRequestExecutor executor;
	private AccountDao dao;
	private TransactionDao transactionDao;
	private HttpServletRequest request;
	
	@Before
	public void setup() {
		dao = mock(AccountDao.class);
		transactionDao = mock(TransactionDao.class);
		executor = new AccountTransferRequestExecutor(dao, transactionDao);
	}
	
	@Test
	public void testGivenCorrectParametersWhenExecutingRequestThenAmountIsTransferredAndTransactionIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("amount", "300", "from", "53", "to", "54"));
		
		Account accountFrom = new Account(new BigDecimal(1000), Currency.EUR, 3);
		accountFrom.setId(53);
		when(dao.findById(53)).thenReturn(accountFrom);
		
		Account accountTo = new Account(new BigDecimal(1000), Currency.EUR, 3);
		accountFrom.setId(54);
		when(dao.findById(54)).thenReturn(accountTo);
		
		List<JsonObject> trList = executor.executeRequest(request);
		
		verify(dao).withdraw(accountFrom, new BigDecimal(300));
		verify(dao).deposit(accountTo, new BigDecimal(300));
		
		assertEquals(1, trList.size());
		
		assertReturnedTransaction(trList, transactionDao, AccountAction.TRANSFER,
				new BigDecimal(300), "Amount transferred successfully",
				new Integer(53), new Integer(54));
	}
	
	@Test
	public void testGivenWrongAmountParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList("from", "53", "to", "54"));
		List<JsonObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
	
	@Test
	public void testGivenWrongFromParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList("from", "53", "amount", "300"));
		List<JsonObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
	
	@Test
	public void testGivenWrongToParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList("to", "54", "amount", "300"));
		List<JsonObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
	
	@Test
	public void testGivenNotAvailableFromAccountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("amount", "300", "from", "53", "to", "54"));
		
		when(dao.findById(53)).thenReturn(null);
		
		List<JsonObject> trList = executor.executeRequest(request);
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Source Account not available");
	}
	
	@Test
	public void testGivenNotAvailableToAccountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("amount", "300", "from", "53", "to", "54"));
		
		when(dao.findById(53)).thenReturn(new Account(new BigDecimal(1000), Currency.EUR, 53));
		when(dao.findById(54)).thenReturn(null);
		
		List<JsonObject> trList = executor.executeRequest(request);
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Target Account not available");
	}
	
	@Test
	public void testGivenAmountToTransferGreaterThanAccountAmountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("amount", "300", "from", "53", "to", "54"));
		
		Account accountFrom = new Account(new BigDecimal(200), Currency.EUR, 3);
		accountFrom.setId(53);
		when(dao.findById(53)).thenReturn(accountFrom);
		
		Account accountTo = new Account(new BigDecimal(1000), Currency.EUR, 3);
		accountFrom.setId(54);
		when(dao.findById(54)).thenReturn(accountTo);
		
		List<JsonObject> trList = executor.executeRequest(request);
		
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Source Account amount is lower than amount to transfer");
	}
	
	@Test
	public void testFromAndToEqualAccountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("amount", "300", "from", "53", "to", "53"));
		
		Account accountFrom = new Account(new BigDecimal(1000), Currency.EUR, 3);
		accountFrom.setId(53);
		when(dao.findById(53)).thenReturn(accountFrom).thenReturn(accountFrom);
		
		List<JsonObject> trList = executor.executeRequest(request);
		
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Source and Target Account are equal");
	}
	
	@Test
	public void testGivenNegativeAmountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("amount", "-300", "from", "53", "to", "55"));
		
		List<JsonObject> trList = executor.executeRequest(request);
		
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Amount is less than zero");
	}

}
