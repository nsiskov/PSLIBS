package com.arcot.apps.callout.sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.StringTokenizer;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.common.COResponse;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.https.ConnectionManager;
import com.arcot.apps.https.ConnectionProperties;
import com.arcot.apps.https.HttpsHandler;

public class SMSSender {

	public static final String A2W_URL = "URL";
	public static final String A2W_PCODE = "pcode";
	public static final String A2W_ACODE = "aid";
	public static final String A2W_PIN = "pin";
	public static final String A2W_MOBILE = "mnumber";
	public static final String A2W_TEXT = "message";
	public static final String A2W_LANG = "lang";
	public static final String A2W_ORDERID = "odreqid";
	public static final String A2W_ROOT_CERT = "rootcert";
	public static final String A2W_DEBUG = "a2wdebug";
	
	/*
	 * Base the check of returned COResponse on getResult/isResult
	 * */
	public static COResponse sendAir2Web(COConfig config, CORequest request, String mobileNumber, String text, String txid){
		COLogger logger = config.getLogger();
		boolean debug = false;
		String httpRes = null;
		if("Y".equalsIgnoreCase(config.getValue(A2W_DEBUG)) || "true".equalsIgnoreCase(config.getValue(A2W_DEBUG))){
			debug = true;
		}
		
		if(debug){
			logger.log(request, "Send sms request recived");
		}
		
		StringBuffer getRequest = new StringBuffer("aid=");
		getRequest.append(config.getValue(A2W_ACODE));
		getRequest.append("&pin=");
		getRequest.append(config.getValue(A2W_PIN));
		getRequest.append("&mnumber=");
		getRequest.append(mobileNumber);
		getRequest.append("&message=");
		try {
			getRequest.append(URLEncoder.encode(text, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ConnectionProperties cp = new ConnectionProperties();
		cp.setUrl(config.getValue(A2W_URL));
		cp.setCertFilePath(config.getValue(A2W_ROOT_CERT));
		cp.setQuery(getRequest.toString());
		logger.log(request, cp.getUrl() + "?" + cp.getQuery());
		
		try {
			HttpsHandler handler = ConnectionManager.getInstance().getHTTPSHandler(cp);
			httpRes = handler.processRequest("");
		} catch (KeyManagementException e) {
			logger.log(request, "KME", e);
			return COUtil.createCOResponse(false, 0, "InvSSL", e.getMessage(), ACS.ACS_CALLOUT_UNSURE);
		} catch (UnrecoverableKeyException e) {
			logger.log(request, "KME", e);
			return COUtil.createCOResponse(false, 0, "InvSSL", e.getMessage(), ACS.ACS_CALLOUT_UNSURE);
		} catch (NoSuchAlgorithmException e) {
			logger.log(request, "KME", e);
			return COUtil.createCOResponse(false, 0, "InvSSL", e.getMessage(), ACS.ACS_CALLOUT_UNSURE);
		} catch (CertificateException e) {
			logger.log(request, "KME", e);
			return COUtil.createCOResponse(false, 0, "InvSSL", e.getMessage(), ACS.ACS_CALLOUT_UNSURE);
		} catch (KeyStoreException e) {
			logger.log(request, "KME", e);
			return COUtil.createCOResponse(false, 0, "InvSSL", e.getMessage(), ACS.ACS_CALLOUT_UNSURE);
		} catch (IOException e) {
			logger.log(request, "KME", e);
			return COUtil.createCOResponse(false, 0, "InvSSL", e.getMessage(), ACS.ACS_CALLOUT_UNSURE);
		}
		
		if(debug){
			logger.log(request, "HTTPResponse is [" + httpRes + "]");
		}
		
		if(httpRes == null){
			return COUtil.createCOResponse(false, 0, "NullA2WRes", "NullA2WRes", ACS.ACS_CALLOUT_FAIL);
		}
		
		//Parse A2W response
		String id = null;
		String restOfIt = null;
		String apiCode = null;
		String description = null;
		String timestamp = null;
		
		int tildaIndex = httpRes.indexOf("~");
		if(tildaIndex > -1){
			id = httpRes.substring(0, tildaIndex);
			restOfIt = httpRes.substring(tildaIndex + 1);
		}else{
			restOfIt = httpRes;
		}
		
		StringTokenizer stkAnd = new StringTokenizer(restOfIt, "&");
		StringTokenizer stkEquals = null;
		while(stkAnd.hasMoreTokens()){
			String key = null;
			String value = null;
			stkEquals = new StringTokenizer(stkAnd.nextToken(), "=");
			if(stkEquals.countTokens() > 0){
				key = stkEquals.nextToken();
				if(stkEquals.countTokens() > 0){
					value = stkEquals.nextToken();
				}
				if("code".equalsIgnoreCase(key)){
					apiCode = value;
				}else if("info".equalsIgnoreCase(key)){
					description = value;
				}else if("Time".equalsIgnoreCase(key)){
					timestamp = value;
				}else{
					continue;
				}
			}
		}
		
		if(apiCode == null || "".equals(apiCode.trim())){
			return COUtil.createCOResponse(false, 0, "InvA2WCode", "InvA2WCode", ACS.ACS_CALLOUT_FAIL);
		}
		
		apiCode = apiCode.trim();
		
		if("API000".equalsIgnoreCase(apiCode) || "API000".equalsIgnoreCase(apiCode)){
			return COUtil.createCOResponse(true, 0, apiCode, description, ACS.ACS_CALLOUT_SUCCESS);
		}
		
		return COUtil.createCOResponse(false, 0, apiCode, description, ACS.ACS_CALLOUT_FAIL);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
