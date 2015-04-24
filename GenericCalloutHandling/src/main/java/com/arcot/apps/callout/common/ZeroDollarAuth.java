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
import java.util.Vector;

import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.callout.CallOutsConfig;
import com.arcot.dboperations.admin.AdminSelectOperations;
import com.arcot.util.ArcotException;
import com.arcot.util.BankInfo;
import com.arcot.vpas.enroll.cache.ESCache;


public class ZeroDollarAuth {
	private static final String FDRCompassURL = "FDRCompassURL";
	private static final String FDRCompassPort = "FDRCompassPort";
	private static final String Timeout = "Timeout";
	private static final String NumTriesAllowed = "NumTriesAllowed";
	private static final String FDRCompassCalloutConfigId = "FDRCompassCalloutConfigId";

	// JJYAO 2013/05/14 -- NOTE: Need to add a new row for TSysCalloutConfigId in ARESConfig table
	private static final String TSysCalloutConfigId = "TSysCalloutConfigId";

	private static final String ArcotMerchantId = "ArcotMerchantId";
	private static COConfig InfoListConfig = null;
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

	public static void setZeroDollarAuthTestConfig(COConfig config) {
		InfoListConfig = config;
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
		int tsysCalloutConfigurationId = -1;
		//Gets the FDRCompassCallout and TSysCalloutConfigId Configuration ID from esconfig table which is cached.
		try{
			String fdrCompassCalloutConfiguration = ESCache.esc.getValue(FDRCompassCalloutConfigId);
			if(fdrCompassCalloutConfiguration == null){
				logger.log(request, "Unable to retrieve the Callouts configuration for FDRCompass");
			}
			else
				fdrCompassCalloutConfigurationId = Integer.parseInt(fdrCompassCalloutConfiguration);

			String tsysCalloutConfiguration = ESCache.esc.getValue(TSysCalloutConfigId);
			if(tsysCalloutConfiguration == null){
				logger.log(request, "Unable to retrieve the Callouts configuration for TSYS");
			}
			else
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
			if(calloutsConfig.getConfID() == fdrCompassCalloutConfigurationId || calloutsConfig.getConfID() == tsysCalloutConfigurationId){
				InfoListConfig = new COConfig(calloutsConfig);
				//Once we get the InfoListConfig find the Merchant ID for Arcot and use that as the default
				//Merchant Id where ever the customer id, merchant id mapping is not Found
				if(InfoListConfig != null){
					defaultMerchantId = InfoListConfig.getValue(ArcotMerchantId);
				}
			}
		}
	}

	public static String doZeroDollarAuth(ACSRequest request, COConfig config, String cvv, String expMonth, String expYear, String zip){
		COLogger logger = config.getLogger();

		if(InfoListConfig == null){
			initializeZeroDollarCalloutConfig(request, config);
		}
		if(InfoListConfig == null){
			logger.log(request, "Unable to initialize the either FDRCompass or TSYS Config");
			return null;
		}

		// JJYAO 2013/05/16: check which vendor to make the Zero Dollar Auth
		String zeroDollarVendor = config.getValue("ZeroDollarVendor");

		if (zeroDollarVendor == null || zeroDollarVendor.equalsIgnoreCase("FDRCompass")) {
			logger.log(request, "Peforming Zero Dollar Auth with FDRCompass...");
			return com.arcot.apps.callout.common.FDRCompassZeroDollarAuth.doZeroDollarAuth(request, config, cvv, expMonth, expYear, zip);
		}
		else if (zeroDollarVendor.equalsIgnoreCase("TSYS")) {
			logger.log(request, "Peforming Zero Dollar Auth with TSYS...");
			return com.arcot.apps.callout.common.TSysZeroDollarAuth.doZeroDollarAuth(request, config, cvv, expMonth, expYear, zip);
		}
		return null;
	}

	public static boolean parseResponse(ACSRequest request, COConfig config, String response, StringBuffer logToDB, boolean matchZipFlag){
		COLogger logger = config.getLogger();

		if(InfoListConfig == null){
			initializeZeroDollarCalloutConfig(request, config);
		}
		if(InfoListConfig == null){
			logger.log(request, "Unable to initialize the either FDRCompass or TSYS Config");
			return false;
		}

		// JJYAO 2013/05/16: check which vendor to make the Zero Dollar Auth
		String zeroDollarVendor = config.getValue("ZeroDollarVendor");

		if (zeroDollarVendor == null || zeroDollarVendor.equalsIgnoreCase("FDRCompass")) {
			logger.log(request, "Parsing Zero Dollar Auth response received from FDRCompass...");
			return com.arcot.apps.callout.common.FDRCompassZeroDollarAuth.parseResponse(request, config, response, logToDB, matchZipFlag);
		}
		else if (zeroDollarVendor.equalsIgnoreCase("TSYS")) {
			logger.log(request, "Parsing Zero Dollar Auth response received from TSYS...");
			return com.arcot.apps.callout.common.TSysZeroDollarAuth.parseResponse(request, config, response, logToDB, matchZipFlag);
		}
		return false;
	}

	public static boolean parseResponseZeroDollarOnly(ACSRequest request, COConfig config, String response, StringBuffer logToDB){
		COLogger logger = config.getLogger();

		if(InfoListConfig == null){
			initializeZeroDollarCalloutConfig(request, config);
		}
		if(InfoListConfig == null){
			logger.log(request, "Unable to initialize the either FDRCompass or TSYS Config");
			return false;
		}

		// JJYAO 2013/05/16: check which vendor to make the Zero Dollar Auth
		String zeroDollarVendor = config.getValue("ZeroDollarVendor");

		if (zeroDollarVendor == null || zeroDollarVendor.equalsIgnoreCase("FDRCompass")) {
			logger.log(request, "Parsing Zero Dollar Auth response received from FDRCompass...");
			return com.arcot.apps.callout.common.FDRCompassZeroDollarAuth.parseResponseZeroDollarOnly(request, config, response, logToDB);
		}
		else if (zeroDollarVendor.equalsIgnoreCase("TSYS")) {
			logger.log(request, "Peforming Zero Dollar Auth with TSYS...");
			return com.arcot.apps.callout.common.TSysZeroDollarAuth.parseResponseZeroDollarOnly(request, config, response, logToDB);
		}
		return false;
	}
}
