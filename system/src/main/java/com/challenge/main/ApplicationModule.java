package com.challenge.main;

import javax.inject.Singleton;

import org.eclipse.jetty.server.Server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ApplicationModule extends AbstractModule {
	
	private static final int PORT = 8080;

	@Override
	protected void configure() {
		
		bind(WebServer.class).in(Singleton.class);
	}
	
	@Provides
	@Singleton
	public Server getServer() {
		return new Server(PORT);
	}

}
