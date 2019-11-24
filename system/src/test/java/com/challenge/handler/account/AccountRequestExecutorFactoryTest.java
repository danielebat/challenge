package com.challenge.handler.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.challenge.action.executor.account.AccountCreateRequestExecutor;
import com.challenge.action.executor.account.AccountDeleteRequestExecutor;
import com.challenge.action.executor.account.AccountDepositRequestExecutor;
import com.challenge.action.executor.account.AccountListRequestExecutor;
import com.challenge.action.executor.account.AccountTransferRequestExecutor;
import com.challenge.action.executor.account.AccountWithdrawRequestExecutor;

public class AccountRequestExecutorFactoryTest {
	
	AccountRequestExecutorFactory factory;
	private AccountCreateRequestExecutor createExec;
	private AccountDeleteRequestExecutor deleteExec;
	private AccountDepositRequestExecutor depositExec;
	private AccountWithdrawRequestExecutor withdrawExec;
	private AccountTransferRequestExecutor transferExec;
	private AccountListRequestExecutor listExec;
	
	@Before
	public void setup() {
		
		createExec = mock(AccountCreateRequestExecutor.class);
		deleteExec = mock(AccountDeleteRequestExecutor.class);
		depositExec = mock(AccountDepositRequestExecutor.class);
		withdrawExec = mock(AccountWithdrawRequestExecutor.class);
		transferExec = mock(AccountTransferRequestExecutor.class);
		listExec = mock(AccountListRequestExecutor.class);
		
		factory = new AccountRequestExecutorFactory(createExec, deleteExec, depositExec, withdrawExec, transferExec, listExec);
	}
	
	@Test
	public void testGivenAccountActionWhenCreateMethodIsCalledThenCorrectExecutorIsReturned() {
		
		assertEquals(createExec, factory.create(AccountAction.CREATE));
		assertEquals(deleteExec, factory.create(AccountAction.DELETE));
		assertEquals(depositExec, factory.create(AccountAction.DEPOSIT));
		assertEquals(withdrawExec, factory.create(AccountAction.WITHDRAW));
		assertEquals(transferExec, factory.create(AccountAction.TRANSFER));
		assertEquals(listExec, factory.create(AccountAction.LIST));
		assertEquals(null, factory.create(null));
	}

}
