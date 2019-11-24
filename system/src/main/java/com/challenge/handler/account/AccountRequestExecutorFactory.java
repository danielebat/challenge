package com.challenge.handler.account;

import javax.inject.Inject;

import com.challenge.action.executor.IRequestExecutor;
import com.challenge.action.executor.account.AccountCreateRequestExecutor;
import com.challenge.action.executor.account.AccountDeleteRequestExecutor;
import com.challenge.action.executor.account.AccountDepositRequestExecutor;
import com.challenge.action.executor.account.AccountListRequestExecutor;
import com.challenge.action.executor.account.AccountTransferRequestExecutor;
import com.challenge.action.executor.account.AccountWithdrawRequestExecutor;

public class AccountRequestExecutorFactory {
	
	private final AccountCreateRequestExecutor createExec;
	private final AccountDeleteRequestExecutor deleteExec;
	private final AccountDepositRequestExecutor depositExec;
	private final AccountWithdrawRequestExecutor withdrawExec;
	private final AccountTransferRequestExecutor transferExec;
	private final AccountListRequestExecutor listExec;
	
	@Inject
	public AccountRequestExecutorFactory(AccountCreateRequestExecutor createExec, AccountDeleteRequestExecutor deleteExec,
			AccountDepositRequestExecutor depositExec, AccountWithdrawRequestExecutor withdrawExec,
			AccountTransferRequestExecutor transferExec, AccountListRequestExecutor listExec) {
				this.createExec = createExec;
				this.deleteExec = deleteExec;
				this.depositExec = depositExec;
				this.withdrawExec = withdrawExec;
				this.transferExec = transferExec;
				this.listExec = listExec;
	}

	public IRequestExecutor create(AccountAction action) {
		if (AccountAction.CREATE.equals(action))
			return createExec;
		else if (AccountAction.DELETE.equals(action))
			return deleteExec;
		else if (AccountAction.DEPOSIT.equals(action))
			return depositExec;
		else if (AccountAction.WITHDRAW.equals(action))
			return withdrawExec;
		else if (AccountAction.TRANSFER.equals(action))
			return transferExec;
		else if (AccountAction.LIST.equals(action))
			return listExec;
		return null;
	}
}
