package com.challenge.main;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.challenge.handler.account.AccountHandler;
import com.challenge.handler.transaction.TransactionHandler;

public class WebServer {
	
	private static final String ACCOUNT_CONTEXT_PATH = "/account";
	private static final String TRANSACTION_CONTEXT_PATH = "/transaction";
	private static final int PORT = 8080;
	private static final String HOST = "localhost";
	private Server server;
	private ServerConnector connector;
	
	/**
	 * Method to instantiate Jetty WebServer, configure host and port, configure handlers to reply to HTTP requests
	 * 
	 * @param server Istance of Jetty Webserver
	 * @param connector Jetty webserver connector
	 * @param contextHandlerProvider Provider of Context Handlers
	 * @param contextHandlerCollection Collection of handlers
	 * @param accountHandler Handler which replies under context path /account HTTP requests
	 * @param transactionHandler Handler which replies under context path /transaction HTTP requests
	 */
	@Inject
	public WebServer(@Named(ApplicationModule.WEBSERVER) Server server,
			@Named(ApplicationModule.WEBSERVER) ServerConnector connector,
			@Named(ApplicationModule.WEBSERVER) Provider<ContextHandler> contextHandlerProvider,
			@Named(ApplicationModule.WEBSERVER) ContextHandlerCollection contextHandlerCollection,
			AccountHandler accountHandler, TransactionHandler transactionHandler) {
		
		this.server = server;
		
		this.connector = connector;
		this.connector.setHost(HOST);
		this.connector.setPort(PORT);
		this.server.addConnector(connector);
		
        ContextHandler accountContext = contextHandlerProvider.get();
        accountContext.setContextPath(ACCOUNT_CONTEXT_PATH);
        accountContext.setHandler(accountHandler);
        
        ContextHandler transactionContext = contextHandlerProvider.get();
        transactionContext.setContextPath(TRANSACTION_CONTEXT_PATH);
        transactionContext.setHandler(transactionHandler);
        
        contextHandlerCollection.addHandler(accountContext);
        contextHandlerCollection.addHandler(transactionContext);
		this.server.setHandler(contextHandlerCollection);
	}
	
	/**
	 * Method to start Jetty Webserver
	 */
	protected void startServer() {
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			System.err.println("Unable to start web server. Exiting...");
		} finally {
			server.destroy();
		}
	}
	
	
	/**
	 * IMPORTANT. THIS METHOD HAS NO REASON TO EXIST IN NORMAL APPLICATION. I HAVE PUT IT HERE JUST FOR THE SAKE OF
	 * ACCEPTANCE TESTS AND TO HAVE A WAY OF STARTING WEB SERVER.
	 * 
	 * @return Jetty Webserver instance
	 */
	public Server getServer() {
		return server;
	}

}
