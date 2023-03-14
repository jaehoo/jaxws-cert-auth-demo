package com.test;

import com.test.client.HelloServiceClientTestIT;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 10/3/2023 20:30
 **/
public class SslContextFactory {

	public SSLContext getSslContext() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {

		URL keystore = HelloServiceClientTestIT.class.getClassLoader().getResource("server.jks");
		URL truststore = HelloServiceClientTestIT.class.getClassLoader().getResource("server.jks");

		String pass= "changeit";

		// Cargar el archivo de clave privada
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(new FileInputStream(keystore.getFile()), pass.toCharArray());

		// Configurar el KeyManagerFactory para el acceso a la clave privada
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, pass.toCharArray());

		// Cargar el archivo de certificado público
		KeyStore trustStore = KeyStore.getInstance("PKCS12");
		trustStore.load(new FileInputStream(truststore.getFile()), pass.toCharArray());

		// Configurar el TrustManagerFactory para la validación del certificado del servidor
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);

		// Configurar SSLContext para la autenticación con certificado
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		return sslContext;
	}

//	public SSLContext getSslContextForTests() throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
//		SSLContext sslContext = getSslContext();
//		SSLParameters sslParameters = sslContext.getDefaultSSLParameters();
//		sslParameters.setEndpointIdentificationAlgorithm("HTTPS");
//
//		return sslContext;
//	}
}
