package com.challenge.main;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.challenge.action.executor.account.AccountCreateRequestExecutor;
import com.challenge.action.executor.account.AccountDeleteRequestExecutor;
import com.challenge.action.executor.account.AccountDepositRequestExecutor;
import com.challenge.action.executor.account.AccountWithdrawRequestExecutor;
import com.challenge.data.store.AccountDao;
import com.challenge.handler.account.AccountHandler;
import com.challenge.handler.account.AccountRequestExecutorFactory;
import com.challenge.handler.transfer.TransferHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ApplicationModule extends AbstractModule {
	
	protected static final String WEBSERVER = "WEBSERVER";

	@Override
	protected void configure() {
		
		bind(AccountDao.class).in(Singleton.class);
		
		bind(AccountHandler.class);
		bind(AccountRequestExecutorFactory.class).in(Singleton.class);
		bind(AccountCreateRequestExecutor.class);
		bind(AccountDeleteRequestExecutor.class);
		bind(AccountDepositRequestExecutor.class);
		bind(AccountWithdrawRequestExecutor.class);
		
		bind(TransferHandler.class);
		
		bind(WebServer.class).in(Singleton.class);
	}
	
	@Provides
	@Singleton
	@Named(WEBSERVER)
	public Server getServer() {
		return new Server();
	}
	
	@Provides
	@Singleton
	@Named(WEBSERVER)
	public ServerConnector getServerConnector(@Named(WEBSERVER) Server server) {
		return new ServerConnector(server);
	}
	
	@Provides
	@Named(WEBSERVER)
	public ContextHandler getContextHandler() {
		return new ContextHandler();
	}
	
	@Provides
	@Named(WEBSERVER)
	public ContextHandlerCollection getContextHandlerCollection() {
		return new ContextHandlerCollection();
	}

}
