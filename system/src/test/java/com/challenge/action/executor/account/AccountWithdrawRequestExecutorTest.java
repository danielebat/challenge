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

public class AccountWithdrawRequestExecutorTest extends RequestExecutorTest {

	AccountWithdrawRequestExecutor executor;
	private AccountDao dao;
	private TransactionDao transactionDao;
	private HttpServletRequest request;
	
	@Before
	public void setup() {
		dao = mock(AccountDao.class);
		transactionDao = mock(TransactionDao.class);
		executor = new AccountWithdrawRequestExecutor(dao, transactionDao);
	}
	
	@Test
	public void testGivenCorrectParametersWhenExecutingRequestThenAmountIsWithDrawnAndTransactionIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("amount", "500", "id", "53"));
		
		Account account = new Account(new BigDecimal(1000), Currency.EUR, 3);
		account.setId(53);
		
		when(dao.findById(53)).thenReturn(account);
		when(dao.withdraw(account, new BigDecimal(500))).thenReturn(new BigDecimal(500));
		
		List<IJsonObject> trList = executor.executeRequest(request);
		assertEquals(1, trList.size());
		assertReturnedTransaction(trList, transactionDao, AccountAction.WITHDRAW,
				new BigDecimal(500), "Amount withdrawn successfully. Updated amount: 500",
				new Integer(53), null);
	}
	
	@Test
	public void testGivenWrongIdParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList("amount", "500"));
		List<IJsonObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
	
	@Test
	public void testGivenWrongAmountParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList("id", "53"));
		List<IJsonObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
	
	@Test
	public void testGivenNotAvailableAccountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("id", "53", "amount", "500"));
		
		when(dao.findById(53)).thenReturn(null);
		
		List<IJsonObject> trList = executor.executeRequest(request);
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Account not available");
	}
	
	@Test
	public void testGivenNegativeAccountAmountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("id", "53", "amount", "500"));
		
		Account account = new Account(new BigDecimal(-1000), Currency.EUR, 3);
		account.setId(53);
		
		when(dao.findById(53)).thenReturn(account);
		
		List<IJsonObject> trList = executor.executeRequest(request);
		
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Account amount is less than zero");
	}
	
	@Test
	public void testGivenWithdrawalAmountGreaterThanAccountAmountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("id", "53", "amount", "1500"));
		
		Account account = new Account(new BigDecimal(1000), Currency.EUR, 3);
		account.setId(53);
		
		when(dao.findById(53)).thenReturn(account);
		
		List<IJsonObject> trList = executor.executeRequest(request);
		
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Account amount is lower than withdrawal");
	}
}
