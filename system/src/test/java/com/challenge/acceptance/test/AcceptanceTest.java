package com.challenge.acceptance.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.challenge.data.model.Account;
import com.challenge.data.model.JsonObject;
import com.challenge.data.model.Transaction;
import com.challenge.handler.account.AccountAction;
import com.challenge.main.ApplicationModule;
import com.challenge.main.WebServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class AcceptanceTest {
	
	private static URIBuilder builder;
	private static HttpClient client;
	private static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
	
	private static WebServer server;
	
	@BeforeClass
    public static void setup() throws Exception {
		
		connManager = new PoolingHttpClientConnectionManager();
		builder = new URIBuilder().setScheme("http").setHost("localhost:8080");
		client= HttpClients.custom()
                .setConnectionManager(connManager)
                .setConnectionManagerShared(true)
                .build();
		
		Injector injector = Guice.createInjector(new ApplicationModule());
        server = injector.getInstance(WebServer.class);
        server.getServer().start();
        }
	
	@Test
    public void testAccountCreate() throws Exception {
		
		URI uri = buildCreateHttpRequest("1000", "1", "EUR");
        
        List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals(new BigDecimal(1000), tr.getAmount());
        assertEquals(AccountAction.CREATE, tr.getAction());
        assertEquals("Account created successfully. IBAN:", tr.getMessage().substring(0, tr.getMessage().indexOf(":")+1));
    }
	
	@Test
    public void testAccountCreateGivingErrorWhenAmountIsNegative() throws Exception {
		
		URI uri = buildCreateHttpRequest("-1000", "1", "EUR");
        
        List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals("Unable to process request - Amount is less than zero", tr.getMessage());
    }
	
	@Test
    public void testAccountDelete() throws Exception {
		
		URI uri = buildCreateHttpRequest("1000", "1", "EUR");
        
        List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        
        uri = buildDeleteHttpRequest(((Transaction) list.get(0)).getSourceAccountId().toString());
        
        list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals(null, tr.getAmount());
        assertEquals(AccountAction.DELETE, tr.getAction());
        assertEquals("Account delete successfully", tr.getMessage());
    }
	
	@Test
    public void testAccountDeleteGivingErrorIfAccountDoesNotExist() throws Exception {
        
        URI uri = buildDeleteHttpRequest("4354635");
        
        List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Transaction tr = (Transaction) list.get(0);

        assertEquals("Unable to process request - Account not available", tr.getMessage());
	}
	
	@Test
    public void testAccountDeposit() throws Exception {
		
		URI uri = buildCreateHttpRequest("1000", "2", "EUR");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        
        uri = buildDepositHttpRequest(((Transaction) list.get(0)).getSourceAccountId().toString(), "200");
        
        list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals(new BigDecimal(200), tr.getAmount());
        assertEquals(AccountAction.DEPOSIT, tr.getAction());
        assertEquals(null, tr.getTargetAccountId());
        assertEquals("Amount deposited successfully - Updated amount: 1200", tr.getMessage());
    }
	
	@Test
    public void testAccountDepositGivingErrorWhenAmountIsNegative() throws Exception {
        
        URI uri = buildDepositHttpRequest("3", "-200");
        
        List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals("Unable to process request - Amount is less than zero", tr.getMessage());
    }
	
	@Test
    public void testAccountDepositGivingErrorWhenAccountDoesNotExist() throws Exception {
        
        URI uri = buildDepositHttpRequest("200", "4354635");
        
        List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Transaction tr = (Transaction) list.get(0);

        assertEquals("Unable to process request - Account not available", tr.getMessage());
    }
	
	@Test
    public void testAccountWithdraw() throws Exception {
		
		URI uri = buildCreateHttpRequest("1000", "3", "EUR");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        
        uri = buildWithdrawHttpRequest(((Transaction) list.get(0)).getSourceAccountId().toString(), "400");
        
        list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals(new BigDecimal(400), tr.getAmount());
        assertEquals(AccountAction.WITHDRAW, tr.getAction());
        assertEquals("Amount withdrawn successfully - Updated amount: 600", tr.getMessage());
    }
	
	@Test
    public void testAccountWithdrawGivingErrorWhenAmountIsNegative() throws Exception {
        
        URI uri = buildWithdrawHttpRequest("3", "-400");
        
        List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals("Unable to process request - Amount is less than zero", tr.getMessage());
    }
	
	@Test
    public void testAccountWithdrawGivingErrorWhenAmountIsLessThanAccountAmount() throws Exception {
		
		URI uri = buildCreateHttpRequest("1000", "1", "EUR");
        
        List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        Transaction tr = (Transaction) list.get(0);
        
        uri = buildWithdrawHttpRequest(((Transaction) list.get(0)).getSourceAccountId().toString(), "2000");
        
        list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        tr = (Transaction) list.get(0);
        
        assertEquals("Unable to process request - Account amount is lower than withdrawal", tr.getMessage());
    }
	
	@Test
    public void testAccountWithdrawGivingErrorWhenAccountDoesNotExist() throws Exception {
        
        URI uri = buildWithdrawHttpRequest("4354635", "200");
        
        List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Transaction tr = (Transaction) list.get(0);

        assertEquals("Unable to process request - Account not available", tr.getMessage());
    }
	
	@Test
    public void testAccountTransfer() throws Exception {
		
		Integer fromAccountId;
		Integer toAccountId;
		
		URI uri = buildCreateHttpRequest("1000", "3", "EUR");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		fromAccountId = ((Transaction) list.get(0)).getSourceAccountId();
		
		uri = buildCreateHttpRequest("2000", "4", "EUR");
		
		list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		toAccountId = ((Transaction) list.get(0)).getSourceAccountId();
        
        uri = buildTransferHttpRequest(fromAccountId.toString(), toAccountId.toString(), "400");
        
        list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals(new BigDecimal(400), tr.getAmount());
        assertEquals(AccountAction.TRANSFER, tr.getAction());
        assertEquals("Amount transferred successfully", tr.getMessage());
    }
	
	@Test
    public void testAccountTransferWithDifferentCurrencies() throws Exception {
		
		Integer fromAccountId;
		Integer toAccountId;
		
		URI uri = buildCreateHttpRequest("1000", "100", "EUR");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		fromAccountId = ((Transaction) list.get(0)).getSourceAccountId();
		
		uri = buildCreateHttpRequest("2000", "101", "USD");
		
		list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		toAccountId = ((Transaction) list.get(0)).getSourceAccountId();
        
        uri = buildTransferHttpRequest(fromAccountId.toString(), toAccountId.toString(), "100"); 
        
        list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals(new BigDecimal(100), tr.getAmount());
        assertEquals(AccountAction.TRANSFER, tr.getAction());
        assertEquals("Amount transferred successfully", tr.getMessage());
        
        uri = buildAccountListHttpRequest("100");
        
        list = executeHttpRequestAndVerifyStatusForAccount(uri, HttpServletResponse.SC_OK);
        
        assertEquals(new BigDecimal(900), ((Account) list.get(0)).getAmount());
        
        uri = buildAccountListHttpRequest("101");
        
        list = executeHttpRequestAndVerifyStatusForAccount(uri, HttpServletResponse.SC_OK);
        
        BigDecimal toAmount = ((Account) list.get(0)).getAmount().round(new MathContext(4));
        
        assertEquals(new BigDecimal(2080), toAmount);
    }
	
	@Test
    public void testAccountTransferGivingErrorWhenFromAccountDoesNotExist() throws Exception {
        
        URI uri = buildTransferHttpRequest("435463645", "3", "400");
        
        List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals("Unable to process request - Source Account not available", tr.getMessage());
	}
	
	@Test
    public void testAccountTransferGivingErrorWhenToAccountDoesNotExist() throws Exception {
		
		Integer fromAccountId;
		
		URI uri = buildCreateHttpRequest("1000", "3", "EUR");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		fromAccountId = ((Transaction) list.get(0)).getSourceAccountId();
        
        uri = buildTransferHttpRequest(fromAccountId.toString(), "3245672", "400");
        
        list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals("Unable to process request - Target Account not available", tr.getMessage());
	}
	
	@Test
    public void testAccountTransferGivingErrorWhenAmountToTransferIsLessThanFromAccount() throws Exception {
		
		Integer fromAccountId;
		Integer toAccountId;
		
		URI uri = buildCreateHttpRequest("1000", "3", "EUR");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		fromAccountId = ((Transaction) list.get(0)).getSourceAccountId();
		
		uri = buildCreateHttpRequest("2000", "4", "EUR");
		
		list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		toAccountId = ((Transaction) list.get(0)).getSourceAccountId();
        
        uri = buildTransferHttpRequest(fromAccountId.toString(), toAccountId.toString(), "2000");
        
        list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals("Unable to process request - Source Account amount is lower than amount to transfer", tr.getMessage());
	}
	
	@Test
    public void testAccountTransferGivingErrorWhenFromAccountIsEqualToToAccount() throws Exception {
		
		Integer fromAccountId;
		Integer toAccountId;
		
		URI uri = buildCreateHttpRequest("1000", "3", "EUR");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		fromAccountId = ((Transaction) list.get(0)).getSourceAccountId();
		toAccountId = ((Transaction) list.get(0)).getSourceAccountId();
        
        uri = buildTransferHttpRequest(fromAccountId.toString(), toAccountId.toString(), "200");
        
        list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Transaction tr = (Transaction) list.get(0);
        
        assertEquals("Unable to process request - Source and Target Account are equal", tr.getMessage());
	}
	
	@Test
    public void testAccountTransferGivingErrorWhenAmountIsNegative() throws Exception {
		
		URI uri = buildTransferHttpRequest("3", "4", "-1000");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		Transaction tr = (Transaction) list.get(0);
        
        assertEquals("Unable to process request - Amount is less than zero", tr.getMessage());
	}
	
	@Test
    public void testAccountListReturningAtLeastOneElement() throws Exception {
		
		URI uri = buildCreateHttpRequest("1000", "1", "EUR");
        
        executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		
		uri = buildAccountListHttpRequest("1");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        
        assertTrue(list.size() >= 1);
	}
	
	@Test
    public void testAccountListReturningEmptyListIfUserIsNotAvailable() throws Exception {
		
		URI uri = buildAccountListHttpRequest("8686");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        
        assertEquals(0, list.size());
	}
	
	@Test
    public void testTransactionListReturningExactlyOneElementWhenCreatingIt() throws Exception {
		
		URI uri = buildCreateHttpRequest("1000", "1", "EUR");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		
		uri = buildTransactionListHttpRequest(((Transaction) list.get(0)).getId().toString());
        
		list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
		Transaction tr = (Transaction) list.get(0);
		
		assertEquals(AccountAction.CREATE, tr.getAction());
		assertEquals(new BigDecimal(1000), tr.getAmount());
        
        assertTrue(list.size() == 1);
	}
	
	@Test
    public void testTransactionListReturningEmptyListIfAccountIsNotAvailable() throws Exception {
        
		builder.clearParameters();
		URI uri = buildTransactionListHttpRequest("8686");
        
		List<JsonObject> list = executeHttpRequestAndVerifyStatusForTransaction(uri, HttpServletResponse.SC_OK);
        
        assertEquals(0, list.size());
	}
	
	@AfterClass
    public static void teardown() throws Exception {
		server.getServer().stop();
    }
	
	private URI buildCreateHttpRequest(String amount, String userId, String currency) throws URISyntaxException {
		builder.clearParameters();
		URI uri = builder.setPath("/account/create")
        		.addParameter("amount", amount).addParameter("userId", userId)
        		.addParameter("currency", currency).build();
		return uri;
	}
	
	private URI buildDeleteHttpRequest(String id) throws URISyntaxException {
		builder.clearParameters();
        URI uri = builder.setPath("/account/delete")
        		.addParameter("id", id)
        		.build();
		return uri;
	}
	
	private URI buildDepositHttpRequest(String id, String amount) throws URISyntaxException {
		builder.clearParameters();
        URI uri = builder.setPath("/account/deposit")
        		.addParameter("id", id).addParameter("amount", amount)
        		.build();
		return uri;
	}
	
	private URI buildWithdrawHttpRequest(String id, String amount) throws URISyntaxException {
		builder.clearParameters();
        URI uri = builder.setPath("/account/withdraw")
        		.addParameter("id", id).addParameter("amount", amount)
        		.build();
		return uri;
	}
	
	private URI buildTransferHttpRequest(String from, String to, String amount) throws URISyntaxException {
		builder.clearParameters();
        URI uri = builder.setPath("/account/transfer")
        		.addParameter("from", from).addParameter("to", to)
        		.addParameter("amount", amount)
        		.build();
		return uri;
	}
	
	private URI buildAccountListHttpRequest(String id) throws URISyntaxException {
		builder.clearParameters();
        URI uri = builder.setPath("/account/list")
        		.addParameter("id", id)
        		.build();
		return uri;
	}
	
	private URI buildTransactionListHttpRequest(String id) throws URISyntaxException {
		builder.clearParameters();
        URI uri = builder.setPath("/transaction/list")
        		.addParameter("id", id)
        		.build();
		return uri;
	}

	private List<JsonObject> executeHttpRequestAndVerifyStatusForTransaction(URI uri,
			int status) throws IOException, ClientProtocolException {
		String jsonString = executeHttpRequestandVerifyStatus(uri, status);
        
        Type returnListType = new TypeToken<ArrayList<Transaction>>(){}.getType();
        List<JsonObject> list = new Gson().fromJson(jsonString, returnListType);
		return list;
	}
	
	private List<JsonObject> executeHttpRequestAndVerifyStatusForAccount(URI uri,
			int status) throws IOException, ClientProtocolException {
		String jsonString = executeHttpRequestandVerifyStatus(uri, status);
        
        Type returnListType = new TypeToken<ArrayList<Account>>(){}.getType();
        List<JsonObject> list = new Gson().fromJson(jsonString, returnListType);
		return list;
	}

	private String executeHttpRequestandVerifyStatus(URI uri, int status) throws IOException, ClientProtocolException {
		HttpPost request = new HttpPost(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(status, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
		return jsonString;
	}

}
