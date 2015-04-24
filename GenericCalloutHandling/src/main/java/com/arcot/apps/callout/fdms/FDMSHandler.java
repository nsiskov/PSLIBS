package com.arcot.apps.callout.fdms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

//import lp.txn.JLinkPointTransaction;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.https.ConnectionManager;
import com.arcot.apps.https.ConnectionProperties;
import com.arcot.apps.https.HttpsHandler;

public class FDMSHandler {
	
	public static final String FDMS_URL = "FDMSUrl";
	
	public static final String CONFIG_FDMS_ONLY = "FDMSOnly";
	public static final String CONFIG_FDMS_CVV2 = "FDMSCVV2Only";
	public static final String CONFIG_FDMS = "FDMS";
	
	//keys for configuring CVV and ZIP match values
	public static final String FDMS_CVV_MATCH_VAL = "FDMSCVVMatchVal";
	public static final String FDMS_ZIP_MATCH_VAL = "FDMSZIPMatchVal";
	
	//Default CVV and ZIP match values
	public static final String DEFAULT_CVV_MATCH = "M";
	public static final String DEFAULT_ZIP_MATCH = "Y";

	public static FDMSResponse doFDMS(CORequest request, COConfig config, String cvv, String expMonth, String expYear, String zip, String address){
		COLogger logger = config.getLogger();
		FDMSResponse response = new FDMSResponse();
		StringBuffer post = null;
		try {
			logger.log(request, "Requesting FDMS");
			post = new StringBuffer("cardnumber="); 
			post.append(URLEncoder.encode(request.getCardNumber(), "UTF-8"));
			post.append("&cardexpmonth="); 
			post.append(URLEncoder.encode(expMonth, "UTF-8"));
			post.append("&cardexpyear="); 
			post.append(URLEncoder.encode(expYear.substring(2), "UTF-8"));

			// send street addr number to servlet only if client provided
			if(address != null){
				// squeeze address {
				StringBuffer avs = new StringBuffer();
				if(address != null) {
					int len = address.length();
					char ch;
					for (int i = 0; i < len; i++) {
						ch = address.charAt(i);
						if (!Character.isWhitespace(ch))
							avs.append(Character.toUpperCase(ch));
					}
					len = avs.length();
					int overflow = avs.length() - 19;
					if (overflow > 0) avs.delete(len - overflow, len);
				}
				// } squeeze address
				post.append("&addrnum=");
				post.append(URLEncoder.encode(avs.toString(), "UTF-8"));
			}
			
			if(zip != null){
				post.append("&zip=");
				post.append(URLEncoder.encode(zip, "UTF-8"));
			}
			
			if(cvv != null){
				post.append("&cvmvalue=");
				post.append(URLEncoder.encode(cvv, "UTF-8"));
			}

		} catch (UnsupportedEncodingException e) {
			// This will never happen, ignore
			logger.log(request, "Wrong Encoding!!!", e);
			response.setArcotError("Error creating request");
			return response;
		}
		
		//Connect toFDMS jsp and get the result
		ConnectionProperties cp = new ConnectionProperties();
		cp.setUrl(config.getValue(FDMS_URL));
		cp.setRequestMethod("POST");
		
		String httpResponse = null;
		try {
			HttpsHandler handler = ConnectionManager.getInstance().getHTTPSHandler(cp);
			httpResponse = handler.processRequest(post.toString());
			response.setResponse(logger, request, httpResponse);
			logger.log(request, response.toString());
		} catch (KeyManagementException e) {
			logger.log(request, e.getMessage(), e);
			response.setArcotError("BadCommKeyKME");
		} catch (UnrecoverableKeyException e) {
			logger.log(request, e.getMessage(), e);
			response.setArcotError("BadCommKeyUKE");
		} catch (NoSuchAlgorithmException e) {
			logger.log(request, e.getMessage(), e);
			response.setArcotError("BadCommKeyNSA");
		} catch (CertificateException e) {
			logger.log(request, e.getMessage(), e);
			response.setArcotError("BadCommCer");
		} catch (KeyStoreException e) {
			logger.log(request, e.getMessage(), e);
			response.setArcotError("BadCommKeyKSE");
		} catch (IOException e) {
			logger.log(request, e.getMessage(), e);
			response.setArcotError("HTTPError");
		}
		
		return response;
	}
	
	public static boolean matchFDMS(CORequest request, COConfig config, FDMSResponse fdmsResponse, StringBuffer logToDB){
		COLogger logger = config.getLogger();
		
		boolean $1status = false;
		boolean cvvStatus = false;
		boolean zipStatus = false;
		
		if(!FDMSResponse.FDMS_APPROVED.equalsIgnoreCase(fdmsResponse.getApproved())){
			//$1 not approved
			logger.log(request, "FDMS Approval failed with Approval code [" + fdmsResponse.getApproved() + "]");
			logToDB.append("$1Auth(F)-");
		}else{
			$1status = true;
			logToDB.append("$1Auth(P)-");
		}
		
		logToDB.append(fdmsResponse.getAvs());
		
		//Match CVV
		if(fdmsResponse.getCvvResponse() == null){
			logToDB.append("-cvv2(NF)-");
		}else{
			String cvvMatch = config.getValue(FDMS_CVV_MATCH_VAL);
			if(cvvMatch == null){
				//use default as per CNS IPGS DB
				cvvMatch = DEFAULT_CVV_MATCH;
			}
			if(cvvMatch.indexOf(fdmsResponse.getCvvResponse()) > -1){
				logToDB.append("-cvv2(P)-");
				cvvStatus = true;
			}else{
				logToDB.append("-cvv2(F)-");
			}
		}

		//Match ZIP
		if(fdmsResponse.getZipResponse() == null){
			logToDB.append("zip(NF)-");
		}else{
			String zipMatch = config.getValue(FDMS_ZIP_MATCH_VAL);
			if(zipMatch == null){
				//use default as per CNS IPGS DB
				zipMatch = DEFAULT_ZIP_MATCH;
			}
			if(zipMatch.indexOf(fdmsResponse.getZipResponse()) > -1){
				logToDB.append("zip(P)-");
				zipStatus = true;
			}else{
				logToDB.append("zip(F)-");
			}
		}
		
		logToDB.append("ON:" + fdmsResponse.getOrdernum());
		if(fdmsResponse.getError() != null){
			logToDB.append("-Err:" + fdmsResponse.getError());
		}
		
		if($1status && cvvStatus && zipStatus){
			logToDB.append("-Fsts(P)");
			return true;
		}else{
			logToDB.append("-Fsts(F)");
			return false;
		}
	}
	
	public static boolean matchFDMSCVV2Only(CORequest request, COConfig config, FDMSResponse fdmsResponse, StringBuffer logToDB){
		COLogger logger = config.getLogger();
		
		boolean $1status = false;
		boolean cvvStatus = false;
		
		if(!FDMSResponse.FDMS_APPROVED.equalsIgnoreCase(fdmsResponse.getApproved())){
			//$1 not approved
			logger.log(request, "FDMS Approval failed with Approval code [" + fdmsResponse.getApproved() + "]");
			logToDB.append("$1Auth(F)-");
		}else{
			$1status = true;
			logToDB.append("$1Auth(P)-");
		}
		
		logToDB.append(fdmsResponse.getAvs());
		
		//Match CVV
		if(fdmsResponse.getCvvResponse() == null){
			logToDB.append("-cvv2(NF)-");
		}else{
			String cvvMatch = config.getValue(FDMS_CVV_MATCH_VAL);
			if(cvvMatch == null){
				//use default as per CNS IPGS DB
				cvvMatch = DEFAULT_CVV_MATCH;
			}
			if(cvvMatch.indexOf(fdmsResponse.getCvvResponse()) > -1){
				logToDB.append("-cvv2(P)-");
				cvvStatus = true;
			}else{
				logToDB.append("-cvv2(F)-");
			}
		}
		
		logToDB.append("ON:" + fdmsResponse.getOrdernum());
		if(fdmsResponse.getError() != null){
			logToDB.append("-Err:" + fdmsResponse.getError());
		}
		
		if($1status && cvvStatus){
			logToDB.append("-Fsts(P)");
			return true;
		}else{
			logToDB.append("-Fsts(F)");
			return false;
		}
	}
	
	public static boolean matchFDMSOnly(CORequest request, COConfig config, FDMSResponse fdmsResponse, StringBuffer logToDB){
		COLogger logger = config.getLogger();
		
		boolean $1status = false;
		
		if(!FDMSResponse.FDMS_APPROVED.equalsIgnoreCase(fdmsResponse.getApproved())){
			//$1 not approved
			logger.log(request, "FDMS Approval failed with Approval code [" + fdmsResponse.getApproved() + "]");
			logToDB.append("$1Auth(F)-");
		}else{
			$1status = true;
			logToDB.append("$1Auth(P)-");
		}
		
		logToDB.append("ON:" + fdmsResponse.getOrdernum());
		if(fdmsResponse.getError() != null){
			logToDB.append("-Err:" + fdmsResponse.getError());
		}
		
		if($1status){
			logToDB.append("-Fsts(P)");
			return true;
		}else{
			logToDB.append("-Fsts(F)");
			return false;
		}
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		/*
		try {
			
			ConnectionProperties cp = new ConnectionProperties();
			cp.setUrl("https://staging.linkpt.net:1129");
			cp.setRequestMethod("POST");
			cp.setCertFilePath("D:/arcot/PS/FDMS/FDMSSiteRoot.cer");
			cp.setClientKeyCertPath("D:/arcot/PS/FDMS/fdms.jks");
			cp.setKeyFilePassPhrase("fdms1234");
			StringBuffer post = new StringBuffer("cardnumber="); 
			post.append(URLEncoder.encode("4275330012345675", "UTF-8"));
			post.append("&cardexpmonth="); 
			post.append(URLEncoder.encode("12", "UTF-8"));
			post.append("&cardexpyear="); 
			post.append(URLEncoder.encode("12", "UTF-8"));
			post.append("&zip=");
			post.append(URLEncoder.encode("121212", "UTF-8"));
			post.append("&cvmvalue=");
			post.append(URLEncoder.encode("123", "UTF-8"));
			HttpsHandler handler = ConnectionManager.getInstance().getHTTPSHandler(cp);
			String httpResponse = handler.processRequest(post.toString());
			System.out.println(httpResponse);
			
			
			
			//System.setProperty("javax.net.ssl.trustStore", "D:/arcot/PS/FDMS/fdms_converted.p12");
			//System.setProperty("javax.net.ssl.trustStorePassword", "fdms1234");
			
			
			JLinkPointTransaction txn = new JLinkPointTransaction();
			txn.setClientCertificatePath("D:/arcot/PS/FDMS/fdms_ram.p12");
			//txn.setPassword("fdms1234");
			txn.setPassword("fdms1234");
			txn.setHost("staging.linkpt.net");
			txn.setPort(1129);
			
			String sXml = "<order>" + "<merchantinfo>"
			+"<configfile>" + "1909429599" + "</configfile>"
			//+"<configfile>" + "1909034124" + "</configfile>"
			+"</merchantinfo>"
			+ "<orderoptions>"
			+ "<ordertype>PREAUTH</ordertype>"
			+ "<result>LIVE</result>"
			+ "</orderoptions>"
			+ "<payment>"
			+ "<chargetotal>1.00</chargetotal>"
			+ "</payment>"
			+ "<creditcard>"
			+ "<cardnumber>4111111111111111</cardnumber>"
			+ "<cardexpmonth>05</cardexpmonth>"
			+ "<cardexpyear>07</cardexpyear>"
			+ "<cvmvalue>123</cvmvalue>"
			+ "<cvmindicator>provided</cvmindicator>"
			+ "</creditcard>"
			+ "<billing>"
			+"<addrnum>12345</addrnum>" + "<zip>12345</zip>" + "</billing>" + "</order>";
			
			String resp = txn.send(sXml);
			System.out.println(resp);
		} catch(Exception e){
			e.printStackTrace();
		}
		*/
		
	}

}
