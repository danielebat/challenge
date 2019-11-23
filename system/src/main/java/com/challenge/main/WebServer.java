package com.challenge.main;

import javax.inject.Inject;

import org.eclipse.jetty.server.Server;

public class WebServer {
	
	private Server server;
	
	@Inject
	public WebServer(Server server) throws Exception {
		this.server = server;
		this.server.start();
		this.server.join();
	}

}
