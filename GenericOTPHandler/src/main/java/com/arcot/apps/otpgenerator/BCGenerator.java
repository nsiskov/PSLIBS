package com.arcot.apps.otpgenerator;

import java.util.HashMap;

public class BCGenerator extends OTPGenerator{

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
	 * This class will implement the BC mode of
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
		GeneratorResponse res = validate(configs, otpType, otpLength);
		if(!res.getResult()){
			return res;
		}
		
		/*
		 * Perform the HSM OTP Generation
		 */
		
		return res;
	}

}
