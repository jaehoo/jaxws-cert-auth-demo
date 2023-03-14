package com.test.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import static org.junit.Assert.*;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 10/3/2023 22:59
 **/
@RunWith(MockitoJUnitRunner.class)
public class LogHandlerTest {

	
    private LogHandler handler;

	@Mock
	private SOAPMessageContext messageContextMock;

	@Mock
	private SOAPMessage soapMessage;

	@Before
	public void setUp() throws Exception {
		handler = new LogHandler();

		Mockito.when(messageContextMock
						.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))
				.thenReturn(true);

		Mockito.when(messageContextMock.getMessage())
				.thenReturn(soapMessage);

	}

	@Test
	public void testHandleMessageOut() {
		boolean result = handler.handleMessage(messageContextMock);
		assertTrue(result);
	}

	@Test
	public void testHandleMessageIn() {
		Mockito.when(messageContextMock
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))
				.thenReturn(false);

		boolean result = handler.handleMessage(messageContextMock);
		assertTrue(result);
	}

	@Test
	public void testHandleFaultMessage() {
		assertFalse(handler.handleFault(messageContextMock));
	}
}