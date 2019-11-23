package com.challenge.main;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main 
{
    public static void main( String[] args )
    {
    	Injector injector = Guice.createInjector(new ApplicationModule());
        injector.getInstance(WebServer.class).startServer();
    }
}
