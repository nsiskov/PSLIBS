package com.arcot.apps.otpgenerator;

import java.util.HashMap;

public abstract class OTPGenerator {

	/*
	 * Static Final Constants
	 */
	public static final String DEFAULT_GENERATOR = "com.arcot.apps.otpgenerator.JSRGenerator";
	public static final String ALPHABETS = "ALPHABETS";
	public static final String ALPHANUMERIC = "ALPHANUMERIC";
	public static final String NUMERIC = "NUMERIC";
	
	public static String DEFAULT_NUMERIC_RANGE = "0123456789";
	public static String DEFAULT_ALPHANUMERIC_RANGE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static String DEFAULT_ALPHA_RANGE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/*
	 * Input Key-Values to be retrieved from the Input HashMap 
	 */
	public static final String ALPHANUMERIC_RANGE_SPECIFIED = "alphanumeric_range_specified";
	public static final String ALPHA_RANGE_SPECIFIED = "alpha_range_specified";
	public static final String NUMERIC_RANGE_SPECIFIED = "numeric_range_specified";
	public static final String USE_DEFAULT_GENERATOR = "use_default_generator";
	
	/**
	 * 
	 * @param The package name of the OTP Generator
	 * @param configs
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static OTPGenerator getInstance(String otpGenerator, HashMap<String, String> configs)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (OTPGenerator)Class.forName(otpGenerator).newInstance();
	}
	
	public abstract GeneratorResponse generateOTP(HashMap<String, String> configs, String otpType, int otpLength);
	
	/**
	 * This method will perform the common validations for All the Generators.
	 * @param configs
	 * @return Generator ConnectorResponse
	 */
	protected GeneratorResponse validate(HashMap<String, String> configs, String otpType, int otpLength){
		/**
		 * Local Variables for the method
		 */
		GeneratorResponse result = new GeneratorResponse();
		
		/**
		 * Performing null checks
		 */
		if(otpLength == 0){
			/*
			 * The OTP Length has not been set return false for the OTP Generation.
			 */
			return setGeneratorResponse(result, false, result.OTP_LENGTH_ZERO_SHORT_ERROR, result.OTP_LENGTH_ZERO_LONG_ERROR);
		}
		if(otpType == null || "".equals(otpType)){
			/*
			 * The OTP Type has not been set return false for the OTP Generation.
			 */
			return setGeneratorResponse(result, false, result.OTP_TYPE_EMPTY_SHORT_ERROR, result.OTP_TYPE_EMPTY_LONG_ERROR);
		}
		/*
		 * Set Successful result as Null Checks are complete
		 */
		result.setResult(true);
		
		return result;
	}
	

	/**
	 * This method returns the value to be set
	 * for the key or the default value in the absence
	 * of the key from the HashMap.
	 * @param config
	 * @param calloutConfigKey
	 * @param defaultVal
	 * @return Value of the Key
	 */
	public static String readHashMapValue(HashMap<String, String> configs, String hashMapKey, String defaultVal)
	{
		if(configs.get(hashMapKey) != null){
			return configs.get(hashMapKey);
		}
		else{
			return defaultVal;
		}
		
	}
	
	/**
	 * This method will set the Connector Response Object
	 * @param generatorResponse
	 * @param result
	 * @param shortErrorMessage
	 * @param longErrorMessage
	 * @return GeneratorResponse Object
	 */
	protected GeneratorResponse setGeneratorResponse(
			GeneratorResponse generatorResponse, boolean result, String shortErrorMessage, String longErrorMessage) {
		generatorResponse.setResult(result);
		generatorResponse.setShortErrorMessage(shortErrorMessage);
		generatorResponse.setLongErrorMessage(longErrorMessage);
		return generatorResponse;
	}
		
}
