package com.test.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 23/2/2023 21:43
 **/
public class DefaultHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange x) throws IOException {
		String Response = "This is the response from default tstack";
		HttpsExchange HTTPS_Exchange = (HttpsExchange) x;
		x.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		x.sendResponseHeaders(200, Response.getBytes().length);
		OutputStream Output_Stream = x.getResponseBody();
		Output_Stream.write(Response.getBytes());
		Output_Stream.close();
	}
}
