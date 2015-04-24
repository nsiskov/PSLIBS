package com.arcot.apps.otpvalidator;

import java.util.HashMap;

public class DummyValidator extends OTPValidator {

	@Override
	public ValidatorResponse validateOTP(HashMap<String, String> configs) {
		ValidatorResponse response = new ValidatorResponse();
		String strOTP = configs.get("OTP");
		if("888888".equalsIgnoreCase(strOTP)){
			response.setResult(true);
			response.setShortErrorMessage("OTPTRUE");
			response.setLongErrorMessage("OTP is Successful");
		}else{
			response.setResult(false);
			response.setShortErrorMessage("[OTPFAILED]");
			response.setLongErrorMessage("OTP Failed");
		}
		return response;
	}

}
