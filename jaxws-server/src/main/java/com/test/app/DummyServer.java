package com.test.app;

import com.sun.net.httpserver.HttpsServer;
import com.test.handler.DefaultHandler;
import com.test.service.HelloServiceImpl;
import com.test.util.CustomHttpServerFactory;
import com.test.util.KeyStoreLoader;
import lombok.extern.slf4j.Slf4j;

import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 6/3/2023 09:31
 **/
@Slf4j
public class DummyServer {

	private static String PKCS12="PKCS12";
	private HttpsServer httpsServer;

	public static void main(String[] args){

		try{
			new DummyServer().launch();
		}
		catch (Exception e){
			log.error("Error creating server",e);
		}

	}

	public void launch() throws IOException, KeyStoreException {

		char [] defPass="changeit".toCharArray();

		// Load the Keystore and Truststore
		KeyStore keystore = KeyStoreLoader.loadFromClassPath("server.jks",defPass, PKCS12);
		KeyStore truststore = KeyStoreLoader.loadFromClassPath("server.jks",defPass, PKCS12);

		// init server factory
		CustomHttpServerFactory serverFactory= new CustomHttpServerFactory(keystore
				,truststore, defPass);

		// Create https server
		httpsServer=serverFactory.createServer(8443);
		httpsServer.createContext("/test", new DefaultHandler());
		httpsServer.start();


		Endpoint ep = Endpoint.create(new HelloServiceImpl());
		ep.publish(httpsServer.createContext("/HelloServer"));

		log.info("Service is Started!: {}",httpsServer.getAddress());
	}

	public void shutdown(){
		httpsServer.stop(0);
	}

}
