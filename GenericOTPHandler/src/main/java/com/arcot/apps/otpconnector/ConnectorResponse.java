package com.arcot.apps.otpconnector;

/**
 * 
 * @author belar03
 *
 */
public class ConnectorResponse {
	private boolean result = false;
	private String shortErrorMessage = null;
	private String longErrorMessage = null;
	private String emailAddress = null;
	private String mobileNumber = null;
	/*
	 * responseCode will be sent to CAP page. 
	 * 
	 * Based on the responseCode we can show required text on CAP Page. 
	 * Hence we are adding this.
	*/
	private String responseCode = null;
	
	/*
	 * Success Message Static Constants
	 */
	public String SUCCESS_SHORT_MSG = "OTP_DELIVERY_SUCCESS";
	public String SUCCESS_LONG_MSG = "The OTP Delivery Was Successful.";
	
	/*
	 * Short Error Message Static Constants
	 */
	public String MOBILE_NUMBER_EMPTY_SHORT_ERROR = "MOBILE_NUMBER_EMPTY";
	public String OTP_EMPTY_SHORT_ERROR = "OTP_EMPTY";
	public String OTP_TEXT_EMPTY_SHORT_ERROR = "OTP_TEXT_EMPTY";
	public String EMAIL_ADDRESS_EMPTY_SHORT_ERROR = "EMAIL_ADDRESS_EMPTY";
	
	/*
	 * Long Error Message Static Constants
	 */
	public String MOBILE_NUMBER_EMPTY_LONG_ERROR = "The mobile number has not been set correctly.";
	public String OTP_EMPTY_LONG_ERROR = "The OTP is not set correctly.";
	public String OTP_TEXT_EMPTY_LONG_ERROR = "The OTP Text is not set correctly.";
	public String EMAIL_ADDRESS_EMPTY_LONG_ERROR = "The email address has not been set correctly.";
	
	/*
	 * Clickatell Short Error Msgs
	 */
	public String CLICKATELL_END_POINT_EMPTY_SHORT_ERROR = "CLICKATELL_END_POINT_EMPTY";
	public String CLICKATELL_RESPONSE_EMPTY_SHORT_ERROR = "CLICKATELL_RESPONSE_EMPTY";
	public String CLICKATELL_RESPONSE_INCORRECT_FORMAT_SHORT_ERROR = "CLICKATELL_RESPONSE_FORMATTED_INCORRECTLY";
	
	/*
	 * Clickatell Long Error Msgs
	 */
	public String CLICKATELL_END_POINT_EMPTY_LONG_ERROR = "The Clickatell End point URL is not set correctly.";
	public String CLICKATELL_RESPONSE_EMPTY_LONG_ERROR = "The Response from Clickatell was empty.";
	public String CLICKATELL_RESPONSE_INCORRECT_FORMAT_LONG_ERROR = "The Response from Clickatell was incorrectly formatted.";
	
	/*
	 * SSL WIRELESS Short Error Msg
	 */
	public String SSLWLS_END_POINT_EMPTY_SHORT_ERROR = "SSLWLS_END_POINT_EMPTY";
	public String SSLWLS_RESPONSE_EMPTY_SHORT_ERROR = "SSLWLS_RESPONSE_EMPTY";
	public String SSLWLS_RESPONSE_INCORRECT_FORMAT_SHORT_ERROR = "SSLWLS_RESPONSE_FORMATTED_INCORRECTLY";
	public String SSLWLS_LOGIN_FAILED_SHORT_ERROR = "SSLWLS_LOGIN_FAILD_SEND_SMS_FAILED";
	public String SSLWLS_INCORRECT_SID_SHORT_ERROR = "SSLWLS_INCORRECT_SID_SMS_FAILED";
	
	/*
	 * SSL WIRELESS Long Error Msgs
	 */
	public String SSLWLS_END_POINT_EMPTY_LONG_ERROR = "The SSL WIRELESS End point URL is not set correctly.";
	public String SSLWLS_RESPONSE_EMPTY_LONG_ERROR = "The Response from SSL WIRELESS was empty.";
	public String SSLWLS_RESPONSE_INCORRECT_FORMAT_LONG_ERROR = "The Response from SSL WIRELESS was incorrectly formatted.";
	public String SSLWLS_LOGIN_FAILED_LONG_ERROR = "The SSL WIRELESS login faild.";
	public String SSLWLS_INCORRECT_SID_LONG_ERROR = "The SSL WIRELESS Stakeholder ID was incorrect.";
	
	/*
	 * UNICEL Short Error Msg
	 */
	public String UNICEL_USER_NAME_EMPTY_SHORT_ERROR = "[UNICEL_USER_NAME_EMPTY]";
	public String UNICEL_PASSWORD_SHORT_EMPTY_ERROR = "[UNICEL_PASSWORD_SHORT_EMPTY_ERROR]";
	public String UNICEL_END_POINT_EMPTY_SHORT_ERROR = "[UNICEL_END_POINT_EMPTY_SHORT_ERROR]";
	public String UNICEL_NO_RESPONSE_SHORT_ERROR ="[UNICEL_NO_RESPONSE_SHORT_ERROR]";
	public String UNICEL_USERNAME_OR_PASSWORD_INVALID_SHORT  = "[UNICEL_USERNAME_OR_PASSWORD_INVALID_SHORT]";
	public String UNICEL_ACCOUNT_SUSPENDED_SHORT  = "[UNICEL_ACCOUNT_SUSPENDED_SHORT]";
	public String UNICEL_INVALID_SENDERID_SHORT  = "[UNICEL_INVALID_SENDERID_SHORT]";
	public String UNICEL_MSG_LENGTH_EXCEEDED_SHORT  = "[UNICEL_MSG_LENGTH_EXCEEDED_SHORT]";
	public String UNICEL_DRL_URL_NOT_SET_SHORT  = "[UNICEL_DRL_URL_NOT_SET_SHORT";
	public String UNICEL_SERVICE_NOT_ACCESSIBLE_SHORT  = "[UNICEL_SERVICE_NOT_ACCESSIBLE_SHORT]";
	public String UNICEL_INVALID_SOURCE_IP_SHORT  = "[UNICEL_INVALID_SOURCE_IP_SHORT]";
	public String UNICEL_ACCOUNT_EXPIRED_SHORT  = "[UNICEL_ACCOUNT_EXPIRED_SHORT]";
	public String UNICEL_INVALID_MSG_LENGTH_SHORT  = "[UNICEL_INVALID_MSG_LENGTH_SHORT]";
	public String UNICEL_INVALID_PARAMETER_SHORT  = "[UNICEL_INVALID_PARAMETER_SHORT]";
	public String UNICEL_INVALID_DESTINATION_NO_SHORT  = "[UNICEL_INVALID_DESTINATION_NO_SHORT]";
	public String UNICEL_UNKNOWN_ERROR_CODE_SHORT  = "[UNICEL_UNKNOWN_ERROR_CODE_SHORT]";
	public String UNICEL_UNKNOWN_ERROR_SHORT = "[UNICEL_UNKNOWN_ERROR_SHORT]";
	
	/*
	 * UNICEL LONG Error Msg
	 */
	public String UNICEL_USER_NAME_EMPTY_LONG_ERROR = "The UNICEL Username is empty";
	public String UNICEL_PASSWORD_EMPTY_LONG_ERROR = "The UNICEL End Point password is empty";
	public String UNICEL_END_POINT_EMPTY_LONG_ERROR = "The UNICEL End point URL is empty.";
	public String UNICEL_PASSWORD_LONG_EMPTY_ERROR = "UNICEL_PASSWORD_LONG_EMPTY_ERROR";
	public String UNICEL_NO_RESPONSE_LONG_ERROR ="UNICEL_NO_RESPONSE_LONG_ERROR";
	public String UNICEL_USERNAME_OR_PASSWORD_INVALID_LONG  = "UNICEL_USERNAME_OR_PASSWORD_INVALID_LONG";
	public String UNICEL_ACCOUNT_SUSPENDED_LONG  = "UNICEL_ACCOUNT_SUSPENDED_LONG";
	public String UNICEL_INVALID_SENDERID_LONG  = "UNICEL_INVALID_SENDERID_LONG";
	public String UNICEL_MSG_LENGTH_EXCEEDED_LONG  = "UNICEL_MSG_LENGTH_EXCEEDED_LONG";
	public String UNICEL_DRL_URL_NOT_SET_LONG  = "UNICEL_DRL_URL_NOT_SET_LONG";
	public String UNICEL_SERVICE_NOT_ACCESSIBLE_LONG  = "UNICEL_SERVICE_NOT_ACCESSIBLE_LONG";
	public String UNICEL_INVALID_SOURCE_IP_LONG  = "UNICEL_INVALID_SOURCE_IP_LONG";
	public String UNICEL_ACCOUNT_EXPIRED_LONG  = "UNICEL_ACCOUNT_EXPIRED_LONG";
	public String UNICEL_INVALID_MSG_LENGTH_LONG  = "UNICEL_INVALID_MSG_LENGTH_LONG";
	public String UNICEL_INVALID_PARAMETER_LONG  = "UNICEL_INVALID_PARAMETER_LONG";
	public String UNICEL_INVALID_DESTINATION_NO_LONG  = "UNICEL_INVALID_DESTINATION_NO_LONG";
	public String UNICEL_UNKNOWN_ERROR_CODE_LONG  = "UNICEL_UNKNOWN_ERROR_CODE_LONG";
	public String UNICEL_UNKNOWN_ERROR_LONG = "UNICEL_UNKNOWN_ERROR_LONG";
	/*
	 * AMDS Short Error Msgs
	 */
	public String AMDS_SENDER_MSG_ID_EMPTY_SHORT_ERROR = "[AMDS_SENDER_MSG_ID_EMPTY]";
	public String AMDS_END_POINT_EMPTY_SHORT_ERROR = "[AMDS_END_POINT_EMPTY]";
	public String AMDS_CALLING_API_EMPTY_SHORT_ERROR = "[AMDS_CALLING_API_EMPTY]";
	public String AMDS_USER_EMPTY_SHORT_ERROR = "[AMDS_USER_EMPTY]";
	public String AMDS_PASS_EMPTY_SHORT_ERROR = "[AMDS_PASSWORD_EMPTY]";
	public String AMDS_PROVIDER_EMPTY_SHORT_ERROR = "[AMDS_PROVIDER_EMPTY]";
	public String AMDS_CUSTOMID_EMPTY_SHORT_ERROR = "[AMDS_CUSTOMID_EMPTY]";
	public String AMDS_RESPONSE_EMPTY_SHORT_ERROR = "[AMDS_RESPONSE_EMPTY]";
	public String AMDS_EMAIL_PROVIDER_EMPTY_SHORT_ERROR = "AMDS_EMAIL_PROVIDER_EMPTY";
	
	/*
	 * AMDS Long Error Msgs
	 */
	public String AMDS_END_POINT_EMPTY_LONG_ERROR = "The AMDS End point URL is not set correctly.";
	public String AMDS_SENDER_MSG_ID_EMPTY_LONG_ERROR = "The AMDS Sender Msg ID is not set correctly.";
	public String AMDS_CALLING_API_EMPTY_LONG_ERROR = "The AMDS Calling API is not set correctly.";
	public String AMDS_USER_EMPTY_LONG_ERROR = "The AMDS User is not set correctly.";
	public String AMDS_PASS_EMPTY_LONG_ERROR = "The AMDS Password is not set correctly.";
	public String AMDS_PROVIDER_EMPTY_LONG_ERROR = "The AMDS Provider is not set correctly.";
	public String AMDS_CUSTOMID_EMPTY_LONG_ERROR = "The AMDS Custom ID is not set correctly.";
	public String AMDS_RESPONSE_EMPTY_LONG_ERROR = "The Response from AMDS was empty.";
	public String AMDS_EMAIL_PROVIDER_EMPTY_LONG_ERROR = "The AMDS Email Provider is not set correctly.";
	
/*
 * SMTP EMAIL Short Error Msg
 */
	public String EMAIL_HOST_EMPTY_SHORT_ERROR = "EMAIL_HOST_NAME_EMPTY";
	public String EMAIL_HOST_PORT_EMPTY_SHORT_ERROR = "EMAIL_HOST_PORT_EMPTY_ERROR";
	
	/*
	 * SMTP EMAIL Long Error Msg
	 */
	public String EMAIL_HOST_EMPTY_ERROR = "The Email service providers host is not set correctly.";
	public String EMAIL_HOST_PORT_EMPTY_ERROR = "The Email service providers host port is not set correctly.";
	/*
	 * CBQ Short Error Msg
	 * 
	 * */
	public String CBQ_END_POINT_EMPTY_SHORT_ERROR = "[CBQ_END_POINT_EMPTY_SHORT_ERROR]";
	public String TRUST_STORE_PATH_EMPTY_SHORT_ERROR = "[TRUST_STORE_PATH_EMPTY]";
	public String TRUST_STORE_PASSWORD_EMPTY_SHORT_ERROR = "[TRUST_STORE_PASSWORD_EMPTY]";
	public String KEY_STORE_PATH_EMPTY_SHORT_ERROR = "[KEY_STORE_PATH_EMPTY]";
	public String KEY_STORE_PASSWORD_EMPTY_SHORT_ERROR = "[KEY_STORE_PASSWORD_EMPTY]";
	public String WS_USERNAME_EMPTY_SHORT_ERROR = "[WEBSERVICE_USERNAME_EMPTY]";
	public String WS_PASSWORD_EMPTY_SHORT_ERROR = "[WEBSERVICE_PASSWORD_EMPTY]";
	/*
	 * CBQ Long Error Msg
	 * 
	 * */
	public String CBQ_END_POINT_EMPTY_LONG_ERROR = "The End poing is empty";
	public String TRUST_STORE_PATH_EMPTY_LONG_ERROR = "The Truststore path is not provided";
	public String TRUST_STORE_PASSWORD_EMPTY_LONG_ERROR = "The Truststore password is not provided";
	public String KEY_STORE_PATH_EMPTY_LONG_ERROR = "The Keytstore path is not provided";
	public String KEY_STORE_PASSWORD_EMPTY_LONG_ERROR = "The Keystore password is not provided";
	public String WS_USERNAME_EMPTY_LONG_ERROR = "The web service username is not provided";
	public String WS_PASSWORD_EMPTY_LONG_ERROR = "The web service password is not provided";
	
	public String END_POINT_EMPTY_SHORT_ERROR = "[END_POINT_EMPTY_SHORT_ERROR]";
	
	
	public String END_POINT_EMPTY_LONG_ERROR = "The End poing is empty";
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
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	
}
