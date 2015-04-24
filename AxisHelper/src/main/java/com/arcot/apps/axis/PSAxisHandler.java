package com.arcot.apps.axis;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPConstants;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.https.ConnectionManager;
import com.arcot.apps.https.ConnectionProperties;
import com.arcot.apps.https.HttpsHandler;

/**
 * @author bossa02
 *
 * Handler to used for SOAP 1.1 compliant webservice. 
 * 
 * This class can be extended incase the Callout needs to modify the following:-
 * 1. SOAP request to be posted to the backend			- 	modifyRequest()
 * 2. SOAP response before sending it over to Axis		- 	modifyResponse()
 * 3. User defined HTTP headers are required			- 	modifyHeader()
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
 * The class which extends this class will be invoked through Callout Config, via  the following parameters:-
 * 
 * HANDLER_CLASS										[User defined ClassName that extends PSAxisHandler]
 * PIVOT												true {Indication that a handler is present}
 *  
 *  
 *  				-- Sample Code [ The following Code extends the PXASixHandler, and overrides the methods modifyRequest() and modifyResponse() ]:-
 *  <pre>
 *  
 *  package com.arcot.apps.xyz;
 *
 *	import com.arcot.apps.axis.PSAxisHandler;
 *
 *	public class DummyHandler extends PSAxisHandler {
 *		public String modifyRequest(String requestString){
 *			System.out.println("DummyHandler - Request - "+requestString);
 *			return requestString;
 *		}
 *
 *		public String modifyResponse(String responseString){
 *			System.out.println("DummyHandler - Response - "+responseString);
 *			return responseString;
 *		}
 *	}
 *  
 *  </pre>
 *  
 */

public class PSAxisHandler extends BasicHandler {

	private static final long serialVersionUID = 1L;

	public void invoke(MessageContext msgContext) throws AxisFault {
		
		CORequest request = (CORequest)options.get("CORequest");
		COConfig config = (COConfig)options.get("COConfig");
		COLogger logger = (COLogger)options.get("COLogger");
		
		Message reqMsg = msgContext.getRequestMessage();
	    String reqStr = reqMsg.getSOAPPartAsString();
	    // replace all new line 
	    reqStr = reqStr.replaceAll("\n","");
	    // replace all spaces between > & <
	    reqStr = reqStr.replaceAll(">[\\s]*<", "><");
	    reqStr = modifyRequest(reqStr);

		ConnectionProperties cp = new ConnectionProperties();
		cp.setUrl(config.getValue("END_POINT_URL"));
		
		if (config.getValue("TRUSTSTORE_PASSPHRASE") != null)
			cp.setTrustFilePassPhrase(config.getValue("TRUSTSTORE_PASSPHRASE"));
		
		if (config.getValue("TRUSTSTORE") != null)
			cp.setCertFilePath(config.getValue("TRUSTSTORE"));
		
		if (config.getValue("KEYSTORE") != null)
			cp.setClientKeyCertPath(config.getValue("KEYSTORE"));
		
		if (config.getValue("KEYSTORE_PASSPHRASE") != null)
			cp.setKeyFilePassPhrase(config.getValue("KEYSTORE_PASSPHRASE"));
		
		if (config.getValue("DISABLE_SERVER_CERT_CHECK") !=null)
			cp.setDisableServerCertCheck(config.getValue("DISABLE_SERVER_CERT_CHECK"));
		
		HttpsHandler handler = null;
		String strRsp = null;
		try {
			handler = ConnectionManager.getInstance().getHTTPSHandler(cp, modifyHeader(msgContext, reqStr.length()));
	    	if (config.getValue("RESPONSE_TIME_OUT") != null)
	    		strRsp = handler.processRequest(reqStr, config.getIntValue("RESPONSE_TIME_OUT"));
	    	else
	    		strRsp = handler.processRequest(reqStr);
		} catch (KeyManagementException e) {
			logger.fatal(request, "PSAxisHandler:invoke() ", e);
		} catch (UnrecoverableKeyException e) {
			logger.fatal(request, "PSAxisHandler:invoke() ", e);
		} catch (NoSuchAlgorithmException e) {
			logger.fatal(request, "PSAxisHandler:invoke() ", e);
		} catch (CertificateException e) {
			logger.fatal(request, "PSAxisHandler:invoke() ", e);
		} catch (KeyStoreException e) {
			logger.fatal(request, "PSAxisHandler:invoke() ", e);
		} catch (IOException e) {
			logger.fatal(request, "PSAxisHandler:invoke() ", e);
		}

		if (strRsp != null)
			strRsp = modifyResponse(strRsp);

		Message message = new Message(strRsp);
        message.setMessageType("response");
        msgContext.setResponseMessage(message);
	}
	
	/**
	 * Override this method, only if a different header needs to be set
	 * 
	 * @param msgContext
	 * @param requestLength
	 * @return
	 */
	public HashMap<String, String> modifyHeader(MessageContext msgContext, int requestLength){
	    HashMap<String, String> reqProps = new HashMap<String, String>();
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
	
	/**
	 * Method to be overridden in case the SOAP request needs to be changed before being sent to the Backend
	 * 
	 * @param requestString
	 * @return
	 */
	public String modifyRequest(String requestString){
		return requestString;
	}

	/**
	 * Method to be overridden in case the SOAP response needs to be changed before being fed to Axis Libraries
	 * 
	 * @param responseString
	 * @return
	 */
	public String modifyResponse(String responseString){
		return responseString;
	}
	

}
