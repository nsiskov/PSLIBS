package com.arcot.apps.otpvalidator;

import java.util.HashMap;

public abstract class OTPValidator {
	
	/*
	 * Input Key-Values to be retrieved from the Input HashMap 
	 */
	public static final String CARDNUMBER = "CARDNUMBER";
	public static final String OTP = "OTP";
	public static final String USERID = "USERID";
	
	/**
	 * 
	 * @param The package name of the OTP Validator
	 * @param configs
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static OTPValidator getInstance(String otpValidator, HashMap<String, String> configs)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (OTPValidator)Class.forName(otpValidator).newInstance();
	}

	public abstract ValidatorResponse validateOTP(HashMap<String, String> configs);
	
	/**
	 * This method will perform the common validations for All the Validators.
	 * @param configs
	 * @return Generator ConnectorResponse
	 */
	protected ValidatorResponse validate(HashMap<String, String> configs){
		ValidatorResponse response = new ValidatorResponse();
		
		/**
		 * Performing null checks
		 */

		/*
		 * Set Successful result as Null Checks are complete
		 */
		response.setResult(true);
		
		return response;
	}
	
}
