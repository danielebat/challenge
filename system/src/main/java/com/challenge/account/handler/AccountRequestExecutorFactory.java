package com.challenge.account.handler;

import javax.inject.Inject;

import com.challenge.account.action.executor.AccountCreateRequestExecutor;
import com.challenge.account.action.executor.AccountDeleteRequestExecutor;
import com.challenge.account.action.executor.AccountDepositRequestExecutor;
import com.challenge.account.action.executor.AccountWithdrawRequestExecutor;
import com.challenge.account.action.executor.IAccountRequestExecutor;

public class AccountRequestExecutorFactory {
	
	private final AccountCreateRequestExecutor createExec;
	private final AccountDeleteRequestExecutor deleteExec;
	private final AccountDepositRequestExecutor depositExec;
	private final AccountWithdrawRequestExecutor withdrawExec;
	
	@Inject
	public AccountRequestExecutorFactory(AccountCreateRequestExecutor createExec, AccountDeleteRequestExecutor deleteExec,
			AccountDepositRequestExecutor depositExec, AccountWithdrawRequestExecutor withdrawExec) {
				this.createExec = createExec;
				this.deleteExec = deleteExec;
				this.depositExec = depositExec;
				this.withdrawExec = withdrawExec;
	}

	public IAccountRequestExecutor create(AccountAction action) {
		if (AccountAction.CREATE.equals(action))
			return createExec;
		else if (AccountAction.DELETE.equals(action))
			return deleteExec;
		else if (AccountAction.DEPOSIT.equals(action))
			return depositExec;
		else if (AccountAction.WITHDRAW.equals(action))
			return withdrawExec;
		return null;
	}
}
