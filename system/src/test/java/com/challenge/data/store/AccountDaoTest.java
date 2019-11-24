package com.challenge.data.store;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.challenge.data.model.Account;
import com.challenge.data.model.Currency;

public class AccountDaoTest {
	
	AccountDao dao;
	Account account;
	
	@Before
	public void setup() {
		
		dao = new AccountDao();
		account = new Account(new BigDecimal(1000), Currency.EUR, 3);
	}
	
	@Test
	public void testGivenAmountToDepositWhenDepositingToAccountThenAccountIsUpdatedCorrectly() {
		dao.deposit(account, new BigDecimal(200));
		assertEquals(new BigDecimal(1200), account.getAmount());
	}
	
	@Test
	public void testGivenAmountToWithdrawWhenWithdrawingFromAccountThenAccountIsUpdatedCorrectly() {
		dao.withdraw(account, new BigDecimal(200));
		assertEquals(new BigDecimal(800), account.getAmount());
	}

}
