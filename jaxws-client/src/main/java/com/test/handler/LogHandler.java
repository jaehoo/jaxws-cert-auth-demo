package com.test.handler;

import lombok.extern.slf4j.Slf4j;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 14/2/2023 23:42
 **/
@Slf4j
public class LogHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		logSoapMessage(context);
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		log.info("***handleFault***");

		logSoapMessage(context);
		return false;
	}

	@Override
	public void close(MessageContext context) {
//		log.info("_______________close_____________ ");
	}

	@Override
	public Set<QName> getHeaders() {
		return new HashSet<QName>();
	}

	private void logSoapMessage(SOAPMessageContext context) {
		Boolean isOutBound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		SOAPMessage soapMsg = context.getMessage();

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {

			if (isOutBound) {
				log.info("Intercepting outbound message:");
			} else {
				log.info("Intercepting inbound message:");
			}

			soapMsg.writeTo(baos);
			log.info(baos.toString());


		} catch (SOAPException |IOException e) {
			log.error("Log handler error ",e);
		}
	}


}