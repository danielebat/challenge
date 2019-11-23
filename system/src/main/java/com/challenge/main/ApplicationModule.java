package com.challenge.main;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;

import com.challenge.account.handler.AccountHandler;
import com.challenge.data.store.AccountDao;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ApplicationModule extends AbstractModule {
	
	protected static final String WEBSERVER = "WEBSERVER";

	@Override
	protected void configure() {
		
		bind(AccountDao.class).in(Singleton.class);
		bind(AccountHandler.class);
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

}
