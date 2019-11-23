package com.challenge.main;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class WebServer {
	
	private static final int PORT = 8080;
	private static final String HOST = "localhost";
	private Server server;
	private ServerConnector connector;
	
	@Inject
	public WebServer(@Named(ApplicationModule.WEBSERVER) Server server,
			@Named(ApplicationModule.WEBSERVER) ServerConnector connector) throws Exception {
		
		this.server = server;
		
		this.connector = connector;
		this.connector.setHost(HOST);
		this.connector.setPort(PORT);
		this.server.addConnector(connector);
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
