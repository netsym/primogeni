package monitor.ssl;

/*
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.mina.filter.ssl.KeyStoreFactory;
import org.apache.mina.filter.ssl.SslContextFactory;
import org.apache.mina.filter.ssl.SslFilter;

/**
 * @author Nathanael Van Vorst
 *
 */
public class PrimoSslContextFactory {
	public static String PASSWD = "foobar";
	private static SSLContext serverInstance = null;
	private static SSLContext clientInstance = null;

	/**
	 * Get SSLContext singleton.
	 *
	 * @return SSLContext
	 * @throws java.security.GeneralSecurityException
	 *
	 */
	public static SslFilter getServerFilter(String keystore)	throws GeneralSecurityException {
		return new SslFilter(getServerContext(keystore));
	}

	public static SSLContext getServerContext(String keystore)	throws GeneralSecurityException {
		synchronized(PrimoSslContextFactory.class) {
			if (serverInstance == null) {
				try {
					serverInstance = getSslContext(keystore);
				} catch (Exception ioe) {
					throw new GeneralSecurityException(
							"Can't create Server SSLContext:" + ioe);
				}
			}
		}
		return serverInstance;
	}

	public static SslFilter getClientFilter(String keystore) throws GeneralSecurityException, IOException {
		SslFilter rv = new SslFilter(getClientContext(keystore));
		rv.setUseClientMode(true);
		return rv;
	}

	public static SSLContext getClientContext(String keystore) throws GeneralSecurityException, IOException {
		synchronized (PrimoSslContextFactory.class) {
			if (clientInstance == null) {
				clientInstance = getSslContext(keystore);
			}
		}
		return clientInstance;
	}

	private static SSLContext getSslContext(String keystorepath) {
		SSLContext sslContext = null;
		try {
			File keyStoreFile = new File(keystorepath);

			if (keyStoreFile.exists()) {
				final KeyStoreFactory keyStoreFactory = new KeyStoreFactory();
				System.out.println("Url is: " + keyStoreFile.getAbsolutePath());
				keyStoreFactory.setDataFile(keyStoreFile);
				keyStoreFactory.setPassword(PASSWD);

				final KeyStoreFactory trustStoreFactory = new KeyStoreFactory();
				trustStoreFactory.setDataFile(keyStoreFile);
				trustStoreFactory.setPassword(PASSWD);

				final SslContextFactory sslContextFactory = new SslContextFactory();
				final KeyStore keyStore = keyStoreFactory.newInstance();
				sslContextFactory.setKeyManagerFactoryKeyStore(keyStore);

				final KeyStore trustStore = trustStoreFactory.newInstance();
				sslContextFactory.setTrustManagerFactoryKeyStore(trustStore);
				sslContextFactory
				.setKeyManagerFactoryKeyStorePassword(PASSWD);
				sslContext = sslContextFactory.newInstance();
				System.out.println("SSL provider is: "
						+ sslContext.getProvider());
			} else {
				throw new RuntimeException("The Keystore '"+keystorepath+"' does not exist");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sslContext;
	}
/*
	private static SSLContext getSslContext_works0() {
		SSLContext sslContext = null;
		try {
			File keyStoreFile = new File(
			"/Users/vanvorst/Documents/workspace/primex/monitor/src/com/sample/ssl/keystore.jks");
			File trustStoreFile = new File(
			"/Users/vanvorst/Documents/workspace/primex/monitor/src/com/sample/ssl/truststore.jks");

			if (keyStoreFile.exists() && trustStoreFile.exists()) {
				final KeyStoreFactory keyStoreFactory = new KeyStoreFactory();
				System.out.println("Url is: " + keyStoreFile.getAbsolutePath());
				keyStoreFactory.setDataFile(keyStoreFile);
				keyStoreFactory.setPassword("techbrainwave");

				final KeyStoreFactory trustStoreFactory = new KeyStoreFactory();
				trustStoreFactory.setDataFile(trustStoreFile);
				trustStoreFactory.setPassword("techbrainwave");

				final SslContextFactory sslContextFactory = new SslContextFactory();
				final KeyStore keyStore = keyStoreFactory.newInstance();
				sslContextFactory.setKeyManagerFactoryKeyStore(keyStore);

				final KeyStore trustStore = trustStoreFactory.newInstance();
				sslContextFactory.setTrustManagerFactoryKeyStore(trustStore);
				sslContextFactory
				.setKeyManagerFactoryKeyStorePassword("techbrainwave");
				sslContext = sslContextFactory.newInstance();
				System.out.println("SSL provider is: "
						+ sslContext.getProvider());
			} else {
				System.out
				.println("Keystore or Truststore file does not exist");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sslContext;
	}
	*/
}
