package com.arcot.apps.callout.common;

import java.util.HashMap;

public class COResponse {

	protected String logToDB = null;
	protected String logToFile = null;
	protected int lockStatus = 0;
	protected boolean result = false;
	protected HashMap additionalCustomerData = null;
	protected int acsCRC = -1;
	protected String ivrMsgRes = null;
	protected String otp = null;
	protected boolean showOtp = false;
	int AuthMethod = -1;

	/*
	 * This field will be used to pass callout message to CAP page
	 * For ES, this can be used in future for purposes such as passing
	 * a customized error string to page when product enhances itself
	 * */
	protected String calloutMessage = null;
	protected String folderId = null;

	public String getCalloutMessage() {
		return calloutMessage;
	}

	public void setCalloutMessage(String calloutMessage) {
		this.calloutMessage = calloutMessage;
	}

	/**
	 * @return Returns the logToDB.
	 */
	public String getLogToDB() {
		return logToDB;
	}

	/**
	 * @param logToDB The logToDB to set.
	 */
	public void setLogToDB(String logToDB) {
		this.logToDB = logToDB;
	}

	/**
	 * @return Returns the logToFile.
	 */
	public String getLogToFile() {
		return logToFile;
	}

	/**
	 * @param logToFile The logToFile to set.
	 */
	public void setLogToFile(String logToFile) {
		this.logToFile = logToFile;
	}

	/**
	 * @return Returns the result.
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @param result The result to set.
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @return Returns the lockStatus.
	 */
	public int getLockStatus() {
		return lockStatus;
	}

	/**
	 * @param lockStatus The lockStatus to set.
	 */
	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}

	/**
	 * @return Returns the additionalCustomerData.
	 */
	public HashMap getAdditionalCustomerData() {
		return additionalCustomerData;
	}

	/**
	 * @param additionalCustomerData The additionalCustomerData to set.
	 */
	public void setAdditionalCustomerData(HashMap additionalCustomerData) {
		this.additionalCustomerData = additionalCustomerData;
	}

	public int getAcsCRC() {
		return acsCRC;
	}

	public void setAcsCRC(int acsCRC) {
		this.acsCRC = acsCRC;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	
	//added for ivr extension by Ranjeet
	public String getIvrMsgRes(){
		return ivrMsgRes;
	}
	public void setIvrMsgRes(String ivrMsg){
		this.ivrMsgRes = ivrMsg;
	}
	public String getOTP() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public boolean isShowOtp() {
		return showOtp;
	}

	/**
	 * @param result The result to set.
	 */
	public void setShowOtp(boolean result) {
		this.showOtp = result;
	}
	/**
	 * @return the authMethod
	 */
	public int getAuthMethod() {
		return AuthMethod;
	}
	/**
	 * @param authMethod the authMethod to set
	 */
	public void setAuthMethod(int authMethod) {
		AuthMethod = authMethod;
	}

}
