package com.test.client;

import com.test.SslContextFactory;
import com.test.client.HelloService;
import com.test.client.HelloServiceClient;
import com.test.client.HelloServiceImpl;
import com.test.handler.LogHandler;
import com.test.model.Message;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 10/3/2023 20:30
 **/
@RunWith(MockitoJUnitRunner.class)
public class HelloServiceClientTest {

	@Mock
	private LogHandler logHandler;

	@Mock
	private HelloServiceImpl service;

	@Mock(extraInterfaces = {BindingProvider.class, SOAPBinding.class} )
	private HelloService port;

	@Mock
	private SOAPBinding soapBindingMock;

	@InjectMocks
	private HelloServiceClient client;

	@Before
	public void setUp() throws Exception {

		SSLContext sslContext = new SslContextFactory().getSslContext();
		SSLParameters sslParameters = sslContext.getDefaultSSLParameters();
		sslParameters.setEndpointIdentificationAlgorithm("HTTPS");

		Mockito.when(port.greet(any())).thenReturn(dummyMessage());
		Mockito.when(((BindingProvider)port).getBinding()).thenReturn(soapBindingMock);
		Mockito.when(service.getHelloServiceImplPort()).thenReturn(port);

		client.setSslContext(sslContext);
		client.setService(service);
		client.setPort(port);
	}

	@Test
	public void greet() {
		Assert.assertNotNull("Error to recover message", client.greet("Hello"));
	}

	@Test
	public void greetWithHandler() {

		// Configure Mock to use handlers
		BindingProvider bindingProvider = (BindingProvider) port;
		bindingProvider.getBinding().setHandlerChain(Arrays.asList(logHandler));

		client.setBindingProvider(bindingProvider);
		Assert.assertNotNull("Error to recover message", client.greet("Hello"));

	}

	@Test
	public void testGreetSettingService() {
		Assert.assertNotNull("Error to recover message", client.greet("Hello"));
	}

	@Test
	public void testThrowsException() {
		Mockito.when(port.greet("Error")).thenThrow(new RuntimeException("Dummy Error"));
		Assert.assertNull(client.greet("Error"));
	}

	private Message dummyMessage(){

		Message message= new Message();
		message.setText("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><S:Body><ns2:greetResponse xmlns:ns2=\"http://service.test.com/\"><return><text>Hello Foo!, date &gt;Fri Mar 10 21:19:54 CST 2023</text></return></ns2:greetResponse></S:Body></S:Envelope>");
		return message;
	}
}