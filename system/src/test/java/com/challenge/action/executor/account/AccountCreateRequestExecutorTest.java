package com.challenge.action.executor.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.challenge.data.model.Account;
import com.challenge.data.model.Currency;
import com.challenge.data.model.IdentityObject;
import com.challenge.data.model.Transaction;
import com.challenge.data.store.AccountDao;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;
import com.google.common.collect.Lists;

public class AccountCreateRequestExecutorTest extends RequestExecutorTest {
	
	AccountCreateRequestExecutor executor;
	private AccountDao dao;
	private TransactionDao transactionDao;
	private HttpServletRequest request;
	
	@Before
	public void setup() {
		dao = mock(AccountDao.class);
		transactionDao = mock(TransactionDao.class);
		executor = new AccountCreateRequestExecutor(dao, transactionDao);
	}
	
	@Test
	public void testGivenCorrectParametersWhenExecutingRequestThenAccountIsAddedAndTransactionIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("amount", "1000", "userId", "53", "currency", "EUR"));
		
		ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
		when(dao.add(captor.capture())).thenReturn(1);
		
		List<IdentityObject> trList = executor.executeRequest(request);
		
		Account acc = captor.getValue();
		
		assertEquals(new BigDecimal(1000), acc.getAmount());
		assertEquals(Currency.EUR, acc.getCurrency());
		assertEquals(new Integer(53), acc.getUserId());
		
		assertEquals(1, trList.size());
		Transaction tr = (Transaction) trList.get(0);
		
		assertEquals(new BigDecimal(1000), tr.getAmount());
		assertEquals(AccountAction.CREATE, tr.getAction());
		assertEquals("Account created successfully. IBAN:", tr.getMessage().substring(0, tr.getMessage().indexOf(":")+1));
		assertEquals(new Integer(1), tr.getSourceAccountId());
		assertNull(tr.getTargetAccountId());
		
		verify(transactionDao).add(tr);
	}
	
	@Test
	public void testGivenWrongAmountParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList("userId", "53", "currency", "EUR"));
		List<IdentityObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
	
	@Test
	public void testGivenUserIdWrongParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList("amount", "1000", "currency", "EUR"));
		List<IdentityObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
		
	@Test
	public void testGivenWrongCurrencyParametersWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		
		mockRequestBehaviour(request, Lists.newArrayList("amount", "1000", "userId", "53"));
		List<IdentityObject> trList = executor.executeRequest(request);
		assertJsonObjectWithErrorMessage(trList, null);
	}
	
	@Test
	public void testGivenNegativeAmountWhenExecutingRequestThenTransactionWithErrorMessageIsReturned() {
		
		request = mock(HttpServletRequest.class);
		mockRequestBehaviour(request, Lists.newArrayList("id", "53", "amount", "-1000"));
		
		List<IdentityObject> trList = executor.executeRequest(request);
		
		assertEquals(1, trList.size());
		assertJsonObjectWithErrorMessage(trList, "Amount is less than zero");
	}

}
