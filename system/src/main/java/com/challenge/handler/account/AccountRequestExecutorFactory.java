package com.challenge.handler.account;

import javax.inject.Inject;

import com.challenge.action.executor.account.AccountCreateRequestExecutor;
import com.challenge.action.executor.account.AccountDeleteRequestExecutor;
import com.challenge.action.executor.account.AccountDepositRequestExecutor;
import com.challenge.action.executor.account.AccountWithdrawRequestExecutor;
import com.challenge.action.executor.account.IAccountRequestExecutor;

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
