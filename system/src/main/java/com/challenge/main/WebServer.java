package com.challenge.main;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.challenge.handler.account.AccountHandler;

public class WebServer {
	
	private static final String ACCOUNT_CONTEXT_PATH = "/account";
	private static final int PORT = 8080;
	private static final String HOST = "localhost";
	private Server server;
	private ServerConnector connector;
	
	@Inject
	public WebServer(@Named(ApplicationModule.WEBSERVER) Server server,
			@Named(ApplicationModule.WEBSERVER) ServerConnector connector,
			@Named(ApplicationModule.WEBSERVER) Provider<ContextHandler> contextHandlerProvider,
			@Named(ApplicationModule.WEBSERVER) ContextHandlerCollection contextHandlerCollection,
			AccountHandler accountHandler) {
		
		this.server = server;
		
		this.connector = connector;
		this.connector.setHost(HOST);
		this.connector.setPort(PORT);
		this.server.addConnector(connector);
		
        ContextHandler accountContext = contextHandlerProvider.get();
        accountContext.setContextPath(ACCOUNT_CONTEXT_PATH);
        accountContext.setHandler(accountHandler);
        
//        ContextHandler transferContext = contextHandlerProvider.get();
//        transferContext.setContextPath(TRANSFER_CONTEXT_PATH);
//        transferContext.setHandler(transferHandler);
        
        contextHandlerCollection.addHandler(accountContext);
//        contextHandlerCollection.addHandler(transferContext);
		this.server.setHandler(contextHandlerCollection);
	}
	
	protected void startServer() {
		try {
			server.start();
			server.join();
		} catch (Exception e) {
//			handle exception
		} finally {
			server.destroy();
		}
	}

}
