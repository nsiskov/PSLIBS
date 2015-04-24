package com.arcot.apps.callout.acs;

/*
 * This interface defines all the constant values in ACS request and response 
 * */

public interface ACS {
	
	/**
	 * compiled on product tag
	 * Tag - release_xfort7_2 
	 * 
	 */
	public static final String GENERIC_CALLOUT_VERSION = "ps_7.2";
	
	public static final String STATUS_Y = "Y";
	public static final String STATUS_N = "N";
	public static final String STATUS_A = "A";
	public static final String STATUS_U = "U"; 
	
	public static final String VERIFY_PASSWORD_REQUEST = "VerifyPasswordReq";
	public static final String VERIFY_HINT_REQUEST = "VerifyHintsAnswersReq";
	public static final String VERIFY_IA_REQUEST = "VerifyIssuerAnswerReq";
	public static final String UPDATE_CHPROFILE_REQUEST = "UpdateCHProfileReq";
	public static final String POST_AUTH_REQUEST = "PostAuthReq";
	public static final String VERIFY_ENROLLMENT_REQUEST ="VEReq";
	public static final String PAREQ_REQUEST = "GetTransactionPhaseReq";
	public static final String POST_ENROLL_REQUEST = "PostEnrollReq";
	public static final String PRE_PARES_REQUEST = "PrePAResGenReq";
	public static final String GET_FYP_REQUEST = "GetFYPReq";
	
	public static final String VERIFY_PASSWORD_RESPONSE = "VerifyPasswordRes";
	public static final String VERIFY_HINT_RESPONSE = "VerifyHintsAnswersRes";
	public static final String VERIFY_IA_RESPONSE = "VerifyIssuerAnswerRes";
	public static final String UPDATE_CHPROFILE_RESPONSE = "UpdateCHProfileRes";
	public static final String POST_AUTH_RESPONSE = "PostAuthRes";
	public static final String VERIFY_ENROLLMENT_RESPONSE ="VERes";
	public static final String PAREQ_RESPONSE = "GetTransactionPhaseRes";
	public static final String POST_ENROLL_RESPONSE = "PostEnrollRes";
	public static final String PRE_PARES_RESPONSE = "PrePAResGenRes";
	public static final String GET_FYP_RESPONSE = "GetFYPRes";
	
	public static final String TX_TYPE_UNKNOWN = "TX_TYPE_UNKNOWN";
	public static final String TX_TYPE_REGULAR = "TX_TYPE_REGULAR";
	public static final String TX_TYPE_ATTEMPTS = "TX_TYPE_ATTEMPTS";
	public static final String TX_TYPE_AE_WITH_PWD = "TX_TYPE_AE_WITH_PWD";
	public static final String TX_TYPE_AE_WITHOUT_PWD = "TX_TYPE_AE_WITHOUT_PWD";
	public static final String TX_TYPE_FORGOT_PWD = "TX_TYPE_FORGOT_PWD";
	public static final String TX_TYPE_SEC_CH = "TX_TYPE_SEC_CH";
	public static final String TX_TYPE_FORGOT_PWD_MULTI_CH = "TX_TYPE_FORGOT_PWD_MULTI_CH";
	public static final String TX_TYPE_FORGOT_PWD_SINGLE_CH = "TX_TYPE_FORGOT_PWD_SINGLE_CH";
	public static final String TX_TYPE_ABRIDGED_ADS = "TX_TYPE_ABRIDGED_ADS";
	public static final String TX_TYPE_SEC_CH_ABRIDGED = "TX_TYPE_SEC_CH_ABRIDGED";

	
	//Below is required because ACS can send the enum value of TX type in certain requests
	public static final int ENUM_TX_TYPE_UNKNOWN = 0;
	public static final int ENUM_TX_TYPE_REGULAR = 1;
	public static final int ENUM_TX_TYPE_ATTEMPTS = 2;
	public static final int ENUM_TX_TYPE_AE_WITH_PWD = 3;
	public static final int ENUM_TX_TYPE_AE_WITHOUT_PWD = 4;
	public static final int ENUM_TX_TYPE_FORGOT_PWD = 5;
	public static final int ENUM_TX_TYPE_SEC_CH = 6;
	public static final int ENUM_TX_TYPE_FORGOT_PWD_MULTI_CH = 7;
	public static final int ENUM_TX_TYPE_FORGOT_PWD_SINGLE_CH = 8;
	public static final int ENUM_TX_TYPE_ABRIDGED_ADS = 9;
	public static final int ENUM_TX_TYPE_SEC_CH_ABRIDGED = 10;


	public static final String REGULAR_PURCHASE_AUTH_SUCCESS="REGULAR_PURCHASE_AUTH_SUCCESS";
	
	public static final String ACS_SERVLET_NAME = "ACSServletName";
	public static final String ACS_SERVLET_LOGGER = "ACSServletLogger";
	public static final String RUNTIME_REINITILIZATION = "RuntimeReinitilization";

	public static final String SECURE_ACS_SERVLET_NAME = "SecureACSServletName";
	public static final String SECURE_ACS_SERVLET_LOGGER = "SecureACSServletLogger";
	

	/*
	 * CALLOUT RETURN CODES FOR ACS (CRC)
	 * */
	public static final int ACS_CALLOUT_UNKERROR = 0;
	public static final int ACS_CALLOUT_SUCCESS = 1;
	public static final int ACS_CALLOUT_FAIL = 2;
	public static final int ACS_CALLOUT_TIMED_OUT = 3;

	public static final int ACS_CALLOUT_ABORT = 4 ;
	public static final int ACS_CALLOUT_RETRY = 5 ;
	public static final int ACS_CALLOUT_INVALID_DATA = 6;
	public static final int ACS_CALLOUT_BLOCK_CH = 7;
	public static final int ACS_CALLOUT_UNHANDLED = 8;
	public static final int ACS_CALLOUT_ATTEMPTS = 9;
	public static final int ACS_CALLOUT_UNSURE = 10;
	public static final int ACS_CALLOUT_BLOCK_NO_LOCK = 11;
	
	/**
	 * Changed from 6.9 onwards
	 */
	public static final int ACS_CALLOUT_BLOCK_FYP = 12;
	public static final int ACS_CALLOUT_RETRY_NO_COUNT = 13;
	public static final int ACS_CALLOUT_FAIL_NO_NUMTRY_INC = 14;
	public static final int ACS_CALLOUT_SEND_PARES_N = 15;
	/**
	 * Changed from 6.9 onwards
	 */

	
	//new range of values for indicating, different transaction phases
	public static final int ACS_CALLOUT_TX_NO_ACTION = 500;
	public static final int ACS_CALLOUT_TX_OPTIN = 501;
	public static final int ACS_CALLOUT_TX_FORCED = 502;

	//new range of values for indicating, different PARes.TX.status
	//earlier return values indicating some PARes.TX.status are still supported
	public static final int ACS_CALLOUT_PARES_Y	= 1000;
	public static final int ACS_CALLOUT_PARES_A = 1001;
	public static final int ACS_CALLOUT_PARES_U  = 1002;
	public static final int ACS_CALLOUT_PARES_N = 1003 ;
	
	//RiskAdvice value
	public static final int RA_ALLOW = 1;
	public static final int RA_ALERT = 2;
	public static final int RA_INCREASE_AUTH = 3;
	public static final int RA_DENY = 4;
	
	// UserAction that comes in PrePares Generation Callout
	public static final int USER_ACTION_DEF = 0;
	public static final int USER_ACTION_OPTIN = 1;
	public static final int USER_ACTION_DECLINE = 2;
	public static final int USER_ACTION_CANCEL = 3;
	public static final int USER_ACTION_PURCH = 4;
	public static final int USER_ACTION_OPTIN_CANCEL = 5;
	public static final int USER_ACTION_REENROLL = 6;
	
	public static final String INDIA_IVR_EXTENSION_ID = "visa.3ds.india_ivr";
}
