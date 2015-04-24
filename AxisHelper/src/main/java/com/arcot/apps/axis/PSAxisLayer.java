package com.arcot.apps.axis;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.transport.http.HTTPTransport;
import org.apache.axis.utils.ClassUtils;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;

/**
 * 
 * @author bossa02
 * To used for SOAP 1.1 compliant Web Service
 * 
 * It uses Reflection to create the Stub,Locator required for a WebService.
 * Using this class the following can be eliminated from BLH code. 
 * 
 * 1. 	Creation of Stub
 * 2. 	Creation of Locator
 * 3. 	SSL Handling/ setting Axis level parameters throw Service options
 * 
 * 				-- CONFIGURATION DETAILS in CALLOUT CONFIGURATION --
 * 
 * 					-- For One Way SSL:-
 * TRUSTSTORE											[Path to the TrustStore Bundle]
 * TRUSTSTORE_TYPE										[TrustStore Type  e.g:- PKCS12, JKS]
 * TRUSTSTORE_PASSPHRASE								[PassPhrase to access the above Bundle]
 * 
 * 					-- For Two Way SSL:-
 * TRUSTSTORE											[Path to the TrustStore Bundle]
 * TRUSTSTORE_TYPE										[TrustStore Type  e.g:- PKCS12, JKS] - by default if not passed, will be set to "JKS"
 * TRUSTSTORE_PASSPHRASE								[PassPhrase to access the above Bundle]
 * KEYSTORE												[Path to the KeyStore Bundle]
 * KEYSTORE_TYPE										[KeyStore Type  e.g:- PKCS12, JKS] - by default if not passed, will be set to "JKS"
 * KEY_PASSPHRASE										[PassPhrase to access the JKS Bundle]
 * KEYSTORE_PASSPHRASE									[PassPhrase to access the private key in JKS Bundle]
 * 
 * 					-- Mandatory Configuration:-
 * END_POINT_URL										[URL to which the SOAP requests needs to be posted]
 * SERVICE_PORT_NAME									[Port Name of the BackEnd web service server]	
 * LOCATOR_CLASS										[ClassName of the Locator generated Client Code]
 * 
 * 
 * 
 *					-- WS Security --
 * 
 * SERVICE_OPTIONS										true {Indicator that SERVICE_OPTIONS are to be used}
 * 
 * BLH has to call setHtServiceOptions(Hashtable) to populate the HashTable containing the different SERVICE_OPTION Parameters for WS Request Handler
 * BLH has to call setResHtServiceOptions(Hashtable) to populate the HashTable containing the different SERVICE_OPTION Parameters for WS Response Handler
 * 
 * 1. WS_REQ_SENDER_CLASS								[If a customized WS request Handler extending 'WSDoAllSender' is created]
 * 2. WS_RESP_RECIEVER_CLASS							[If a customized WS request Handler extending 'WSDoAllReceiver' is created]
 * 
 * 					-- IF A CUSTOMIZED HANDLER IS WRITTEN -- 
 * 
 * 1. Extend PSAxisHandler, and override ONLY the methods required
 * HANDLER_CLASS										[User defined ClassName that extends PSAxisHandler]
 * PIVOT												true {Indication that a handler is present}
 * 
 * 					-- For One Way SSL:-
 * 
 * TRUSTSTORE_PASSPHRASE								[Required ONLY IF the TRUSTSTORE below is a bundle {JKS,P12}]
 * TRUSTSTORE											[Path to the TrustStore Bundle OR Server Certificate which needs to be trusted]
 * 
 * 					-- For Two Way SSL:-
 * 
 * KEYSTORE												[Path to the KeyStore Bundle]
 * KEYSTORE_PASSPHRASE									[PassPhrase to access the above Bundle] 
 * 
 * 					-- Other Params:-
 * 
 * DISABLE_SERVER_CERT_CHECK							[If the Server Certificate check needs to be disabled. Should never be used on Production Systems]
 * RESPONSE_TIME_OUT									[Time in millisecond for the request/response Cycle to complete]
 * 
 * 					
 */

public class PSAxisLayer{
	
	
	private Hashtable<String, String> htServiceOptions = null;
	private Hashtable<String, String> htResServiceOptions = null;
	/**
	 * Following method will create the Stub for the Web Service in question. 
	 * To be called from the BLH
	 * 
	 * 
	 * @param portName	
	 * 
	 *  You can get this from the WSDL file 
	 *  Section:-
	 *  <wsdl:port binding="{bindName}" name="{portName}">
	 * 
	 * @param config
	 * @param logger
	 * @return - Will be NULL in case there is an issue in creation of the STUB
	 *
	 */
	public Object fetchStub(CORequest request, COConfig config, COLogger logger){
		
		Class<?> locatorInstanse = null;
		try {
			locatorInstanse = ClassUtils.forName(config.getValue("LOCATOR_CLASS"));
		} catch (ClassNotFoundException e) {
			logger.fatal("CLASS NOT FOUND for Config [LOCATOR_CLASS] Val [ "+config.getValue("LOCATOR_CLASS")+" ]", e);
			return null;
		}
		Object objLocator = null;
		Object objStub = null;

		SimpleProvider clientConfig = null;
		AxisClient axisClient = null;
		Handler pivot = null;
		ArcotAxisEngineConfig axisConfig = null;
		Class<?> handlerInstance = null;
		
		if (config.getValue("PIVOT") != null && "true".equalsIgnoreCase(config.getValue("PIVOT"))){
			clientConfig = new SimpleProvider();
			Hashtable<String, Object> opts = new Hashtable<String, Object>();
			
			if (request !=null)
				opts.put("CORequest", request);
			if (config !=null)
				opts.put("COConfig", config);
			if (logger !=null)
				opts.put("COLogger", logger);
			
			if (config.getValue("HANDLER_CLASS") != null){
				try {
					handlerInstance = ClassUtils.forName(config.getValue("HANDLER_CLASS"));
				} catch (ClassNotFoundException e) {
					logger.fatal("CLASS NOT FOUND for Config [HANDLER_CLASS] Val [ "+config.getValue("HANDLER_CLASS")+" ]", e);
					return null;
				}
				try {
					pivot = (PSAxisHandler) handlerInstance.newInstance();
				} catch (InstantiationException e) {
					logger.fatal("CLASS could not be instantiated for Config [HANDLER_CLASS] Val [ "+config.getValue("HANDLER_CLASS")+" ]", e);
					return null;
				} catch (IllegalAccessException e) {
					logger.fatal("Illegal Access for Config [HANDLER_CLASS] Val [ "+config.getValue("HANDLER_CLASS")+" ]", e);
					return null;
				}
			}else{	
				pivot = new PSAxisHandler();
			}
			pivot.setOptions(opts);
			Handler transport = new SimpleTargetedChain(new SimpleChain(), pivot, new SimpleChain());
			clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME,transport);
			axisClient = new AxisClient(clientConfig);
		}else{
			if ((config.getValue("TRUSTSTORE") != null) || (config.getValue("KEYSTORE") != null)){
				axisConfig = new ArcotAxisEngineConfig();
				if (config.getValue("TRUSTSTORE") != null){
					axisConfig = createTrustStore(axisConfig, config, logger);
				}
				if (config.getValue("TRUSTSTORE_PROVIDER_NAME") != null){
					axisConfig = selectTrustStoreProviderName(axisConfig, config, logger);
				}
				if (config.getValue("TRUSTSTORE_PROVIDER") != null){
					try {
						axisConfig = selectTrustStoreProvider(axisConfig, config, logger);
					} catch (InstantiationException e) {
						logger.fatal("Class could not be instantiated for Config [TRUSTSTORE_PROVIDER] Val [ "+config.getValue("TRUSTSTORE_PROVIDER")+" ]", e);
						return null;
					} catch (IllegalAccessException e) {
						logger.fatal("Illegal Access for Config [TRUSTSTORE_PROVIDER] Val [ "+config.getValue("TRUSTSTORE_PROVIDER")+" ]", e);
						return null;
					} catch (ClassNotFoundException e) {
						logger.fatal("CLASS NOT FOUND for Config [TRUSTSTORE_PROVIDER] Val [ "+config.getValue("TRUSTSTORE_PROVIDER")+" ]", e);
						return null;
					}
				}
				if (config.getValue("KEYSTORE") != null){
					axisConfig = createKeyStore(axisConfig, config, logger);
				}
				if (config.getValue("KEYSTORE_PROVIDER_NAME") != null){
					axisConfig = selectKeyStoreProviderName(axisConfig, config, logger);
				}
				if (config.getValue("KEYSTORE_PROVIDER") != null){
					try {
						axisConfig = selectKeyStoreProvider(axisConfig, config, logger);
					} catch (InstantiationException e) {
						logger.fatal("Class could not be instantiated for Config [KEYSTORE_PROVIDER] Val [ "+config.getValue("KEYSTORE_PROVIDER")+" ]", e);
						return null;
					} catch (IllegalAccessException e) {
						logger.fatal("Illegal Access for Config [KEYSTORE_PROVIDER] Val [ "+config.getValue("KEYSTORE_PROVIDER")+" ]", e);
						return null;
					} catch (ClassNotFoundException e) {
						logger.fatal("CLASS NOT FOUND for Config [KEYSTORE_PROVIDER] Val [ "+config.getValue("KEYSTORE_PROVIDER")+" ]", e);
						return null;
					}
				}
				if (config.getValue("SERVICE_OPTIONS") != null && "true".equalsIgnoreCase(config.getValue("SERVICE_OPTIONS"))){
					
					Hashtable<String, String> htReqServiceOptions = getHtServiceOptions();
					Hashtable<String, String> htRespServiceOptions = getRespHtServiceOptions();
					axisConfig.setServiceOptions(config.getValue("SERVICE_PORT_NAME"), htReqServiceOptions, htRespServiceOptions);
					
					if ((htReqServiceOptions != null) && (htReqServiceOptions.get("WS_REQ_SENDER_CLASS") != null)){
						// in case for some reason the developer wants to use a customized version of  'org.apache.ws.axis.security.WSDoAllSender'
						// in such a scenario , create a class extending 'org.apache.ws.axis.security.WSDoAllSender' and provide the CLASS NAME in 'WS_REQ_SENDER_CLASS'
						try {
							axisConfig.setWsReqHandler((ClassUtils.forName(config.getValue("WS_REQ_SENDER_CLASS")).newInstance()));
						} catch (InstantiationException e) {
							logger.fatal("CLASS could not be instantiated for Config [WS_REQ_SENDER_CLASS] Val [ "+config.getValue("WS_REQ_SENDER_CLASS")+" ]", e);
							return null;
						} catch (IllegalAccessException e) {
							logger.fatal("Illegal Access Exception for Config [WS_REQ_SENDER_CLASS] Val [ "+config.getValue("WS_REQ_SENDER_CLASS")+" ]", e);
							return null;
						} catch (ClassNotFoundException e) {
							logger.fatal("CLASS NOT FOUND for Config [WS_REQ_SENDER_CLASS] Val [ "+config.getValue("WS_REQ_SENDER_CLASS")+" ]", e);
							return null;
						}
					}
					
					if ((htRespServiceOptions != null) && (htRespServiceOptions.get("WS_RESP_RECIEVER_CLASS") != null)){
						// in case for some reason the developer wants to use a customized version of 'org.apache.ws.axis.security.WSDoAllReceiver'
						// in such a scenario , create a class extending 'org.apache.ws.axis.security.WSDoAllReceiver' and provide the CLASS NAME in 'WS_RESP_RECIEVER_CLASS'
						try {
							axisConfig.setWsRespHandler((ClassUtils.forName(config.getValue("WS_RESP_RECIEVER_CLASS")).newInstance()));
						} catch (InstantiationException e) {
							logger.fatal("CLASS could not be instantiated for Config [WS_RESP_RECIEVER_CLASS] Val [ "+config.getValue("WS_RESP_RECIEVER_CLASS")+" ]", e);
							return null;
						} catch (IllegalAccessException e) {
							logger.fatal("Illegal Access Exception for Config [WS_RESP_RECIEVER_CLASS] Val [ "+config.getValue("WS_RESP_RECIEVER_CLASS")+" ]", e);
							return null;
						} catch (ClassNotFoundException e) {
							logger.fatal("CLASS NOT FOUND for Config [WS_RESP_RECIEVER_CLASS] Val [ "+config.getValue("WS_RESP_RECIEVER_CLASS")+" ]", e);
							return null;
						}
					}
				}
				
				axisConfig.initialize(false);
			}
		}
		if ((clientConfig != null) && 
				(axisClient != null)){
			try {
				objLocator = locatorInstanse.getConstructor(org.apache.axis.EngineConfiguration.class).newInstance(clientConfig);
				locatorInstanse.getSuperclass().getDeclaredMethod("setEngineConfiguration",org.apache.axis.EngineConfiguration.class).invoke(objLocator, clientConfig);			
				locatorInstanse.getSuperclass().getDeclaredMethod("setEngine",org.apache.axis.AxisEngine.class).invoke(objLocator, axisClient);
			} catch (IllegalArgumentException e) {
				logger.fatal("Illegal Argument Exception For AxisClient Object", e);
				return null;
			} catch (SecurityException e) {
				logger.fatal("Security Exception For AxisClient Object", e);
				return null;
			} catch (InstantiationException e) {
				logger.fatal("Instantiation Exception For AxisClient Object", e);
				return null;
			} catch (IllegalAccessException e) {
				logger.fatal("Illegal Access Exception For AxisClient Object", e);
				return null;
			} catch (InvocationTargetException e) {
				logger.fatal("Invocation Target Exception For AxisClient Object", e);
				return null;
			} catch (NoSuchMethodException e) {
				logger.fatal("No Such Method Exception For AxisClient Object", e);
				return null;
			}
		
		}else if (axisConfig != null){
			try {
				objLocator = locatorInstanse.getConstructor(org.apache.axis.EngineConfiguration.class).newInstance(axisConfig);
			} catch (IllegalArgumentException e) {
				logger.fatal("Illegal Argument Exception For AxisConfig Object", e);
				return null;
			} catch (SecurityException e) {
				logger.fatal("Security Exception For AxisConfig Object", e);
				return null;
			} catch (InstantiationException e) {
				logger.fatal("Instantiation Exception For AxisConfig Object", e);
				return null;
			} catch (IllegalAccessException e) {
				logger.fatal("Illegal Access Exception For AxisConfig Object", e);
				return null;
			} catch (InvocationTargetException e) {
				logger.fatal("Invocation Target Exception For AxisConfig Object", e);
				return null;
			} catch (NoSuchMethodException e) {
				logger.fatal("No Such Method Exception For AxisConfig Object", e);
				return null;
			}
		}else{
			try {
				objLocator = locatorInstanse.newInstance();
			} catch (InstantiationException e) {
				logger.fatal("Instantiation Exception For Creation Of Locator", e);
				return null;
			} catch (IllegalAccessException e) {
				logger.fatal("Illegal Exception For Creation Of Locator", e);
				return null;
			}
		}
		try {
			objStub = locatorInstanse.getDeclaredMethod("get".concat(config.getValue("SERVICE_PORT_NAME")), java.net.URL.class).invoke(objLocator, new java.net.URL(config.getValue("END_POINT_URL")));
		} catch (IllegalArgumentException e) {
			logger.fatal("Illegal Argument Exception For Creation Of Locator", e);
			return null;
		} catch (SecurityException e) {
			logger.fatal("Security Exception For Creation Of Locator", e);
			return null;
		} catch (MalformedURLException e) {
			logger.fatal("MalformedURLException For Creation Of Locator", e);
			return null;
		} catch (IllegalAccessException e) {
			logger.fatal("Illegal Access Exception For AxisConfig Object", e);
			return null;
		} catch (InvocationTargetException e) {
			logger.fatal("Invocation Target Exception For AxisConfig Object", e);
			return null;
		} catch (NoSuchMethodException e) {
			logger.fatal("NoSuchMethodException For AxisConfig Object", e);
			return null;
		}
		return objStub;
	}
	
	/**
	 * Function to populate the TrustStore
	 * 
	 * @param axisConfig
	 * @param config
	 * @param logger
	 * @return
	 */
	private ArcotAxisEngineConfig createTrustStore(ArcotAxisEngineConfig axisConfig, COConfig config, COLogger logger){
		axisConfig.setTruststorePassword(config.getValue("TRUSTSTORE_PASSPHRASE"));
		axisConfig.setTruststoreType((config.getValue("TRUSTSTORE_TYPE")==null) ? "JKS" : config.getValue("TRUSTSTORE_TYPE"));
		axisConfig.setTruststore(config.getValue("TRUSTSTORE"));
		return axisConfig;
	}
	
	/**
	 * Select a different TrustStore provider
	 * 
	 * @refer ArcotAxisEngineConfig.setTrustStoreProvider(String)
	 * 
	 * @param axisConfig
	 * @param config
	 * @param logger
	 * @return
	 */
	private ArcotAxisEngineConfig selectTrustStoreProviderName(ArcotAxisEngineConfig axisConfig, COConfig config, COLogger logger){
		axisConfig.setTrustStoreProvider(config.getValue("TRUSTSTORE_PROVIDER_NAME"));
		return axisConfig;
	}

	/**
	 * Provide the fully qualified class name of the provider
	 * 
	 * @refer ArcotAxisEngineConfig.setObjTrustStoreProvider(Object of Provider)
	 * 
	 * @param axisConfig
	 * @param config
	 * @param logger
	 * @return
	 */
	private ArcotAxisEngineConfig selectTrustStoreProvider(ArcotAxisEngineConfig axisConfig, COConfig config, COLogger logger) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		axisConfig.setObjTrustStoreProvider((ClassUtils.forName(config.getValue("TRUSTSTORE_PROVIDER"))).newInstance());
		return axisConfig;
	}	

	/**
	 * Function to populate the KeyStore
	 * 
	 * @param axisConfig
	 * @param config
	 * @param logger
	 * @return
	 */
	private ArcotAxisEngineConfig createKeyStore(ArcotAxisEngineConfig axisConfig, COConfig config, COLogger logger){
		axisConfig.setKeystorePassword(config.getValue("KEYSTORE_PASSPHRASE"));
		axisConfig.setKeyPassPhrase(config.getValue("KEY_PASSPHRASE"));
		axisConfig.setKeystoreType((config.getValue("KEYSTORE_TYPE")==null) ? "JKS" : config.getValue("KEYSTORE_TYPE"));
		axisConfig.setKeystore(config.getValue("KEYSTORE"));
		return axisConfig;
	}
	
	
	/**
	 * Select a different KeyStore provider
	 * 
	 * @refer ArcotAxisEngineConfig.setTrustStoreProvider(String)
	 * 
	 * @param axisConfig
	 * @param config
	 * @param logger
	 * @return
	 */
	private ArcotAxisEngineConfig selectKeyStoreProviderName(ArcotAxisEngineConfig axisConfig, COConfig config, COLogger logger){
		axisConfig.setKeyStoreProvider(config.getValue("KEYSTORE_PROVIDER_NAME"));
		return axisConfig;
	}

	/**
	 * Provide the fully qualified class name of the provider
	 * 
	 * @refer ArcotAxisEngineConfig.setObjKeyStoreProvider(Object of Provider)
	 * 
	 * @param axisConfig
	 * @param config
	 * @param logger
	 * @return
	 */
	private ArcotAxisEngineConfig selectKeyStoreProvider(ArcotAxisEngineConfig axisConfig, COConfig config, COLogger logger) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		axisConfig.setObjKeyStoreProvider((ClassUtils.forName(config.getValue("KEYSTORE_PROVIDER"))).newInstance());
		return axisConfig;
	}
	
	private Hashtable<String, String> getHtServiceOptions() {
		return htServiceOptions;
	}

	private Hashtable<String, String> getRespHtServiceOptions() {
		return htResServiceOptions;
	}

	/**
	 * To be populated by the invoking BLH, in case service options needs to be passed in the REQUEST handler 
	 * @param htServiceOptions
	 */
	public void setHtServiceOptions(Hashtable<String, String> htServiceOptions) {
		this.htServiceOptions = htServiceOptions;
	}	

	/**
	 * To be populated by the invoking BLH, in case service options needs to be passed in the RESPONSE handler 
	 * @param htResServiceOptions
	 */
	public void setResHtServiceOptions(Hashtable<String, String> htResServiceOptions) {
		this.htResServiceOptions = htResServiceOptions;
	}	
	
	
}
