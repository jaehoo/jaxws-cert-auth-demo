package com.test.service;

import com.test.model.Message;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Calendar;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 23/2/2023 20:10
 **/
@WebService(endpointInterface = "com.test.service.HelloService")
@HandlerChain(file = "server-handlers.xml")
public class HelloServiceImpl implements HelloService{

	@WebMethod(action = "http://dummy/example/action", operationName = "hello")
	@Override
	public Message greet(
			@WebParam(name = "name", targetNamespace ="http://snd/test",  partName = "hello" )
			String name) {

		return new Message( "Hello "+ name + ", date >"+ Calendar.getInstance().getTime());
	}


}
