package com.challenge.acceptance.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
		
		builder.clearParameters();
		URI uri = builder.setPath("/account/create")
        		.addParameter("amount", "1000").addParameter("userId", "1")
        		.addParameter("currency", "EUR").build();
        
        List<Transaction> list = executeHttpRequestAndVerifyStatus(uri, 200);
        Transaction tr = list.get(0);
        
        assertEquals(new BigDecimal(1000), tr.getAmount());
        assertEquals(AccountAction.CREATE, tr.getAction());
        assertEquals("Account created successfully. IBAN:", tr.getMessage().substring(0, tr.getMessage().indexOf(":")+1));
    }
	
	@Test
    public void testAccountDelete() throws Exception {
		
		builder.clearParameters();
		URI uri = builder.setPath("/account/create")
        		.addParameter("amount", "1000").addParameter("userId", "1")
        		.addParameter("currency", "EUR").build();
        
        List<Transaction> list = executeHttpRequestAndVerifyStatus(uri, 200);
        
        builder.clearParameters();
        uri = builder.setPath("/account/delete")
        		.addParameter("id", list.get(0).getSourceAccountId().toString())
        		.build();
        
        list = executeHttpRequestAndVerifyStatus(uri, 200);
        Transaction tr = list.get(0);
        
        assertEquals(null, tr.getAmount());
        assertEquals(AccountAction.DELETE, tr.getAction());
        assertEquals("Account delete successfully", tr.getMessage());
    }
	
	@Test
    public void testAccountDeposit() throws Exception {
        
		builder.clearParameters();
		URI uri = builder.setPath("/account/create")
        		.addParameter("amount", "1000").addParameter("userId", "2")
        		.addParameter("currency", "EUR").build();
        
		List<Transaction> list = executeHttpRequestAndVerifyStatus(uri, 200);
        
        builder.clearParameters();
        uri = builder.setPath("/account/deposit")
        		.addParameter("amount", "200").addParameter("id", list.get(0).getSourceAccountId().toString())
        		.build();
        
        list = executeHttpRequestAndVerifyStatus(uri, 200);
        Transaction tr = list.get(0);
        
        assertEquals(new BigDecimal(200), tr.getAmount());
        assertEquals(AccountAction.DEPOSIT, tr.getAction());
        assertEquals(null, tr.getTargetAccountId());
        assertEquals("Amount deposited successfully - Updated amount: 1200", tr.getMessage());
    }
	
	@Test
    public void testAccountWithdraw() throws Exception {
        
		builder.clearParameters();
		URI uri = builder.setPath("/account/create")
        		.addParameter("amount", "1000").addParameter("userId", "3")
        		.addParameter("currency", "EUR").build();
        
		List<Transaction> list = executeHttpRequestAndVerifyStatus(uri, 200);
        
        builder.clearParameters();
        uri = builder.setPath("/account/withdraw")
        		.addParameter("amount", "400").addParameter("id", list.get(0).getSourceAccountId().toString())
        		.build();
        
        list = executeHttpRequestAndVerifyStatus(uri, 200);
        Transaction tr = list.get(0);
        
        assertEquals(new BigDecimal(400), tr.getAmount());
        assertEquals(AccountAction.WITHDRAW, tr.getAction());
        assertEquals("Amount withdrawn successfully - Updated amount: 600", tr.getMessage());
    }
	
	@Test
    public void testAccountTransfer() throws Exception {
		
		Integer fromAccountId;
		Integer toAccountId;
        
		builder.clearParameters();
		URI uri = builder.setPath("/account/create")
        		.addParameter("amount", "1000").addParameter("userId", "3")
        		.addParameter("currency", "EUR").build();
        
		List<Transaction> list = executeHttpRequestAndVerifyStatus(uri, 200);
		fromAccountId = list.get(0).getSourceAccountId();
		
		builder.clearParameters();
		uri = builder.setPath("/account/create")
        		.addParameter("amount", "2000").addParameter("userId", "4")
        		.addParameter("currency", "EUR").build();
		
		list = executeHttpRequestAndVerifyStatus(uri, 200);
		toAccountId = list.get(0).getSourceAccountId();
        
        builder.clearParameters();
        uri = builder.setPath("/account/transfer")
        		.addParameter("amount", "400").addParameter("from", fromAccountId.toString())
        		.addParameter("to", toAccountId.toString()).build();
        
        list = executeHttpRequestAndVerifyStatus(uri, 200);
        Transaction tr = list.get(0);
        
        assertEquals(new BigDecimal(400), tr.getAmount());
        assertEquals(AccountAction.TRANSFER, tr.getAction());
        assertEquals("Amount transferred successfully", tr.getMessage());
    }
	
	@AfterClass
    public static void teardown() throws Exception {
		server.getServer().stop();
    }

	private List<Transaction> executeHttpRequestAndVerifyStatus(URI uri, int status) throws IOException, ClientProtocolException {
		HttpPost request = new HttpPost(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(status, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
        
        Type returnListType = new TypeToken<ArrayList<Transaction>>(){}.getType();
        List<Transaction> list = new Gson().fromJson(jsonString, returnListType);
		return list;
	}

}
