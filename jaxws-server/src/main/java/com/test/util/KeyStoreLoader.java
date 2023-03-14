package com.test.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 8/3/2023 12:21
 **/
@Slf4j
public class KeyStoreLoader {

	public static KeyStore loadFromClassPath(String resourceName, char [] pass, String type) throws KeyStoreException {

		URL url = KeyStoreLoader.class.getClassLoader().getResource(resourceName);

		KeyStore keyStore = KeyStore.getInstance(type);
		try (FileInputStream inputStream = new FileInputStream(url.getFile())) {

			keyStore.load(inputStream, pass);

		} catch (IOException | CertificateException | NoSuchAlgorithmException e) {
			log.error("Error loading keystore: {}",resourceName, e);
		}

		return keyStore;

	}

}
