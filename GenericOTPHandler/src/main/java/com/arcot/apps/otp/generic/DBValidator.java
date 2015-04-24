package com.arcot.apps.otp.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.COResponse;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.otpvalidator.OTPValidator;
import com.arcot.apps.otpvalidator.ValidatorResponse;
import com.arcot.vpas.enroll.EnrollmentCrypto;

public class DBValidator extends OTPValidator implements ACS {
	
	/**
	 * TIME_BETWEEN_RESENDS
	 * Datatype :: int
	 * Time in milliseconds between two consecutive resends
	 */
	private static final String TIME_BETWEEN_RESENDS = "TIME_BETWEEN_RESENDS";
	
	/**
	 * OTP_PER_TRX_PROXY_PAN
	 * Datatype :: boolean:Default - false
	 * if otp is to be supported per transaction proxy pan		[true]
	 * [Default, if NOT configured]otp supported per cardnumber					
	 * 		
	 */
	private static final String OTP_PER_TRX_PROXY_PAN = "OTP_PER_TRX_PROXY_PAN";
	/**
	 * NUM_OF_RESENDS
	 * Datatype :: int
	 * number of times resend can be clicked
	 */
	private static final String NUM_OF_RESENDS = "NUM_OF_RESENDS";
	
	/**OTP_TIMEOUT_ACTION
	 * The ACS response in the advent of the Mobile Number not present/returned.		
	 * Response values to be set in the callout config				[ACS_CALLOUT_PARES_N/ACS_CALLOUT_PARES_U/ACS_CALLOUT_TX_NO_ACTION]
	 * if not configured, default value is 							[ACS_CALLOUT_PARES_N]
	 */
	private static final String OTP_TIMEOUT_ACTION = "OTP_TIMEOUT_ACTION";
	
	private static final String OTPFAILED = "[OTPFAILED]";
	
	
	private static final String OTPTIMEOUT = "[OTPTIMEOUT]";
	

	/**
	 * 
	 */
	@Override
	public ValidatorResponse validateOTP(HashMap<String, String> configs) {
		ValidatorResponse res = super.validate(configs);
		/*
		 * perform additional validations and modify res if needed
		 */
		return res;
	}

	@Override
	protected ValidatorResponse validate(HashMap<String, String> configs) {
		// TODO Auto-generated method stub
		return super.validate(configs);
	}

		
	/**
	 * This shows last 4 digit of mobile number
	 * 
	 * @param mobileNumber
	 * @return
	 */
	public static String formatMobileNumber(String mobileNumber) {
		return mobileNumber.substring(mobileNumber.length()-4, mobileNumber.length());
	}
	
	
	/**
	 * This method will return the Locale of the Cardholder
	 * 
	 * @param request
	 * @param config
	 * @return String
	 * 
	 */
	public static String setCalloutMsgForOTPPage(ACSRequest request, COConfig config) {
		return "";
	}
	
	public static String provideExtraCalloutData(ACSRequest request, COConfig config, List<String> calloutData){
		String str = "";
		if (calloutData.size()>0){
			for(int i=0; i<calloutData.size();i++){
				str += calloutData.get(i)+ "~";
			}
			return str;
			
		}
		return "";
	}
	
	/**
	 * fetch the calloutdata from AHA
	 * @param request
	 * @param config
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String[] fetchCalloutData(ACSRequest request, COConfig config){
		
		COLogger logger = config.getLogger();
		logger.info(request, "fetchCalloutData()");
		
		String strCalloutData[] = null;
		
		if (Boolean.valueOf(config.getValue(OTP_PER_TRX_PROXY_PAN))){
			
			logger.info(request, "fetchCalloutData() :: Get CALLOUTDATA from ARCALLOUTXNDATA: "+COUtil.getTxnCalloutData(request, config));
			// otp at trxproxypan level
			if(COUtil.getTxnCalloutData(request, config) != null){
				strCalloutData = COUtil.getTxnCalloutData(request, config)!= null ? COUtil.getTxnCalloutData(request, config).split("~"): null;
			}
			
		}else{
			
			logger.info(request, "fetchCalloutData() :: Get CALLOUTDATA from ARACCTHOLDERAUTH");
			// otp at cardnumber level
			strCalloutData = COUtil.getCalloutData(request, config)[0].split("~");
		}
		
		return strCalloutData;
	}
	
	/**
	 * The OTP Validation through the DB is performed.
	 * @param request
	 * @param config
	 * @param strOTP
	 * @param logger
	 * @return COResponse
	 */
	@SuppressWarnings("unchecked")
	public static COResponse performDBValidation(ACSRequest request, COConfig config,
			String strOTP, COLogger logger, String localeName, HashMap<String, Object> additionalCustomerData) {
		String strCalloutData[] = null;
		strCalloutData = fetchCalloutData(request, config);
		String contactDetails = null;
		COResponse objCOResp = null;
		
		if(strCalloutData != null && strCalloutData.length > 3){
			contactDetails = strCalloutData[3];
		}
		
		if (config.getValue(GenericOTPHandler.OTP_VALIDITY_PERIOD) != null){

			// check for validity
			objCOResp = checkExpiryOfOTP(request, config, Long.valueOf(strCalloutData[1]), additionalCustomerData);
			objCOResp.setCalloutMessage(setCalloutMessageOnExpiry(request, config, objCOResp.getCalloutMessage()));
			
			if (objCOResp.getAcsCRC() == ACS_CALLOUT_TX_NO_ACTION){
				objCOResp = doesOTPMatch(request, config, strOTP, strCalloutData[0], contactDetails, localeName, objCOResp.getAdditionalCustomerData());
				
				return  objCOResp;
			}else{
				return objCOResp;
			}
			
		}else{

			// if validity_period is not configured proceed with the validation of OTP
			logger.warn(request, "validateOTP():: OTP_VALIDITY_PERIOD Not Configured");
			return  doesOTPMatch(request, config, strOTP, strCalloutData[0], contactDetails, localeName, objCOResp!= null? objCOResp.getAdditionalCustomerData(): null);
		}
	}
	
	/**
	 * Checks whether the OTP is still valid after 'OTP_VALIDITY_PERIOD' ms
	 * 
	 * @param request
	 * @param config
	 * @param otpGenerationTime
	 * @return
	 */
	public static COResponse checkExpiryOfOTP(ACSRequest request, COConfig config, long otpGenerationTime, HashMap<String, Object> additionalCustomerData){
		
		COLogger logger = config.getLogger();
		logger.info(request, "checkExpiryOfOTP()");
		if(additionalCustomerData == null)
		additionalCustomerData = new HashMap<String, Object>();
		COResponse coResponse = null;
		
		long currentTime = System.currentTimeMillis();
		long result = currentTime - otpGenerationTime;
		long longConfigOtpGenerationTime = Long.parseLong(config.getValue(GenericOTPHandler.OTP_VALIDITY_PERIOD));
		
		logger.info(request, "checkExpiryOfOTP():: CURRENT_TIME["+currentTime+"] " +
				"OTP_GENERATION_TIME["+otpGenerationTime+"] " +
						"VALIDITY_PERIOD_CONFIGURED["+longConfigOtpGenerationTime+"]");
		
		if (result > longConfigOtpGenerationTime){
			return actionOnOTPTimeOut(request,config, additionalCustomerData);
		}else{
			// otp is still valid
			coResponse = COUtil.createCOResponse(true, 0, "", "", ACS_CALLOUT_TX_NO_ACTION,"",request.getFolderId());
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		}
	}
	
	/**
	 * 
	 * Presently it will sent out OTPTIMEOUT
	 * 
	 * @param request
	 * @param config
	 * @param exsistingCalloutMsg 
	 * @return
	 */
	public static String setCalloutMessageOnExpiry(ACSRequest request,
			COConfig config, String exsistingCalloutMsg) {
		return exsistingCalloutMsg;
	}
	
	/**
     * 
     * @param request
     * @param config
     * @return
     */
	public static COResponse actionOnOTPTimeOut(ACSRequest request, COConfig config, HashMap<String, Object> additionalCustomerData) {
		
		COLogger logger = config.getLogger();
		logger.info(request, "actionOnOTPTimeOut()");
		COResponse coResponse = null;
		
		if (config.getValue(OTP_TIMEOUT_ACTION) != null){
			logger.info(request, "OTP_TIMEOUT_ACTION config has been Set.");
			coResponse = COUtil.createCOResponse(false, 0, OTPTIMEOUT, OTPTIMEOUT, GenericOTPHandler.retrieveACSResponse(config.getValue(OTP_TIMEOUT_ACTION)),
					OTPTIMEOUT, request.getFolderId());
			additionalCustomerData.put(GenericOTPHandler.error_msg, additionalCustomerData.get(GenericOTPHandler.error_msg) != null?
					additionalCustomerData.get(GenericOTPHandler.error_msg) + "," + OTPTIMEOUT: OTPTIMEOUT);
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		}else{
			logger.info(request, "The Default behaviour of sending PARES_N is being executed as OTP_TIMEOUT_ACTION is not set.");
			coResponse =  COUtil.createCOResponse(false, 0, OTPTIMEOUT, OTPTIMEOUT, ACS_CALLOUT_PARES_N,
					GenericOTPHandler.getLocaleName(request, logger) + OTPTIMEOUT, request.getFolderId());
			additionalCustomerData.put(GenericOTPHandler.error_msg, additionalCustomerData.get(GenericOTPHandler.error_msg) != null?
					additionalCustomerData.get(GenericOTPHandler.error_msg) + "," + OTPTIMEOUT: OTPTIMEOUT);
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param config
	 * @param userOTP
	 * @param storedOTP
	 * @param storedContactDetails 
	 * @return COResponse
	 */
	public static COResponse doesOTPMatch(ACSRequest request, COConfig config, String userOTP, String storedOTP,
			String storedContactDetails, String localeName, HashMap<String, Object> additionalCustomerData){
		
		COLogger logger = config.getLogger();
		logger.info(request, "doesOTPMatch()");
		COResponse coResponse = null;
		
		if(additionalCustomerData == null)
			additionalCustomerData = new HashMap<String, Object>();
		
		if(storedContactDetails == null || "".equals(storedContactDetails)){
			storedContactDetails = "";
		}
		
		String encryptedUserOTP = EnrollmentCrypto.encrypt(userOTP, request.getBankId());
		
		if (encryptedUserOTP.equals(storedOTP)){
			additionalCustomerData.put(GenericOTPHandler.success_msg, "OTP[s]");
			coResponse = COUtil.createCOResponse(true, 0, "OTP[s]", "OTP[s]", ACS_CALLOUT_SUCCESS, localeName, request.getFolderId());
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		
		}else{
			
			logger.info(request, "doesOTPMatch() :: Encrypted UserOTP [" + encryptedUserOTP + "] Encrypted StoredOTP [" + storedOTP + "]");
			
			String timeBetweenResends = ((config.getValue(TIME_BETWEEN_RESENDS) != null)) ? config.getValue(TIME_BETWEEN_RESENDS) : "";
			
			additionalCustomerData = setCalloutMsgForOTPPageOnFailure(request, config, additionalCustomerData);
			
			coResponse =  COUtil.createCOResponse(false, 0, "OTP[f]", "OTP[f]", ACS_CALLOUT_FAIL,"", 
					request.getFolderId());
			additionalCustomerData.put(GenericOTPHandler.time_period, timeBetweenResends);
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param config
	 * @return String
	 * 
	 */	
	public static HashMap<String, Object> setCalloutMsgForOTPPageOnFailure(ACSRequest request, COConfig config, HashMap<String, Object> additionalCustomerData){
		 additionalCustomerData.put(GenericOTPHandler.error_msg, OTPFAILED);
		
		return additionalCustomerData;
	}
	
	/**
	 * 
	 * @param request
	 * @param config
	 * @return
	 */
	public static String setCalloutMessageOnResendExceed(ACSRequest request,
			COConfig config) {
		return GenericOTPHandler.RESENTEXCEEDED;
	}
}
