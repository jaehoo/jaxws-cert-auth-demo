package com.test.util;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 23/2/2023 20:56
 **/
@Slf4j
public class CustomHttpServerFactory {

	@Setter private KeyStore keystore;
	@Setter private KeyStore truststore;
	@Setter private char[] keystorePassword;

	public CustomHttpServerFactory(KeyStore keystore, KeyStore truststore, char[] keystorePassword) {
		this.keystore = keystore;
		this.truststore = truststore;
		this.keystorePassword = keystorePassword;
	}

	public SSLContext geDummySslContext() throws NoSuchAlgorithmException, KeyManagementException {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		// Creamos un TrustManager que acepte todos los certificados
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
					public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				}
		};

		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

		return sslContext;
	}

	public SSLContext getSslContext() throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {

		// setup the key manager factory
		KeyManagerFactory keyManager = KeyManagerFactory.getInstance(
				KeyManagerFactory.getDefaultAlgorithm()
		);
		keyManager.init(keystore, keystorePassword);

		// setup the trust manager factory
		TrustManagerFactory trustManager = TrustManagerFactory.getInstance(
				KeyManagerFactory.getDefaultAlgorithm()
		);
		trustManager.init(truststore);

		SSLContext sslContext = SSLContext.getInstance("TLS");

		// setup the HTTPS context and parameters
		sslContext.init(keyManager.getKeyManagers()
				, trustManager.getTrustManagers()
				, null);

		return sslContext;
	}

	public HttpsConfigurator getHttpsConfigurator() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

		SSLContext sslContext = getSslContext();

		return new HttpsConfigurator(sslContext){
			public void configure(HttpsParameters params) {
				try {

					// get the remote address if needed
					InetSocketAddress remote = params.getClientAddress();
					log.info("client addr:{}",remote.getAddress() );

					// initialise the SSL context
					SSLEngine sslEngine = sslContext.createSSLEngine();
					SSLParameters sslParameters = sslContext.getDefaultSSLParameters();

					sslParameters.setNeedClientAuth( true );
					sslParameters.setCipherSuites( sslEngine.getEnabledCipherSuites() );
					sslParameters.setProtocols( sslEngine.getEnabledProtocols() );

					params.setSSLParameters(sslParameters);

					log.info("The HTTPS server is connected");

				} catch (Exception ex) {
					log.info("Failed to create the HTTPS port");
				}
			}
		};
	}


	public HttpsServer createServer(int port) throws IOException {
		HttpsServer httpsServer=HttpsServer.create(new InetSocketAddress(port), 0);

		try {
			HttpsConfigurator httpsConfigurator  = getHttpsConfigurator();
			httpsServer.setHttpsConfigurator(httpsConfigurator);
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException |KeyStoreException |KeyManagementException e) {
			log.error("Error creating the https server", e);
		}
		return httpsServer;
	}

}
