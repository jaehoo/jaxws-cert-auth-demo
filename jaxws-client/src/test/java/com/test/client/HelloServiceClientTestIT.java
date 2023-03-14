package com.test.client;


import com.test.SslContextFactory;
import com.test.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 10/3/2023 15:08
 **/
@RunWith(BlockJUnit4ClassRunner.class)
@Slf4j
public class HelloServiceClientTestIT {

	private HelloServiceClient client;


	@Before
	public void setUp() throws Exception {

		SSLContext sslContext = new SslContextFactory().getSslContext();
		SSLParameters sslParameters = sslContext.getDefaultSSLParameters();
		sslParameters.setEndpointIdentificationAlgorithm("HTTPS");

		//Uncomment this line to skip the hostname validation from certificate.
//		HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) ->true);

		client = new HelloServiceClient();
		client.setSslContext(sslContext);
		client.setService(new HelloServiceImpl());

	}


	@Test
	public void greet() {

		Message resp = client.greet("Foo!");
		Assert.assertNotNull(resp);
	}

}