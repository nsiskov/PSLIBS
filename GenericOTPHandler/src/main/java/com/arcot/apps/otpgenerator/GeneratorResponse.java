package com.arcot.apps.otpgenerator;

/**
 * 
 * @author belar03
 *
 */
public class GeneratorResponse {
	private boolean result = false;
	private String shortErrorMessage = null;
	private String LongErrorMessage = null;
	private String otpValue = null;
	
	/*
	 * Short Error Message Static Constants
	 */
	public String OTP_LENGTH_ZERO_SHORT_ERROR = "OTP_LENGTH_ZERO";
	public String OTP_TYPE_EMPTY_SHORT_ERROR = "OTP_TYPE_EMPTY";
	
	/*
	 * Long Error Message Static Constants
	 */
	public String OTP_LENGTH_ZERO_LONG_ERROR = "The OTP Length is not set correctly.";
	public String OTP_TYPE_EMPTY_LONG_ERROR = "The OTP Type is not set correctly.";
	
	/*
	 * Success Message Static Constants
	 */
	public String SUCCESS_SHORT_MSG = "OTP_GENERATION_SUCCESS";
	public String SUCCESS_LONG_MSG = "The OTP Generation Was Successful.";
	
	
	public String getShortErrorMessage() {
		return shortErrorMessage;
	}
	public void setShortErrorMessage(String shortErrorMessage) {
		this.shortErrorMessage = shortErrorMessage;
	}
	public String getLongErrorMessage() {
		return LongErrorMessage;
	}
	public void setLongErrorMessage(String longErrorMessage) {
		LongErrorMessage = longErrorMessage;
	}
	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getOtpValue() {
		return otpValue;
	}
	public void setOtpValue(String otpValue) {
		this.otpValue = otpValue;
	}
	
	
	
}
