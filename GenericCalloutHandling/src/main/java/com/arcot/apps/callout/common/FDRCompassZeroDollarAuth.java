package com.arcot.apps.callout.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.callout.CallOutsConfig;
import com.arcot.dboperations.admin.AdminSelectOperations;
import com.arcot.util.ArcotException;
import com.arcot.util.BankInfo;
import com.arcot.vpas.enroll.cache.ESCache;


public class FDRCompassZeroDollarAuth {
	private static final String FDRCompassURL = "FDRCompassURL";
	private static final String FDRCompassPort = "FDRCompassPort";
	private static final String Timeout = "Timeout";
	private static final String NumTriesAllowed = "NumTriesAllowed";
	private static final String FDRCompassCalloutConfigId = "FDRCompassCalloutConfigId";
	private static final String ArcotMerchantId = "ArcotMerchantId";
	private static COConfig FDRCompassConfig = null;
	private static String defaultMerchantId = null;
	private static final String DEFAULT_CVV_MATCH = "M";
	private static final String FDMS_CVV_MATCH_VAL = "FDMSCVVMatchVal";
	private static final String Zero_Dollar_Zip_MATCH_VAL = "ZERODOLLARZIPMatchVal";
	private static final String Zero_Dollar_Response_Code_Match_VAL = "ZERODOLLARResponseCodeMatchVal";
	private static final String Zero_Dollar_DEFAULT_Zip_MATCH = "I3";
	private static final String Zero_Dollar_DEFAULT_Response_Code_MATCH = "100";
	private static final String FDRCompassBackupURL = "FDRCompassBackupURL";
	private static final String FDRCompassBackupPort = "FDRCompassBackupPort";
	private static final String DollarAmount = "DollarAmount";
	private static final String Authorize = "Authorize";

	private static String padSpaceRight(String value, int size){
		char pad = ' ';
		int length = value.length();
		int right = size - length;
		StringBuffer buf = new StringBuffer();
		buf.append(value);
		for (int index = 0; index < right; index++)
		  buf.append(pad);

		return buf.toString();
	}

	private static String padZerosLeft(String value, int size){
		char pad = '0';
		int length = value.length();
		int left = size - length;
		StringBuffer buf = new StringBuffer();
		for (int index = 0; index < left; index++)
		  buf.append(pad);
		buf.append(value);

		return buf.toString();
	}
	/*
	 * We need to make the Merchant Order Number Unique so using the
	 * Last 4 digits for the Card number along with the timeStamp
	 */

	private static String createTxId(String cardNumber){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
		StringBuffer txId = new StringBuffer();
		txId.append(cardNumber.substring(12));
		txId.append(dateFormat.format(cal.getTime()).toString());
		for(int i=txId.length(); i<22; i++){
			txId.append(" ");
		}
		return txId.toString();
	}

	/*
	 * InitializeZeroDollarCalloutConfig:
	 * Input : ACSRequest, COCOnfig
	 * Purpose: Get the callout configuration corresponding to Zero Dollar Auth
	 * It takes the callout configuration ID from ESConfig and get the callout configuration
	 * from ES Cache. The callout configuration contains the Mapping for the CustomerID and
	 * Merchant ID.
	 */
	private static synchronized void initializeZeroDollarCalloutConfig(ACSRequest request, COConfig config){
		COLogger logger = config.getLogger();
		String masterKey = ESCache.bic.getBank(0).BANKKEY;
		Vector calloutConfigs = new Vector();
		CallOutsConfig calloutsConfig = null;
		int fdrCompassCalloutConfigurationId = -1;
		//Gets the FDRCompassCallout Configuration ID from esconfig table which is cached.
		try{
			String fdrCompassCalloutConfiguration = ESCache.esc.getValue(FDRCompassCalloutConfigId);
			if(fdrCompassCalloutConfiguration == null){
				logger.log(request, "Unable to retrieve the Callouts configuration for FDRCompass");
				return;
			}
			fdrCompassCalloutConfigurationId = Integer.parseInt(fdrCompassCalloutConfiguration);
		}catch(NumberFormatException e){
			logger.log(request, "Unable to parse the configuration ID");
			return;
		}
		//Gets all the callout configurations
		try {
			AdminSelectOperations.getAllCallOutConfig(calloutConfigs, masterKey);
		} catch (ArcotException e) {
			// TODO Auto-generated catch block
			logger.log(request, "Arcot Exception when trying to get all the callouts configuration from DB");
		}
		//Parses through the configuration list to get the callout configuration for the ID
		int calloutConfigurationsSize = calloutConfigs.size();
		for(int i=0; i< calloutConfigurationsSize; i++){
			calloutsConfig = (CallOutsConfig)calloutConfigs.get(i);
			if(calloutsConfig.getConfID() == fdrCompassCalloutConfigurationId){
				FDRCompassConfig = new COConfig(calloutsConfig);
				//Once we get the FDRCompassConfig find the Merchant ID for Arcot and use that as the default
				//Merchant Id where ever the customer id, merchant id mapping is not Found
				if(FDRCompassConfig != null){
					defaultMerchantId = FDRCompassConfig.getValue(ArcotMerchantId);
				}
			}
		}
	}

	public static String doZeroDollarAuth(ACSRequest request, COConfig config, String cvv, String expMonth, String expYear, String zip){
		COLogger logger = config.getLogger();

		if(FDRCompassConfig == null){
			initializeZeroDollarCalloutConfig(request, config);
		}
		if(FDRCompassConfig == null){
			logger.log(request, "Unable to initialize the FDRCompass Config");
			return null;
		}
		//Get all the connection related details from the configuration
		String URL = FDRCompassConfig.getValue(FDRCompassURL);
		int port = FDRCompassConfig.getIntValue(FDRCompassPort);
		int timeout = FDRCompassConfig.getIntValue(Timeout);
		int numTriesAllowed = FDRCompassConfig.getIntValue(NumTriesAllowed);
		String backupURL = FDRCompassConfig.getValue(FDRCompassBackupURL);
		int backupPort = FDRCompassConfig.getIntValue(FDRCompassBackupPort);



		FDRCompassConfig.getLogger().log(request, "FDRCompass ZeroDollar Auth Version 1.1");

		StringBuffer maskedString = new StringBuffer();
		if(port == -1){
			port = 6105;
		}
		if(timeout == -1){
			timeout = 10000;
		}
		if(numTriesAllowed == -1){
			numTriesAllowed = 3;
		}
		StringBuffer postData = new StringBuffer();
		/*
		 * Request to FDRCompass is the following :
		 * RecordType : "P"
		 * Format Constant : "74"
		 * Format Constant : "V"
		 * Merchant Order Number : Unique 22 characters field Left justified Spaces filled (We put last 4 digits CardNumber + timestamp + spaces)
		 * Method Of payment : VI for Visa or MC for MasterCard
		 * Account Number : 19 Character field Left justified Spaces filled. (CardNumber + spaces)
		 * Expiration Date : 4 character MMYY
		 * Division Number : 10 characters field right justified zeros filled SpaceMerchant ID provided by FDR
		 */
		postData.append("P74V");
		String cardNumber = request.getCardNumber();
		/*
		 * Unique Transaction ID upto 22 characters
		 */
		postData.append(createTxId(cardNumber));
		/*
		 * For VISA Cards Method of Payment should be 'VI'
		 * For MasterCard Method of Payment should be 'MC'
		 */
		if(cardNumber.charAt(0) == '4'){
			postData.append("VI");
		} else {
			postData.append("MC");
		}
		//Card Number
		postData.append(padSpaceRight(cardNumber, 19));
		//Expiry Date
		postData.append(expMonth);
		if(expYear.length() == 2){
			postData.append(expYear);
		} else if(expYear.length() == 4){
			postData.append(expYear.substring(2));
		}
		/*
		 * Merchant ID : First get the Customer Id from Cache for the Bank.
		 * Then Search for that customer id, merchant id mapping in the configuration
		 * If not found use the default arcot merchant ID
		 */
		BankInfo bankInfo = ESCache.bic.getBank(request.getBankId());
		String customerId = bankInfo.CustomerID;
		String merchantId = null;
		if(customerId != null && !("".equals(customerId))){
			merchantId = FDRCompassConfig.getValue(customerId);
		}
		if(merchantId == null || "".equals(merchantId))
			merchantId = defaultMerchantId;
		postData.append(padZerosLeft(merchantId, 10));
		if(config.getValue(DollarAmount) == null || ("").equals(config.getValue(DollarAmount))){
			//Amount should all zero of length 12 characters in case the dollar amount is not configured
			postData.append("000000000000");
		} else {
			postData.append(padZerosLeft(config.getValue(DollarAmount), 12));
		}
		//Currency type = USD i.e 840
		postData.append("840");
		//Transaction type = 7 (ECI Indicator: Channel Encrypted Transaction)
		postData.append("7");
		//N -
		//VF - Verification Only
		postData.append("   " );
		if(config.getValue(Authorize) == null || ("").equals(config.getValue(Authorize)) || !("y".equalsIgnoreCase(config.getValue(Authorize)))){
			postData.append("NVF");
		} else {
			postData.append(" AU");
		}
		postData.append(" ");
		String interMediatePostData = postData.toString();
		maskedString.append(interMediatePostData.substring(0, 28));
		maskedString.append("XXXXXXXXXXXX");
		maskedString.append(interMediatePostData.substring(40, 47));
		maskedString.append("XXXX");
		maskedString.append(interMediatePostData.substring(51));
		if(zip != null){
			// IF postal Code is provided then send the Postal code as well for Address verification
			postData.append("AZ");
			postData.append(padSpaceRight(zip, 10));
			postData.append("  ");
			String zipString = postData.toString().substring(interMediatePostData.length());
			maskedString.append(zipString.substring(0, 2));
			maskedString.append("XXXXXXXXXXXX");
		}
		// Send the CVV for CVV verification
		postData.append("FR1");
		postData.append(padSpaceRight(cvv, 4));
		postData.append("\r");
		maskedString.append("FR1");
		maskedString.append("XXXX");
		//Including the Fallback URL functionality.
		int primaryTries = 1;
		boolean secondryTry = false;
		//If no back Up URL is configured then use all the tries with primary URL
		// If the num tries allowed is just 1 then only the primary needs to be used
		if(backupURL == null || "".equals(backupURL) || backupPort == -1 || numTriesAllowed == 1){
			primaryTries = numTriesAllowed;
		} else {
			//Else save last try for the Back up URL
			primaryTries = numTriesAllowed - 1;
			secondryTry = true;
		}

		int numTries = 0;
		String response = null;
		Socket conn = null;
		OutputStream socketOutputStream = null;
		InputStream socketInputStream = null;
		//Make a socket Connection and post the request and get the Response for the primary URL
		String authReversalResponse = null;
		try{
			do
			{
				try{
					cleanUpSocketConnection(request, config, conn, socketOutputStream, socketInputStream);
					conn = new Socket();
					InetSocketAddress socketAddress = new InetSocketAddress(URL, port);
					conn.connect(socketAddress, timeout);
					conn.setSoTimeout(timeout);
					socketOutputStream = conn.getOutputStream();
					socketInputStream = conn.getInputStream();
					response = socketConnection(request, config, postData.toString(), maskedString.toString(), conn, socketOutputStream, socketInputStream);
				}catch(IOException e){
					logger.log(request, "IO Exception: " + e.getMessage());
				}
				numTries++;
			} while(numTries < primaryTries && response == null);
			COLogger fdrCompassLogger = FDRCompassConfig.getLogger();
			//If backup Configured try for the Back UP url
			if(secondryTry == true && response == null){
				try{
					cleanUpSocketConnection(request, config, conn, socketOutputStream, socketInputStream);
					conn = new Socket();
					InetSocketAddress socketAddress = new InetSocketAddress(backupURL, backupPort);
					conn.connect(socketAddress, timeout);
					conn.setSoTimeout(timeout);
					socketOutputStream = conn.getOutputStream();
					socketInputStream = conn.getInputStream();
					fdrCompassLogger.log(request, "Trying Fallback URL");
					response = socketConnection(request, config, postData.toString(), maskedString.toString(), conn, socketOutputStream, socketInputStream);
				}catch(SocketException e){
					logger.log(request, "SocketException: " + e.getMessage());
				}catch(IOException e){
					logger.log(request, "IOException: " + e.getMessage());
				}
				numTries++;
			}
			String maskedResponse = maskedResponse(request, config, response);

			int relevantResponseIndex = response.indexOf("T74V");
			if(relevantResponseIndex < 0){
				logger.log(request, "Unable to get the starting index 'T74V' in the response");
				return response;
			}
			String relevantResponse = response.substring(relevantResponseIndex);


			String zeroDollarAuthResponse = relevantResponse.substring(26,29);
			String zipVerificationResponse = relevantResponse.substring(41, 43);
			char cvvVerificationResponse = relevantResponse.charAt(43);
			fdrCompassLogger.log(request, "Response received after numTries : " + (numTries) + ", Response : " + maskedResponse + ", FDMSResponse : " + zeroDollarAuthResponse +", CVV : " + cvvVerificationResponse + ", AVS : " + zipVerificationResponse);
			if(config.getValue(Authorize) != null && !("").equals(config.getValue(Authorize)) && ("y".equalsIgnoreCase(config.getValue(Authorize)))){
				if(zeroDollarAuthResponse != null && "100".equals(zeroDollarAuthResponse)){
					String authReversalPostData = createAuthReversalMessage(request, config, relevantResponse, merchantId, expMonth, expYear);
					authReversalResponse =sendAuthReversalMessage(request, config, relevantResponse, URL, port, timeout, backupURL, backupPort, conn, socketOutputStream, socketInputStream, authReversalPostData);
					if(authReversalResponse == null){
						authReversalResponse = "null";
						logger.log(request, "Auth Reversal Response is null");
					} else {
						logger.log(request, "Auth Reversal Response is [" + authReversalResponse + "]");
					}
				} else {
					logger.log(request, "As Response is [" + zeroDollarAuthResponse + "] not doing a auth reversal" );
				}
			}
		}catch(Exception e){
			logger.log(request, "Exception in doZeroDollarAuth: " + e.getMessage());
		}finally{
			cleanUpSocketConnection(request, config, conn, socketOutputStream, socketInputStream);
		}
		if(authReversalResponse != null)
			return response + "AR" + authReversalResponse;
		else
			return response;
	}
	public static String createAuthReversalMessage(ACSRequest request, COConfig config, String authorizeResponse,String merchantId, String expMonth, String expYear){
		StringBuffer postData = new StringBuffer();
		postData.append("P74V");
		String cardNumber = request.getCardNumber();
		postData.append(padSpaceRight(authorizeResponse.substring(4,26), 22));
		if(cardNumber.charAt(0) == '4'){
			postData.append("VI");
		} else {
			postData.append("MC");
		}
		postData.append(padSpaceRight(cardNumber, 19));
		postData.append(expMonth);
		if(expYear.length() == 2){
			postData.append(expYear);
		} else if(expYear.length() == 4){
			postData.append(expYear.substring(2));
		}
		postData.append(padZerosLeft(merchantId, 10));
		if(config.getValue(DollarAmount) == null || ("").equals(config.getValue(DollarAmount))){
			//Amount should all zero of length 12 characters in case the dollar amount is not configured
			postData.append("000000000000");
		} else {
			postData.append(padZerosLeft(config.getValue(DollarAmount), 12));
		}
		//Currency type = USD i.e 840
		postData.append("840");
		//Transaction type = 7 (ECI Indicator: Channel Encrypted Transaction)
		postData.append("7");
		postData.append("    ");
		postData.append("AR");
		postData.append(" ");
		postData.append("PA");
		postData.append(authorizeResponse.substring(29, 35));
		postData.append(authorizeResponse.substring(35, 41));
		postData.append("        ");
		postData.append("\r");
		return postData.toString();
	}
	public static void cleanUpSocketConnection(ACSRequest request, COConfig config, Socket conn, OutputStream socketOutputStream, InputStream socketInputStream){
		COLogger logger = config.getLogger();
		try {
			if(socketOutputStream != null)
				socketOutputStream.close();
			if(socketInputStream != null)
				socketInputStream.close();
		} catch (IOException e1) {
			 logger.log(request, e1.getMessage());
		}
		try{
			if(conn!= null)
				conn.close();
		}catch (IOException e1) {
			logger.log(request, e1.getMessage());
		}
	}
	public static String maskingRequest(String request){
		StringBuffer maskedRequest = new StringBuffer();
		maskedRequest.append(request.substring(0, 28));
		maskedRequest.append("XXXXXXXXXXXX");
		maskedRequest.append(request.substring(40, 47));
		maskedRequest.append("XXXX");
		maskedRequest.append(request.substring(51));
		return maskedRequest.toString();
	}
	public static String sendAuthReversalMessage(ACSRequest request, COConfig config, String authorizeResponse, String URL, int port, int timeout, String backupURL, int backupPort, Socket socketConnection, OutputStream socketOutputStream, InputStream socketInputStream, String postData){
		COLogger logger = config.getLogger();
		String maskedRequest = maskingRequest(postData);
		String heartBeatMessage = authorizeResponse.substring(0,20);
		if(heartBeatMessage.startsWith("HO")){
			heartBeatMessage = heartBeatMessage.replace('O', 'I');
		}
		else
		{
			heartBeatMessage = null;
		}

		if(heartBeatMessage == null)
		{
			logger.log(request,"No HeartBeat Response was received, so not sending one back.");
		}
		else
		{
			try {
				logger.log(request, "HeartBeat Response: " + heartBeatMessage);
				socketOutputStream.write(heartBeatMessage.getBytes(), 0, heartBeatMessage.length());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.log(request, "Exception while Posting Heart Beat Message: " + e1.getMessage());
			}
		}
		String response = null;
		response = socketConnection(request, config, postData.toString(), maskedRequest.toString(), socketConnection, socketOutputStream, socketInputStream);

		boolean secondaryTry = false;
		if(backupURL != null && !("".equals(backupURL)) && backupPort != -1 ){
			secondaryTry = true;
		}
		COLogger fdrCompassLogger = FDRCompassConfig.getLogger();
		if(secondaryTry == true && response == null){
			try{
				cleanUpSocketConnection(request, config, socketConnection, socketOutputStream, socketInputStream);
				socketConnection = new Socket();
				InetSocketAddress socketAddress = new InetSocketAddress(backupURL, backupPort);
				socketConnection.connect(socketAddress, timeout);
				socketConnection.setSoTimeout(timeout);
				socketOutputStream = socketConnection.getOutputStream();
				socketInputStream = socketConnection.getInputStream();
				fdrCompassLogger.log(request, "Trying Fallback URL for auth Reversal");
				response = socketConnection(request, config, postData.toString(), maskedRequest.toString(), socketConnection, socketOutputStream, socketInputStream);
			}catch(SocketException e){
				logger.log(request, "SocketException: " + e.getMessage());
			}catch(IOException e){
				logger.log(request, "IOException: " + e.getMessage());
			}
		}
		if(response != null && !("".equals(response))){
			response = padSpaceRight("", 20) + response;
			String maskedResponse = maskedResponse(request, config, response);
			String zeroDollarReversalAuthResponse = response.substring(46,49);
			fdrCompassLogger.log(request, "Response received after Auth Reversal : " + maskedResponse + ", FDMSResponse : " + zeroDollarReversalAuthResponse );
			if(zeroDollarReversalAuthResponse != null )

				return zeroDollarReversalAuthResponse;

		}
		return null;
	}

	public static String maskedResponse(ACSRequest request, COConfig config, String responseString ){
		StringBuffer maskedResponse = new StringBuffer();

		if(responseString.length() < 103 && responseString.length() >= 83)
		{
			responseString = "* NO HDR AVAILABLE *"+responseString;
		}


		if(responseString != null && responseString.length() >= 103){
			maskedResponse.append(responseString.substring(0, 19));
			maskedResponse.append(" ");
			maskedResponse.append(responseString.substring(20, 64));
			maskedResponse.append("XXXXXXXXXXXX");
			maskedResponse.append(responseString.substring(76, 83));
			maskedResponse.append("XXXX");
			maskedResponse.append(responseString.substring(87));
		}
		return maskedResponse.toString();
	}


	public static boolean parseResponse(ACSRequest request, COConfig config, String response, StringBuffer logToDB, boolean matchZipFlag){
		boolean zeroDollarStatus = false;
		boolean cvvStatus = false;
		boolean zipStatus = false;
		COLogger logger = config.getLogger();
		/*
		 * The response begins with the heart beat message which we need to ignore and
		 * then read the actual message. The relevant response begins with "T74V"
		 */
		int relevantResponseIndex = response.indexOf("T74V");
		if(relevantResponseIndex < 0){
			logger.log(request, "Unable to get the starting index 'T74V' in the response");
			return false;
		}
		String relevantResponse = response.substring(relevantResponseIndex);
		String zeroDollarAuthResponse = relevantResponse.substring(26,29);
		String zipVerificationResponse = relevantResponse.substring(41, 43);
		char cvvVerificationResponse = relevantResponse.charAt(43);
		logger.log(request, "zeroDollarAuthResponse : " + zeroDollarAuthResponse + ", zipVerification : " + zipVerificationResponse + ", cvvVerificationCode : " + cvvVerificationResponse);
		//Rahul: From the Documentation we found that for multiple Response codes FDR Compass treats them as success.
		//So we have made it configurable.
		String responseCodeMatch = config.getValue(Zero_Dollar_Response_Code_Match_VAL);
		if(responseCodeMatch == null || "".equals(responseCodeMatch))
			responseCodeMatch = Zero_Dollar_DEFAULT_Response_Code_MATCH;
		if(responseCodeMatch.indexOf(zeroDollarAuthResponse) <= -1){
			//$0 not approved
			//logger.log(request, "FDMS Approval failed with Approval code [" + zeroDollarAuthResponse + "]");
			logToDB.append("$0Auth(F)-");
		}else{
			zeroDollarStatus = true;
			//logger.log(request, "FDMS Approval with Approval code [" + zeroDollarAuthResponse + "]");
			logToDB.append("$0Auth(P)-");
		}
		//Get the Successful codes for CVV Match
		String cvvMatch = config.getValue(FDMS_CVV_MATCH_VAL);
		if(cvvMatch == null){
			//use default as per CNS IPGS DB
			cvvMatch = DEFAULT_CVV_MATCH;
		}
		//Check if the response that we received from FDRCompass belongs to the Success codes.
		if(cvvMatch.indexOf(cvvVerificationResponse) > -1){
			logToDB.append("-cvv2(P)-");
			cvvStatus = true;
		}else{
			logToDB.append("-cvv2(F)-");
		}
		//Only If we got the zip as request do we need to validate them.
		if(matchZipFlag){
			//Get the Successful codes for Postal Code match
			String zipMatch = config.getValue(Zero_Dollar_Zip_MATCH_VAL);
			if(zipMatch == null){
				//use default as per CNS IPGS DB
				zipMatch = Zero_Dollar_DEFAULT_Zip_MATCH;
			}
			if(zipMatch.indexOf(zipVerificationResponse) > -1){
				logToDB.append("zip(P)-");
				zipStatus = true;
			}else{
				logToDB.append("zip(F)-");
			}
		}
		if(zeroDollarStatus && cvvStatus){
			if(matchZipFlag == false || (matchZipFlag && zipStatus )){
				logToDB.append("-Fsts(P)");
				logToDB.append(relevantResponse.substring(3, 44) + relevantResponse.substring(relevantResponse.length() - 6));
				return true;
			}
		}
		logToDB.append("-Fsts(F)");
		logToDB.append(relevantResponse.substring(3, 44) + relevantResponse.substring(relevantResponse.length() - 6));
		return false;
	}



	public static Map<String, Object> parseResponseForPlugin(ACSRequest request, COConfig config, String response){
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		COLogger logger = config.getLogger();
		/*
		 * The response begins with the heart beat message which we need to ignore and
		 * then read the actual message. The relevant response begins with "T74V"
		 */
		int relevantResponseIndex = response.indexOf("T74V");

		if(relevantResponseIndex < 0){
			logger.log(request, "Unable to get the starting index 'T74V' in the response");
			return null;
		}
		String relevantResponse = response.substring(relevantResponseIndex);
		String zeroDollarAuthResponse = relevantResponse.substring(26,29);
		String zipVerificationResponse = relevantResponse.substring(41, 43);
		char cvvVerificationResponse = relevantResponse.charAt(43);
		logger.log(request, "zeroDollarAuthResponse : " + zeroDollarAuthResponse + ", zipVerification : " + zipVerificationResponse + ", cvvVerificationCode : " + cvvVerificationResponse);
		//Rahul: From the Documentation we found that for multiple Response codes FDR Compass treats them as success.
		//So we have made it configurable.
		String responseCodeMatch = config.getValue(Zero_Dollar_Response_Code_Match_VAL);
				
		
		if(responseCodeMatch == null || "".equals(responseCodeMatch))
			responseCodeMatch = Zero_Dollar_DEFAULT_Response_Code_MATCH;
		if(responseCodeMatch.indexOf(zeroDollarAuthResponse) <= -1){
			//$0 not approved
			//logger.log(request, "FDMS Approval failed with Approval code [" + zeroDollarAuthResponse + "]");
			retMap.put("DOLLAR_AUTH_STATUS", Boolean.FALSE);
		}else{
			
			retMap.put("DOLLAR_AUTH_STATUS", Boolean.TRUE);
		}
		
		retMap.put("DOLLAR_AUTH_STATUS_CODE", zeroDollarAuthResponse);
		
		//Get the Successful codes for CVV Match
		String cvvMatch = config.getValue(FDMS_CVV_MATCH_VAL);
		if(cvvMatch == null){
			//use default as per CNS IPGS DB
			cvvMatch = DEFAULT_CVV_MATCH;
		}
		//Check if the response that we received from FDRCompass belongs to the Success codes.
		if(cvvMatch.indexOf(cvvVerificationResponse) > -1){
			retMap.put("CVV_STATUS", Boolean.TRUE);			
		}else{
			retMap.put("CVV_STATUS", Boolean.FALSE);
		}
		
		retMap.put("CVV_STATUS_CODE", String.valueOf(cvvVerificationResponse));
		//Only If we got the zip as request do we need to validate them.
	
		//Get the Successful codes for Postal Code match
		String zipMatch = config.getValue(Zero_Dollar_Zip_MATCH_VAL);
		if(zipMatch == null){
			//use default as per CNS IPGS DB
			zipMatch = Zero_Dollar_DEFAULT_Zip_MATCH;
		}
		if(zipMatch.indexOf(zipVerificationResponse) > -1){
			retMap.put("ZIP_STATUS", Boolean.TRUE);
			
		}else{
			retMap.put("ZIP_STATUS", Boolean.FALSE);
		}
		
		retMap.put("ZIP_STATUS_CODE", String.valueOf(zipVerificationResponse));
		
		String txnRefDetails = relevantResponse.substring(3, 44) + relevantResponse.substring(relevantResponse.length() - 6);
		
		retMap.put("TXN_REF_DETAILS", txnRefDetails);
		
		logger.info(request, "DollarAuthRef:"+txnRefDetails);
		
		return retMap;
	}

	
	public static boolean parseResponseZeroDollarOnly(ACSRequest request, COConfig config, String response, StringBuffer logToDB){
		boolean zeroDollarStatus = false;
		COLogger logger = config.getLogger();
		/*
		 * The response begins with the heart beat message which we need to ignore and
		 * then read the actual message. The relevant response begins with "T74V"
		 */
		int relevantResponseIndex = response.indexOf("T74V");
		if(relevantResponseIndex < 0){
			logger.log(request, "Unable to get the starting index 'T74V' in the response");
			return false;
		}
		String relevantResponse = response.substring(relevantResponseIndex);
		String zeroDollarAuthResponse = relevantResponse.substring(26,29);

		logger.log(request, "zeroDollarAuthResponse : " + zeroDollarAuthResponse );
		//Rahul: From the Documentation we found that for multiple Response codes FDR Compass treats them as success.
		//So we have made it configurable. So that if we decide to support them then its possible.
		String responseCodeMatch = config.getValue(Zero_Dollar_Response_Code_Match_VAL);
		if(responseCodeMatch == null || "".equals(responseCodeMatch))
			responseCodeMatch = Zero_Dollar_DEFAULT_Response_Code_MATCH;
		if(responseCodeMatch.indexOf(zeroDollarAuthResponse) <= -1){
			//$0 not approved
			//logger.log(request, "FDMS Approval failed with Approval code [" + zeroDollarAuthResponse + "]");
			logToDB.append("$0Auth(F)");
		}else{
			zeroDollarStatus = true;
			//logger.log(request, "FDMS Approval with Approval code [" + zeroDollarAuthResponse + "]");
			logToDB.append("$0Auth(P)");
		}
		if(zeroDollarStatus){
			logToDB.append("-Fsts(P)");
			logToDB.append(relevantResponse.substring(3, 44) + relevantResponse.substring(relevantResponse.length() - 6));
			return true;
		}
		logToDB.append("-Fsts(F)");
		logToDB.append(relevantResponse.substring(3, 44) + relevantResponse.substring(relevantResponse.length() - 6));
		return false;
	}

	private static String socketConnection(ACSRequest request, COConfig config, String postData, String maskedRequest, Socket socketConnection, OutputStream socketOutputStream, InputStream socketInputStream){


		String response = null;
		COLogger logger = FDRCompassConfig.getLogger();
		try {
			//Do a socket connection
			//Flush the Input Stream just in case there is some data remaining there
			if (socketInputStream.available() > 0) {
				byte[] discardByte = new byte[socketInputStream.available()];
				socketInputStream.read(discardByte);
			}
			logger.log(request, "Created Socket connection and posting Data : " + maskedRequest);
			socketOutputStream.write(postData.getBytes(), 0, postData.length());
			socketOutputStream.flush();
			/*
			 * It is a fixed length Response of total 103 characters.
			 * With the heart beat message of 20 characters and the remaining 83 characters
			 * with the actual response. After the heart beat message there is '\r'.
			 *  So we need to read again to get the actual response.
			 */
			byte[] buffer = new byte[83];
			StringBuffer responseRead = new StringBuffer();
			int totalRead = 0;
			/*
			 * Once we have read 103 characters we should not wait for them to
			 * close the connection as we found in the test environment that they sometimes take
			 * 40 - 50 seconds to return a carriage return or eof. Thus we just read the amount of data
			 * required and then we return.
			 */

			while(totalRead < 83){
				int respLen = socketInputStream.read(buffer);
				responseRead.append(new String(buffer, 0, respLen));
				totalRead += respLen;
			}

			logger.log(request, "Received Bytes : "+totalRead);

			response = responseRead.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.log(request, "IO Exception While doing transaction:" + e.getMessage());

		}
		return response;
	}
}
