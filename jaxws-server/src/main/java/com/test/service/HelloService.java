package com.test.service;

import com.test.model.Message;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 23/2/2023 20:06
 **/
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface HelloService {

	@WebMethod
	Message greet(@WebParam(name = "name") String name);
}
