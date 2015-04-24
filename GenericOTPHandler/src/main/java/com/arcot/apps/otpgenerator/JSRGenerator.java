package com.arcot.apps.otpgenerator;

import java.security.SecureRandom;
import java.util.HashMap;

public class JSRGenerator extends OTPGenerator{

	/**
	 * This method will call the parents validate function and can
	 * perform any additional validation specific to the OTP Generator
	 */
	protected GeneratorResponse validate(HashMap<String, String> configs, String otpType, int otpLength){
		GeneratorResponse res = super.validate(configs, otpType, otpLength);
		/*
		 * perform additional validations and modify res if needed
		 */
		return res;
	}
		
	/**
	 * This class will implement the Java Secure Random mode of
	 * OTP Generation.
	 * @author belar03
	 *
	 */
	@Override
	public GeneratorResponse generateOTP(HashMap<String, String> configs, String otpType, int otpLength) {
		/*
		 * Calling the Validate Function to Verify the
		 * presence of necessary inputs like OTP Length,
		 * OTP Type etc.
		 */
		
		String alphaNumericRange = null;
		String alphaRange = null;
		String numericRange = null;
		
		
		
		GeneratorResponse res = validate(configs, otpType, otpLength);
		if(!res.getResult()){
			return res;
		}
		
		/*
		 * Extracting the OTP Length and OTP Type
		 */
		SecureRandom random = new SecureRandom();
		StringBuffer strOTP = new StringBuffer(otpLength);
		
		
		if (ALPHANUMERIC.equalsIgnoreCase(otpType)){
			
			/*
			 * Determine the alphanumeric range
			 */
			alphaNumericRange = readHashMapValue(configs, ALPHANUMERIC_RANGE_SPECIFIED,
					DEFAULT_ALPHANUMERIC_RANGE);
			
			for( int i = 0; i < otpLength; i++ ) 
				strOTP.append( alphaNumericRange.
						charAt(random.nextInt(alphaNumericRange.length())));
		
		}else if (ALPHABETS.equalsIgnoreCase(otpType)){
			
			/*
			 * Determine the Alpha range
			 */
			alphaRange = readHashMapValue(configs, ALPHA_RANGE_SPECIFIED,
					DEFAULT_ALPHA_RANGE);
			
			for( int i = 0; i < otpLength; i++ ) 
				strOTP.append( alphaRange.
						charAt(random.nextInt(alphaRange.length())));
		
		}else if (NUMERIC.equalsIgnoreCase(otpType)){
			
			/*
			 * Determine the Numeric range
			 */
			numericRange = readHashMapValue(configs, NUMERIC_RANGE_SPECIFIED,
					DEFAULT_NUMERIC_RANGE);
			
			for( int i = 0; i < otpLength; i++ ) 
				strOTP.append( numericRange.
						charAt(random.nextInt(numericRange.length())));
		
		}
		
		/*
		 * Setting the GeneratorResponse Object
		 */
		res.setResult(true);
		res.setOtpValue(strOTP.toString());
		res.setLongErrorMessage(res.SUCCESS_LONG_MSG);
		res.setShortErrorMessage(res.SUCCESS_SHORT_MSG);
		
		return res;
	}

}
