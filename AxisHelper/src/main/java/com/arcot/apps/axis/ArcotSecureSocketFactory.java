/**
 * 
 */
package com.arcot.apps.axis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Hashtable;

import org.apache.axis.components.net.BooleanHolder;
import org.apache.axis.components.net.SunJSSESocketFactory;


/**
 * <p>
 * Secure Socket Factory
 * </p>
 * <p>
 * Capable of initializing secure socket with its own trust and key-store.
 * </p>
 * @author Richard Unger
 */
public class ArcotSecureSocketFactory extends SunJSSESocketFactory {
	
	/**
	 * @param attributes
	 */
	public ArcotSecureSocketFactory(Hashtable attributes) {
		super(attributes);
	}	
	
	/**
	 * @see org.apache.axis.components.net.JSSESocketFactory#create(java.lang.String, int, java.lang.StringBuffer, org.apache.axis.components.net.BooleanHolder)
	 */
	@Override
	public Socket create(String arg0, int arg1, StringBuffer arg2, BooleanHolder arg3) throws Exception {
		Socket sock = super.create(arg0, arg1, arg2, arg3);
		return sock;
	}

	/**
	 * @see org.apache.axis.components.net.SunJSSESocketFactory#getContext()
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected com.sun.net.ssl.SSLContext getContext() throws Exception {
        if(attributes == null) {
        	com.sun.net.ssl.SSLContext context = com.sun.net.ssl.SSLContext.getInstance("SSL");    // SSL
            // init context with the key managers
            context.init(null, null, null);
            return context;
        }
        // Please don't change the name of the attribute - other
        // software may depend on it ( j2ee for sure )
        String keystoreFile = (String) attributes.get("keystore");
        //if (keystoreFile == null)
            //keystoreFile = System.getProperty("user.home") + "/.keystore";
        String keystoreType = (String) attributes.get("keystoreType");
        if (keystoreType == null)
            keystoreType = "JKS";
        String keyPass = (String) attributes.get("keypass");
        if (keyPass == null)
            keyPass = "changeit";
        String keystorePass = (String) attributes.get("keystorePass");
        if (keystorePass == null)
            keystorePass = keyPass;
        /**
         * keystore provider name
         */
        String keyStoreProvider = null;
        keyStoreProvider = (String) attributes.get("keyStoreProvider");
        /**
         * keystore provider class
         */
        Object objKeyStoreProvider = null;
        objKeyStoreProvider = (java.security.Provider) attributes.get("objKeyStoreProvider"); 

        String truststorePass = (String) attributes.get("truststorePass");
        if (truststorePass==null)
        	truststorePass = keyPass;
        String truststoreFile = (String) attributes.get("truststore");
        if (truststoreFile == null)
        	truststoreFile = System.getProperty("user.home") + "/.truststore";
        String truststoreType = (String) attributes.get("truststoreType");
        if (truststoreType==null)
        	truststoreType = "JKS";
        /**
         * truststore provider name
         */
        String trustStoreProvider = null;
        trustStoreProvider = (String) attributes.get("trustStoreProvider");  
        /**
         * truststore provider class
         */
        Object objTrustStoreProvider = null;
        objTrustStoreProvider = (java.security.Provider) attributes.get("objTrustStoreProvider"); 
        
        
        // protocol for the SSL ie - TLS, SSL v3 etc.
        String protocol = (String) attributes.get("protocol");
        if (protocol == null)
            protocol = "TLSv1.2";
        // Algorithm used to encode the certificate ie - SunX509
        String algorithm = (String) attributes.get("algorithm");
        if (algorithm == null)
            algorithm = "SunX509";
        
        // Create a KeyStore
        com.sun.net.ssl.KeyManagerFactory kmf = com.sun.net.ssl.KeyManagerFactory.getInstance(algorithm);
        if(keystoreFile != null){
        	KeyStore kstore = null;
        	if (objKeyStoreProvider !=null)
        		kstore = initKeyStore(keystoreFile, keystorePass, keystoreType,objKeyStoreProvider);
        	else if (keyStoreProvider !=null)	 	
        		kstore = initKeyStore(keystoreFile, keystorePass, keystoreType,keyStoreProvider);
        	else
        		kstore = initKeyStore(keystoreFile, keystorePass, keystoreType);
            kmf.init(kstore, keyPass.toCharArray());        	
        }else{
        	kmf.init(null, null);
        }
        
        // Set up TrustManager
        KeyStore tStore = null;
        if (objTrustStoreProvider !=null)
        	tStore = initKeyStore(truststoreFile, truststorePass, truststoreType, objTrustStoreProvider);
        else if (trustStoreProvider !=null)
        	tStore = initKeyStore(truststoreFile, truststorePass, truststoreType, trustStoreProvider);
        else
        	tStore = initKeyStore(truststoreFile, truststorePass, truststoreType);
        
        com.sun.net.ssl.TrustManagerFactory tmf = com.sun.net.ssl.TrustManagerFactory.getInstance("SunX509");
        tmf.init(tStore);

        // Create a SSLContext ( to create the ssl factory )
        com.sun.net.ssl.SSLContext context = com.sun.net.ssl.SSLContext.getInstance(protocol);    // SSL

        // init context with the key managers
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());
        return context;
	}

	
	


    /**
     * Intializes a keystore with the default provider
     *
     * @param keystoreFile
     * @param keyPass
     *
     * @return keystore
     * @throws IOException
     */
    protected KeyStore initKeyStore(String keystoreFile, String keyPass,String type) throws IOException {
        try {
            KeyStore kstore = null;
            kstore = KeyStore.getInstance(type);
            InputStream istream = new FileInputStream(keystoreFile);
            kstore.load(istream, keyPass.toCharArray());
            return kstore;
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("initKeyStore(String keystoreFile, String keyPass,String type) :: Exception trying to load keystore " + keystoreFile + ": " + ex.getMessage());
        }
    }
    
    /**
     * Passing the Provider NAME
     *   
	 * Example -
	 * for PKCS12 bundle, name = "SunJSSE" , package = com.sun.net.ssl.internal.ssl.Provider()
	 * for JKS    bundle, name    = "SUN" , package = sun.security.provider.Sun()
     *
     * Note:- If you are using the Bouncy Castle Provider, you are to use; since the Name {"BC"} is not recognized
     *        
     *  @ref - initKeyStore(String keystoreFile, String keyPass,String type, Object objProvider)
     *
     * @param keystoreFile
     * @param keyPass
     *
     * @return keystore
     * @throws IOException
     */
    protected KeyStore initKeyStore(String keystoreFile, String keyPass,String type, String keyStoreProvider) throws IOException {
        if (keyStoreProvider == null)
        	return initKeyStore(keystoreFile, keyPass, type);
    	
        try {
            KeyStore kstore = null;
            kstore = KeyStore.getInstance(type.toUpperCase(),keyStoreProvider);
            InputStream istream = new FileInputStream(keystoreFile);
            kstore.load(istream, keyPass.toCharArray());
            return kstore;
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("initKeyStore(String keystoreFile, String keyPass,String type, String keyStoreProvider) :: Exception trying to load keystore " + keystoreFile + ": " + ex.getMessage());
        }
    }    
	
    /**
     * Passing the provider CLASS
 	 *	
     * Example -
	 * for PKCS12 bundle, provider class = com.sun.net.ssl.internal.ssl.Provider()
	 * for JKS    bundle, provider class = sun.security.provider.Sun()
	 * 
	 * If you are using BouncyCastleProvider for achieving certain functionality
	 * 					  provider class = org.bouncycastle.jce.provider.BouncyCastleProvider()
     * 
     * @param keystoreFile
     * @param keyPass
     * @param type
     * @param objProvider
     * @return
     * @throws IOException
     */
    protected KeyStore initKeyStore(String keystoreFile, String keyPass,String type, Object objProvider) throws IOException {
    	
        if (objProvider == null)
        	return initKeyStore(keystoreFile, keyPass, type);       
    	try {
            KeyStore kstore = null;
            kstore = KeyStore.getInstance(type.toUpperCase(),(java.security.Provider)objProvider);
            InputStream istream = new FileInputStream(keystoreFile);
            kstore.load(istream, keyPass.toCharArray());
            return kstore;
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("initKeyStore(String keystoreFile, String keyPass,String type, Object objProvider) :: Exception trying to load keystore " + keystoreFile + ": " + ex.getMessage());
        }
    }    
	
}
