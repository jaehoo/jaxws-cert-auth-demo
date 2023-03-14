package com.test.app;

import com.test.util.KeyStoreLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 10/3/2023 19:40
 **/
@RunWith(JUnit4.class)
@Slf4j
public class DummyServerTest {

	private static String PKCS12 = "PKCS12";
	private static DummyServer server;

	@BeforeClass
	public static void beforeClass() throws Exception {
		server = new DummyServer();
	}

	@Before
	public void setUp() throws IOException, KeyStoreException {
		server.launch();
	}

	@After
	public void tearDown() throws Exception {
		server.shutdown();
	}

	@Test
	public void testWsdlRequest() throws Exception {

		URL url = new URL("https://localhost:8443/HelloServer?wsdl");

		// Establish connection
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setSSLSocketFactory(getSslContext().getSocketFactory());

		// Configure parameters
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);

		Assert.assertEquals(200, connection.getResponseCode());

		String response = getResponse(connection);
		log.info("{}", response);
		Assert.assertNotNull(response);
	}


	@Test
	public void testRequestDefault() throws Exception {

		URL url = new URL("https://localhost:8443/test");

		// Establish connection
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setSSLSocketFactory(getSslContext().getSocketFactory());

		// Configure parameters
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		Assert.assertEquals(200, connection.getResponseCode());


	}

	@Test
	public void testSoapRequest() throws Exception {

		URL url = new URL("https://localhost:8443/HelloServer");

		// Establish connection
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setSSLSocketFactory(getSslContext().getSocketFactory());

		// Configure parameters
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		connection.setRequestProperty("SOAPAction", "\"http://dummy/example/action\"");
		connection.setDoOutput(true);

		// Envío de la solicitud al servicio web
		String request =
		"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.test.com/\">\n" +
		"   <soapenv:Header/>\n" +
		"   <soapenv:Body>\n" +
		"      <ser:greet>\n" +
		"         <name>Alberto</name>\n" +
		"      </ser:greet>\n" +
		"   </soapenv:Body>\n" +
		"</soapenv:Envelope>";

		try(OutputStream requestStream = connection.getOutputStream()){
			requestStream.write(request.getBytes());
			requestStream.flush();
		}

		Assert.assertEquals(200, connection.getResponseCode());
		String resp= getResponse(connection);

		Assert.assertTrue(resp.contains("ns2:greetResponse"));

	}

	private String getResponse(HttpURLConnection connection){
		StringBuilder response = new StringBuilder();

		// Read response
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()))) {

			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
		}
		catch (IOException e){
			log.error("{}", e.getMessage(),e );
		}

		return response.toString();
	}

	private SSLContext getSslContext() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {

		char[] defPass = "changeit".toCharArray();

		// Load the Keystore and Truststore
		KeyStore keystore = KeyStoreLoader.loadFromClassPath("server.jks", defPass, PKCS12);
		KeyStore truststore = KeyStoreLoader.loadFromClassPath("server.jks", defPass, PKCS12);


		KeyManagerFactory kmf = KeyManagerFactory.getInstance(
				KeyManagerFactory.getDefaultAlgorithm()
		);
		kmf.init(keystore, defPass);

		// setup the trust manager factory
		TrustManagerFactory trustManager = TrustManagerFactory.getInstance(
				KeyManagerFactory.getDefaultAlgorithm()
		);
		trustManager.init(truststore);


		SSLContext sslContext = SSLContext.getInstance("TLS");

		// setup the HTTPS context and parameters
		sslContext.init(kmf.getKeyManagers()
				, trustManager.getTrustManagers()
				, null);

		return sslContext;
	}

}