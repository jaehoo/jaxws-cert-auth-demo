package com.test.handler;

import com.sun.net.httpserver.HttpsExchange;


import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 24/2/2023 00:13
 **/
@Slf4j
public class ReadCertificateHandler implements SOAPHandler<SOAPMessageContext> {


	@Override
	public Set<QName> getHeaders() {
		return new HashSet<QName>();
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		handleCerts(context);
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		log.info("Error reading certificate");
		log.error("{}", context.getMessage());

		return false;

	}

	@Override
	public void close(MessageContext context) {
		log.info("Close CertificateHandler ");
	}

	private void handleCerts(SOAPMessageContext context){

		Boolean isOutBound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if(isOutBound){

			//""com.sun.xml.ws.http.exchange"" // jdk 11
			//"com.sun.xml.internal.ws.http.exchange" // jdk 8
			HttpsExchange exchange = (HttpsExchange) context.get("com.sun.xml.internal.ws.http.exchange");
			SSLSession sslsession = exchange.getSSLSession();

			Certificate[] certificates;
			try {
				certificates = sslsession.getPeerCertificates();

			Arrays.stream(certificates)
					.filter(cer -> cer instanceof X509Certificate)
					.forEach( cer ->{
								log.info("subject:{}", ((X509Certificate)cer).getSubjectDN());
								log.info("issuer:{}", ((X509Certificate)cer).getIssuerDN());
							}
					);
			}
			catch (SSLPeerUnverifiedException ex) {
				log.error("No certs founds in request");
			}
		}



	}
}
