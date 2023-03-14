package com.test.client;


import com.test.model.Message;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 10/3/2023 14:09
 **/
@Slf4j
public class HelloServiceClient implements HelloService{

	@Setter
	private HelloServiceImpl service;
	@Setter
	private SSLContext sslContext;
	@Setter
	private BindingProvider bindingProvider;
	@Setter
	private HelloService port;

	@Override
	public Message greet(String name) {


		port = service.getHelloServiceImplPort();

		if(sslContext != null){
			bindingProvider = (BindingProvider) port;
			SOAPBinding binding = (SOAPBinding) bindingProvider.getBinding();
			binding.setMTOMEnabled(true);
			bindingProvider.getRequestContext().put(
					//com.sun.xml.internal.ws.developer.JAXWSProperties.SSL_SOCKET_FACTORY
					"com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory"
					, sslContext.getSocketFactory());
		}

		// Invoke Web service
		Message resp= null;
		try{
			resp= port.greet(name);
			log.info("reply:{}",resp.getText());

		}
		catch(Exception e){
			log.error("{}", e.getMessage(), e);
		}

		return resp;
	}
}
