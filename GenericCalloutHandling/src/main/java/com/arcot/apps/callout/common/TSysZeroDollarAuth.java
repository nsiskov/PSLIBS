package com.arcot.apps.callout.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.callout.CallOutsConfig;
import com.arcot.dboperations.admin.AdminSelectOperations;
import com.arcot.util.ArcotException;
import com.arcot.util.GenericValidator;
import com.arcot.vpas.enroll.cache.ESCache;

public class TSysZeroDollarAuth {
	private static final String TSysURL = "TSysURL";
	private static final String NumTriesAllowed = "NumTriesAllowed";
	private static final String TSysCalloutConfigId = "TSysCalloutConfigId";
	private static final String ArcotMerchantId = "ArcotMerchantId";
	private static COConfig TSysConfig = null;
	private static String defaultMerchantId = null;
	private static final String DEFAULT_CVV_MATCH = "M";
	private static final String FDMS_CVV_MATCH_VAL = "FDMSCVVMatchVal";
	private static final String Zero_Dollar_Zip_MATCH_VAL = "ZERODOLLARZIPMatchVal";
	private static final String Zero_Dollar_Response_Code_Match_VAL = "ZERODOLLARResponseCodeMatchVal";
	private static final String Zero_Dollar_DEFAULT_Zip_MATCH = "ZMYXW";
	private static final String Zero_Dollar_DEFAULT_Response_Code_MATCH = "A0000";
	private static final String TSysBackupURL = "TSysBackupURL";
	private static final String DollarAmount = "DollarAmount";
	private static final String Authorize = "Authorize";
	private static final String TSysDeviceID = "TSysDeviceID";
	private static final String TSysTxKey = "TSysTxKey";
	private static	String merchantID = null;
	private static	String deviceID = null;
	private static String txKey = null;
	private static String adminUID = null;
	private static String adminPwd = null;

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
		int tsysCalloutConfigurationId = -1;
		//Gets the TSysCallout Configuration ID from esconfig table which is cached.
		try{
			String tsysCalloutConfiguration = ESCache.esc.getValue(TSysCalloutConfigId);
			if(tsysCalloutConfiguration == null){
				logger.log(request, "Unable to retrieve the Callouts configuration for TSys");
				return;
			}
			tsysCalloutConfigurationId = Integer.parseInt(tsysCalloutConfiguration);
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
			if(calloutsConfig.getConfID() == tsysCalloutConfigurationId){
				TSysConfig = new COConfig(calloutsConfig);
				//Once we get the TSysConfig find the Merchant ID for Arcot and use that as the default
				//Merchant Id where ever the customer id, merchant id mapping is not Found
				if(TSysConfig != null){
					defaultMerchantId = TSysConfig.getValue(ArcotMerchantId);
				}
			}
		}
	}

	private static String getResponseValue(String response, String fieldName){
		int startIndex = response.indexOf("<" +  fieldName + ">");
		int endIndex = response.indexOf("</" +  fieldName + ">");
		if (startIndex == -1 || endIndex == -1)
		    return null;

		startIndex = startIndex + 2 + fieldName.length();

		return response.substring(startIndex, endIndex);
	}

	public static void setZeroDollarAuthTestConfig(COConfig config) {
		TSysConfig = config;
	}

	public static String doZeroDollarAuth(ACSRequest request, COConfig config, String cvv, String expMonth, String expYear, String zip){
		COLogger logger = config.getLogger();

		if(TSysConfig == null){
			initializeZeroDollarCalloutConfig(request, config);
		}
		if(TSysConfig == null){
			logger.log(request, "Unable to initialize the TSys Config");
			return null;
		}
		//Get all the connection related details from the configuration
		String cardNumber = request.getCardNumber();
		String url = TSysConfig.getValue(TSysURL);
		String backupURL = TSysConfig.getValue(TSysBackupURL);
		int numTriesAllowed = TSysConfig.getIntValue(NumTriesAllowed);
		deviceID = TSysConfig.getValue(TSysDeviceID);
		txKey = TSysConfig.getValue(TSysTxKey);

		TSysConfig.getLogger().log(request, "TSYS ZeroDollar Auth Version 1.0");

		TSysAuthentication tsaObject = new TSysAuthentication();
		String response = null;
		try
		{
			response = tsaObject.doZeroDollarAuth(url, backupURL, numTriesAllowed, deviceID, txKey, cardNumber, cvv, expMonth, expYear, zip);
			TSysConfig.getLogger().trace(request, "TSYS ZeroDollar Auth Response : "+response);
		}
		catch(IOException e)
		{
			logger.log(request, "IO Exception: " + e.getMessage());
		}
		return response;

	}

	public static boolean parseResponse(ACSRequest request, COConfig config, String response, StringBuffer logToDB, boolean matchZipFlag){
		boolean zeroDollarStatus = false;
		boolean cvvStatus = false;
		boolean zipStatus = false;
		COLogger logger = config.getLogger();

		/* responseCode: A0000 -- Success APPROVED */
		String zeroDollarAuthResponse = getResponseValue(response, "responseCode");

		/* cvvVerificationCode:
		   M: CVV2 verification successful
		   P: CVV2 verification not performed
		   U: CVV2 verification not available
		   N: CVV2 verification fail/mismatch
		   S: CVV2 code not present on card
		*/
		String cvvVerificationResponse = getResponseValue(response, "cvvVerificationCode");

		/* AVS addressVerificationCode:
		   0:	Address verification was not requested.
		   A:	Address match only.
		   B:	Street Address match for international transaction Postal Code not verified because of incompatible formats (Acquirer sent both street address and Postal Code).
		   C:	Street address and Postal Code not verified for international transaction because of incompatible formats (Acquirer sent both street and Postal Code).
		   D:	Street Address match for international transaction.
		   F:	Street Address and Postal Code Match. Applies to UK only.
		   G:	Non-U.S. Issuer does not participate.
		   I:	Address information not verified for international transaction.
		   M:	Street Address match for international transaction.
		   N:	No address or ZIP match.
		   P:	Postal Codes match for international transaction Street address not verified because of incompatible formats (Acquirer sent both street address and Postal Code).
		   R:	Issuer system unavailable.
		   S:	Service not supported.
		   U:	Address unavailable.
		   W:	Nine character numeric ZIP match only.
		   X:	Exact match, nine character numeric ZIP.
		   Y:	Exact match, five character numeric ZIP.
		   Z:	Five character numeric ZIP match only.
		*/
		String zipVerificationResponse = getResponseValue(response, "addressVerificationCode");

		logger.log(request, "zeroDollarAuthResponse : " + zeroDollarAuthResponse + ", zipVerification : " + zipVerificationResponse + ", cvvVerificationCode : " + cvvVerificationResponse);
		if(!zeroDollarAuthResponse.equalsIgnoreCase("A0000")) {
			//$0 not approved
			logToDB.append("$0Auth(F)-");
		}else{
			zeroDollarStatus = true;
			logToDB.append("$0Auth(P)-");
		}

		//Check if the response that we received from TSys belongs to the Success codes.
		if(cvvVerificationResponse.equalsIgnoreCase("M")) {
			logToDB.append("-cvv2(P)-");
			cvvStatus = true;
		}else{
			logToDB.append("-cvv2(F)-");
		}

		//Only If we got the zip as request do we need to validate them.
		if(matchZipFlag){
			//Get the Successful codes for Postal Code match
			if(zipVerificationResponse.equalsIgnoreCase("Z")) {
				logToDB.append("zip(P)-");
				zipStatus = true;
			}else{
				logToDB.append("zip(F)-");
			}
		}
		if(zeroDollarStatus && cvvStatus){
			if(matchZipFlag == false || (matchZipFlag && zipStatus )){
				logToDB.append("-Fsts(P)");
				logToDB.append(zipVerificationResponse);
				return true;
			}
		}
		logToDB.append("-Fsts(F)");
		logToDB.append(zipVerificationResponse);
		return false;
	}

	public static boolean parseResponseZeroDollarOnly(ACSRequest request, COConfig config, String response, StringBuffer logToDB){
		boolean zeroDollarStatus = false;
		COLogger logger = config.getLogger();

		/* responseCode: A0000 -- Success APPROVED */
		String zeroDollarAuthResponse = getResponseValue(response, "responseCode");
		logger.log(request, "zeroDollarAuthResponse : " + zeroDollarAuthResponse );

		/* AVS addressVerificationCode:
		   0:	Address verification was not requested.
		   A:	Address match only.
		   B:	Street Address match for international transaction Postal Code not verified because of incompatible formats (Acquirer sent both street address and Postal Code).
		   C:	Street address and Postal Code not verified for international transaction because of incompatible formats (Acquirer sent both street and Postal Code).
		   D:	Street Address match for international transaction.
		   F:	Street Address and Postal Code Match. Applies to UK only.
		   G:	Non-U.S. Issuer does not participate.
		   I:	Address information not verified for international transaction.
		   M:	Street Address match for international transaction.
		   N:	No address or ZIP match.
		   P:	Postal Codes match for international transaction Street address not verified because of incompatible formats (Acquirer sent both street address and Postal Code).
		   R:	Issuer system unavailable.
		   S:	Service not supported.
		   U:	Address unavailable.
		   W:	Nine character numeric ZIP match only.
		   X:	Exact match, nine character numeric ZIP.
		   Y:	Exact match, five character numeric ZIP.
		   Z:	Five character numeric ZIP match only.
		*/
		String zipVerificationResponse = getResponseValue(response, "addressVerificationCode");

		if(!zeroDollarAuthResponse.equalsIgnoreCase("A0000")) {
			//$0 not approved
			logToDB.append("$0Auth(F)");
		}else{
			zeroDollarStatus = true;
			logToDB.append("$0Auth(P)");
		}

		if(zeroDollarStatus){
			logToDB.append("-Fsts(P)");
			logToDB.append(zipVerificationResponse);
			return true;
		}
		logToDB.append("-Fsts(F)");
		logToDB.append(zipVerificationResponse);
		return false;
	}

	public static Map<String, Object> parseResponseForPlugin(ACSRequest acsRequest,
			COConfig config, String response)
	{

		Map<String, Object> retMap = new HashMap<String, Object>(8);

		COLogger logger = config.getLogger();

		/* responseCode: A0000 -- Success APPROVED */
		String zeroDollarAuthResponse = getResponseValue(response, "responseCode");

		
		String cvvVerificationResponse = getResponseValue(response, "cvvVerificationCode");

		
		String zipVerificationResponse = getResponseValue(response, "addressVerificationCode");
		
		String txnId = getResponseValue(response, "transactionID");

		logger.trace(acsRequest, "zeroDollarAuthResponse : " + zeroDollarAuthResponse + ", zipVerification : " + zipVerificationResponse + ", cvvVerificationCode : " + cvvVerificationResponse);

		//So we have made it configurable.
		String responseCodeMatch = config.getValue(Zero_Dollar_Response_Code_Match_VAL);
				
		
		if(responseCodeMatch == null || "".equals(responseCodeMatch))
			responseCodeMatch = Zero_Dollar_DEFAULT_Response_Code_MATCH;
		if(!GenericValidator.isBlankOrNull(zeroDollarAuthResponse)){
			if(responseCodeMatch.indexOf(zeroDollarAuthResponse) > -1){
				//$0 not approved
				//logger.log(request, "FDMS Approval failed with Approval code [" + zeroDollarAuthResponse + "]");
				retMap.put("DOLLAR_AUTH_STATUS", Boolean.TRUE);

			}else{
				retMap.put("DOLLAR_AUTH_STATUS", Boolean.FALSE);

			}
			retMap.put("DOLLAR_AUTH_STATUS_CODE", zeroDollarAuthResponse);
		}else{
			retMap.put("DOLLAR_AUTH_STATUS", Boolean.FALSE);
			retMap.put("DOLLAR_AUTH_STATUS_CODE", zeroDollarAuthResponse);
		}
		
		//Get the Successful codes for CVV Match
		String cvvMatch = config.getValue(FDMS_CVV_MATCH_VAL);
		if(GenericValidator.isBlankOrNull(cvvMatch)){
			//use default as per CNS IPGS DB
			cvvMatch = DEFAULT_CVV_MATCH;
		}
		//Check if the response that we received from FDRCompass belongs to the Success codes.
		if(!GenericValidator.isBlankOrNull(cvvVerificationResponse)){
			if(cvvMatch.indexOf(cvvVerificationResponse) > -1){
				retMap.put("CVV_STATUS", Boolean.TRUE);			
			}else{
				retMap.put("CVV_STATUS", Boolean.FALSE);
			}
			retMap.put("CVV_STATUS_CODE", cvvVerificationResponse);
		}else{
			retMap.put("CVV_STATUS", Boolean.FALSE);
			retMap.put("CVV_STATUS_CODE", cvvVerificationResponse);
		}
		
		//Only If we got the zip as request do we need to validate them.
	
		//Get the Successful codes for Postal Code match
		String zipMatch = config.getValue(Zero_Dollar_Zip_MATCH_VAL);
		if(GenericValidator.isBlankOrNull(zipMatch)){
			//use default as per CNS IPGS DB
			zipMatch = Zero_Dollar_DEFAULT_Zip_MATCH;
		}
		if(!GenericValidator.isBlankOrNull(zipVerificationResponse)){
			if(zipMatch.indexOf(zipVerificationResponse) > -1){
				retMap.put("ZIP_STATUS", Boolean.TRUE);

			}else{
				retMap.put("ZIP_STATUS", Boolean.FALSE);
			}
			retMap.put("ZIP_STATUS_CODE", zipVerificationResponse);
		}else{
			retMap.put("ZIP_STATUS", Boolean.FALSE);
			retMap.put("ZIP_STATUS_CODE", zipVerificationResponse);
		}
		
		retMap.put("TXN_REF_DETAILS", txnId);
		
		return retMap;
		
		
	}

}
