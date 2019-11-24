package com.challenge.action.executor.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.challenge.data.model.IJsonObject;
import com.challenge.data.model.Transaction;
import com.challenge.data.store.TransactionDao;
import com.challenge.handler.account.AccountAction;

public class RequestExecutorTest {
	
	public void mockRequestBehaviour(HttpServletRequest request, List<String> paramValueList) {
		for (int i = 0; i < paramValueList.size(); i+=2) {
			when(request.getParameter(paramValueList.get(i))).thenReturn(paramValueList.get(i+1));
		}
	}
	
	public void assertJsonObjectWithErrorMessage(List<IJsonObject> trList, String optionalMsg) {
		assertEquals(1, trList.size());
		Transaction tr = (Transaction) trList.get(0);
		String message = "Unable to process request";
		if (optionalMsg != null)
			message = message + " - " + optionalMsg;
		assertEquals(message, tr.getMessage());
	}
	
	public void assertReturnedTransaction(List<IJsonObject> trList, TransactionDao dao,
			AccountAction action, BigDecimal amount, String message, Integer sourceAccountId, Integer targetAccountId) {
		Transaction tr = (Transaction) trList.get(0);
		
		assertEquals(amount, tr.getAmount());
		assertEquals(action, tr.getAction());
		assertEquals(message, tr.getMessage());
		assertEquals(sourceAccountId, tr.getSourceAccountId());
		assertEquals(targetAccountId, tr.getTargetAccountId());
		
		verify(dao).add(tr);
	}

}
