package com.arcot.apps.axis;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPConstants;

import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.https.ConnectionManager;
import com.arcot.apps.https.ConnectionProperties;
import com.arcot.apps.https.HttpsHandler;
import com.arcot.apps.jni.Connection;

/**
 * 
 * @author bossa02
 * Handler to used for SOAP 1.1 compliant webservice
 * 
 * Used mainly for MasterCard projects
 * 
 * This class can be extended incase the Callout needs to modify the following:-
 * 1. SOAP request to be posted to the backend			- 	modifyRequest()
 * 2. SOAP response before sending it over to Axis		- 	modifyResponse()
 * 3. User defined HTTP headers are required			- 	modifyHeader()
 * 
 * 
 * IF INVOCATION IS HAPENNING THROUGH JAVA, CALLOUT CONFIGURATION SHOULD contain
 * [REQUIRED]invokeThroughJava			-		Set it to true
 * [REQUIRED]EndPointUrl
 * [REQUIRED]TrustStorePath				-		Can be a certificate or a JKS/P12 bundle
 * [OPTIONAL]TrustStorePassword			- 		Required ONLY if TrustStorePath is JKS or P12 Bundle
 * [OPTIONAL]KeyStorePath				-		Required if its 2 way SSL.
 * [OPTIONAL]KeyStorePassword			-		Required if its 2 way SSL.
 * [OPTIONAL]responseTimeOut			-		If you want to setup a user defined response Time out {in milliseconds}.
 * 
 * 
 * IF INVOCATION IS HAPENNING THROUGH JNI, CALLOUT CONFIGURATION SHOULD contain
 * [REQUIRED]EndPointUrl
 * [REQUIRED]SERVER_CERT_FILE_PATH		-		Server Root Certificate
 * [OPTIONAL]ISSUER_CERT_FILE_PATH		-		Required if its 2 way SSL.
 * [OPTIONAL]ISSUER_KEY_FILE_PATH		-		Required if its 2 way SSL; and private key is in the filesystem.
 * [REQUIRED]JNI_ConnectionTimeout		-		Connection timeout in milliseconds
 * [REQUIRED]JNI_ResponseTimeout		-		Response timeout in milliseconds			
 * 
 * 
 * 
 * "debug" flag can be passed from the callout configuration if request/ response needs to be printed in the callout logs.
 */
public class PSAxisSender extends BasicHandler{

	public void invoke(MessageContext msgContext) throws AxisFault {
		
		COConfig config = (COConfig)options.get("COConfig");
	    COLogger logger = null;
	    if(config != null){
        	logger = config.getLogger();
        }
	    
	    Message reqMsg = msgContext.getRequestMessage();
	    String reqStr = reqMsg.getSOAPPartAsString();
	    // replace all new line 
	    reqStr = reqStr.replaceAll("\n","");
	    // replace all spaces between > & <
	    reqStr = reqStr.replaceAll(">[\\s]*<", "><");
	    reqStr = modifyRequest(reqStr);
	    
	    if (config.getValue("debug") != null){
	    	logger.log("REQUEST Posted - "+reqStr);
	    }else{
	        logger.log("RequestLength - "+reqStr.length());
	    }
	    
	    String response = null;
	    String backendUrl = null;
		try {
			backendUrl = URLDecoder.decode(config.getValue("EndPointUrl"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			backendUrl = config.getValue("EndPointUrl");
			logger.logException(e, "UnsupportedEncodingException");
		}
		if (config.getValue("invokeThroughJava") !=null ){
			response = invokeThrougJava(reqStr, backendUrl, modifyHeader(msgContext, reqStr.length()), config);
		}else{
			response = invokeThroughJNI(reqStr, backendUrl, modifyHeader(msgContext, reqStr.length()), config);
		}
		
		if (response != null){
			response = modifyResponse(response);
		    
			if (config.getValue("debug") != null){
		    	logger.log("RESPONSE Rcd - "+response);
		    }else{
		    	logger.log("ResponseLength - "+response.length());
		    }

		}
		
		Message message = new Message(response);
        message.setMessageType("response");
        msgContext.setResponseMessage(message);
	}
	
	public HashMap modifyHeader(MessageContext msgContext, int requestLength){
	    HashMap reqProps = new HashMap();
	    reqProps.put(HTTPConstants.HEADER_ACCEPT, HTTPConstants.HEADER_ACCEPT_APPL_SOAP + ", " 
	    		+ HTTPConstants.HEADER_ACCEPT_APPLICATION_DIME + ", " + HTTPConstants.HEADER_ACCEPT_MULTIPART_RELATED 
	    		+ ", " + HTTPConstants.HEADER_ACCEPT_TEXT_ALL);
	    reqProps.put(HTTPConstants.HEADER_USER_AGENT, "Axis/1.4");
	    reqProps.put(HTTPConstants.HEADER_CACHE_CONTROL, HTTPConstants.HEADER_CACHE_CONTROL_NOCACHE	);
	    reqProps.put(HTTPConstants.HEADER_PRAGMA, HTTPConstants.HEADER_CACHE_CONTROL_NOCACHE);
	    reqProps.put(HTTPConstants.HEADER_CONTENT_TYPE.toLowerCase(), "application/soap+xml; charset=utf-8");
	    reqProps.put(HTTPConstants.HEADER_CONTENT_LENGTH, (new Integer(requestLength)).toString());
	    reqProps.put(HTTPConstants.HEADER_SOAP_ACTION, msgContext.getSOAPActionURI());
	    return reqProps;
	}
	
	public String modifyRequest(String requestString){
		return requestString;
	}

	public String modifyResponse(String responseString){
		return responseString;
	}
	
	/**
	 * Invocation through JNI 
	 * FLAVOURS which can be used
	 * 1. 1 way SSL, only SERVER_CERT_FILE_PATH is required
	 * 2. 2 way SSL with the private key in system
	 * 3. 2 way SSL with the private key in H/W
	 * @param requestString
	 * @param backendUrl
	 * @param mapHeaders
	 * @param config
	 * @return
	 */
	public String invokeThroughJNI(String requestString, String backendUrl, HashMap mapHeaders, COConfig config){
		
		String strRsp = null;

		Set set = mapHeaders.entrySet(); 
		Iterator i = set.iterator();
		StringBuffer httpHdrsToBePassed = new StringBuffer(); 
		//System.out.println("\n");
		while(i.hasNext()) { 
			Entry headerPair = (Entry)i.next(); 
			String headerName = "";
			String headerValue = "";
			headerName = (String)headerPair.getKey();
			headerValue = (String)headerPair.getValue();
			if (!headerName.equals("")){
				httpHdrsToBePassed.append(headerName+"="+headerValue+"~");
			}
		}

		
		
		if (config.getValue("ISSUER_CERT_FILE_PATH") == null){
			/**
			 * 1 way SSL
			 */
			strRsp = Connection.TwoWaySSLConnectionHttpHeaders(requestString, config.getValue("SERVER_CERT_FILE_PATH"), 
					"", "", backendUrl, config.getIntValue("JNI_ConnectionTimeout"), config.getIntValue("JNI_ResponseTimeout"), httpHdrsToBePassed.toString().substring(0, httpHdrsToBePassed.toString().length()-1));
		
		}else if (config.getValue("ISSUER_KEY_FILE_PATH") != null){
			/**
			 * 2 way SSL, with Key present in S/W
			 */
			strRsp = Connection.TwoWaySSLConnectionHttpHeaders(requestString, config.getValue("SERVER_CERT_FILE_PATH"), 
				config.getValue("ISSUER_CERT_FILE_PATH"), config.getValue("ISSUER_KEY_FILE_PATH"), backendUrl, config.getIntValue("JNI_ConnectionTimeout"), config.getIntValue("JNI_ResponseTimeout"), httpHdrsToBePassed.toString().substring(0, httpHdrsToBePassed.toString().length()-1));
		
		}else{
			/**
			 * 2 way SSL, with Key present in H/W
			 */
			strRsp = Connection.TwoWaySSLConnectionHttpHeaders(requestString, config.getValue("SERVER_CERT_FILE_PATH"), 
				config.getValue("ISSUER_CERT_FILE_PATH"), "", backendUrl, config.getIntValue("JNI_ConnectionTimeout"), config.getIntValue("JNI_ResponseTimeout"), httpHdrsToBePassed.toString().substring(0, httpHdrsToBePassed.toString().length()-1));
		}
		
		return strRsp;
	}
	
	/**
	 * Invocation through Java
	 * FLAVOURS which can be used
	 * 1. 1 way SSL ; with or without truststore containning JKS/P12 bundle or certificate
	 * 2. 2 way SSL ; with or without truststore containning JKS/P12 bundle or certificate
	 * @param requestString
	 * @param backendUrl
	 * @param mapHeaders
	 * @param config
	 * @return
	 */
	public String invokeThrougJava(String requestString, String backendUrl, 
			HashMap mapHeaders, COConfig config){
	    
		String strRsp = null;
		COLogger logger = config.getLogger();
		ConnectionProperties cp = new ConnectionProperties();
	    cp.setUrl(backendUrl);
	    
	    
	    cp.setCertFilePath(config.getValue("TrustStorePath"));
	    logger.log("TrustStorePath - "+ config.getValue("TrustStorePath"));
	    if (config.getValue("TrustStorePassword") != null){
	    	// this would mean that the TrustStore is a JKS bundle or a P12 Bundle 
	    	logger.log("TrustStorePassword - "+ config.getValue("TrustStorePassword"));
	    	cp.setTrustFilePassPhrase(config.getValue("TrustStorePassword"));
	    }

	    if (config.getValue("KeyStorePath") != null){
	    	cp.setClientKeyCertPath(config.getValue("KeyStorePath"));
	    	cp.setKeyFilePassPhrase(config.getValue("KeyStorePassword"));
	    }

	    try{
	    	HttpsHandler handler = ConnectionManager.getInstance().getHTTPSHandler(cp,mapHeaders);
	    	if (config.getValue("responseTimeOut") != null){
	    		strRsp = handler.processRequest(requestString, config.getIntValue("responseTimeOut"));
	    	}else{
	    		strRsp = handler.processRequest(requestString);
	    	}
		} catch (KeyManagementException e) {
			logger.logException(e, "PSAxisSender::KeyManagementException");
		} catch (NoSuchAlgorithmException e) {
			logger.logException(e, "PSAxisSender::NoSuchAlgorithmException");
		} catch (CertificateException e) {
			logger.logException(e, "PSAxisSender::CertificateException");
		} catch (KeyStoreException e) {
			logger.logException(e, "PSAxisSender::KeyStoreException");
		} catch (UnrecoverableKeyException e) {
			logger.logException(e, "PSAxisSender::UnrecoverableKeyException");
		} catch (IOException e) {
			logger.logException(e, "PSAxisSender::IOException");
		} catch (Exception e){
			logger.logException(e, "PSAxisSender::Exception");
		}
	    
	    return strRsp;
		
	}
	
}
