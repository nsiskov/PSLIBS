/**
 * 
 */
package com.arcot.apps.axis;

//import java.util.HashMap;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.imageio.spi.ServiceRegistry;
import javax.net.ssl.KeyManagerFactory;

import org.apache.axis.AxisEngine;
import org.apache.axis.AxisProperties;
import org.apache.axis.ConfigurationException;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.encoding.TypeMappingRegistry;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.apache.ws.axis.security.WSDoAllReceiver;
import org.apache.ws.axis.security.WSDoAllSender;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.logger.ArcotLogger;
import com.arcot.vpas.enroll.cache.ESCache;

/**
 * <p>
 * Axis Client Configuration
 * </p>
 * @author Richard Unger
 */
public class ArcotAxisEngineConfig extends SimpleProvider {

	/**
	 * Keystore filename
	 */
	private String keystore = null;
	/**
	 * Keystore type
	 */
	private String keystoretype = null;
	/**
	 * Keystore password
	 */
	private String keystorepin = null;
	/**
	 * Key password
	 */
	private String keypin = null;
	/**
	 * Pass the keystore provider name
	 */
	private String keyStoreProvider = null;
	/**
	 * Pass the truststore provider name
	 */
	private String trustStoreProvider = null;
	/**
	 * Pass the truststore provider class
	 */
	private Object objTrustStoreProvider = null;
	/**
	 * Pass the keystore provider class
	 */
	private Object objKeyStoreProvider = null;

	/**
	 * Pass ones own WS_REQUEST_HANDLER class
	 * Default - org.apache.ws.axis.security.WSDoAllSender
	 */
	private Object wsReqHandler = null;
	
	/**
	 * Pass ones own WS_RESPONSE_HANDLER class
	 * Default - org.apache.ws.axis.security.WSDoAllReceiver
	 */
	private Object wsRespHandler = null;


	/*
	 * serviceOptions
	 * */
	Hashtable serviceOptions = null;
	Hashtable serviceOptionsResponse = null;
	String serviceName = null;
	private static String DISABLE_CERT_CHECK = "DISABLE_CERT_CHECK";
	
	public String getKeyPassPhrase() {
		return keypin;
	}

	public void setKeyPassPhrase(String keypin) {
		this.keypin = keypin;
	}

	/**
	 * Truststore filename
	 */
	private String truststore = null;
	/**
	 * Truststore PIN
	 */
	private String truststorepin = null;
	/**
	 * Truststore Type
	 */
	private String truststoreType = null;
	/**
	 * true to disable XML formatting
	 */
	private boolean disablePrettyXML = true;
	/**
	 * true to enable namespace prefix optimization (see Axis docs)
	 */
	private boolean enableNamespacePrefixOptimization = false;

	/**
	 * Constructor
	 */
	public ArcotAxisEngineConfig() {
		super();
	}

	/**
	 * @param arg0
	 */
	public ArcotAxisEngineConfig(EngineConfiguration arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public ArcotAxisEngineConfig(TypeMappingRegistry arg0) {
		super(arg0);
	}

	
	
	
	/**
	 * @see org.apache.axis.configuration.SimpleProvider#configureEngine(org.apache.axis.AxisEngine)
	 */
	
	public void configureEngine(AxisEngine engine) throws ConfigurationException {
		super.configureEngine(engine);
		engine.refreshGlobalOptions();
	}

	/**
	 * @param keystore the keystore to set
	 */
	public void setKeystore(String keystore) {
		this.keystore = keystore;
	}

	/**
	 * @param keystorepin the keystorepin to set
	 */
	public void setKeystorePassword(String keystorepin) {
		this.keystorepin = keystorepin;
	}

	/**
	 * @param keystoretype the keystoretype to set
	 */
	public void setKeystoreType(String keystoretype) {
		this.keystoretype = keystoretype;
	}

	/**
	 * @param truststore the truststore to set
	 */
	public void setTruststore(String truststore) {
		this.truststore = truststore;
	}

	/**
	 * @param truststorepin the truststorepin to set
	 */
	public void setTruststorePassword(String truststorepin) {
		this.truststorepin = truststorepin;
	}

	/**
	 * @return the truststoreType
	 */
	public String getTruststoreType() {
		return truststoreType;
	}

	/**
	 * @param truststoreType the truststoreType to set
	 */
	public void setTruststoreType(String truststoreType) {
		this.truststoreType = truststoreType;
	}
	
	public void setServiceOptions(String name, Hashtable options){
		serviceName = name;
		serviceOptions = options;
	}
	
	public void setServiceOptions(String name, Hashtable options, Hashtable optionsResponse){
		serviceName = name;
		serviceOptions = options;
		serviceOptionsResponse = optionsResponse;
	}


	/**
	 * <p>
	 * Initialize, with logging off
	 * </p>
	 */
	public void initialize() {
		initialize(false);
	}	
	
	/**
	 * 
	 * @return
	 */
	public String getKeyStoreProvider() {
		return keyStoreProvider;
	}

	/**
	 * Set the provider name to be used for KeyStore Provider
	 * Example -
	 * for PKCS12 bundle, name = "SunJSSE" , package = com.sun.net.ssl.internal.ssl.Provider()
	 * for JKS bundle, name    = "SUN" , package = sun.security.provider.Sun()
	 * @param trustStoreProvider
	 */
	public void setKeyStoreProvider(String keyStoreProvider) {
		this.keyStoreProvider = keyStoreProvider;
	}

	/**
	 * 
	 * @return
	 */
	public String getTrustStoreProvider() {
		return trustStoreProvider;
	}

	/**
	 * Set the provider name to be used for TrustStore Provider
	 * Example -
	 * for PKCS12 bundle, name = "SunJSSE" , package = com.sun.net.ssl.internal.ssl.Provider()
	 * for JKS bundle, name    = "SUN" , package = sun.security.provider.Sun()
	 * @param trustStoreProvider
	 */
	public void setTrustStoreProvider(String trustStoreProvider) {
		this.trustStoreProvider = trustStoreProvider;
	}
	
	public Object getObjTrustStoreProvider() {
		return objTrustStoreProvider;
	}

	/**
	 * Set the provider CLASS to be used for TrustStore Provider
	 * Example -
	 * for PKCS12 bundle, provider class = com.sun.net.ssl.internal.ssl.Provider()
	 * for JKS    bundle, provider class = sun.security.provider.Sun()
	 * 
	 * If you are using BouncyCastleProvider for achieving certain functionality
	 * 					  provider class = org.bouncycastle.jce.provider.BouncyCastleProvider()
	 * 
	 * @param objTrustStoreProvider
	 */
	public void setObjTrustStoreProvider(Object objTrustStoreProvider) {
		this.objTrustStoreProvider = objTrustStoreProvider;
	}

	public Object getObjKeyStoreProvider() {
		return objKeyStoreProvider;
	}

	/**
	 * Set the provider CLASS to be used for TrustStore Provider
	 * Example -
	 * for PKCS12 bundle, provider class = com.sun.net.ssl.internal.ssl.Provider()
	 * for JKS    bundle, provider class = sun.security.provider.Sun()
	 * 
	 * If you are using BouncyCastleProvider for achieving certain functionality
	 * 					  provider class = org.bouncycastle.jce.provider.BouncyCastleProvider()
	 * 
	 * @param objKeyStoreProvider
	 */
	public void setObjKeyStoreProvider(Object objKeyStoreProvider) {
		this.objKeyStoreProvider = objKeyStoreProvider;
	}
	
	public Object getWsReqHandler() {
		return wsReqHandler;
	}

	public void setWsReqHandler(Object wsReqHandler) {
		this.wsReqHandler = wsReqHandler;
	}

	public Object getWsRespHandler() {
		return wsRespHandler;
	}

	public void setWsRespHandler(Object wsRespHandler) {
		this.wsRespHandler = wsRespHandler;
	}
	
	/**
	 * <p>
	 * Initialize
	 * </p>
	 * @param logging true if logging is desired
	 */
	@SuppressWarnings("unchecked")
	
	public void initialize(boolean logging, COConfig config) {
		if( config != null && config.getValue(DISABLE_CERT_CHECK) != null && config.getValue(DISABLE_CERT_CHECK).equalsIgnoreCase("y")){
			AxisProperties.setProperty("axis.socketSecureFactory","org.apache.axis.components.net.SunFakeTrustSocketFactory");
		} else {
			AxisProperties.setProperty("axis.socketSecureFactory", "com.arcot.apps.axis.ArcotSecureSocketFactory");
		}		
		AxisProperties.setProperty("axis.socketFactory","org.apache.axis.components.net.DefaultSocketFactory");

		Hashtable<String, Boolean> opts = new Hashtable<String, Boolean>();
        opts.put(AxisEngine.PROP_DISABLE_PRETTY_XML, disablePrettyXML);
        opts.put(AxisEngine.PROP_ENABLE_NAMESPACE_PREFIX_OPTIMIZATION, enableNamespacePrefixOptimization);
        setGlobalOptions(opts);
        
        Handler pivot = new HTTPSender();
        if (truststore!=null){
        	pivot.setOption("truststore",truststore);
	        if (truststoreType!=null)
	        	pivot.setOption("truststoreType",truststoreType);
	        if (truststorepin!=null)
	        	pivot.setOption("truststorePass",truststorepin);
	        if (trustStoreProvider!=null)
	        	pivot.setOption("trustStoreProvider",trustStoreProvider);
	        if (objTrustStoreProvider!=null)
	        	pivot.setOption("objTrustStoreProvider", objTrustStoreProvider);
            // new code
            // lets not stop if the validity check fails for some reason
            if (truststorepin!=null && truststore!=null){
		        try{
	            	checkValidityOfBundle(truststore, truststorepin);
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
            }
        }
        
        if (keystore != null){
        	pivot.setOption("clientauth","true");
           	pivot.setOption("keystore", keystore);
            if (keystoretype!=null)
            	pivot.setOption("keystoreType", keystoretype);
            if (keystorepin!=null){
            	pivot.setOption("keystorePass", keystorepin);        	
            }
            if (keypin!=null){
            	pivot.setOption("keypass",keypin);        	
            }
	        if (keyStoreProvider!=null)
	        	pivot.setOption("keyStoreProvider",keyStoreProvider);   
	        if (objKeyStoreProvider!=null)
	        	pivot.setOption("objKeyStoreProvider", objKeyStoreProvider);
            // new code
            // lets not stop if the validity check fails for some reason
            if (keystorepin!=null && keystore!=null){
		        try{
	            	checkValidityOfBundle(keystore, keystorepin);
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
            }
        }
        
        Handler transport = new SimpleTargetedChain(pivot);
        deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, transport);
        
        /**
         * 
         * Deploy service handlers
         * 
         **/
        if(serviceOptions != null){
        	//Need to add required handler as provided in service options
        	Handler requestHandler = null;
        	Handler responseHandler = null;        	
        	
        	if (wsReqHandler == null)
        		requestHandler = new WSDoAllSender();
        	else
        		requestHandler = (Handler) wsReqHandler;
        	requestHandler.setOptions(serviceOptions);
        	
        	if (serviceOptionsResponse == null){
	        	try {
					responseHandler = getGlobalResponse();
				} catch (ConfigurationException e) {
					e.printStackTrace();
				}
        	}else{
        		if (wsRespHandler == null)
        			responseHandler = new WSDoAllReceiver();
        		else
        			responseHandler = (Handler) wsRespHandler;
        		responseHandler.setOptions(serviceOptionsResponse);
        	}
        	
        	deployService(serviceName, new SOAPService(requestHandler, pivot, responseHandler));
        }
	}
	
	public void initialize(boolean logging) {
		initialize(logging, null);
	}

	/**
	 * @return the disablePrettyXML
	 */
	public boolean isDisablePrettyXML() {
		return disablePrettyXML;
	}

	/**
	 * @param disablePrettyXML the disablePrettyXML to set
	 */
	public void setDisablePrettyXML(boolean disablePrettyXML) {
		this.disablePrettyXML = disablePrettyXML;
	}

	/**
	 * @return the enableNamespacePrefixOptimization
	 */
	public boolean isEnableNamespacePrefixOptimization() {
		return enableNamespacePrefixOptimization;
	}

	/**
	 * @param enableNamespacePrefixOptimization the enableNamespacePrefixOptimization to set
	 */
	public void setEnableNamespacePrefixOptimization(
			boolean enableNamespacePrefixOptimization) {
		this.enableNamespacePrefixOptimization = enableNamespacePrefixOptimization;
	}
	
	/**
	 * Will print info with regards to a certificate in a bundle if its about to expire in the next 30 days period
	 * @param storePath
	 * @param passphrase
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 */
	private static void checkValidityOfBundle(String storePath, String passphrase) 
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException{
        KeyStore keyStore = null;
        if(storePath.endsWith("p12") || storePath.endsWith("P12"))
        	keyStore = KeyStore.getInstance("PKCS12");
        else
        	keyStore = KeyStore.getInstance("JKS");
        
        if(storePath != null && !"".equals(storePath.trim())){
            InputStream in = new FileInputStream(storePath);
            in = new BufferedInputStream(in);
            keyStore.load(in, passphrase.toCharArray());
        }
		Enumeration keyEnum = keyStore.aliases();
        String elem = null;
        String threshold = null;
        try{
        	threshold = ESCache.esc.getValue("PS_CERT_THRESHOLD");
        }catch(Exception e){
        	e.printStackTrace();
        }
		if(threshold == null || threshold.length()==0){
			threshold = "30";
		}
        while(keyEnum.hasMoreElements()){
        	elem = (String)keyEnum.nextElement();
        	if (keyStore.getCertificate(elem).getType().equals("X.509")){
        		Date expiryDateOfCert = ((X509Certificate) keyStore.getCertificate(elem)).getNotAfter();
        		Date todayDate = new Date();
        		Calendar calendarExpDate = Calendar.getInstance();
        		Calendar calendarToday = Calendar.getInstance();
        		calendarExpDate.setTime(expiryDateOfCert);
        		calendarToday.setTime(todayDate);
        		long millisecondsExpDate = calendarExpDate.getTimeInMillis();
        		long millisecondsToday = calendarToday.getTimeInMillis();
        		long diff = millisecondsExpDate - millisecondsToday;
        		long dayAlert = diff / (24 * 60 * 60 * 1000);
        		if (dayAlert < Integer.parseInt(threshold)){
        				ArcotLogger.logInfo("SSL CERT EXPIRY INFO :: STORE [ " + storePath + " ]  about to expire in "+dayAlert+" days. Certificate alias [ " + elem + " ] " +
        					"Subject DN [" + ((X509Certificate) keyStore.getCertificate(elem)).getSubjectDN() + " ] Issuer DN [ "+((X509Certificate) keyStore.getCertificate(elem)).getIssuerDN() +
        					" ] Valid FROM [ " + ((X509Certificate) keyStore.getCertificate(elem)).getNotBefore() + " ] " +
        							"TO [ "+ ((X509Certificate) keyStore.getCertificate(elem)).getNotAfter() +" ]");
        		}
        	}
        }
	}
}
