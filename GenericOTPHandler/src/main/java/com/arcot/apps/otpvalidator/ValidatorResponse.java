package com.arcot.apps.otpvalidator;

public class ValidatorResponse {
	
	private boolean result = false;
	private String shortErrorMessage = null;
	private String longErrorMessage = null;
	private String mobileNumber = null;
	private String emailAddress = null;
	
	/*
	 * Success Message Static Constants
	 */
	public String SUCCESS_SHORT_MSG = "OTP_VALIDATION_SUCCESS";
	public String SUCCESS_LONG_MSG = "The OTP Validation Was Successful.";
	
	
	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getShortErrorMessage() {
		return shortErrorMessage;
	}
	public void setShortErrorMessage(String shortErrorMessage) {
		this.shortErrorMessage = shortErrorMessage;
	}
	public String getLongErrorMessage() {
		return longErrorMessage;
	}
	public void setLongErrorMessage(String longErrorMessage) {
		this.longErrorMessage = longErrorMessage;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	
}
