package com.arcot.apps.otp.generic;

/**
 * @author belar03
 * @version 1.0
 * @since August 20, 2014
 * 												-------------------------------
 * 												POSSIBLE CALLOUT CONFIGURATIONS 
 * 												-------------------------------
 * 
 * OTP_PER_TRX_PROXY_PAN
 * Datatype 	:: boolean	:Default - false
 * OTP supported per card number					
 * OTP supported per transaction proxy pan			[true]
 * If not configured , OTP will be stored against a single card number.
 *
 * USE_NEW_OTP_ALWAYS
 * Datatype 	:: boolean	:Default - false
 * Reuse OTP										
 * Always create a new OTP							[true]
 * If not configured, we will use the same OTP always.
 * 									
 * NUM_OF_RESENDS
 * Datatype		:: int		
 * Number of times CH can use the 'Resend link'.
 * If not configured, this functionality will NOT be kicked in.
 * 
 * MAX_RESEND_ACTION
 * Datatype 	:: int
 * Do you want to BLOCK(1)/LOCK(2) the CH, after finishing the number of resent clicks
 * 1 - BLOCK
 * 2 - LOCK
 * @refer 'NUM_OF_RESENDS' is configured, this SHOULD be configured.
 * If not configured, we will go ahead and BLOCK it.
 * 
 * TIME_BETWEEN_RESENDS
 * Datatype 	:: int
 * Time in 'milliseconds' between which the RESEND link on page is kept disabled
 * If configured, this should be handled on the page using $$CALLOUT_MSG$$ 
 * If not configured, this value will NOT be passed to the page.
 * 
 * MOBILE_NUMBER_ON_PAGE
 * Datatype 	:: boolean	:Default - false
 * Not to be shown on page							
 * To be shown on page								[true]
 * If not configured, Mobile Number will not be shown on page.
 * 
 * MOBILE_NUMBER_IN_AHA
 * Datatype 	:: boolean	:Default - false
 * Stored in ARISSUERANSWERS				
 * Stored in ARACCTHOLDERAUTH {column MOBILEPHONE}				[true]
 * if not configured, will fetch the number from ARISSUERANSWERS
 * 
 * MOBILE_NUMBER_ONLY
 * Datatype 	:: boolean	:Default - false
 * Should ONLY the mobile number is considered for OTP delivery			
 * ONLY mobile number is considered for OTP delivery			[true]
 * if not configured, we will check for the presence of email address as well.
 * 
 * NO_MOBILE_PRESENT_ACTION
 * Datatype 	:: String
 * The ACS response in the advent of the Mobile Number not present/returned.		
 * Response values to be set in the callout config				[ACS_CALLOUT_PARES_N/ACS_CALLOUT_PARES_U etc.]
 * if not configured, default value is 							[ACS_CALLOUT_PARES_N]
 * 
 * MOBILE_QUES_ID
 * Datatype 	:: int	
 * QUESTION ID of the mobile uploaded
 * This SHOULD be configured is Mobile Number is stored in ARISSUERANSWERS
 * 
 * EMAIL_IN_AHA
 * Datatype 	:: boolean	:Default - false
 * Stored in ARISSUERANSWERS				
 * Stored in ARACCTHOLDERAUTH {column EMAILADDR}				[true]
 * if not configured, will fetch the number from ARISSUERANSWERS
 * 
 * EMAIL_QUES_ID
 * Datatype 	:: int	
 * QUESTION ID of the email uploaded
 * This SHOULD be configured is Email Address stored in ARISSUERANSWERS
 * 
 * NO_EMAIL_PRESENT_ACTION
 * Datatype 	:: boolean	:Default - false
 * The ACS response in the advent of the Email Address not present/returned.		
 * Response values to be set in the callout config				[ACS_CALLOUT_PARES_N/ACS_CALLOUT_PARES_U/ACS_CALLOUT_TX_NO_ACTION]
 * if not configured, default value is 							[ACS_CALLOUT_PARES_N]
 * 
 * EMAIL_ON_PAGE
 * Datatype 	:: boolean	:Default - false
 * Not to be shown on page							
 * To be shown on page								[true]
 * If not configured, Email Address will not be shown on page.
 * 
 * UNENROLLED_FLOW
 * Datatype 	:: String
 * First folder to show for an UnEnrolled ch
 * Possible values
 * 		LANDING
 * 		OTP 
 *
 * ENROLLED_FLOW
 * Datatype 	:: String
 * First folder to show for an Enrolled ch
 * Possible values
 * 		LANDING
 * 		OTP 
 *
 * OTP_LENGTH
 * Datatype 	:: int
 * Length of the OTP to be configured
 *
 * OTP_TYPE
 * Datatype 	:: String
 * Type of OTP to be created
 * 	Possible values
 * 		NUMERIC
 * 		ALPHANUMERIC
 * 		ALPHABETS	
 *
 * NUMERIC_RANGE_SPECIFIED
 * Datatype		:: String
 * List of numerics from where OTP is created
 * This is used to override the existing set {'0123456789'}
 *
 * ALPHANUMERIC_RANGE_SPECIFIED
 * Datatype		:: String
 * List of numerics from where OTP is created
 * This is used to override the existing set {'0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'}
 *
 * ALPHA_RANGE_SPECIFIED
 * Datatype		:: String
 * List of numerics from where OTP is created
 * This is used to override the existing set {'ABCDEFGHIJKLMNOPQRSTUVWXYZ'}
 *
 * OTP_<localename>
 * Datatype		:: String
 * Will contain the name of the OTP foldername against that locale
 *
 * LANDING_<localename>
 * Datatype		:: String
 * Will contain the name of the Landing foldername against that locale
 * 
 * SMS_CONNECTOR
	 * Datatype 	:: string
	 * Example: com.arcot.apps.otpconnector.CATConnector
 *
 * EMAIL_CONNECTOR
	 * Datatype 	:: string
	 * Example: com.arcot.apps.otpconnector.AMDSConnector
 *
 * 
 * MESSAGE_TEXT / EMAIL_TEMPLATE_PATH
 * Datatype 	:: String
 * OTP Message Text to be sent to the Cardholder.
 * PlaceHolders to be used:
 * 
 * ${otp} 					- Otp Value
 * ${last4digits} 			- Last 4 Digits of the CardNumber
 * ${transaction_amt}	  	- Transaction Amount of the CardHolder
 * ${transaction_currency}	- Transaction Currency
 * ${merchant_name}  		- Merchant Name
 * ${bank_name}				- Bank Name
 * ${transaction_time}		- Transaction Time
 * ${cardholder_name} 		- CardHolder Name
 * 
 * TRANSACTION_DATE_FORMAT
 * Datatype 	:: String
 * Format of the Transaction Date to be displayed in the OTP Message.
 * 
 * IGNORE_SMS_DELIVERY_RES
 * Datatype		:: boolean 	:Default - true
 * If true will send ACS_CALLOUT_SUCCESS in Pareq
 * and ACS_CALLOUT_TX_NO_ACTION during Resend.
 * 
 * IGNORE_EMAIL_DELIVERY_RES
 * Datatype		:: boolean 	:Default - true
 * If true will send ACS_CALLOUT_SUCCESS in Pareq
 * and ACS_CALLOUT_TX_NO_ACTION during Resend.
 * 
 * EMAIL_SUBJECT
	 * Datatype 	:: string

 * 
 * Of Course you will have to configure 
 * 
 * LogicHandler
 * AND
 * LogFilePath
 * AND
 * LogLevel {INFO,DEBUG,WARN,ERROR,TRACE,FATAL}
 * 
 * 												--------
 * 												CALLOUTS
 * 												--------
 * 
 * 									----- PAREQ CALLOUT ----
 * 
 * 1. Used if both flows {UNENROLLED_FLOW/ENROLLED_FLOW} are OTP. Will be used to create/send OTP.
 *    
 *    The callout config should have:-
 *    		UNENROLLED_FLOW 	= OTP
 *    		ENROLLED_FLOW	  	= OTP	
 *    
 *    ARDEVICE needs to have the OTP folder against each locale.
 *    
 * 2. Used if one flow is LANDING, and one is OTP {i.e. UNENROLLED_FLOW is LANDING, ENROLLED_FLOW is OTP}.
 *    If it is an enrolled card, it will create/send the OTP and switch folder to respective locale of the CH.
 *    If it is an unenrolled card, it will just show the Landing page for the default Locale
 *    
 *    The callout config should have:-
 *    		UNENROLLED_FLOW 	= LANDING
 *    		ENROLLED_FLOW	  	= OTP
 *          OTP_<localename>    = <OTP_folder_name_for_that_locale>	
 *	  		LANDING_<localename>= <LANDING_folder_name_for_that_locale>	
 *     	
 *     ARDEVICE needs to have the LANDING folder against each locale.
 *     
 * 3. Default Behavior	{UNENROLLED_FLOW & ENROLLED_FLOW is NOT Configured in callout config}
 *    Will always show the Landing Page; if plugged in by mistake. 
 *    
 *    The callout config should have:-
 *          OTP_<localename>    = <OTP_folder_name_for_that_locale>	
 *	  		LANDING_<localename>= <LANDING_folder_name_for_that_locale>	
 *     	
 *    ARDEVICE needs to have the LANDING folder against each locale.
 *       
 * 									----- VIA CALLOUT ----
 * 
 * 	 	--------------------------------
 * 		PIN FORMATS from Page to CALLOUT
 * 		--------------------------------
 * 
 * 1. Change in locale , in a multilocale approach
 *    	LOCALE~<localename>
 * 		Where <localename> corresponds to an entry in ARLOCALE 
 * 
 * 2. Move from landing page to OTP page
 * 		ToOTP~<localename>
 * 		Where <localename> corresponds to an entry in ARLOCALE 
 * 
 * 3. Submission Of OTP
 * 		OTP~<otp value>
 * 		Where <otp value> is the OTP entered by the CH
 * 
 * 4. Resend of OTP
 * 		RESEND
 * 
 * 5. If 'Attempts' is to be supported
 *      NOTNOW
 *      
 * 6. If 'cancel' is to be supported
 * 		CANCEL      
 * 									----- VP CALLOUT ----- 
 * 
 * Achieve the same functionality as is been coded in the VIA Callout.
 * 
 * 
 * 												------------------
 * 												POINTS TO BE NOTED
 * 												------------------
 * 
 * 1. OTP is always stored in the Database, and is encrypted with the the Bank Key.
 * 2. CALLOUTDATA is always stored in format 					:- <OTP_ENCRYPTED_WITH_BANKKEY>~<OTP_GENERATION_TIME_in_ms>~<RESEND_COUNTER>~<MOLBILE_NUMBER>
 * 
 * 3.1 CALLOUT_MSG to OTP page will have format 				:- <TIME_TO_DISABLE_RESENT_LINK>~<MOBILE_NUMBER>~<USER_DEFINED_STRING>
 * 3.2 CALLOUT_MSG on Failure to OTP page will have format		:- <TIME_TO_DISABLE_RESENT_LINK>~<MOBILE_NUMBER>~OTPFAILED~<USER_DEFINED_STRING>
 * 
 * 4. CALLOUT_MSG sent to exit pages + scenario
 *     
 *    NOMOBILEPRESENT			MOBILE NUMBER not found, althou MOBILE_NUMBER_ON_PAGE is turned on.
 *    RESENTEXCEEDED			Number of resent tries exceeded.
 *    OTPTIMEOUT				Validity of OTP expired
 *    
 * 
 *    
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSPARequest;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.acs.ACSResponse;
import com.arcot.apps.callout.acs.ACSXMLHandler;
import com.arcot.apps.callout.common.BusinessLogicHandler;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.COResponse;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.callout.common.RiskFortRules;
import com.arcot.apps.callout.common.RiskFortRules.RA_ACTION;
import com.arcot.apps.callout.es.ES;
import com.arcot.apps.callout.es.ESRequest;
import com.arcot.apps.callout.es.ESResponse;

/*
 * The Connector and ConnectorResponse Classes are present
 * in the package com.arcot.apps.otpconnector as well as
 * com.arcot.apps.connector. Further it will be used from
 * com.arcot.apps.otpconnector. 
 */
import com.arcot.apps.otpconnector.Connector;
import com.arcot.apps.otpconnector.ConnectorResponse;
import com.arcot.apps.otpgenerator.GeneratorResponse;
import com.arcot.apps.otpgenerator.OTPGenerator;
import com.arcot.apps.otpvalidator.OTPValidator;
import com.arcot.apps.otpvalidator.ValidatorResponse;
import com.arcot.callout.CallOutsConfig;
import com.arcot.crypto.CryptoUtil;
import com.arcot.database.DatabaseConnection;
import com.arcot.dboperations.DBHandler;
import com.arcot.dboperations.admin.AdminQueryStrings;
import com.arcot.dboperations.enroll.EnrollOperations;
import com.arcot.util.ArcotException;
import com.arcot.util.BankInfo;
import com.arcot.util.Brand;
import com.arcot.vpas.enroll.EnrollmentCrypto;
import com.arcot.vpas.enroll.cache.ESCache;
import com.arcot.vpas.profile.CardHolderBean;


public class GenericOTPHandler extends DBHandler implements BusinessLogicHandler, ACS, ES {

	/**
	 * USE_NEW_OTP_ALWAYS
	 * Datatype :: boolean:Default - false
	 * Always create a new otp									[true]
	 * Default, if NOT configured reuse the same otp									
	 * 					
	 */
	private static final String USE_NEW_OTP_ALWAYS = "USE_NEW_OTP_ALWAYS";
	/**
	 * NUM_OF_RESENDS
	 * Datatype :: int
	 * number of times resend can be clicked
	 */
	private static final String NUM_OF_RESENDS = "NUM_OF_RESENDS";
	/**
	 * MAX_RESEND_ACTION
	 * Datatype :: int
	 * Do you want to BLOCK(1)/LOCK(2) the CH, after finishing the number of resent clicks
	 * 1 - BLOCK
	 * 2 - LOCK
	 */
	private static final String MAX_RESEND_ACTION = "MAX_RESEND_ACTION";
	/**
	 * OTP_EXPIRY_ACTION
	 * Datatype :: int
	 * Do you want to BLOCK(1)/LOCK(2) the CH, once the OTP has expired.
	 * 1 - BLOCK
	 * 2 - LOCK
	 */
	private static final String OTP_EXPIRY_ACTION = "OTP_EXPIRY_ACTION";
	/**
	 * TIME_BETWEEN_RESENDS
	 * Datatype :: int
	 * Time in milliseconds between two consecutive resends
	 */
	private static final String TIME_BETWEEN_RESENDS = "TIME_BETWEEN_RESENDS";
	
	/** MOBILE_NUMBER_ON_PAGE
	 * Datatype 	:: boolean	:Default - false
	 * To be shown on page									[true]
	 * Default, if not Configured, Not to be shown on page
	 */
	private static final String MOBILE_NUMBER_ON_PAGE = "MOBILE_NUMBER_ON_PAGE";
	
	/** EMAIL_ON_PAGE
	* Datatype 	:: boolean	:Default - false
 	* Not to be shown on page							
 	* To be shown on page								[true]
 	* If not configured, Email Address will not be shown on page.
 	*/
	private static final String EMAIL_ON_PAGE = "EMAIL_ON_PAGE";
	
	/** MOBILE_NUMBER_IN_AHA
	 * Datatype 	:: boolean	:Default - false
	 * Stored in AHA {column MOBILEPHONE}					[true]
	 * Default, if not configured, is Stored in IssuerQuestionAnswerTable
	 */
	private static final String MOBILE_NUMBER_IN_AHA = "MOBILE_NUMBER_IN_AHA";
	
	 /** MOBILE_QUES_ID
	 * Datatype 	:: int	
	 * QUESTION ID of the mobile uploaded
	 * This SHOULD be configured if Mobile Number is stored in ARISSUERANSWERS
	 */	
	private static final String MOBILE_QUES_ID = "MOBILE_QUES_ID";
	
	/**MOBILE_NUMBER_ONLY
	 * Datatype 	:: boolean	:Default - false
	 * Should ONLY the mobile number is considered for OTP delivery	
	 * ONLY mobile number is considered for OTP delivery			[true]
	 * if not configured, we will check for the presence of email address as well.
	 */
	private static final String MOBILE_NUMBER_ONLY = "MOBILE_NUMBER_ONLY";
	
	/**NO_MOBILE_PRESENT_ACTION
	 * Datatype 	:: boolean	:Default - false
	 * The ACS response in the advent of the Mobile Number not present/returned.		
	 * Response values to be set in the callout config				[ACS_CALLOUT_PARES_N/ACS_CALLOUT_PARES_U]
	 * if not configured, default value is 							[ACS_CALLOUT_PARES_N]
	 */
	private static final String NO_MOBILE_PRESENT_ACTION = "NO_MOBILE_PRESENT_ACTION";
	
	/** EMAIL_IN_AHA
	 * Datatype 	:: boolean	:Default - false
	 * Stored in ARISSUERANSWERS				
	 * Stored in ARACCTHOLDERAUTH {column EMAILADDR}				[true]
	 * if not configured, will fetch the number from ARISSUERANSWERS
	 */
	private static final String EMAIL_IN_AHA = "EMAIL_IN_AHA";
	
	/** EMAIL_QUES_ID
	 * Datatype 	:: int	
	 * QUESTION ID of the email uploaded
	 * This SHOULD be configured is Email Address stored in ARISSUERANSWERS
	 */
	private static final String EMAIL_QUES_ID = "EMAIL_QUES_ID";
	
	/** NO_EMAIL_PRESENT_ACTION
	 * Datatype 	:: boolean	:Default - false
	 * The ACS response in the advent of the Email Address not present/returned.		
	 * Response values to be set in the callout config				[ACS_CALLOUT_PARES_N/ACS_CALLOUT_PARES_U etc.]
	 * if not configured, default value is 							[ACS_CALLOUT_PARES_N]
	 */
	private static final String NO_EMAIL_PRESENT_ACTION = "NO_EMAIL_PRESENT_ACTION";
	
	/**
	 * UNENROLLED_FLOW
	 * Datatype :: String
	 * First folder to show for an Unenrolled ch
	 * 	Possible values
	 * 		LANDING
	 * 		OTP 
	 */
	
	private static final String UNENROLLED_FLOW = "UNENROLLED_FLOW";
	/**
	 * ENROLLED_FLOW
	 * Datatype :: String
	 * First folder to show for an Enrolled ch
	 * 	Possible values
	 * 		LANDING
	 * 		OTP 
	 */
	private static final String ENROLLED_FLOW = "ENROLLED_FLOW";
	/**
	 * OTP_VALIDITY_PERIOD
	 * Datatype :: int
	 * Time in milliseconds for the OTP to be valid
	 */
	public static final String OTP_VALIDITY_PERIOD = "OTP_VALIDITY_PERIOD";
	/** IGNORE_SMS_DELIVERY_RES
	 * Datatype		:: boolean 	:Default - true
	 * If true will send ACS_CALLOUT_SUCCESS in Pareq
	 * and ACS_CALLOUT_TX_NO_ACTION during Resend.
	 */
	public static final String IGNORE_SMS_DELIVERY_RES = "IGNORE_SMS_DELIVERY_RES";
	 /** 
	 * IGNORE_EMAIL_DELIVERY_RES
	 * Datatype		:: boolean 	:Default - true
	 * If true will send ACS_CALLOUT_SUCCESS in Pareq
	 * and ACS_CALLOUT_TX_NO_ACTION during Resend.
	 */
	public static final String IGNORE_EMAIL_DELIVERY_RES = "IGNORE_EMAIL_DELIVERY_RES";
	
	private static final String LANDING = "LANDING";
	private static final String OTP = "OTP";
	private static final String NOMOBILEPRESENT = "[NOMOBILEPRESENT]";
	private static final String NOEMAILPRESENT = "[NOEMAILPRESENT]";
	/**
	 * RESEND PIN values:
	 * RESEND: 			The OTP will be delivered through SMS and Email.
	 * RESEND_SMS: 		The OTP will be delivered through SMS alone.
	 * RESEND_EMAIL: 	The OTP will be delivered through Email alone.
	 * RESEND_SMS_EMAIL:The OTP will be delivered through SMS if it fails 
	 * it will de delivered by Email.
	 */
	private static final String RESEND = "RESEND";
	private static final String RESEND_SMS = "RESEND_SMS";
	private static final String RESEND_EMAIL = "RESEND_EMAIL";
	private static final String RESEND_SMS_EMAIL = "RESEND_SMS_EMAIL";
	
	protected static final String RESENTEXCEEDED = "[RESENTEXCEEDED]";
	private static final String OTPEXPIRED = "[OTPEXPIRED]";
	
	
	/**
	 * OTP_LENGTH
	 * Datatype :: int
	 * Length of the OTP to be configured
	 */
	private static final String OTP_LENGTH = "OTP_LENGTH";
	/**
	 * OTP_TYPE
	 * Datatype :: String
	 * Type of OTP to be created
	 * 	Possible values
	 * 		NUMERIC
	 * 		ALPHANUMERIC
	 * 		ALPHABETS	
	 */
	private static final String OTP_TYPE = "OTP_TYPE";
	
	/**
	 * Callout Config Keys used to define the OTP Generator, Connector and Validator.
	 */
	private static final String GENERATOR = "GENERATOR";
	/** SMS_CONNECTOR
	 * Datatype 	:: string
	 * Example: com.arcot.apps.otpconnector.CATConnector
	 */
	private static final String SMS_CONNECTOR = "SMS_CONNECTOR";
	/** EMAIL_CONNECTOR
	 * Datatype 	:: string
	 * Example: com.arcot.apps.otpconnector.AMDSConnector
	 */
	private static final String EMAIL_CONNECTOR = "EMAIL_CONNECTOR";
	private static final String VALIDATOR = "VALIDATOR";
	
	
	public static final String DEFAULT_VALIDATOR = "com.arcot.apps.otp.generic.DBValidator";
	
	/** MESSAGE_TEXT
	 * Datatype 	:: String
	 * OTP Message Text to be sent to the Cardholder.
	 * PlaceHolders to be used:
	 * 
	 * ${otp} 					- Otp Value
	 * ${last4digits} 			- Last 4 Digits of the CardNumber
	 * ${transaction_amt}	  	- Transaction Amount of the CardHolder
	 * ${transaction_currency}	- Transaction Currency
	 * ${merchant_name}  		- Merchant Name
	 * ${bank_name}				- Bank Name
	 * ${transaction_time}		- Transaction Time
	 * ${cardholder_name}		- Cardholder Name
	 */
	 private static final String MESSAGE_TEXT = "MESSAGE_TEXT";
	 
	 private static final String EMAIL_TEMPLATE_PATH = "EMAIL_TEMPLATE_PATH";
	 
	 /** TRANSACTION_DATE_FORMAT
	* Datatype 	:: String
 	* Format of the Transaction Date to be displayed in the OTP Message.
 	*/
	 private static final String TRANSACTION_DATE_FORMAT = "TRANSACTION_DATE_FORMAT";
	 
	 /** EMAIL_SUBJECT
	 * Datatype 	:: string
	 * It could be locale specific also eg. EMAIL_SUBJECT_en_GB.
	*/
	 private static final String EMAIL_SUBJECT = "EMAIL_SUBJECT";
	/**
	 * OTP Message PlaceHolders Static Final Constants
	 */
	private static final String otp = "otp";
	private static final String last4digits = "last4digits";
	private static final String transaction_amt = "transaction_amt";
	private static final String transaction_currency = "transaction_currency";
	private static final String merchant_name = "merchant_name";
	private static final String bank_name = "bank_name";
	private static final String transaction_time = "transaction_time";
	private static final String cardholder_name = "cardholder_name";
	private static final String email_images_path = "email_images_path";
	private static final String card_type = "card_type";
	private static final String DefaultTransDateFormat = "yyyy-MM-dd HH:mm:ss";
	
	/*
	 * ACS Response Codes
	 */
	private static final String pares_N = "ACS_CALLOUT_PARES_N";
	private static final String pares_U = "ACS_CALLOUT_PARES_U";
	private static final String txn_No_Action = "ACS_CALLOUT_TX_NO_ACTION";
	private static final String pares_a = "ACS_CALLOUT_PARES_A";
	private static final String pares_y = "ACS_CALLOUT_PARES_Y";
	private static final String pares_S_N = "ACS_CALLOUT_SEND_PARES_N";
	
	/*
	 * Static Final Constants
	 */
	private static final String ATTEMPTSEXCEEDED = "[ATTEMPTSEXCEEDED]";
	private static final String SMS_DELIVERY_FAILED = "[SMS_DELIVERY_FAILED]";
	private static final String EMAIL_DELIVERY_FAILED = "[EMAIL_DELIVERY_FAILED]";
	private static final String SMS_DELIVERY_SUCCESS = "[SMS_DELIVERY_SUCCESS]";
	private static final String EMAIL_DELIVERY_SUCCESS = "[EMAIL_DELIVERY_SUCCESS]";
	private static final String CANCEL = "[CANCEL]";
	private static final String PINEMPTY = "[PINEMPTY]";
	private static final String CARDLOCKED = "[CARDLOCKED]";
	private static final String RESEND_SUCCESS = "[RESEND_SUCCESS]";
	private static final String RESEND_SMS_SUCCESS = "[RESEND_SMS_SUCCESS]";
	private static final String RESEND_EMAIL_SUCCESS = "[RESEND_EMAIL_SUCCESS]";
	private static final String PASSWORD_MATCH_SUCCESS = "[PASSWORD_MATCH_SUCCESS]";
	private static final String FYP_SUCCESS = "[FYP_SUCCESS]";
	private static final String FYP_FAILED = "[FYP_FAILED]";
	
	private static final String RESEND_FAILURE = "[RESEND_FAILURE]";
	private static final String RESEND_SMS_FAILURE = "[RESEND_SMS_FAILURE]";
	private static final String RESEND_EMAIL_FAILURE = "[RESEND_EMAIL_FAILURE]";
	private static final String PASSWORD_MATCH_FAILURE = "[PASSWORD_MATCH_FAILURE]";
	
	private static final String RA_FLOW_PASSWORD = "PASSWORD";
	private static final String CARDNUMBER_CLEAR = "CARDNUMBER";
	private static final String CARDNUMBER_TYPE = "CARDTYPE";
	public static final String MASKINGSTRING = "X";
	public static final String SEPERATOR = "-";
	public static final String SEND_OTP_ON_FYP = "SEND_OTP_ON_FYP";
	
	
	/*
	 * DB Statements
	*/
	  private static final String getCurrType = "select CURRTYPE from ARCURRENCY where CURRCODE=?";
	  private static final String updateNumDecAHA = "update aracctholderauth set numdeclines=? where bankid = ? and cardnumber = ? and cardholdername=?";
	  private static final String getCardHolderAnswers = "select answer from arissueranswers where pan=? and questionid=? and bankid=?";
	
	/*
	 * Key Values for Callout Msg Values
	 */
	  protected static final String callout_msg = "callout_msg";
	  protected static final String locale = "locale";
	  protected static final String time_period = "time_period";
	  protected static final String mobnum = "mobnum";
	  protected static final String email = "email";
	  protected static final String success_msg = "success_msg";
	  protected static final String error_msg = "error_msg";
	  protected static final String flow = "flow";
	  /*
	   * connector_response is Key for Json object. Content of this key value will content the connector response code 
	   */ 
	  protected static final String connector_response = "connector_response";
	  
	@SuppressWarnings("unchecked")
	public ACSResponse process(ACSRequest request, COConfig config) {
		
		COLogger objLogger = config.getLogger();
		ACSResponse acsResponse = null;
		HashMap<String, Object> additionalCustomerData = null;
		String calloutMessage = null;
		
		if(PRE_PARES_REQUEST.equalsIgnoreCase(request.getMessageType())){
			//TODO: Add a logger to check for Cardholder IP from the request
			objLogger.info(request, "The Pre-Pares Callout has been invoked, checking for Cardlocked state.");
			/*
	         * Check if the Card is Locked else continue the Pre-Pares Processing.
	         */
			additionalCustomerData = new HashMap<String, Object>();
	        if(checkForCardLockedState(request, objLogger)){
	        	additionalCustomerData.put(error_msg, CARDLOCKED);
	        	acsResponse = COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, CARDLOCKED, CARDLOCKED,
	        			ACS.ACS_CALLOUT_PARES_N, getLocaleName(request, objLogger) + CARDLOCKED));
	        	setUIParam(request, config, additionalCustomerData);
    			createCalloutMessage(request, objLogger, acsResponse, additionalCustomerData, calloutMessage);
	        	
	        	return acsResponse;
	        }else{
	        	return performPrePares(request, objLogger);
	        }
		}
		
		if (PAREQ_REQUEST.equalsIgnoreCase(request.getMessageType())){
			
			try {
				RA_ACTION raAction = performRfProcess(request, config);
                if(raAction!=null && raAction.getFlow() == null){
                	
                	/*
                	 * Setting Callout Msg
                	 */
                	additionalCustomerData = new HashMap<String, Object>();
                	additionalCustomerData.put(error_msg, raAction.getCOStatusLog());
                	acsResponse = COUtil.createACSResponse(request, raAction.getAcsRetStatus(), null, raAction.getResult(),
                 		   0, "PAREQ["+raAction.getCOStatusLog()+"]", "PAREQ["+raAction.getCOStatusLog()+"]");
                	setUIParam(request, config, additionalCustomerData);
                	createCalloutMessage(request,objLogger, acsResponse, additionalCustomerData, calloutMessage);
                	
                    return acsResponse;
                }
                else{
                	
                	/*
                	 * Placed a hook to perform any pre-processing by the BLH.
                	 */
                	acsResponse = performPrePareqProcessing(request, config, raAction);
                	if(acsResponse != null && acsResponse.getAcsCRC() != ACS_CALLOUT_SUCCESS){
                       return acsResponse;
                	}else {
                		
                		/*TODO: consider performPrePareqProcessing response
                    	 * Decide to show OTP, PASSWORD and
                    	 * OTP + PASSWORD.
                    	 * Private method to decide on the pareq
                    	 * being called or not based on flow and if card is enrolled.
                    	 */
                		
                		if(acsResponse != null)
                			additionalCustomerData = acsResponse.getAdditionalCustomerData() != null ? acsResponse.getAdditionalCustomerData(): new HashMap<String, Object>();
                		else
                			additionalCustomerData = new HashMap<String, Object>();
                		
                		if(request.getIsAbridged() > 0 && request.getIsAbridged() < 99){
                			objLogger.info(request, "The Card is enrolled.");
                			if(raAction != null){
                				if(RA_FLOW_PASSWORD.equals(raAction.getFlow())){
                    				objLogger.info(request, "The RF Flow set is to display Password alone.");
                    			}else{
                    				objLogger.info(request, "The RF Flow is: " + raAction.getFlow() + ".");
                    				acsResponse = doPareqFlow(request, config, additionalCustomerData);
                    			}
                			}else{
                				objLogger.info(request, "The RF Flow is: OTP.");
                				acsResponse = doPareqFlow(request, config, additionalCustomerData);
                			}
                		}else{
                			objLogger.info(request, "This is an unenrolled card. Performing Pareq.");
                			acsResponse = doPareqFlow(request, config, additionalCustomerData);
                		}
                		/*
                		 * Setting additionalCustomerData HashMap with the required Key-Values pairs
                		 */
                		if(acsResponse != null)
                			additionalCustomerData = acsResponse.getAdditionalCustomerData() != null ? acsResponse.getAdditionalCustomerData(): new HashMap<String, Object>();
                		else
                			additionalCustomerData = new HashMap<String, Object>();
                    	
                    	if(request.getIsAbridged() > 0 && request.getIsAbridged() < 99){
	                    	/*
	                    	 * Set the Flow in the HashMap
	                    	 */
                    		if(raAction != null){
                    			additionalCustomerData.put(flow, raAction.getFlow());
                    		}else{
                    			additionalCustomerData.put(flow, "");
                    		}
                    	}
                    	
                    	/*
                    	 * Placing a hook to record any bank's custom message in callout status during PAreq
                    	 *
                    	 */
                    	String bankCalloutStatus = getBankSpecificCalloutStatus(request,config);
                    	if(bankCalloutStatus != null)
                    		acsResponse.setLogToDB(bankCalloutStatus+acsResponse.getLogToDB());
                    	
                		/*
                		 * Check if the Opt Outs has Exceeded the Max Opt Outs and append
                		 * the callout Msg with "ATTEMPTSEXCEEDED".
                		 */
                    	objLogger.info("Check for additionalCustomerData");
                		setUIParam(request, config, additionalCustomerData);
		        		objLogger.info("additionalCustomerData Size(process): "+additionalCustomerData.size());
	        			createCalloutMessage(request, objLogger, acsResponse, additionalCustomerData, calloutMessage);
	        			return acsResponse;
                	}
                }

			
			} catch (ArcotException e) {
				objLogger.error(request, "doPareqFlow", e);
				return COUtil.createACSResponse(request, ACS_CALLOUT_PARES_N, "", false, 0, "PAREQExp", "PAREQExp");
			}
		
		}else if (VERIFY_IA_REQUEST.equalsIgnoreCase(request.getMessageType())){
			
			/*
			 * Placed a hook to perform any pre-processing by the BLH.
			 */
			acsResponse = performPreVIAProcessing(request, config);
			if(acsResponse != null && acsResponse.getAcsCRC() != ACS_CALLOUT_SUCCESS){
				   return acsResponse;
			}else {
			
					try {
						
						if(acsResponse != null)
                			additionalCustomerData = acsResponse.getAdditionalCustomerData() != null ? acsResponse.getAdditionalCustomerData(): new HashMap<String, Object>();
                		else
                			additionalCustomerData = new HashMap<String, Object>();
						
						acsResponse = doViaFlow(request, config, request.getIssuerAnswer(), additionalCustomerData);
						
						/*
		        		 * Setting additionalCustomerData HashMap with the required Key-Values pairs
		        		 */
		        		additionalCustomerData = acsResponse.getAdditionalCustomerData()!=null ? acsResponse.getAdditionalCustomerData(): new HashMap<String, Object>();
		            	
		   
		        		setUIParam(request, config, additionalCustomerData);
		        		objLogger.info("additionalCustomerData Size(process): "+additionalCustomerData.size());
	        			createCalloutMessage(request, objLogger, acsResponse, additionalCustomerData, calloutMessage);
	        			
	        			
	        			return acsResponse;
						
					} catch (ArcotException e) {
						objLogger.error(request, "doViaFlow", e);
						return COUtil.createACSResponse(request, ACS_CALLOUT_PARES_N, "", false, 0, "ViaExp", "ViaExp");
					}
			}
		
		}else if (VERIFY_PASSWORD_REQUEST.equalsIgnoreCase(request.getMessageType())) {
			
			/*
			 * Placed a hook to perform any pre-processing by the BLH.
			 */
			acsResponse = performPreVPProcessing(request, config);
			
			if(acsResponse != null && acsResponse.getAcsCRC() != ACS_CALLOUT_SUCCESS){
				   return acsResponse;
			}else {
				/*
				 * The Secondary Auth pin is populated only when password is entered by the user on the purchase
				 * screen. The Password validation is done first then if required the OTP validation if the
				 * validation was successful.
				 */
				if(acsResponse != null)
        			additionalCustomerData = acsResponse.getAdditionalCustomerData() != null ? acsResponse.getAdditionalCustomerData(): new HashMap<String, Object>();
        		else
        			additionalCustomerData = new HashMap<String, Object>();
				
					if(request.getSecAuthPin() != null && !"".equals(request.getSecAuthPin())){
						
						/*
						 * If SecAuth alone is populated the flow is PASSWORD, else flow is OTP_PASSWORD
						 */
						
						String flowValue = request.getPin() != null && !"".equals(request.getPin()) ? "OTP_PASSWORD": "PASSWORD";
						
						objLogger.info(request, "The Static Password has been set, proceeding with Validation");
						acsResponse = performPasswordValidation(request, config, request.getSecAuthPin(), additionalCustomerData);
						
						/*
		        		 * Setting additionalCustomerData HashMap with the required Key-Values pairs
		        		 */
		        		additionalCustomerData = acsResponse.getAdditionalCustomerData()!=null ? acsResponse.getAdditionalCustomerData(): new HashMap<String, Object>();
		            	
		        
		            	additionalCustomerData.put(locale, getLocaleName(request, objLogger));
		            
		            	
		            	/*
		            	 * Setting Flow in JSON
		            	 */
		            	additionalCustomerData.put(flow, flowValue);
		            	
		            	if(acsResponse.getAcsCRC() == ACS_CALLOUT_FAIL){
		            		acsResponse.setAdditionalCustomerData(additionalCustomerData);
		            		createCalloutMessage(request, objLogger, acsResponse, additionalCustomerData, calloutMessage);
		            		return acsResponse;
		            	}else{
		            		if(request.getPin() != null && !"".equals(request.getPin())){
		            			objLogger.info(request, "Pin is set, OTP flow present as well.");
		            		     		
			            		
								return performOTPFlow(request, config, objLogger, acsResponse,
		            					acsResponse.getAdditionalCustomerData(), calloutMessage);
		            		}else{
		            			return acsResponse;
		            		}
		            	}
		        		
					}else{
						/*
						 * If Sec Auth is not populated flow is OTP.
						 */
						
						return performOTPFlow(request, config, objLogger, acsResponse,
								additionalCustomerData, calloutMessage);
					}
			}
		}else if (GET_FYP_REQUEST.equalsIgnoreCase(request.getMessageType())) {
			
			objLogger.info(request, "FYP Called:");
        		additionalCustomerData = new HashMap<String, Object>();
        		additionalCustomerData.put(locale, getLocaleName(request, objLogger));
        		
        		acsResponse = processFYP(request,config);
    		//acsResponse = COUtil.createACSResponse(request, ACS_CALLOUT_TX_OPTIN, "", true, 0, FYP_SUCCESS, FYP_SUCCESS);
    		acsResponse.setAdditionalCustomerData(additionalCustomerData);
			createCalloutMessage(request, objLogger, acsResponse,
					additionalCustomerData, calloutMessage);
			return acsResponse;
		}else{
			objLogger.warn(request, "Check the configuration. Unhandled Callout Configured - "+request.getMessageType());
			return COUtil.createACSResponse(request, ACS_CALLOUT_UNHANDLED, null, false, 0,
					request.getMessageType() + " UNHANDLED", "Callout type not supported: " + request.getMessageType());
		
		}
	}

	public String getBankSpecificCalloutStatus(ACSRequest request,
			COConfig config) {
		
		return null;
	}

	/*
	 * Checking if resend and optout exhausted. Also checking for locale
	*/
	public void setUIParam(ACSRequest request, COConfig config, HashMap<String, Object> additionalCustomerData){
		COLogger objLogger = config.getLogger();
		
		String[] strCalloutData = DBValidator.fetchCalloutData(request, config);
		objLogger.info("checkRefValuesforUI");
		// 1. Not increment the counter
		if(strCalloutData != null && strCalloutData.length > 2){
			int updateResendCounter = Integer.parseInt(strCalloutData[2]);
		
			if (!Boolean.valueOf(config.getValue(NUM_OF_RESENDS))){
				if (updateResendCounter >= (config.getIntValue(NUM_OF_RESENDS))){
					objLogger.info("checkRefValuesforUI:"+RESENTEXCEEDED);
					additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
	    					additionalCustomerData.get(error_msg) + "," + RESENTEXCEEDED: RESENTEXCEEDED);
				}
			}
		}
		
		if(isOptOutExceeded(request, config)){
			objLogger.info("checkRefValuesforUI:"+ATTEMPTSEXCEEDED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + ATTEMPTSEXCEEDED: ATTEMPTSEXCEEDED);
		}
		additionalCustomerData.put(locale, getLocaleName(request, objLogger));
		objLogger.info("additionalCustomerData Size: "+additionalCustomerData.size());
		//return additionalCustomerData;
	}
	/**
	 * This method is overriden by the BLH to perform any VP pre processing.
	 * 
	 * @param request
	 * @param config
	 * @return
	 */
	public ACSResponse processFYP(ACSRequest request,
			COConfig config) {
		ACSResponse acsResponce = null;
		COLogger objLogger = config.getLogger();
		HashMap<String, Object> additionalCustomerData = null;
		if(Boolean.valueOf(config.getValue(SEND_OTP_ON_FYP))){
			objLogger.info("SEND_OTP_ON_FYP True");
			//HashMap<String, Object> additionalCustomerData;
			try {
				acsResponce = doPareqFlow(request, config, additionalCustomerData);
				if("1".equals(Integer.toString(acsResponce.getAcsCRC())) || "500".equals(Integer.toString(acsResponce.getAcsCRC()))){
					
					acsResponce.setAcsCRC(ACS_CALLOUT_TX_OPTIN);
					
					objLogger.info("FYP return Success :"+acsResponce.getAcsCRC());
				}
			} catch (ArcotException e) {
				acsResponce = COUtil.createACSResponse(request, ACS_CALLOUT_PARES_N, FYP_FAILED, false, 0, FYP_FAILED, FYP_FAILED);
				e.printStackTrace();
			}
		}else{
		 acsResponce = COUtil.createACSResponse(request, ACS_CALLOUT_TX_OPTIN, "", true, 0, FYP_SUCCESS, FYP_SUCCESS);
		}
		return acsResponce;
	}
	public ACSResponse performPreVPProcessing(ACSRequest request,
			COConfig config) {
		return COUtil.prepareACSResponse(request, COUtil.createCOResponse(true, 0, "", "",
				ACS.ACS_CALLOUT_SUCCESS));
	}

	/**
	 * This method is overriden by the BLH to perform any VIA pre processing.
	 * 
	 * @param request
	 * @param config
	 * @return
	 */
	public ACSResponse performPreVIAProcessing(ACSRequest request,
			COConfig config) {
		return COUtil.prepareACSResponse(request, COUtil.createCOResponse(true, 0, "", "",
				ACS.ACS_CALLOUT_SUCCESS));
	}

	/**
	 * @param request
	 * @param config
	 * @param objLogger
	 * @param acsResponse
	 * @param additionalCustomerData
	 * @param calloutMessage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ACSResponse performOTPFlow(ACSRequest request, COConfig config,
			COLogger objLogger, ACSResponse acsResponse,
			HashMap<String, Object> additionalCustomerData,
			String calloutMessage) {
		try {
			
			if(acsResponse != null)
				acsResponse.setAdditionalCustomerData(additionalCustomerData);
			
			acsResponse = doVPFlow(request, config, additionalCustomerData);
			
			additionalCustomerData = acsResponse.getAdditionalCustomerData()!=null ? acsResponse.getAdditionalCustomerData(): new HashMap<String, Object>();
        	
			setUIParam(request, config, additionalCustomerData);
			createCalloutMessage(request, objLogger, acsResponse, additionalCustomerData, calloutMessage);
			
			return acsResponse;
		} catch (ArcotException e) {
			objLogger.error(request, "doVpFlow", e);
			return COUtil.createACSResponse(request, ACS_CALLOUT_PARES_N, "", false, 0, "VpExp", "VpExp");
		}
	}

	/**
	 * The Static Password Entered by the User is validated
	 * 
	 * @param request
	 * @param config
	 * @param secAuthPin
	 * @return ACSResponse
	 */
	private ACSResponse performPasswordValidation(ACSRequest request,
			COConfig config, String secAuthPin, HashMap<String, Object> additionalCustomerData) {

		COLogger logger = config.getLogger();
		CardHolderBean objTempBean = new CardHolderBean();
		String secCodeDB = null;
		ACSResponse acsResponse = null;
		
		if(additionalCustomerData == null)
			additionalCustomerData = new HashMap<String, Object>();
		
		/*
		 * Populate the Cardholder Bean
		 */
		try {
			EnrollOperations.getCHData(request.getCardNumber(), request.getCardholderName(), request.getBankId(), objTempBean);
			secCodeDB = objTempBean.getSecretCode();
			
			logger.info(request, "The secCodeDB Value: " + secCodeDB + ".");
			logger.info(request, "The CryptoUtil.hashDataB64(secAuthPin) Value: " + CryptoUtil.hashDataB64(secAuthPin) + ".");
			
			if(secCodeDB != null && !"".equals(secCodeDB)){
				logger.info(request, "The Static Password has been retrieved from AHA.");
				if(secAuthPin != null && !"".equals(secAuthPin) && CryptoUtil.hashDataB64(secAuthPin).equals(secCodeDB)){
					logger.info(request, "The Static password has matched.");
					additionalCustomerData.put(success_msg, PASSWORD_MATCH_SUCCESS);
					acsResponse = COUtil.createACSResponse(request, ACS_CALLOUT_SUCCESS, PASSWORD_MATCH_SUCCESS, true, 0, PASSWORD_MATCH_SUCCESS, PASSWORD_MATCH_SUCCESS);
					acsResponse.setAdditionalCustomerData(additionalCustomerData);
					return acsResponse;
				}else{
					logger.info(request, "The Static Password match failed.");
					additionalCustomerData.put(error_msg, PASSWORD_MATCH_FAILURE);
					acsResponse = COUtil.createACSResponse(request, ACS_CALLOUT_FAIL, PASSWORD_MATCH_FAILURE, false, 0, PASSWORD_MATCH_FAILURE, PASSWORD_MATCH_FAILURE);
					acsResponse.setAdditionalCustomerData(additionalCustomerData);
					return acsResponse;
				}
			}else{
				logger.info(request, "The Static Password was not retrieved from AHA.");
				additionalCustomerData.put(error_msg, PASSWORD_MATCH_FAILURE);
				acsResponse = COUtil.createACSResponse(request, ACS_CALLOUT_FAIL, PASSWORD_MATCH_FAILURE, false, 0, PASSWORD_MATCH_FAILURE, PASSWORD_MATCH_FAILURE);
				acsResponse.setAdditionalCustomerData(additionalCustomerData);
				return acsResponse;
			}
		} catch (ArcotException e1) {
			logger.error(request, "ArcotException", e1);
			logger.info(request, "The Static Password was not retrieved from AHA.");
			additionalCustomerData.put(error_msg, PASSWORD_MATCH_FAILURE);
			acsResponse = COUtil.createACSResponse(request, ACS_CALLOUT_FAIL, PASSWORD_MATCH_FAILURE, false, 0, PASSWORD_MATCH_FAILURE, PASSWORD_MATCH_FAILURE);
			acsResponse.setAdditionalCustomerData(additionalCustomerData);
			return acsResponse;
		}
	}

	/**
	 * @param request
	 * @param objLogger
	 * @param acsResponse
	 * @param additionalCustomerData
	 * @param calloutMessage
	 */
	private void createCalloutMessage(ACSRequest request, COLogger objLogger,
			ACSResponse acsResponse,
			HashMap<String, Object> additionalCustomerData,
			String calloutMessage) {
		try {
			calloutMessage = createCalloutMsg(additionalCustomerData);
		} catch (JSONException e) {
			objLogger.error(request, "JSONException", e);
		}
		acsResponse.setCalloutMessage(calloutMessage);
	}

	/**
	 * Returns the Callout Msg to be set on the ACS Response
	 * 
	 * @param additionalCustomerData
	 * @return
	 * @throws JSONException
	 */
	private String createCalloutMsg(
			HashMap<String, Object> additionalCustomerData) throws JSONException {
		JSONObject calloutMsgJsonObject = new JSONObject();
		calloutMsgJsonObject.put(callout_msg, new JSONObject(additionalCustomerData));
		return calloutMsgJsonObject != null ? calloutMsgJsonObject.toString() : null;
	}

	public static void printAdditionalCustomerDataValues(
			HashMap<String, Object> additionalCustomerData, ACSRequest request,
			COLogger objLogger) {
		for (Map.Entry<String, Object> entry : additionalCustomerData.entrySet()) {
			objLogger.info(request,entry.getKey()+" : "+entry.getValue());
		}
	}

	/**
	 * The Number of Opt Outs performed by the Cardholder
	 * is verified against the Max Opt Outs available.
	 * If the Maximum number of Max Opt Outs is reached
	 * set ATTEMPTSEXCEEDED in the calloutMsg.
	 * 
	 * @param request
	 * @param config
	 * @return
	 */
	private boolean isOptOutExceeded(ACSRequest request, COConfig config) {
		boolean isOptOutExceeded = false;
		COLogger logger = config.getLogger();
		
		CardHolderBean objChBean = new CardHolderBean();
		try {
			EnrollOperations.getCHData(request.getCardNumber(), request.getCardholderName(), request.getBankId(), objChBean);
		} catch (ArcotException e) {
			logger.error(request, "isOptOutExceeded(): ArcotException", e);
			return isOptOutExceeded;
		}
		int maxDecline = getMaxDecline(request, config);
		if(objChBean.getNumDeclines() == maxDecline){
			logger.info(request, "The Opt Outs has reached the Max Opt Outs.");
			isOptOutExceeded = true;
			return isOptOutExceeded;
		}else{
			return isOptOutExceeded;
		}
		
	}

	/**
	 * This method is overridden by the BLH if any pre processing
	 * needs to be performed before the CH Details is verified
	 * and OTP is generated and delivered.
	 * 
	 * @param request
	 * @param config
	 * @param RA_ACTION
	 * @return
	 */
	public ACSResponse performPrePareqProcessing(ACSRequest request,
			COConfig config, RA_ACTION raAction) {
		
		return COUtil.prepareACSResponse(request, COUtil.createCOResponse(true, 0, "", "",
				ACS.ACS_CALLOUT_SUCCESS));
	}

	/**
	 * This method will be overridden by the BLH to perform any additional processing
	 * before the Pares if returned.
	 * 
	 * @param request
	 * @param objLogger
	 * @return
	 */
	public ACSResponse performPrePares(ACSRequest request, COLogger objLogger) {
		objLogger.info(request, "The Card is not locked sending Success. Processing Pareq.");
		return COUtil.prepareACSResponse(request, COUtil.createCOResponse(true, 0, "", "",
				ACS.ACS_CALLOUT_PARES_Y));
	}
	
	public RA_ACTION performRfProcess(ACSRequest request, COConfig config) {
		return RiskFortRules.getActionForRiskAdvice(request, config);
	}
	

	/**
	 *  This method is not meant to be overriden by the Business Logic Handler as this encompasses the
	 *  complete flow.
	 *  
	 *  
	 *  PAREQ Callout SHOULD NOT be used if LANDING is required for both UNENROLLED_FLOW & ENROLLED_FLOW Flow.
	 *  In this scenario the Landing Folder will be configured in ARDEVICE table.
	 * 
	 *  PAREQ will be called to deliver the OTP only if UNENROLLED_FLOW/ENROLLED_FLOW flow has OTP configured.
	 *  NO DB query to be made if BOTH the flows are OTP. In ARDEVICE, configure the OTP FOLDER.
	 *  
	 *  DB query to be made only if UNENROLLED_FLOW is LANDING , AND ENROLLED_FLOW is OTP
	 *  
	 *  
	 *     Scenario						ARDEVICE 								CALLOUTCONFIG
	 *  ----------------------------------------------------------------------------------------------------------
	 *  ALWAYS OTP			     <localename>_OTP_folder_name		    PARAMNAME				|		PARAMVALUES
	 *  																---------						-----------	
	 *  					    										UNENROLLED_FLOW 		|		OTP
	 *  																ENROLLED_FLOW			|		OTP
	 *  																OTP_<localename>		|		<localename>_OTP_folder_name
	 *  																
	 *  
	 *  
	 *  UNENROLLED-LANDING		 <localename>_Landing_folder_name		PARAMNAME				|		PARAMVALUES
	 *  ENROLLED- OTP													---------						-----------
	 *  																UNENROLLED_FLOW 		|		LANDING
	 *  																ENROLLED_FLOW			|		OTP
	 *  																OTP_<localename>		|		<localename>_OTP_folder_name
	 *  																LANDING_<localename>	|		<localename>_Landing_folder_name
	 *	
	 *  --------
	 * PAREQ CALLOUT will only show the landing page. this callout is not required in this scenario.
	 * The code has a safe check in place.
	 *  --------
	 *  
	 *  ALWAYS LANDING			 <localename>_Landing_folder_name		PARAMNAME				|		PARAMVALUES
	 *  																---------						-----------
	 *  																OTP_<localename>		|		<localename>_OTP_folder_name
	 *  																LANDING_<localename>	|		<localename>_Landing_folder_name
	 * 
	 * @param request
	 * @param config
	 * @return
	 * @throws ArcotException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	private ACSResponse doPareqFlow(ACSRequest request, COConfig config, HashMap<String, Object> additionalCustomerData) throws ArcotException {
		
		COLogger logger = config.getLogger();
		logger.info(request, "doPareqFlow()");
		String strOTP = null;	
		ACSResponse acsResponse = null;
		COResponse coResponse = null;
        
		/*
         * Check if the Card is Locked else continue the Pareq Processing.
         * Pre-Pareq is called after Pareq hence we need to Check in 
         * Pareq as well to ensure OTP delivery does not occur.
         */
        
        if(checkForCardLockedState(request, logger)){
        	acsResponse = COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, "", "",
        			ACS.ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + CARDLOCKED));
        	
        	
        	
        	additionalCustomerData.put(error_msg, CARDLOCKED);
        	
        	acsResponse.setAdditionalCustomerData(additionalCustomerData);
        	return acsResponse;
        }
		
		GeneratorResponse generatorResponse = new GeneratorResponse();
		
		CardHolderBean objChBean = new CardHolderBean();
		EnrollOperations.getCHData(request.getCardNumber(), request.getCardholderName(), request.getBankId(), objChBean);
		
		logger.info(request, "doPareqFlow():: Flow Configured: " +
				"UNENROLLED_FLOW ["+config.getValue(UNENROLLED_FLOW)+"] ENROLLED_FLOW ["+config.getValue(ENROLLED_FLOW)+"]");
		
		/*
		 * Method call to create OTP
		 */
		try {
			generatorResponse = createOTP(config, request, config.getValue(OTP_TYPE), config.getIntValue(OTP_LENGTH));
		} catch (InstantiationException e) {
			logger.error(request, "InstantiationException", e);
			return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "InstantiationException",
					"InstantiationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
			
		} catch (IllegalAccessException e) {
			logger.error(request, "IllegalAccessException", e);
			return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "IllegalAccessException",
					"IllegalAccessException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
		} catch (ClassNotFoundException e) {
			logger.error(request, "ClassNotFoundException", e);
			return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "ClassNotFoundException",
					"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
		}
		
		
		/*
		 * Verifying the Generator's Result, if False we will return with Pares of N.
		 */
		if(!generatorResponse.getResult()){
			/*
			 * The OTP Generation Failed.
			 */
			return COUtil.prepareACSResponse(request,generatorFailureResponse(generatorResponse));
		}
		if(generatorResponse.getOtpValue()!=null){
			strOTP = generatorResponse.getOtpValue();
		}else{
			strOTP = "";
			
		}
		
		if (OTP.equalsIgnoreCase(config.getValue(UNENROLLED_FLOW)) &&
				OTP.equalsIgnoreCase(config.getValue(ENROLLED_FLOW))){
			
			/**
			 * in ARDEVICE, configure the OTP FOLDER.
			 * NO need to pass the Folder ID here
			 */
			
			logger.info(request, "doPareqFlow():: ALWAYS_OTP - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			
			try {
				
				coResponse = sendOTP(config, request, strOTP, 0, objChBean, false, additionalCustomerData);				
				acsResponse = COUtil.prepareACSResponse(request, coResponse);				
				acsResponse.setAdditionalCustomerData(coResponse.getAdditionalCustomerData());
				return acsResponse;
			} catch (InstantiationException e) {
				logger.error(request, "InstantiationException", e);
				return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "InstantiationException",
						"InstantiationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
			} catch (IllegalAccessException e) {
				logger.error(request, "IllegalAccessException", e);
				return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "IllegalAccessException",
						"IllegalAccessException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
			} catch (ClassNotFoundException e) {
				logger.error(request, "ClassNotFoundException", e);
				return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "ClassNotFoundException",
						"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
			}
		
		}else if (LANDING.equalsIgnoreCase(config.getValue(UNENROLLED_FLOW)) &&
				OTP.equalsIgnoreCase(config.getValue(ENROLLED_FLOW))){
			
			/**
			 * in ARDEVICE, configure the LANDING FOLDER {1 against each locale}
			 * FOLDERID needs to be passed for Enrolled Flow ONLY
			 * 
			 * Callout Config should have name as OTP_<localename> which contains the foldername
			 * e.g :- OTP_en_GB
			 *        OTP_en_US
			 *        ..
			 *        .
			 */
			
			if (objChBean.getIsAbridgedRegistration() < 100){

				logger.info(request, "doPareqFlow():: ENROLLED_FLOW(OTP) - ToFolder - "+
				config.getValue("OTP_"+COUtil.getLocaleName(Integer.toString(objChBean.getLocaleId()))));
				
				try {
					coResponse = sendOTP(config, request, strOTP, 0, objChBean, false, additionalCustomerData);					
					acsResponse =  COUtil.prepareACSResponse(request, COUtil.prepareACSResponse(request, coResponse, 
							COUtil.getFolderId(config.getValue("OTP_"+COUtil.getLocaleName(Integer.toString(objChBean.getLocaleId()))))));
					acsResponse.setAdditionalCustomerData(coResponse.getAdditionalCustomerData());
					return acsResponse;
				} catch (InstantiationException e) {
					logger.error(request, "InstantiationException", e);
					return COUtil.prepareACSResponse(request,COUtil.createCOResponse(true, 0, "InstantiationException",
							"InstantiationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
				} catch (IllegalAccessException e) {
					logger.error(request, "IllegalAccessException", e);
					return COUtil.prepareACSResponse(request,COUtil.createCOResponse(true, 0, "IllegalAccessException",
							"IllegalAccessException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
				} catch (ClassNotFoundException e) {
					logger.error(request, "ClassNotFoundException", e);
					return COUtil.prepareACSResponse(request,COUtil.createCOResponse(true, 0, "ClassNotFoundException",
							"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
				}

			}else{
				
				logger.info(request, "doPareqFlow():: UNENROLLED_FLOW(LANDING) - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
				return COUtil.createACSResponse(request, ACS_CALLOUT_SUCCESS, "", true, 0, "[L]", "[L]");
			
			}
			
		}else {
			// nothing is configured(i.e UNENROLLED_FLOW & ENROLLED_FLOW is missing from callout config), 
			// and PAREQ is plugged in by mistake, just return a success
			
			// in this scenario we should assume that we always show the LANDING PAGE
			logger.info(request, "doPareqFlow():: ALWAYS_LANDING - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			acsResponse = COUtil.createACSResponse(request, ACS_CALLOUT_SUCCESS, "", true, 0, "[L]", "[L]");
			acsResponse.setAdditionalCustomerData(additionalCustomerData);
			logger.info(request, "acsResponse.getAdditionalCustomerData(): "+acsResponse.getAdditionalCustomerData());
			return acsResponse;
		}
	}
	
	/**
	 * This method will return the locale Name to be used on the cap page to get the relevant string.
	 * @param request
	 * @param logger
	 * @return
	 */
	public static String getLocaleName(ACSRequest request, COLogger logger){
		CardHolderBean objChBean = new CardHolderBean();
		try {
			EnrollOperations.getCHData(request.getCardNumber(), request.getCardholderName(), request.getBankId(), objChBean);
		} catch (ArcotException e) {
			logger.error(request, "ArcotException", e);
			return null;
		}
		
		return COUtil.getLocaleName(Integer.toString(objChBean.getLocaleId()));
	}

	/**
	 * This method will verify if the card is locked, if yes we will not proceed with the
	 * Pareq Callout.
	 * @param ACSRequest
	 * @param COLogger
	 */
	public boolean checkForCardLockedState(ACSRequest request, COLogger logger) {
		String encCardNumber = "";
		boolean isCardLocked = false;
		
		try {
            /*
             * If the card is locked do not do any processing.
             */
            encCardNumber = EnrollmentCrypto.encrypt(request.getCardNumber(), request.getBankId());      
            if(EnrollOperations.isCardLocked(encCardNumber)){
                  logger.info(request, "The card is locked hence sending Pares of N.");
                  isCardLocked = true;
            }                    
            
	     } catch (ArcotException e) {
	     	/*
	     	 * Proceeding even though we have received an exception.
	     	 */
	     	logger.error(request, "ArcotException", e);
	     }
		return isCardLocked;
		
	}

	/**
	 * This will be invoked to achieve VIA/VP flow, it is not meant to be overriden by the Business Logic Handler. 
	 * 
	 * @param request
	 * @param config
	 * @param pinFrmPage - populate this IF called from 'doVPFlow()'; 
	 * 					   ELSE send in as null
	 * 
	 * PIN FORMATS
	 * -----------
	 * 
	 * 1. Change in locale , in a multilocale approach
	 * LOCALE~<localename>
	 * <localename> corresponds to an entry in ARLOCALE 
	 * 
	 * 2. Move from landing page to OTP page
	 * ToOTP~<localename>
	 * <localename> corresponds to an entry in ARLOCALE 
	 * 
	 * 3. Submission Of OTP
	 * OTP~<otp value>
	 * 
	 * 4. resend of OTP
	 * RESEND
	 * 
	 * 5. If 'Attempts' is to be supported
	 * NOTNOW
	 * 
	 * 6. If 'cancel' is to be supported
	 * CANCEL
	 *      
	 * @return
	 * @throws ArcotException 
	 */
	@SuppressWarnings("unchecked")
	private ACSResponse doViaFlow(ACSRequest request, COConfig config, String pinFrmPage, HashMap<String, Object> additionalCustomerData) throws ArcotException {
		
		COLogger logger = config.getLogger();
		logger.info(request, "doViaFlow()");
		ACSResponse acsResponse = null;
		COResponse coResponse = null;
		
		if (pinFrmPage == null){
			logger.warn(request, "PIN FROM PAGE IS EMPTY !");
			acsResponse = COUtil.prepareACSResponse(request, COUtil.createACSResponse(request, ACS_CALLOUT_PARES_N,"", true, 0, PINEMPTY, PINEMPTY, request.getFolderId()));
			//additionalCustomerData = new HashMap<String, Object>();
			
			additionalCustomerData.put(error_msg, PINEMPTY);
			acsResponse.setAdditionalCustomerData(additionalCustomerData);
			return acsResponse;
		}
		
		/**
		 * Good Practice:-
		 * 
		 * Fixed length checks done explicitly so that we know 
		 * which part of the PIN needs to be taken into consideration.
		 *
		 */
		
		logger.trace(request, "doViaFlow():: PIN_FROM_PAGE - "+pinFrmPage);
		
		CardHolderBean objChBean = new CardHolderBean();
		EnrollOperations.getCHData(request.getCardNumber(), request.getCardholderName(), request.getBankId(), objChBean);
		
		/*
		 * The AHA localeid value is not being modified here this will be done in the "Bankdirname.json" file with locale
		 * key-value pairs. Eg: en_US = {content}; en_GB = {content};.
		 */
		if (pinFrmPage.contains("LOCALE~")){
			
			String localeToSwitch = pinFrmPage.substring(pinFrmPage.indexOf("LOCALE~")+7, pinFrmPage.length());
			
			/*
			 * Change the localeID in AHA
			 */
			if(!updateLocaleInAHA(config, localeToSwitch, request)){
				logger.info(request, "The LocaleID was not updated in AHA, performing folder switch only.");
			}
			
			if ((OTP.equalsIgnoreCase(config.getValue(UNENROLLED_FLOW)) && OTP.equalsIgnoreCase(config.getValue(ENROLLED_FLOW))) ||
					(OTP.equalsIgnoreCase(config.getValue(ENROLLED_FLOW)))){
					
				
				logger.info(request, "doViaFlow():: OTP - ToFolder - "+config.getValue("OTP_"+localeToSwitch));
				return COUtil.prepareACSResponse(request, COUtil.createACSResponse(request, ACS_CALLOUT_TX_NO_ACTION, "", true, 0,
						"LocaleOTP["+localeToSwitch+"]", "Locale["+localeToSwitch+"]", 
					COUtil.getFolderId(config.getValue("OTP_"+localeToSwitch))));
			
			}else{
				
				logger.info(request, "doViaFlow():: LANDING - ToFolder - "+config.getValue("LANDING_"+localeToSwitch));
				return COUtil.prepareACSResponse(request, COUtil.createACSResponse(request, ACS_CALLOUT_TX_NO_ACTION, "", true, 0,
						"LocaleLanding["+localeToSwitch+"]", "Locale["+localeToSwitch+"]", 
						COUtil.getFolderId(config.getValue("LANDING_"+localeToSwitch))));
				
			}
		
		}else if (pinFrmPage.contains("ToOTP~")){
			logger.trace(request, "pinFrmPage - "+pinFrmPage);
			
			String localeToSwitch = getLocale(logger, pinFrmPage);
			logger.info(request, "localeToSwitch - "+localeToSwitch);
			logger.info(request, "doViaFlow():: LANDING To OTP - ToFolder - "+config.getValue("OTP_"+localeToSwitch));

			/*
			 * Change the localeID in AHA
			 */
			if(!updateLocaleInAHA(config, localeToSwitch, request)){
				logger.info(request, "The LocaleID was not updated in AHA, performing folder switch only.");
			}			
			
			coResponse = moveToOTPPage(request, config, localeToSwitch, objChBean, additionalCustomerData);
			acsResponse =  COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request, 
					coResponse, 
					COUtil.getFolderId(config.getValue("OTP_"+localeToSwitch))));
			acsResponse.setAdditionalCustomerData(coResponse.getAdditionalCustomerData());
			return acsResponse;
		
		}else if (pinFrmPage.contains("OTP~")){
			
			String strOTP = pinFrmPage.substring(pinFrmPage.indexOf("OTP~")+4, pinFrmPage.length());
			logger.info(request, "doViaFlow():: OTP Validation - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			coResponse = validateOTP(request, config, strOTP, objChBean, additionalCustomerData);
			acsResponse =    COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request,
					coResponse,
					 request.getFolderId()));
			
			additionalCustomerData = coResponse.getAdditionalCustomerData();
			
			//Method to provide any additional callout message data to the cap page in form of json.
			HashMap additionalCustomerDataFromBank = getAdditionalCustomerDataFromBank(request,config);
			
			if(additionalCustomerDataFromBank != null)
			{
				logger.debug(request, "Adding additionalCustomerDataFromBank to callout message");
				additionalCustomerData.putAll(additionalCustomerDataFromBank);
			}
			acsResponse.setAdditionalCustomerData(additionalCustomerData);
			
			logger.info(acsResponse.getCalloutMessage());
			return acsResponse;
			
		}else if (pinFrmPage.contains(RESEND_SMS)){
			
			logger.info(request, "doViaFlow():: RESEND_SMS - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			RA_ACTION raAction = performRfProcess(request, config);
			coResponse = resendOTP(request, config, RESEND_SMS, additionalCustomerData);
			additionalCustomerData = coResponse.getAdditionalCustomerData();
			
			if(additionalCustomerData != null && additionalCustomerData.get(success_msg) != null
					&& SMS_DELIVERY_SUCCESS.equals(additionalCustomerData.get(success_msg))){
				
				additionalCustomerData.put(success_msg, additionalCustomerData.get(success_msg) != null?
						additionalCustomerData.get(success_msg) + "," + RESEND_SMS_SUCCESS: RESEND_SMS_SUCCESS);
				
			}else if(additionalCustomerData != null && additionalCustomerData.get(error_msg) != null
					&& SMS_DELIVERY_FAILED.equals(additionalCustomerData.get(error_msg))){
				additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
						additionalCustomerData.get(error_msg) + "," + RESEND_SMS_FAILURE: RESEND_SMS_FAILURE);
			}
			if(raAction!=null && raAction.getFlow() != null){
				additionalCustomerData.put(flow, raAction.getFlow());
			}
			acsResponse =   COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request,
					coResponse,
					 request.getFolderId()));	
			acsResponse.setAdditionalCustomerData(coResponse.getAdditionalCustomerData());
			return acsResponse;
		
		}else if (pinFrmPage.contains(RESEND_EMAIL)){
			
			logger.info(request, "doViaFlow():: RESEND_EMAIL - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			RA_ACTION raAction = performRfProcess(request, config);
			coResponse = resendOTP(request, config, RESEND_EMAIL, additionalCustomerData);
			additionalCustomerData = coResponse.getAdditionalCustomerData();
			
			if(additionalCustomerData != null && additionalCustomerData.get(success_msg) != null
					&& EMAIL_DELIVERY_SUCCESS.equals(additionalCustomerData.get(success_msg))){
				
				additionalCustomerData.put(success_msg, additionalCustomerData.get(success_msg) != null?
						additionalCustomerData.get(success_msg) + "," + RESEND_EMAIL_SUCCESS: RESEND_EMAIL_SUCCESS);
				
			}else if(additionalCustomerData != null && additionalCustomerData.get(error_msg) != null
					&& EMAIL_DELIVERY_FAILED.equals(additionalCustomerData.get(error_msg))){
				additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
						additionalCustomerData.get(error_msg) + "," + RESEND_EMAIL_FAILURE: RESEND_EMAIL_FAILURE);
				
			}
			
			if(raAction!=null && raAction.getFlow() != null){
				additionalCustomerData.put(flow, raAction.getFlow());
			}
			acsResponse =    COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request,
					coResponse,
					 request.getFolderId()));
			acsResponse.setAdditionalCustomerData(coResponse.getAdditionalCustomerData());
			return acsResponse;
		
		}else if (pinFrmPage.contains(RESEND)){
			
			logger.info(request, "doViaFlow():: RESEND - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			RA_ACTION raAction = performRfProcess(request, config);
			coResponse = resendOTP(request, config, null, additionalCustomerData);
			additionalCustomerData = coResponse.getAdditionalCustomerData();
			
			if(additionalCustomerData != null && additionalCustomerData.get(success_msg) != null
					&& (additionalCustomerData.get(success_msg).toString().indexOf(SMS_DELIVERY_SUCCESS) >= 0 ||
					additionalCustomerData.get(success_msg).toString().indexOf(EMAIL_DELIVERY_SUCCESS) >= 0)){
				
				additionalCustomerData.put(success_msg, additionalCustomerData.get(success_msg) != null?
						additionalCustomerData.get(success_msg) + "," + RESEND_SUCCESS: RESEND_SUCCESS);
				
			}else if(additionalCustomerData != null && additionalCustomerData.get(error_msg) != null
					&& (additionalCustomerData.get(error_msg).toString().indexOf(SMS_DELIVERY_FAILED) >= 0 ||
					additionalCustomerData.get(error_msg).toString().indexOf(EMAIL_DELIVERY_FAILED) >= 0)){
				additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
						additionalCustomerData.get(error_msg) + "," + RESEND_FAILURE: RESEND_FAILURE);
			}
			if(raAction!=null && raAction.getFlow() != null){
				additionalCustomerData.put(flow, raAction.getFlow());
			}
			acsResponse =   COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request,
					coResponse,
					 request.getFolderId()));		
			acsResponse.setAdditionalCustomerData(additionalCustomerData);
			return acsResponse;
		
		}else if (pinFrmPage.contains("NOTNOW")){
			
			logger.info(request, "doViaFlow():: NOTNOW - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			coResponse = attemptsProcessing(request, config, objChBean);
			acsResponse =  COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request,
					coResponse,
					 request.getFolderId()));
			acsResponse.setAdditionalCustomerData(coResponse.getAdditionalCustomerData());
			return acsResponse;
			
		}else if (pinFrmPage.contains("CANCEL")){
			
			logger.info(request, "doViaFlow():: CANCEL - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			coResponse = cancelProcessing(request, config);
			acsResponse=  COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request,
					coResponse,
					 request.getFolderId()));	
			acsResponse.setAdditionalCustomerData(coResponse.getAdditionalCustomerData());
			return acsResponse;
			
		}else{
			
			logger.warn(request, "Unrecognized Pin Rcvd - "+pinFrmPage);
			return COUtil.prepareACSResponse(request, 
					COUtil.createCOResponse(false, 0, "UnknownPin", "UnknownPin - "+pinFrmPage, ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
		}
	}
	
	public HashMap getAdditionalCustomerDataFromBank(ACSRequest request,
			COConfig config) {
		return null;
	}

		
	public String getLocale(COLogger logger, String pinFrmPage){
		String locale="";
		
		locale = pinFrmPage.substring(pinFrmPage.indexOf("ToOTP~")+6, pinFrmPage.length());
		
		return locale;
	}
	
	/**
	 * Invoked in case we click on Not Now
	 * to be overwritten in case you want to send a different PARES Status
	 * 
	 * @param request
	 * @param config
	 * @param CardHolderBean
	 * @return
	 */
	public COResponse attemptsProcessing(ACSRequest request, COConfig config, CardHolderBean objChBean) {
		COLogger logger = config.getLogger();
		logger.info(request, "attemptsProcessing()");
		
		HashMap<String, Object> additionalCustomerData = new HashMap<String, Object>();
		COResponse coResponse = null;
		
		/*
		 * Check for the number of attempts available
		 * Send N if it exceeds the Max Attempts.
		 */
		if(objChBean.getNumDeclines() == getMaxDecline(request, config)){
			logger.info(request, "The NumDeclines equals the Maxdecline, send Pares of N.");
			coResponse =  COUtil.prepareACSResponse(request, 
					COUtil.createCOResponse(true, 0, ATTEMPTSEXCEEDED, ATTEMPTSEXCEEDED, ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + ATTEMPTSEXCEEDED, 
					request.getFolderId()));
			additionalCustomerData.put(error_msg, ATTEMPTSEXCEEDED);
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		}else{
		
			logger.info(request, "The NumDeclines has not exceeded the Maxdecline, sending Attempts. Incrementing the NumDeclines");
			
			/*
			 * Increment the Num Declines
			 */
			if(incrementNumDec(request,config, objChBean)){
				
				logger.info(request, "The Numdeclines Count is:" + objChBean.getNumDeclines());
				
			}else{
				logger.info(request, "The NumDeclines was not incremented sending Attempts.");
			}
			
			return COUtil.prepareACSResponse(request, 
					COUtil.createCOResponse(true, 0, "NOTNOW", "NOTNOW", ACS_CALLOUT_ATTEMPTS, setCalloutMsgForAttempts(request, config)), 
					request.getFolderId());
		}
	}
	
	/**
	 * The Numdeclines is incremented.
	 * @param request
	 * @param config
	 * @param CardholderBean
	 * @return Result
	 */
	private boolean incrementNumDec(ACSRequest request, COConfig config, CardHolderBean objChBean){
		COLogger logger = config.getLogger();
		int numDecline = 0;
		
		numDecline = objChBean.getNumDeclines();
		numDecline = numDecline + 1;
		/*
		 * Call DB method to increment NumDecline
		 */
		if(updateNumDec_AHA(request,config,numDecline)){
			logger.info(request, "The NumDeclines was Incremented.");
			return true;
		}
		else{
			logger.info(request, "The NumDeclines did not get Incremented.");
			return false;
		}
		
		
	}
	
	/**
	 * The Numdeclines is incremented by making a DB call.
	 * @param request
	 * @param config
	 * @param numDecLine
	 * @return
	 */
	private boolean updateNumDec_AHA(ACSRequest request, COConfig config,
			int numDecLine) {
		COLogger logger = config.getLogger();
		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
				
		String encCardNumber = EnrollmentCrypto.encrypt(request.getCardNumber(), request.getBankId());
		String encCardHolderName = EnrollmentCrypto.encrypt("", request.getBankId());
			try {
				conn = dbMan.getConnection();
				if (conn != null && !conn.isClosed()) {

					stmt = conn.prepareStatement(updateNumDecAHA);
					stmt.setInt(1, numDecLine);
					stmt.setInt(2, request.getBankId());
					stmt.setString(3, encCardNumber);
					stmt.setString(4, encCardHolderName);
					
					rs = stmt.executeQuery();
					
				} else {
					logger.info(request, "Could not get DB connection");
					return false;
				}
			} catch (SQLException sqle) {
				try {
					if (dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
						return false;
				} catch (SQLException sqle1) {
					logger.error(request, "updateNumDec_AHA: SQLException", sqle1);
					return false;
				}

				// DB down
				logger.error(request, "SQL Error in updateNumDec_AHA", sqle);
				return false;
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException sqle) {
					logger.error(request, "updateNumDec_AHA: SQLException", sqle);
				}
				if (conn != null)
					dbMan.release(conn);
			}	
		return true;
	}
	
	
	
	/**
	 * Invoked in case we click on Cancel
	 * to be overwritten in case you want to send a different PARES Status
	 * 
	 * @param request
	 * @param config
	 * @return
	 */
	public COResponse cancelProcessing(ACSRequest request, COConfig config) {
		COLogger logger = config.getLogger();
		logger.info(request, "cancelProcessing()");
		HashMap<String, Object> additionalCustomerData = new HashMap<String, Object>();
		COResponse coResponse = null;
		
		coResponse =  COUtil.prepareACSResponse(request, 
				COUtil.createCOResponse(true, 0, CANCEL, CANCEL, ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + setCalloutMsgForCancel(request, config)), 
				request.getFolderId());
		additionalCustomerData.put(error_msg, CANCEL);
		coResponse.setAdditionalCustomerData(additionalCustomerData);
		return coResponse;
	}

	/**
	 * Resend functionality will only be applicable for DBValidator.
	 * If we are generating the OTP and storing in the DB we can resend an existing
	 * OTP.
	 * 
	 * @param request
	 * @param config
	 * @param resendType [The Resend Type could be empty(null) in which case we will resend
	 * via SMS and or Email, RESEND_SMS will resend ONLY via SMS and RESEND_EMAIL will
	 * resend ONLY via EMAIL.
	 * @return
	 */
	public COResponse resendOTP(ACSRequest request, COConfig config, String resendType, HashMap<String, Object> additionalCustomerData) {
		
		COLogger logger = config.getLogger();
		GeneratorResponse generatorResponse = new GeneratorResponse();
		String strOTP = null;
		boolean isGenerateNewOTP = false;
		StringBuffer logToDB = new StringBuffer();
		StringBuffer logToFile = new StringBuffer();
		List<String> calloutData = new ArrayList<String>();
		long currentTime = System.currentTimeMillis();
		if(additionalCustomerData == null)
		additionalCustomerData = new HashMap<String, Object>();
		
		
		logger.info(request, "resendOTP()");
		
		// 1. get the OTP from calloutData
		String[] strCalloutData = DBValidator.fetchCalloutData(request, config);
		
		int updateResendCounter = Integer.parseInt(strCalloutData[2]);
		
		CardHolderBean objTempBean = new CardHolderBean();
		
		/*
		 * Populate the Cardholder Bean
		 */
		try {
			EnrollOperations.getCHData(request.getCardNumber(), request.getCardholderName(), request.getBankId(), objTempBean);
		} catch (ArcotException e1) {
			logger.error(request, "ArcotException", e1);
			return COUtil.createCOResponse(false, 0, "ArcotException",
					"ArcotException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger));
		}
		
		/*
		 * The Resend Counter will equal the NUM_OF_RESENDS
		 * on the last resend try by the cardholder when we will
		 * either block/lock the cardholder or disable the Resend link.
		 * We will check the count here for the MAX_RESEND_ACTION
		 * setting else disable it on the last resend attempt.
		 */
		if (config.getIntValue(NUM_OF_RESENDS) > -1 && config.getIntValue(MAX_RESEND_ACTION) > -1){
			if (updateResendCounter == (config.getIntValue(NUM_OF_RESENDS))){
				return actionOnResend(request, config, updateResendCounter, additionalCustomerData);
			}
		}
		
		/*
		 * Check if the the current OTP has expired.
		 * If YES then check the action on OTP Expired.
		 * 
		 */
		
		long result = currentTime -  Long.valueOf(strCalloutData[1]);
		long longConfigOtpGenerationTime = Long.parseLong(config.getValue(OTP_VALIDITY_PERIOD));
		
		if(result > longConfigOtpGenerationTime){
			/*
			 * If OTP_EXPIRY_ACTION is configured we will either lock/block the card.
			 * Else create a new OTP and send. Set the boolean variable isGenerateNewOTP to true.
			 */
			if (!Boolean.valueOf(config.getValue(OTP_EXPIRY_ACTION))){
				return actionOnOTPExpiry(request, config, additionalCustomerData);
			}else{
				/*
				 * Create a new OTP
				 */
				isGenerateNewOTP = true;
			}
			
			
		}

		COResponse objCOResp = null;
				
		/**
		 * updateResendCounter will be updated in AHA, only if there is a delivery success.
		 * If the Callout Config Variable USE_NEW_OTP_ALWAYS is set or the OTP has expired
		 * and we need to generate a new one and send the following code is executed.
		 */
		if (Boolean.valueOf(config.getValue(USE_NEW_OTP_ALWAYS)) || isGenerateNewOTP){

			
			/*
			 * Method call to create OTP
			 */
			try {
				generatorResponse = createOTP(config, request, config.getValue(OTP_TYPE), config.getIntValue(OTP_LENGTH));
			} catch (InstantiationException e) {
				logger.error(request, "InstantiationException", e);
				return COUtil.createCOResponse(false, 0, "InstantiationException",
						"InstantiationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger));
			} catch (IllegalAccessException e) {
				logger.error(request, "IllegalAccessException", e);
				return COUtil.createCOResponse(false, 0, "IllegalAccessException",
						"IllegalAccessException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger));
			} catch (ClassNotFoundException e) {
				logger.error(request, "ClassNotFoundException", e);
				return COUtil.createCOResponse(false, 0, "ClassNotFoundException",
						"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger));
			}
			
			/*
			 * Verifying the Generator's Result, if False we will return with Pares of N.
			 */
			if(!generatorResponse.getResult()){
				/*
				 * The OTP Generation Failed.
				 */
				return COUtil.prepareACSResponse(request,generatorFailureResponse(generatorResponse));
			}
			
			strOTP = generatorResponse.getOtpValue();
			
			
			/*
			 * Different OTP to be used, send new OTP. The respective method is called
			 * based on the Resend Type.
			 */
			logger.info(request, "resendOTP():: Configured to use the different OTP");
			objCOResp = resendOTPOnPinAction(request, config, resendType,
					logger, strOTP, logToDB, logToFile, calloutData,
					updateResendCounter, objTempBean, objCOResp, additionalCustomerData);
 
		}else{
			
			/*
			 * Same OTP is to be used.
			 */
			
			String decryptedUserOTP = EnrollmentCrypto.decrypt(strCalloutData[0], request.getBankId());
			
			logger.trace(request, "resendOTP():: Configured to use the same OTP"+decryptedUserOTP);
			
			objCOResp = resendOTPOnPinAction(request, config, resendType,
						logger, decryptedUserOTP, logToDB, logToFile, calloutData,
						updateResendCounter, objTempBean, objCOResp, additionalCustomerData);
	 
		}
		
		
		return objCOResp;
	}

	/**
	 * The PIN from the CAP page could be RESEND, RESEND_SMS, RESEND_EMAIL and RESEND_SMS_EMAIL
	 * 
	 * @param request
	 * @param config
	 * @param resendType
	 * @param logger
	 * @param strOTP
	 * @param logToDB
	 * @param logToFile
	 * @param calloutData
	 * @param updateResendCounter
	 * @param objTempBean
	 * @param objCOResp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private COResponse resendOTPOnPinAction(ACSRequest request,
			COConfig config, String resendType, COLogger logger, String strOTP,
			StringBuffer logToDB, StringBuffer logToFile,
			List<String> calloutData, int updateResendCounter,
			CardHolderBean objTempBean, COResponse objCOResp, HashMap<String, Object> additionalCustomerData) {
		
		
		COResponse objSendEmailCOResp = null;
		
		try {
			
			if(RESEND_SMS_EMAIL.equalsIgnoreCase(resendType)){
				/*
				 * Resend Via SMS first.
				 */
				objCOResp = deliverSMSOTP(config, request, strOTP, updateResendCounter, objTempBean, logger, calloutData, null, true, true, additionalCustomerData);
				/*
				 * If resend Via SMS Fails and Email is configured try Resend Via Email
				 * Append the Log to DB and Log to File
				 */
				if(objCOResp != null && objCOResp.getAcsCRC() == ACS_CALLOUT_PARES_N){
					logger.info(request, "The Resend Via SMS has failed, hence we are delivering VIA Email.");

					objSendEmailCOResp = sendEmailOTP(config, request, strOTP, updateResendCounter, objTempBean, logger, calloutData, null, 
							true, true, objCOResp.getAdditionalCustomerData());
					
					/*
					 * Modifying the objCOResp Object with the Send Email Response Object
					 */
					logToDB.append(objCOResp.getLogToDB()).append(SMS_DELIVERY_FAILED).append(objSendEmailCOResp.getLogToDB());
					logToFile.append(objCOResp.getLogToFile()).append(objSendEmailCOResp.getLogToFile());
					
					objCOResp.setAcsCRC(objSendEmailCOResp.getAcsCRC());
					objCOResp.setLogToDB(logToDB.toString());
					objCOResp.setLogToFile(logToFile.toString());
					if(objSendEmailCOResp.isResult())
						objCOResp.setCalloutMessage(SMS_DELIVERY_FAILED + objSendEmailCOResp.getCalloutMessage());
					else{
						logger.info(request, "The Resend Through Email has failed as well.");
						objCOResp.setCalloutMessage(SMS_DELIVERY_FAILED + objSendEmailCOResp.getCalloutMessage() + EMAIL_DELIVERY_FAILED);
					}
				}
				objCOResp.setAdditionalCustomerData(objSendEmailCOResp.getAdditionalCustomerData());
				
			}else if(RESEND_SMS.equalsIgnoreCase(resendType)){
				/*
				 * Resend Via SMS ONLY.
				 */
				objCOResp = sendSMSOTP(config, request, strOTP, updateResendCounter, objTempBean, logger, calloutData, null, true, true, additionalCustomerData);
				
				/*
				 * Check for NOMOBILEPRESENT If yes return.
				 */
				if(objCOResp.getLogToDB().contains(NOMOBILEPRESENT)){
					/*
					 * Mobile Number is Not Present in the DB or returned from the back-end
					 */
					return objCOResp;
				}
				
				if(!objCOResp.isResult()){
					logger.info(request, "Resend Via SMS has failed.");
					objCOResp.setLogToDB(SMS_DELIVERY_FAILED + objCOResp.getLogToDB());
					objCOResp.setCalloutMessage(SMS_DELIVERY_FAILED + objCOResp.getCalloutMessage());
				}
				
				/*
				 * Check for IGNORE_SMS_DELIVERY_RES flag set
				 * by default its true hence ignoring the SMS Delivery
				 * response and sending success
				 * 
				 */
				
				//TODO: Change getBoolean to valueOf
				if(config.getValue(IGNORE_SMS_DELIVERY_RES) == null || Boolean.valueOf(config.getValue(IGNORE_SMS_DELIVERY_RES))){
					logger.info(request, "The SMS Delivery Response is being ignored Sending TX No Action.");
					objCOResp.setAcsCRC(ACS_CALLOUT_TX_NO_ACTION);
				}
				
			}else if(RESEND_EMAIL.equalsIgnoreCase(resendType)){
				/*
				 * Resend Via EMAIL ONLY.
				 */
				objCOResp = sendEmailOTP(config, request, strOTP, updateResendCounter, objTempBean, logger, calloutData, null, true, true, additionalCustomerData);
				
				/*
				 * Check for NOEMAILPRESENT If yes return.
				 */
				if(objCOResp.getLogToDB().contains(NOEMAILPRESENT)){
					/*
					 * Email is not present in the DB or returned from the back-end.
					 */
					return objCOResp;
				}
				
				if(!objCOResp.isResult()){
					logger.info(request, "Resend Via Email has failed.");
					objCOResp.setLogToDB(EMAIL_DELIVERY_FAILED + objCOResp.getLogToDB());
					objCOResp.setCalloutMessage(EMAIL_DELIVERY_FAILED + objCOResp.getCalloutMessage());
				}
				
				/*
				 * Check for IGNORE_EMAIL_DELIVERY_RES flag set
				 * by default its true hence ignoring the Email Delivery
				 * response and sending success
				 */
				
				//TODO: Change getBoolean to valueOf
				if(config.getValue(IGNORE_EMAIL_DELIVERY_RES) == null || Boolean.valueOf(config.getValue(IGNORE_EMAIL_DELIVERY_RES))){
					logger.info(request, "The Email Delivery Response is being ignored Sending Success.");
					objCOResp.setAcsCRC(ACS_CALLOUT_TX_NO_ACTION);
				}
				
			}else{
				/*
				 * Resend using both SMS and Email.
				 */
				objCOResp = sendOTP(config, request,strOTP, 
						updateResendCounter, objTempBean, true, additionalCustomerData);
			}
		} catch (InstantiationException e) {
			logger.error(request, "InstantiationException", e);
			return COUtil.createCOResponse(false, 0, "InstantiationException",
					"InstantiationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger));
		} catch (IllegalAccessException e) {
			logger.error(request, "IllegalAccessException", e);
			return COUtil.createCOResponse(false, 0, "IllegalAccessException",
					"IllegalAccessException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger));
		} catch (ClassNotFoundException e) {
			logger.error(request, "ClassNotFoundException", e);
			return COUtil.createCOResponse(false, 0, "ClassNotFoundException",
					"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger));
		}
		return objCOResp;
	}

	
	/** Checks the action to be performed on resend attempts expired. If the Parameter MAX_RESEND_ACTION
	 * is not set then we will need to disable the "Resend OTP" link.
	 * @param request
	 * @param config
	 * @return
	 */
	public COResponse actionOnResend(ACSRequest request, COConfig config, int resendNumber, HashMap<String, Object> additionalCustomerData) {
		
		COLogger logger = config.getLogger();
		logger.info(request, "actionOnResend()");
		COResponse coResponse = null;

		if (config.getIntValue(MAX_RESEND_ACTION) == 2) {
			
			logger.info(request, "actionOnResend():: LOCKING is configured");
			// Lock the card
			coResponse = COUtil.prepareACSResponse(request, 
					COUtil.createCOResponse(false, 0, 
							"ResendTry(L)["+ resendNumber + "]", "ResendTry(L)["+ resendNumber + "]",
							ACS_CALLOUT_BLOCK_CH, setCalloutMessageOnResendExceed(request, config), request.getFolderId()));
			additionalCustomerData.put(error_msg, RESENTEXCEEDED);
			coResponse.setAdditionalCustomerData(additionalCustomerData);			
			return coResponse;
		} else {
			
			logger.info(request, "actionOnResend():: BLOCKING is configured");
			// block the card
			coResponse =  COUtil.prepareACSResponse(request, 
					COUtil.createCOResponse(false, 0, 
							"ResendTry(B)["+ resendNumber + "]", "ResendTry(B)["+ resendNumber + "]",
							ACS_CALLOUT_BLOCK_NO_LOCK, setCalloutMessageOnResendExceed(request, config),request.getFolderId()));
			additionalCustomerData.put(error_msg, RESENTEXCEEDED);
			coResponse.setAdditionalCustomerData(additionalCustomerData);			
			return coResponse;
		}
	}
	
	/** Checks the action to be performed on OTP expiry.
	 * @param request
	 * @param config
	 * @return
	 */
	public COResponse actionOnOTPExpiry(ACSRequest request, COConfig config, HashMap<String, Object> additionalCustomerData) {
		
		COLogger logger = config.getLogger();
		logger.info(request, "actionOnOTPExpiry()");
		COResponse coResponse = null;

		if (config.getIntValue(OTP_EXPIRY_ACTION) == 2) {
			
			logger.info(request, "actionOnOTPExpiry():: LOCKING is configured");
			// Lock the card
			coResponse = COUtil.prepareACSResponse(request, 
					COUtil.createCOResponse(false, 0, 
							"OTPExpired(L)", "OTPExpired(L)",
							ACS_CALLOUT_BLOCK_CH, setCalloutMessageOnOTPExpiry(request, config), request.getFolderId()));
			additionalCustomerData.put(error_msg, OTPEXPIRED);
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		} else {
			
			logger.info(request, "actionOnOTPExpiry():: BLOCKING is configured");
			// block the card
			coResponse =  COUtil.prepareACSResponse(request, 
					COUtil.createCOResponse(false, 0, 
							"OTPExpired(B)", "OTPExpired(B)",
							ACS_CALLOUT_BLOCK_NO_LOCK, setCalloutMessageOnOTPExpiry(request, config),request.getFolderId()));
			additionalCustomerData.put(error_msg, OTPEXPIRED);
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		}
	}

	/**
	 * By Default lets have the VP functionality same as the VIA functionality, this is
	 * not meant to be overrident by the Business Logic Handler.
	 * 
	 * 
	 * @param request
	 * @param config
	 * @return
	 * @throws ArcotException 
	 */
	private ACSResponse doVPFlow(ACSRequest request, COConfig config, HashMap<String, Object> additionalCustomerData) throws ArcotException {
		
		return doViaFlow(request, config, request.getPin(), additionalCustomerData);
	
	}

	public ESResponse process(ESRequest request, COConfig config) {
		return null;
	}
	
	/**
	 * Create the OTP depending on the OTP_TYPE
	 * @param request 
	 * 
	 * @param otpType 		:	ALPHANUMERIC/ALPHABETS/NUMERIC
	 * @param otpLength		:	Length of OTP
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public GeneratorResponse createOTP( COConfig config, ACSRequest request, String otpType, int otpLength )
			throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		
		COLogger logger = config.getLogger();
		GeneratorResponse generatorResponse = new GeneratorResponse();		
		String otpGeneratorType = null;
		OTPGenerator otpGenerator = null;
		logger.info(request, "createOTP()");
		
		otpGeneratorType = config.getValue(GENERATOR);
		HashMap<String, String> calloutConfigVals = config.getAll();
		otpGenerator = OTPGenerator.getInstance(otpGeneratorType, calloutConfigVals);
		calloutConfigVals.put(CARDNUMBER_CLEAR.toLowerCase(), request.getCardNumber());
		generatorResponse = otpGenerator.generateOTP(calloutConfigVals, otpType, otpLength);
		
		/*
		 * If the Result is true the OTP Generation was successful.
		 */
		if(generatorResponse.getResult()){
			logger.trace(request, "createOTP():: OTP Type Configured ["+otpType+"] OTP ["+generatorResponse.getOtpValue()+"]");
			logger.info(request, "createOTP():: OTP Type Configured ["+otpType+"] OTPLength ["+generatorResponse.getOtpValue().length()+"]");
		}
		
		return generatorResponse;
	}
	
	/**
	 * This method returns the Callout Config value set
	 * for the key or the default value in the absence
	 * of the key from the Callout Config.
	 * @param config
	 * @param calloutConfigKey
	 * @param defaultVal
	 * @return Value of the Key
	 */
	public String readConfigValue(COConfig config, String calloutConfigKey, String defaultVal)
	{
		if(config.getValue(calloutConfigKey) != null){
			return config.getValue(calloutConfigKey);
		}
		else{
			return defaultVal;
		}
		
	}
	
	/**
	 * Sent the OTP
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @param objChBean 
	 * @param isResend
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public COResponse sendOTP( COConfig config, ACSRequest request, String strOTP,
			int resentCounter, CardHolderBean objChBean, boolean isResend, HashMap<String, Object> additionalCustomerData) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		
		COLogger logger = config.getLogger();
		logger.info(request, "sendOTP()");
		List<String> calloutDataMobile = new ArrayList<String>();
		List<String> calloutDataEmail = new ArrayList<String>();
		String mobileNumber = null;
		String emailAddress = null;
		boolean isDBValidator = false;
		String otpValidatorType = null;
		String localeName = null;
		String calloutMsg = null;
		StringBuffer logToDB = new StringBuffer();
		StringBuffer logToFile = new StringBuffer();
		
		if(additionalCustomerData == null)
		additionalCustomerData = new HashMap<String, Object>();
		
		otpValidatorType = readConfigValue(config, VALIDATOR, DEFAULT_VALIDATOR);
		
		if(DEFAULT_VALIDATOR.equals(otpValidatorType)){
			/*
			 * The Validator is DBValidator hence we would be storing
			 * and generating the OTP.
			 */
			isDBValidator = true;
		}
		
		/*
		 * Determining the Locale
		 */
		localeName = COUtil.getLocaleName(Integer.toString(objChBean.getLocaleId()));
		
		COResponse deliveryResp = null;
		COResponse deliveryEmailResp = null;
		
		deliveryResp = deliverSMSOTP(config, request, strOTP, resentCounter, objChBean,
				logger, calloutDataMobile, mobileNumber, isDBValidator, false, additionalCustomerData);
		logger.info(request, " CRC recieved from deliverSMSOTP(): "+deliveryResp.getAcsCRC());
		if(deliveryResp.getAcsCRC() == ACS_CALLOUT_PARES_A || deliveryResp.getAcsCRC() == ACS_CALLOUT_PARES_Y) {
			//This means the ovveridden method deliverSMSOTP() wants to send direct PAres value in special cases.
			//So Otp Handler will send same response to ACS.
			logger.info(request, "This means the ovveridden method deliveryResp() wants to send direct PAres value in special cases.: "+deliveryResp.getAcsCRC());
			//To prepare callout messages in case of any scenarios other than INCREASE_AUTH
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("locale", localeName);
			hm.put("error_msg", "[RA_ALLOW]");
			deliveryResp.setAdditionalCustomerData(hm);
			return deliveryResp;
		}
		
		
		if(!Boolean.valueOf(config.getValue(MOBILE_NUMBER_ONLY))){
			/*
			 * If the OTP delivery through SMS was successful send back a success
			 * irrespective of sendEmailOTP response. Send OTP through Email.
			 */
			
			if(deliveryResp.getAcsCRC() == ACS_CALLOUT_SUCCESS){
				
				mobileNumber = calloutDataMobile.size() > 0 ? calloutDataMobile.get(0): null;
				
				logger.info(request, "SMS delivery is success checking whether to send email");
				deliveryEmailResp = deliverEmailOTP(config, request, strOTP, resentCounter, objChBean,
					logger, calloutDataEmail, emailAddress, isDBValidator, false, deliveryResp.getAdditionalCustomerData(),deliveryResp);
				
				if(deliveryEmailResp.getAcsCRC() == ACS_CALLOUT_PARES_A || deliveryEmailResp.getAcsCRC() == ACS_CALLOUT_PARES_Y) {
					//This means the ovveridden method deliveryEmailResp() wants to send direct PAres value in special cases.
					//So Otp Handler will send same response to ACS.
					logger.info(request, "This means the ovveridden method deliveryEmailResp() wants to send direct PAres value in special cases.: "+deliveryEmailResp.getAcsCRC());
					return deliveryEmailResp;
				}
				
				if(deliveryEmailResp.getAcsCRC() == ACS_CALLOUT_SUCCESS){
					emailAddress = calloutDataEmail.size() > 0 ? calloutDataEmail.get(0): null;
				}else{				
					deliveryResp.setAcsCRC(ACS_CALLOUT_SUCCESS);
				}
				
				/**
				 * store the OTP if the Validator is DBValidator. Is Resend increment resend Counter.
				 */
				if(isResend){
					resentCounter = resentCounter + 1;
				}
				
				logToDB.append(deliveryResp.getLogToDB()).append(deliveryEmailResp.getLogToDB());
				logToFile.append(deliveryResp.getLogToFile()).append(deliveryEmailResp.getLogToFile());
				
				deliveryResp.setLogToDB(logToDB.toString());
				deliveryResp.setLogToFile(logToFile.toString());
				
				return storeOTPOnDeliverySuccess(config, request, strOTP,
						resentCounter, mobileNumber, emailAddress, isDBValidator, deliveryResp, localeName, deliveryEmailResp.getAdditionalCustomerData());
				
			}else{
				
				calloutMsg = deliveryResp.getCalloutMessage();
				logger.info(request, "No only mobile, sending to email aswell: "+calloutMsg);

				deliveryEmailResp = deliverEmailOTP(config, request, strOTP, resentCounter, objChBean,
						logger, calloutDataEmail, emailAddress, isDBValidator, false, deliveryResp.getAdditionalCustomerData(), deliveryResp);

				if(deliveryEmailResp.getAcsCRC() == ACS_CALLOUT_PARES_A || deliveryEmailResp.getAcsCRC() == ACS_CALLOUT_PARES_Y) {
					//This means the ovveridden method deliveryEmailResp() wants to send direct PAres value in special cases.
					//So Otp Handler will send same response to ACS.
					logger.info(request, "This means the ovveridden method deliveryEmailResp() wants to send direct PAres value in special cases.: "+deliveryEmailResp.getAcsCRC());
					return deliveryEmailResp;
				}
				/*
				 * Check for NOEMAILPRESENT If yes return.
				 */
				if(deliveryEmailResp.getLogToDB().contains(NOEMAILPRESENT)){
					/*
					 * Email is not present in the DB or returned from the back-end.
					 */
					deliveryEmailResp.setLogToDB(deliveryResp.getLogToDB() + deliveryEmailResp.getLogToDB());
					deliveryEmailResp.setLogToFile(deliveryResp.getLogToFile() + deliveryEmailResp.getLogToFile());
					deliveryEmailResp.setCalloutMessage(calloutMsg + deliveryEmailResp.getCalloutMessage());
					return deliveryEmailResp;
				}
				
				/*
				 * Check for IGNORE_EMAIL_DELIVERY_RES flag set
				 * by default its true hence ignoring the Email Delivery
				 * response and sending success
				 */
				if(config.getValue(IGNORE_EMAIL_DELIVERY_RES) == null || Boolean.valueOf(config.getValue(IGNORE_EMAIL_DELIVERY_RES))){
					logger.info(request, "The Email Delivery Response is being ignored Sending Success.");
					deliveryEmailResp.setAcsCRC(ACS_CALLOUT_SUCCESS);
					
					setCRCValueBasedOnResponsesFromDeliveryMethods(deliveryEmailResp);
					
				}
				
				if(deliveryEmailResp.getAcsCRC() == ACS_CALLOUT_SUCCESS){
					emailAddress = calloutDataEmail.size() > 0 ? calloutDataEmail.get(0): null;
					
					/**
					 * store the OTP if the Validator is DBValidator. Is Resend increment resend Counter.
					 */
					if(isResend){
						resentCounter = resentCounter + 1;
					}
					
					logToDB.append(deliveryResp.getLogToDB()).append(SMS_DELIVERY_FAILED).append(deliveryEmailResp.getLogToDB());
					logToFile.append(deliveryResp.getLogToFile()).append(SMS_DELIVERY_FAILED).append(deliveryEmailResp.getLogToFile());
					
					deliveryResp.setLogToDB(logToDB.toString());
					deliveryResp.setLogToFile(logToFile.toString());
					deliveryResp.setCalloutMessage(SMS_DELIVERY_FAILED + deliveryResp.getCalloutMessage());
					
					return storeOTPOnDeliverySuccess(config, request, strOTP,
						resentCounter, mobileNumber, emailAddress, isDBValidator, deliveryResp, localeName, deliveryEmailResp.getAdditionalCustomerData());
				}else{
					
					logToDB.append(deliveryResp.getLogToDB()).append(SMS_DELIVERY_FAILED).append(deliveryEmailResp.getLogToDB()).append(EMAIL_DELIVERY_FAILED);
					logToFile.append(deliveryResp.getLogToFile()).append(SMS_DELIVERY_FAILED).append(deliveryEmailResp.getLogToFile()).append(EMAIL_DELIVERY_FAILED);
					
					deliveryResp.setLogToDB(logToDB.toString());
					deliveryResp.setLogToFile(logToFile.toString());
					
					deliveryResp.setCalloutMessage(SMS_DELIVERY_FAILED + deliveryResp.getCalloutMessage() +
							EMAIL_DELIVERY_FAILED + deliveryEmailResp.getCalloutMessage());
					return deliveryResp;
				}
				
			}			
		}else{
			
			/*
			 * Check for NOMOBILEPRESENT If yes return.
			 */
			if(deliveryResp.getLogToDB().contains(NOMOBILEPRESENT)){
				/*
				 * Mobile is not present in the DB or returned from the back-end.
				 */
				return deliveryResp;
			}
			
			/*
			 * Check for IGNORE_SMS_DELIVERY_RES flag set
			 * by default its true hence ignoring the SMS Delivery
			 * response and sending success
			 */
			//TODO: Change getBoolean to valueOf
			if(Boolean.valueOf(config.getValue(IGNORE_SMS_DELIVERY_RES))){
				logger.info(request, "The SMS Delivery Response is being ignored Sending Success.");
				deliveryResp.setAcsCRC(ACS_CALLOUT_SUCCESS);
			}
			
			if(deliveryResp.getAcsCRC() == ACS_CALLOUT_SUCCESS){
			/*
			 * Only Mobile Number is to be retrieved.
			 */
			mobileNumber = calloutDataMobile.size() > 0 ? calloutDataMobile.get(0): null;
			
			/**
			 * store the OTP if the Validator is DBValidator. Is Resend increment resend Counter.
			 */
			if(isResend){
				resentCounter = resentCounter + 1;
			}
			
				return storeOTPOnDeliverySuccess(config, request, strOTP,
					resentCounter, mobileNumber, emailAddress, isDBValidator, deliveryResp, localeName, deliveryResp.getAdditionalCustomerData());
			}else{
				setLocaleNameInCalloutMsg(localeName, deliveryResp);
				return deliveryResp;
			}
		}
	}

	/**
	 * 
	 * @param deliveryEmailResp
	 * By default OTP handler will make CRC value as success as ignore response is configured in configuration.
	   The BLH classes who are extendeding can overide this method to change begaviour
	 */
	public void setCRCValueBasedOnResponsesFromDeliveryMethods(
			COResponse deliveryEmailResp) {
		deliveryEmailResp.setAcsCRC(ACS_CALLOUT_SUCCESS);
	}

	public COResponse deliverEmailOTP(COConfig config, ACSRequest request,
			String strOTP, int resentCounter, CardHolderBean objChBean,
			COLogger logger, List<String> calloutDataEmail,
			String emailAddress, boolean isDBValidator, boolean isResend,
			HashMap additionalCustomerData, COResponse deliveryResp) {
		
		logger.info(request, "In deliverEmailOTP()");
		return sendEmailOTP(config, request, strOTP, resentCounter, objChBean,
						logger, calloutDataEmail, emailAddress, isDBValidator, isResend, deliveryResp.getAdditionalCustomerData());
				
	}

	public COResponse deliverSMSOTP(COConfig config, ACSRequest request,
			String strOTP, int resentCounter, CardHolderBean objChBean,
			COLogger logger, List<String> calloutDataMobile,
			String mobileNumber, boolean isDBValidator, boolean isResend,
			HashMap<String, Object> additionalCustomerData) {
		
		logger.info(request, "In deliverSMSOTP()");
		return sendSMSOTP(config, request, strOTP, resentCounter, objChBean,
				logger, calloutDataMobile, mobileNumber, isDBValidator, isResend, additionalCustomerData);
	}

	/*
	 * Appends the Locale Name in the Callout Msg
	 */
	public void setLocaleNameInCalloutMsg(String localeName,
			COResponse deliveryResp) {
		String calloutMsg;
		calloutMsg = deliveryResp.getCalloutMessage();
		calloutMsg = calloutMsg + "~" + localeName;
		deliveryResp.setCalloutMessage(calloutMsg);
	}

	public COResponse storeOTPOnDeliverySuccess(COConfig config,
			ACSRequest request, String strOTP, int resentCounter,
			String mobileNumber, String emailAddress, boolean isDBValidator,
			COResponse deliveryResp, String localeName, HashMap<String, Object> additionalCustomerData) {
		
		setLocaleNameInCalloutMsg(localeName, deliveryResp);
		
		return storeOTP(config, request, strOTP, deliveryResp.getLogToDB(),
				deliveryResp.getCalloutMessage(), resentCounter,
				mobileNumber, emailAddress, additionalCustomerData);
	}

	/**
	 * Store the OTP in Transfort DB for Validation
	 * 
	 * 
	 * CALLOUTDATA --> OTP~TIME_OF_GENERATION_in_millisecond~NUMBER_OF_RESENDS~MOBILE_NUMBER or
	 * OTP~TIME_OF_GENERATION_in_millisecond~NUMBER_OF_RESENDS~MOBILE_NUMBER~EMAIL_ADDRESS
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @param strLogToDB 
	 * @param mobileNumber 
	 * @return
	 */
	public COResponse storeOTP(COConfig config, ACSRequest request, 
			String strOTP, String strLogToDB, String exsistingCalloutMsg, int resentCounter, 
			String mobileNumber, String emailAddress, HashMap<String, Object> additionalCustomerData) {
		StringBuffer contactDetails= new StringBuffer();
		List<String> calloutData = new ArrayList<String>();
		
		String strCalloutData = null;
		COLogger logger = config.getLogger();
		logger.info(request, "storeOTP()");
		COResponse coResponse = null;
		
		contactDetails = maskingEmailMobNo(request, config, mobileNumber, emailAddress, additionalCustomerData);
		
		if(contactDetails.length() > 0){
			if(calloutData.size() > 0)
				calloutData.set(0, contactDetails.toString());
			else
				calloutData.add(0, contactDetails.toString());
		}
		
		strCalloutData = createCalloutData(request, config, strOTP, resentCounter, calloutData);

		/**
		 * OTP will be stored at 
		 * trxpropxypan level
		 *  
		 */
		logger.info(request, "storeOTP():: CALLOUTDATA is stored in ARCALLOUTXNDATA");
		COUtil.setTxnCalloutData(request, config, strCalloutData);	
		// setting the timeout period for Resend Link
		
		String timeBetweenResends = ((config.getValue(TIME_BETWEEN_RESENDS) != null)) ? config.getValue(TIME_BETWEEN_RESENDS) : "";
		mobileNumber = (mobileNumber != null)? mobileNumber : "";
		
		coResponse = COUtil.createCOResponse(true, 0, strLogToDB, strLogToDB, 
				ACS_CALLOUT_TX_NO_ACTION, "");
		additionalCustomerData.put(GenericOTPHandler.time_period, timeBetweenResends);
		coResponse.setAdditionalCustomerData(additionalCustomerData);
		
		return coResponse;
	}

	
	/**
	 * CALLOUTDATA will be always of the format - <encrypted_otp>~<generation_time_of_otp_in_milliseconds>~<resent_counter>~<mobile_Number>
	 * 
	 * @param request
	 * @param config 
	 * @param strOTP
	 * @param resendNumber
	 * @param mobileNumber 
	 * @return
	 */
	public String createCalloutData(ACSRequest request, COConfig config, String strOTP, int resendNumber, List<String> calloutData){

		COLogger logger = config.getLogger();
		logger.info(request,"createCalloutData()");
		String strCalloutString = COUtil.getTxnCalloutData(request, config);
		String strCalloutData = "";

		if(calloutData.size() > 0)
			calloutData.set(0, "");
		else
			calloutData.add(0, "");
		
		//Write a method which will getCalloutdataext - split on "~" expiry check
		//
		
		if(strCalloutString != null){
			logger.info("strCalloutString "+strCalloutString);
			String[] strCallout =  strCalloutString.split("~");
			if(strCallout != null && strCallout.length > 1){
				long currentTime = System.currentTimeMillis();
				long longConfigOtpGenerationTime = Long.parseLong(config.getValue(OTP_VALIDITY_PERIOD));
				long result = currentTime - Long.valueOf(strCallout[1]);
				if(result < longConfigOtpGenerationTime){
				strCalloutData = EnrollmentCrypto.encrypt(strOTP, request.getBankId()) + "~" + 
						String.valueOf(strCallout[1]) + "~" + resendNumber + 
						"~" + provideExtraCalloutData(request,config,calloutData);
				
				}
			}
			
			
		}

		if("".equalsIgnoreCase(strCalloutData) || "null".equalsIgnoreCase(strCalloutData)){
			
			strCalloutData = EnrollmentCrypto.encrypt(strOTP, request.getBankId()) + "~" + 
								String.valueOf(System.currentTimeMillis()) + "~" + resendNumber + 
								"~" + provideExtraCalloutData(request,config,calloutData);
		}
			
		logger.info(request,"createCalloutData():: CALLOUTDATA - "+strCalloutData);
		return strCalloutData;
	}
	
	/**
	 * The OTP delivery through Email is performed in this method
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @param resentCounter
	 * @param objChBean
	 * @param logger
	 * @param calloutData
	 * @param emailAddress
	 * @param isDBValidator
	 * @param isResend - is true then store otp.
	 * @return
	 * 
	 * connector_response will take the value of response code from the connector to CAP. Based of the connector_response we will show spacific text on CAP.
	 */
	@SuppressWarnings("unchecked")
	public COResponse sendEmailOTP(COConfig config, ACSRequest request,
			String strOTP, int resentCounter, CardHolderBean objChBean,
			COLogger logger, List<String> calloutData, String emailAddress,
			boolean isDBValidator, boolean isResend, HashMap<String, Object> additionalCustomerData) {
		COResponse deliveryResp;
		String localeName = null;
		/*
		 * Based on the presence of Callout Config Parameters "EMAIL_IN_AHA" and 
		 * "EMAIL_QUES_ID" which will establish the fact that Email Address is stored
		 * with us in the Transfort DB.
		 * We need to verify the presence of the same in AHA/AIA and send back a response
		 * of Pares of N if it is not found in the DB.
		 */
		
		if(Boolean.valueOf(config.getValue(EMAIL_IN_AHA)) || config.getValue(EMAIL_QUES_ID) != null){
			emailAddress = populateEmail(config, request, emailAddress, objChBean);
			
			/*
			 * Verifying if the Email Address was present in the DB.
			 */
			if(emailAddress == null || "".equals(emailAddress)){
				logger.info(request, "The Email Address was not retreived from the DB.");
				return noEmailPresentAction(request, config, additionalCustomerData);
			}
			
		}
		
		/*
		 * If the Email Address is not uploaded in our DB and the Business logic
		 * is to provide the email address on user selection, this handle below
		 * is provided to be overriden by the BLH.
		 */
		if(emailAddress == null)
			emailAddress = provideEmailAddress(request, config);
		
		List<Object> listFrmDeliverCall;
		try {
			listFrmDeliverCall = deliverOTPViaEmail(config, request, strOTP, emailAddress);
		} catch (MalformedURLException e) {
			logger.error(request, "MalformedURLException", e);
			deliveryResp = COUtil.createCOResponse(false, 0, "MalformedURLException",
					"MalformedURLException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + "MalformedURLException": "MalformedURLException");
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		} catch (IllegalArgumentException e) {
			logger.error(request, "IllegalArgumentException", e);
			deliveryResp =  COUtil.createCOResponse(false, 0, "IllegalArgumentException",
					"IllegalArgumentException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + "IllegalArgumentException": "IllegalArgumentException");
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		} catch (NullPointerException e) {
			logger.error(request, "NullPointerException", e);
			deliveryResp =   COUtil.createCOResponse(false, 0, "NullPointerException",
					"NullPointerException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + "NullPointerException": "NullPointerException");
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		} catch (IOException e) {
			logger.error(request, "IOException", e);
			deliveryResp = COUtil.createCOResponse(false, 0, "NullPointerException",
					"IOException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + "NullPointerException": "NullPointerException");
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		
		} catch (ParserConfigurationException e) {
			logger.error(request, "ParserConfigurationException", e);
			deliveryResp = COUtil.createCOResponse(false, 0, "ParserConfigurationException",
					"ParserConfigurationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + "ParserConfigurationException": "ParserConfigurationException");
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		} catch (SAXException e) {
			logger.error(request, "SAXException", e);
			deliveryResp = COUtil.createCOResponse(false, 0, "SAXException",
					"SAXException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + "SAXException": "SAXException");
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		} catch (InstantiationException e) {
			logger.error(request, "InstantiationException", e);
			deliveryResp = COUtil.createCOResponse(false, 0, "InstantiationException",
					"InstantiationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + "InstantiationException": "InstantiationException");
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		} catch (IllegalAccessException e) {
			logger.error(request, "IllegalAccessException", e);
			deliveryResp = COUtil.createCOResponse(false, 0, "IllegalAccessException",
					"IllegalAccessException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + "IllegalAccessException": "IllegalAccessException");
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		} catch (ClassNotFoundException e) {
			logger.error(request, "ClassNotFoundException", e);
			deliveryResp = COUtil.createCOResponse(false, 0, "ClassNotFoundException",
					"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + "ClassNotFoundException": "ClassNotFoundException");
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		} catch (Exception e) {
			logger.error(request, "Exception", e);
			deliveryResp = COUtil.createCOResponse(false, 0, "Exception",
					"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + "Exception": "Exception");
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		}
		
		deliveryResp = (COResponse)listFrmDeliverCall.get(0);
						
		logger.info(request, "sendEmailOTP():: listFrmDeliverCall size"+listFrmDeliverCall.size());
		
		/*
		 * We need to retrieve the Mobile Number from the back-end if it is not uploaded
		 * in our DB.
		 */
		
		if(emailAddress == null || "".equals(emailAddress)){
		
				if(listFrmDeliverCall.size()>1){
					if (listFrmDeliverCall.get(1) !=null && ((String)listFrmDeliverCall.get(1)).length()> 0)
						emailAddress = (String)listFrmDeliverCall.get(1);
					for(int i=1;i<listFrmDeliverCall.size();i++){
						logger.trace(request, "sendOTP():: calloutData "+i+" =>"+listFrmDeliverCall.get(i));
						calloutData.add((String)listFrmDeliverCall.get(i));
					}
					logger.info(request, "sendEmailOTP():: calloutData size"+calloutData.size());
				}
				
				if (emailAddress != null){
					logger.trace(request, "sendEmailOTP():: Email Address fetched from deliverOTP Method - "+emailAddress);
					logger.info(request, "sendEmailOTP():: Email Address fetched from deliverOTP Method - "+emailAddress.length());
				
				}else{
					
					logger.info(request, "sendEmailOTP():: Email Address NOT fetched from sendEmailOTP Method");
				
				}
		}
		
		if (ACS_CALLOUT_SUCCESS == deliveryResp.getAcsCRC()){
			
			additionalCustomerData.put(success_msg, additionalCustomerData.get(success_msg) != null?
					additionalCustomerData.get(success_msg) + "," + EMAIL_DELIVERY_SUCCESS: EMAIL_DELIVERY_SUCCESS);
			
			/**
			 * Check if EMAIL is supported; i.e. do we have to show 
			 */
			
			if (Boolean.valueOf(config.getValue(EMAIL_ON_PAGE))){
				
				if (resentCounter > 0){
					
					logger.info(request, "sendEmailOTP():: RESENT FLOW - Counter - "+resentCounter);
					
					if (emailAddress != null){
						
						logger.trace(request, "sendEmailOTP():: RESENT FLOW - Email Address fetched from CALLOUTDATA - "+emailAddress);
						logger.info(request, "sendEmailOTP():: RESENT FLOW - Email Address fetched from CALLOUTDATA - "+emailAddress.length());
					
					}else{
						
						// safe Check , this should never happen
						
						logger.info(request, "sendEmailOTP():: RESENT FLOW - Email Address Not present in CALLOUTDATA during RESENT FLOW");
						return COUtil.createCOResponse(false, 0, "RESENT_FLOW[NoEmailAddress]", "RESENT_FLOW[NoEmailAddress]",
								ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + "[RESENT_FLOW[NoEmailAddress]]");
					}
					
					if(calloutData.size() > 0)
						calloutData.set(0, emailAddress);
					else
						calloutData.add(0, emailAddress);
				}else{
				
					logger.info(request, "sendEmailOTP():: NORMAL FLOW - Email Address to be Stored in CALLOUTDATA");
					emailAddress = populateEmail(config, request, emailAddress, objChBean);
					
					if (emailAddress == null){
						/**
						 * will be NULL, if:-
						 * 1.  backend returns no mobile number
						 * 2.  not stored in AHA
						 * 
						 * 3.1 not stored in ARISSUERANSWER
						 * OR
						 * 3.2 stored in ARISSUERANSWER, but MOBILE_QUES_ID is not configured   
						 */
						return noEmailPresentAction(request, config, additionalCustomerData);
					}else{
						if(calloutData.size() > 0)
							calloutData.set(0, emailAddress);
						else
							calloutData.add(0, emailAddress);
					}
				
				}
			}else{
				
				logger.info(request, "sendEmailOTP():: Email Address not to be shown on page");
			
			}
			
			if(isResend){
					/**
					 * store the OTP if the Validator is DBValidator and increment resend.
					 */
					resentCounter = resentCounter + 1;
					additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
							additionalCustomerData.get(connector_response) + "," + deliveryResp.getCalloutMessage(): deliveryResp.getCalloutMessage());
					

					deliveryResp.setAdditionalCustomerData(additionalCustomerData);
					return storeOTPOnDeliverySuccess(config, request, strOTP,
							resentCounter, null, emailAddress, isDBValidator, deliveryResp, localeName, deliveryResp.getAdditionalCustomerData());
			}else{
				deliveryResp.setAdditionalCustomerData(additionalCustomerData);
				return deliveryResp;
			}
			
		}else{
			// if CRC is anything other than ACS_CALLOUT_SUCCESS, 
			// let the deliverOTP() method decide what needs to be done
			additionalCustomerData.put(error_msg, additionalCustomerData.get(error_msg) != null?
					additionalCustomerData.get(error_msg) + "," + EMAIL_DELIVERY_FAILED: EMAIL_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, additionalCustomerData.get(connector_response) != null?
					additionalCustomerData.get(connector_response) + "," + deliveryResp.getCalloutMessage(): deliveryResp.getCalloutMessage());
			
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		}

	}

	/**
	 * This method is the handle provided to provide the Email
	 * Address for OTP Email delivery.
	 * 
	 * @param request
	 * @param config
	 * @return
	 */
	public String provideEmailAddress(ACSRequest request, COConfig config) {
		return null;
	}

	/**
	 * The OTP delivery through SMS is performed in this method
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @param resentCounter
	 * @param objChBean
	 * @param logger
	 * @param calloutData
	 * @param mobileNumber
	 * @param isDBValidator
	 * @param isResend - If true then store OTP
	 * @return COResponse
	 * 
	 * 
	 * connector_response will take the value of response code from the connector to CAP. Based of the connector_response we will show spacific text on CAP.
	 */
	@SuppressWarnings("unchecked")
	public COResponse sendSMSOTP(COConfig config, ACSRequest request,
			String strOTP, int resentCounter, CardHolderBean objChBean,
			COLogger logger, List<String> calloutData, String mobileNumber,
			boolean isDBValidator, boolean isResend, HashMap<String, Object> additionalCustomerData) {
		COResponse deliveryResp;
		String localeName = null;
		
		
		/*
		 * Derive the locale Name.
		 */
		localeName = COUtil.getLocaleName(Integer.toString(objChBean.getLocaleId()));
		/*
		 * Based on the presence of Callout Config Parameters "MOBILE_NUMBER_IN_AHA" and 
		 * "MOBILE_QUES_ID" which will establish the fact that Mobile Number is stored
		 * with us in the Transfort DB.
		 * We need to verify the presence of the same in AHA/AIA and send back a response
		 * of Pares of N if it is not found in the DB.
		 */
		
		if(Boolean.valueOf(config.getValue(MOBILE_NUMBER_IN_AHA)) || config.getValue(MOBILE_QUES_ID) != null){
			mobileNumber = populateMobileNumber(config, request, mobileNumber, objChBean);
			
			/*
			 * Verifying if the Mobile Number was present in the DB.
			 */
			if(mobileNumber == null || "".equals(mobileNumber)){
				logger.info(request, "The Mobile Number was not retreived from the DB.");
				return noMobilePresentAction(request, config, additionalCustomerData);
			}
			
		}
		
		/*
		 * If the Mobile Number is not uploaded in our DB and the Business logic
		 * is to provide the mobile number on user selection, this handle below
		 * is provided to be overriden by the BLH.
		 */
		if(mobileNumber == null)
			mobileNumber = provideMobileNumber(request, config);
				
		List<Object> listFrmDeliverCall;
		try {
			listFrmDeliverCall = deliverOTPViaSMS(config, request, strOTP, mobileNumber);
		} catch (MalformedURLException e) {
			
			logger.error(request, "MalformedURLException", e);
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, "MalformedURLException");
			deliveryResp = COUtil.createCOResponse(false, 0, "MalformedURLException",
					"MalformedURLException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)+ SMS_DELIVERY_FAILED);
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);			
			return deliveryResp;
			
		} catch (IllegalArgumentException e) {
			logger.error(request, "IllegalArgumentException", e);
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, "IllegalArgumentException");
			deliveryResp =  COUtil.createCOResponse(false, 0, "IllegalArgumentException",
					"IllegalArgumentException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)+ SMS_DELIVERY_FAILED);
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);			
			return deliveryResp;
		} catch (NullPointerException e) {
			logger.error(request, "NullPointerException", e);
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, "NullPointerException");
			deliveryResp =   COUtil.createCOResponse(false, 0, "NullPointerException",
					"NullPointerException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)+ SMS_DELIVERY_FAILED);
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);			
			return deliveryResp;
		} catch (IOException e) {
			logger.error(request, "IOException", e);
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, "IOException");
			deliveryResp =   COUtil.createCOResponse(false, 0, "IOException",
					"IOException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)+ SMS_DELIVERY_FAILED);
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);			
			return deliveryResp;
		}  catch (ParserConfigurationException e) {
			logger.error(request, "ParserConfigurationException", e);
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, "ParserConfigurationException");
			deliveryResp =    COUtil.createCOResponse(false, 0, "ParserConfigurationException",
					"ParserConfigurationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)+ SMS_DELIVERY_FAILED);
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);			
			return deliveryResp;
		} catch (SAXException e) {
			logger.error(request, "SAXException", e);
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, "SAXException");
			deliveryResp =     COUtil.createCOResponse(false, 0, "SAXException",
					"SAXException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)+ SMS_DELIVERY_FAILED);
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);			
			return deliveryResp;
		} catch (InstantiationException e) {
			logger.error(request, "InstantiationException", e);
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, "InstantiationException");
			deliveryResp =      COUtil.createCOResponse(false, 0, "InstantiationException",
					"InstantiationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)+ SMS_DELIVERY_FAILED);
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);			
			return deliveryResp;
		} catch (IllegalAccessException e) {
			logger.error(request, "IllegalAccessException", e);
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, "IllegalAccessException");
			deliveryResp =      COUtil.createCOResponse(false, 0, "IllegalAccessException",
					"IllegalAccessException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)+ SMS_DELIVERY_FAILED);
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);			
			return deliveryResp;
		} catch (ClassNotFoundException e) {
			logger.error(request, "ClassNotFoundException", e);
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, "ClassNotFoundException");
			deliveryResp =       COUtil.createCOResponse(false, 0, "ClassNotFoundException",
					"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)+ SMS_DELIVERY_FAILED);
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);			
			return deliveryResp;
		}catch (Exception e) {
			logger.error(request, "Exception", e);
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);
			additionalCustomerData.put(connector_response, "Exception");
			deliveryResp =       COUtil.createCOResponse(false, 0, "Exception",
					"Exception",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)+ SMS_DELIVERY_FAILED);
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);			
			return deliveryResp;
		}
		
		deliveryResp = (COResponse)listFrmDeliverCall.get(0);
						
		logger.info(request, "sendSMSOTP():: listFrmDeliverCall size"+listFrmDeliverCall.size());
		
		/*
		 * We need to retrieve the Mobile Number from the back-end if it is not uploaded
		 * in our DB.
		 */
		
		if(mobileNumber == null || "".equals(mobileNumber)){
		
				if(listFrmDeliverCall.size()>1){
					if (listFrmDeliverCall.get(1) !=null && ((String)listFrmDeliverCall.get(1)).length()> 0)
						mobileNumber = (String)listFrmDeliverCall.get(1);
					for(int i=1;i<listFrmDeliverCall.size();i++){
						logger.trace(request, "sendSMSOTP():: calloutData "+i+" =>"+listFrmDeliverCall.get(i));
						calloutData.add((String)listFrmDeliverCall.get(i));
					}
					logger.info(request, "sendSMSOTP():: calloutData size"+calloutData.size());
				}
				
				if (mobileNumber != null){
					logger.trace(request, "sendSMSOTP():: Mobile Number fetched from deliverOTP Method - "+mobileNumber);
					logger.info(request, "sendSMSOTP():: Mobile Number fetched from deliverOTP Method - "+mobileNumber.length());
				
				}else{
					
					logger.info(request, "sendOTP():: Mobile Number NOT fetched from deliverOTP Method");
				
				}
		}
		
		if ("1".equals(Integer.toString(deliveryResp.getAcsCRC()))){
			
			additionalCustomerData.put(success_msg, SMS_DELIVERY_SUCCESS);
			
			/**
			 * Check if MOBILE is supported; i.e. do we have to show 
			 */
			
			if (Boolean.valueOf(config.getValue(MOBILE_NUMBER_ON_PAGE))){
				
				if (resentCounter > 0){
					
					logger.info(request, "sendSMSOTP():: RESENT FLOW - Counter - "+resentCounter);
					
					if (mobileNumber != null){
						
						logger.trace(request, "sendSMSOTP():: RESENT FLOW - Mobile Number fetched from CALLOUTDATA - "+mobileNumber);
						logger.info(request, "sendSMSOTP():: RESENT FLOW - Mobile Number fetched from CALLOUTDATA - "+mobileNumber.length());
					
					}else{
						
						// safe Check , this should never happen
						
						return COUtil.createCOResponse(false, 0, "RESENT_FLOW[NoMobileNumber]", "RESENT_FLOW[NoMobileNumber]",
								ACS_CALLOUT_PARES_N, getLocaleName(request, logger) + "[RESENT_FLOW[NoMobileNumber]]");
					}
					
					if(calloutData.size() > 0)
						calloutData.set(0, mobileNumber);
					else
						calloutData.add(0, mobileNumber);
				}else{
				
					logger.info(request, "sendSMSOTP():: NORMAL FLOW - Mobile Number to be Stored in CALLOUTDATA");
					mobileNumber = populateMobileNumber(config, request, mobileNumber, objChBean);
					
					if (mobileNumber == null){
						/**
						 * will be NULL, if:-
						 * 1.  backend returns no mobile number
						 * 2.  not stored in AHA
						 * 
						 * 3.1 not stored in ARISSUERANSWER
						 * OR
						 * 3.2 stored in ARISSUERANSWER, but MOBILE_QUES_ID is not configured   
						 */
						return noMobilePresentAction(request, config, additionalCustomerData);
					}else{
						if(calloutData.size() > 0)
							calloutData.set(0, mobileNumber);
						else
							calloutData.add(0, mobileNumber);
					}
				
				}
			}else{
				
				logger.info(request, "sendSMSOTP():: Mobile number not to be shown on page");
			
			}
			
			if(isResend){
					/**
					 * store the OTP if the Validator is DBValidator and increment resend.
					 */
					resentCounter = resentCounter + 1;
					/*
					 * 
					 */
					
					additionalCustomerData.put(connector_response, deliveryResp.getCalloutMessage());
					deliveryResp.setAdditionalCustomerData(additionalCustomerData);
					return storeOTPOnDeliverySuccess(config, request, strOTP,
							resentCounter, mobileNumber, null, isDBValidator, deliveryResp, localeName, deliveryResp.getAdditionalCustomerData());
			}else{
				additionalCustomerData.put(connector_response, deliveryResp.getCalloutMessage());
				deliveryResp.setAdditionalCustomerData(additionalCustomerData);
				return deliveryResp;
			}
			
		}else{
			additionalCustomerData.put(error_msg, SMS_DELIVERY_FAILED);		
			additionalCustomerData.put(connector_response, deliveryResp.getCalloutMessage());
			// if CRC is anything other than ACS_CALLOUT_SUCCESS, 
			// let the deliverOTP() method decide what needs to be done
			deliveryResp.setAdditionalCustomerData(additionalCustomerData);
			return deliveryResp;
		}
	}

	/**
	 * This is the handle provided for BusinessLogicHandlers where in if the 
	 * Mobile Number will be provided by the Handler for OTP delivery
	 * 
	 * @param request
	 * @param config
	 * @return Mobile Number
	 */
	public String provideMobileNumber(ACSRequest request, COConfig config) {
		return null;
	}

	/**
	 * Determine the Maxdecline for the Brand that the range belongs to.
	 * 
	 * @param ACSRequest
	 * @param COConfig
	 * 
	 * return MaxDecline
	 */
	private int getMaxDecline(ACSRequest request, COConfig config){
		int maxDecline = 0;
		int bankid = request.getBankId();
		String cardNumClear = request.getCardNumber();
		
		Brand brandInfo = new Brand();
		brandInfo = ESCache.bric.getBrand(bankid, cardNumClear);
		
		maxDecline = brandInfo.getMaxDeclines();
		
		return maxDecline;
	}
	
	/**
	 * if 
	 * 	mobileNumber is not null, return the mobile number - this would mean we get the mobile number from backend
	 * else
	 *  get it from AHA or IQA
	 * 
	 * @param config
	 * @param request
	 * @param mobileNumber
	 * @param objChBean 
	 * @return
	 */
	public String populateMobileNumber(COConfig config, ACSRequest request,
			String mobileNumber, CardHolderBean objChBean) {
		
		COLogger logger = config.getLogger();
		logger.info(request, "populateMobileNumber()");
		
		if (mobileNumber != null){
		
			return mobileNumber;
		
		}else{
			if (Boolean.valueOf(config.getValue(MOBILE_NUMBER_IN_AHA))){
				
				if(objChBean.getMobilePhoneEnc() != null && !"".equals(objChBean.getMobilePhoneEnc())){
					logger.info(request, "populateMobileNumber() :: Mobile Number fetched from ARACCTHOLDERAUTH - " + objChBean.getMobilePhoneEnc());
					return EnrollmentCrypto.decrypt(objChBean.getMobilePhoneEnc(), request.getBankId());
				}else{
					logger.info(request, "The Mobile Number was not retreived from ARACCTHOLDERAUTH.");
					return null;
				}
					
			
			}else {
				
				if (config.getValue(MOBILE_QUES_ID) != null){
					
					logger.info(request, "populateMobileNumber():: MOBILE_QUES_ID configured - "+config.getValue(MOBILE_QUES_ID));

				}else{

					logger.info(request, "populateMobileNumber():: No QuestionID configured for Mobile Number");
					return null;
					
				}
				
				try {
					mobileNumber = QueryByCardNumberQuestionID(request.getCardNumber(), config.getIntValue(MOBILE_QUES_ID),
							ESCache.bic.getBank(request.getBankId()).BANKKEY_CLEAR, request.getBankId(), config);
				} catch (ArcotException e) {
					logger.error(request, "populateMobileNumber():: ", e);
					return null;
				}
				
				if(mobileNumber != null){
					logger.info(request, "The Mobile Number was retrieved from ARISSUERANSWERS.");
					return mobileNumber;
				}else{
					logger.info(request, "The Mobile Number was not retreived from ARISSUERANSWERS.");
					return null;
				}
			}
		}
	}
	
	/**
	 * if 
	 * 	email is not null, return the email address - this would mean we get the email from backend
	 * else
	 *  get it from AHA or IQA
	 * 
	 * @param config
	 * @param request
	 * @param mobileNumber
	 * @param objChBean 
	 * @return
	 */
	public String populateEmail(COConfig config, ACSRequest request,
			String email, CardHolderBean objChBean) {
		
		COLogger logger = config.getLogger();
		logger.info(request, "populateEmail()");
		
		if (email != null){
		
			return email;
		
		}else{
			if (Boolean.valueOf(config.getValue(EMAIL_IN_AHA))){
				
				if(objChBean.getEmailAddressClear() != null && !"".equals(objChBean.getEmailAddressClear())){
					logger.info(request, "populateEmail() :: Email Address fetched from ARACCTHOLDERAUTH Length - " + objChBean.getEmailAddressClear().length());
					return objChBean.getEmailAddressClear();
				}else{
					logger.info(request, "The Email Address was not retreived from ARACCTHOLDERAUTH.");
					return null;
				}
					
			
			}else {
				
				if (config.getValue(EMAIL_QUES_ID) != null){
					
					logger.info(request, "populateEmail():: EMAIL_QUES_ID configured - "+config.getValue(EMAIL_QUES_ID));

				}else{

					logger.info(request, "populateEmail():: No QuestionID configured for Email Address");
					return null;
					
				}
				
				try {
					email = QueryByCardNumberQuestionID(request.getCardNumber(), config.getIntValue(EMAIL_QUES_ID),
							ESCache.bic.getBank(request.getBankId()).BANKKEY_CLEAR, request.getBankId(), config);
				} catch (ArcotException e) {
					logger.error(request, "populateEmail():: ", e);
					return null;
				}
				
				if(email != null){
					logger.info(request, "The Email was retrieved from ARISSUERANSWERS.");
					return email;
				}else{
					logger.info(request, "The Email was not retreived from ARISSUERANSWERS.");
					return null;
				}
			}
		}
	}

	
	/*
	 * To be overridden by all handlers in case you want to add extra data in calloutData, Database
	 * 
	 * always start with '~' if you are adding valuea and '~' between each values
	 */
	public String provideExtraCalloutData(ACSRequest request, COConfig config, List<String> calloutData){
		String str = "";
		if (calloutData.size()>0){
			for(int i=0; i<calloutData.size();i++){
				str += calloutData.get(i)+ "~";
			}
			return str;
			
		}
		return "";
	}

	/**
	 * 
	 * Since in OTP we can get the mobile number from backend, the sentOTP() , uses returnList[0] as 'COResponse' and returnList[1] as 'mobileNumber'
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws InvalidMessageException 
	 * @throws IOException 
	 * @throws NullPointerException 
	 * @throws IllegalArgumentException 
	 * @throws MalformedURLException 
	 */
	@SuppressWarnings("unchecked")
	public List<Object> deliverOTPViaSMS( COConfig config, ACSRequest request, String strOTP, String mobileNumber) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedURLException,
			IllegalArgumentException, NullPointerException, IOException, Exception,ParserConfigurationException, SAXException, IOException{
		List<Object> returnList = new ArrayList<Object>();
		COLogger logger = config.getLogger();
		ConnectorResponse connectorResponse = new ConnectorResponse();
		String otpMessage = null;
		ArrayList<String> calloutConfigKeys = null;
		HashMap<String, String> calloutConfigVals = null;
		
		/*
		 * Getting the OTP SMS Text. Also Checking for Locale Specific SMS Text.
		 * If the keys like MESSAGE_TEXT_en_GB or MESSAGE_TEXT_de_DE are present
		 * they will be sent to the connector as the SMS Text.
		 */
		calloutConfigKeys = new ArrayList<String>(config.getAll().keySet());
		
		if(calloutConfigKeys != null && calloutConfigKeys.size() > 0 && calloutConfigKeys.contains(MESSAGE_TEXT.toLowerCase()
				+ "_" + getLocaleName(request, logger).toLowerCase())){
			logger.info(request, "Locale Specific SMS Text is configured.");
			otpMessage = config.getValue(MESSAGE_TEXT + "_" + getLocaleName(request, logger));
		}else{
			logger.info(request, "Locale Specific SMS Text is not configured taking the default Key Value.");
			if(config.getValue(MESSAGE_TEXT) == null || "".equals(config.getValue(MESSAGE_TEXT))){
				logger.info(request, "The MESSAGE_TEXT key is not set in the callout config returning null.");
				otpMessage = "OTP Text";
			}else{
				otpMessage = config.getValue(MESSAGE_TEXT);
			}
		}
		
		/*
		 * Construct the OTP Message to be sent to the Connector after replacing the
		 * placeholders.
		 */
		otpMessage = createOTPMessage(config, request, strOTP, logger, otpMessage);
		
		/*
		 * Pass it through the URL Decoder and send the OTP message to the Connector this is used to send
		 * special characters from the UI to the connector.
		 */
		if(otpMessage != null)
			otpMessage = URLDecoder.decode(otpMessage, "UTF-8");
		
		/*
		 * Call the Connector Class and pass the HashList of Parameters required.
		 * calloutConfigVals = new HashMap(config.getAll());
		 */
		
		calloutConfigVals = new HashMap(config.getAll());
		calloutConfigVals.put(CARDNUMBER_CLEAR.toLowerCase(), request.getCardNumber());
		String cardtype = getRangeType(request,config);
		calloutConfigVals.put(CARDNUMBER_TYPE, cardtype);
		
		Connector otpConnector = Connector.getInstance(config.getValue(SMS_CONNECTOR), calloutConfigVals);
		connectorResponse = otpConnector.deliver(calloutConfigVals, mobileNumber, otpMessage);
		
		logger.info(request, "Connector Response: Result[" + connectorResponse.getResult() + "] Short Error Message[" + connectorResponse.getShortErrorMessage()
				+ "] Long Error Message[" + connectorResponse.getLongErrorMessage() + "]");
		
		/*
		 * If the Response is successful
		 */
		if(connectorResponse.getResult()){
			returnList.add(COUtil.createCOResponse(connectorResponse.getResult(), 0, connectorResponse.getShortErrorMessage(),
					connectorResponse.getLongErrorMessage() , ACS_CALLOUT_SUCCESS, connectorResponse.getResponseCode(), request.getFolderId()));
		}else{
			returnList.add(COUtil.createCOResponse(connectorResponse.getResult(), 0, connectorResponse.getShortErrorMessage(),
					connectorResponse.getLongErrorMessage() , ACS_CALLOUT_PARES_N, connectorResponse.getResponseCode(), request.getFolderId()));
		}
		
		/*
		 * Set Mobile Number Sent From the Clients Back-end
		 */
		if(connectorResponse.getMobileNumber()!= null && !"".equals(connectorResponse.getMobileNumber()))
			returnList.add(connectorResponse.getMobileNumber());
		
	
		return returnList;
	}
	
	public String getRangeType(ACSRequest request, COConfig config){
		COLogger logger = config.getLogger();
		Brand brandInfo = ESCache.bric.getBrand(request.getRangeId());
		String cardType = brandInfo.getCardType();
		if(cardType.equals("1") || cardType.equals("3")){
			logger.info(request,"It is an Credit card");
			return "Credit";
		} else if(cardType.equals("2") || cardType.equals("4")) {
			logger.info(request,"It is an Debit card");
			return "Debit";
		} else {
			logger.info(request,"Unable to detect the card type");
			return "null";
		}
	}
	/**
	 * 
	 * Since in OTP we can get the email address from backend, the sentOTP() , uses returnList[0] as 'COResponse' and returnList[1] as 'emailAddress'
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws InvalidMessageException 
	 * @throws IOException 
	 * @throws NullPointerException 
	 * @throws IllegalArgumentException 
	 * @throws MalformedURLException 
	 */
	@SuppressWarnings("unchecked")
	public List<Object> deliverOTPViaEmail( COConfig config, ACSRequest request, String strOTP, String emailAddress) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedURLException,
			IllegalArgumentException, NullPointerException, IOException, Exception,ParserConfigurationException, SAXException, IOException{
		List<Object> returnList = new ArrayList<Object>();
		COLogger logger = config.getLogger();
		ConnectorResponse connectorResponse = new ConnectorResponse();
		String emailTemplateLocation = null;
		String otpMessage = null;
		ArrayList<String> calloutConfigKeys = null;
		String emailSubject = null;
		HashMap<String, String> calloutConfigVals = null;
		
		/*
		 * Getting the OTP Email Text. Also Checking for Locale Specific Email Text.
		 * If the keys like EMAIL_TEMPLATE_PATH_en_GB or EMAIL_TEMPLATE_PATH_de_DE are present
		 * they will be sent to the connector as the Email Text.
		 */
		calloutConfigKeys = new ArrayList<String>(config.getAll().keySet());
		
		emailTemplateLocation = extractCalloutConfigDetails(config, request,
				logger, emailTemplateLocation, calloutConfigKeys, EMAIL_TEMPLATE_PATH);
		
		if(emailTemplateLocation != null && !"".equals(emailTemplateLocation)){
			otpMessage = parseFile(emailTemplateLocation);
		}else{
			logger.info(request, "The Email Template Path is not provided. Return and send a Pares of N.");
			returnList.add(COUtil.createCOResponse(connectorResponse.getResult(), 0, connectorResponse.getShortErrorMessage(),
					connectorResponse.getLongErrorMessage() , ACS_CALLOUT_PARES_N, getLocaleName(request, logger), request.getFolderId()));
			return returnList;
		}
		
		/*
		 * Similarly check for Locale specific email subject and set it in the HashMap.
		 */
		emailSubject = extractCalloutConfigDetails(config, request,
				logger, emailSubject, calloutConfigKeys, EMAIL_SUBJECT);
		
		/*
		 * Construct the OTP Email and Subject to be sent to the Connector after replacing the
		 * placeholders.
		 */
		
		otpMessage = createOTPMessage(config, request, strOTP, logger, otpMessage);
		emailSubject = createOTPMessage(config, request, strOTP, logger, emailSubject);
		
		/*
		 * Pass it through the URL Decoder and send the OTP message to the Connector this is used to send
		 * special characters from the UI to the connector.
		 */
		
		emailSubject = URLDecoder.decode(emailSubject, "UTF-8");
		/*
		 * Call the Connector Class and pass the HashList of Parameters required.
		 */
		
		calloutConfigVals = config.getAll();
		calloutConfigVals.put(EMAIL_SUBJECT.toLowerCase(), emailSubject);
		
		Connector otpConnector = Connector.getInstance(config.getValue(EMAIL_CONNECTOR), calloutConfigVals);
		connectorResponse = otpConnector.deliver(calloutConfigVals, emailAddress, otpMessage);
		
		logger.info(request, "Connector Response: Result[" + connectorResponse.getResult() + "] Short Error Message[" + connectorResponse.getShortErrorMessage()
				+ "] Long Error Message[" + connectorResponse.getLongErrorMessage() + "]");
		
		/*
		 * If the Response is successful
		 */
		if(connectorResponse.getResult()){
			returnList.add(COUtil.createCOResponse(connectorResponse.getResult(), 0, connectorResponse.getShortErrorMessage(),
					connectorResponse.getLongErrorMessage() , ACS_CALLOUT_SUCCESS, connectorResponse.getResponseCode(), request.getFolderId()));
		}else{
			returnList.add(COUtil.createCOResponse(connectorResponse.getResult(), 0, connectorResponse.getShortErrorMessage(),
					connectorResponse.getLongErrorMessage() , ACS_CALLOUT_PARES_N, connectorResponse.getResponseCode(), request.getFolderId()));
		}
		
		/*
		 * Set Email Address Sent From the Clients Back-end
		 */
		if(connectorResponse.getEmailAddress()!= null && !"".equals(connectorResponse.getEmailAddress()))
			returnList.add(connectorResponse.getEmailAddress());
		
		return returnList;
	}

	/**
	 * This method will extract the locale specific email content
	 * and email subject if required.
	 * 
	 * @param config
	 * @param request
	 * @param logger
	 * @param emailTemplateLocation
	 * @param calloutConfigKeys
	 * @return
	 */
	private String extractCalloutConfigDetails(COConfig config,
			ACSRequest request, COLogger logger, String emailContent,
			ArrayList<String> calloutConfigKeys, String calloutConfigKey) {
		if(calloutConfigKeys != null && calloutConfigKeys.size() > 0 && calloutConfigKeys.contains(calloutConfigKey.toLowerCase()
				+ "_" + getLocaleName(request, logger).toLowerCase())){
			logger.info(request, "Locale Specific Email content has been configured.");
			emailContent = config.getValue(calloutConfigKey + "_" + getLocaleName(request, logger));
		}else{
			logger.info(request, "Locale Specific Email content has not been configured, taking the default key value.");
			/*
			 * Getting the Email Template Location.
			 */
			if(config.getValue(calloutConfigKey) == null || "".equals(config.getValue(calloutConfigKey))){
				logger.info(request, "The " + calloutConfigKey + " is not set in the callout config returning null.");
			}else{
				emailContent = config.getValue(calloutConfigKey);
			}
		}
		return emailContent;
	}
	
	private String parseFile(String filePath)
		    throws FileNotFoundException, IOException
		       {
		           BufferedReader in = null;
		           in = new BufferedReader(new FileReader(filePath));
		           StringBuffer strBuff = new StringBuffer();
		           for(String lines = null; (lines = in.readLine()) != null;)
		           {
		               strBuff.append(lines);
		           }
		       
		           in.close();
		           return strBuff.toString();
		       }


	/**
	 * The OTP Message placeholders are replaced with the actual data before being sent
	 * to the connector to be delivered.
	 * PlaceHolders allowed: 
	 * ${otp} 					- Otp Value
	 * ${last4digits} 			- Last 4 Digits of the CardNumber
	 * ${transaction_amt}	  	- Transaction Amount of the CardHolder
	 * ${transaction_currency}	- Transaction Currency
	 * ${merchant_name}  		- Merchant Name
	 * ${bank_name}				- Bank Name
	 * ${transaction_time}		- Transaction Time.
	 * ${cardholder_name}		- Cardholder Name
	 * 
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @param otpMessage 
	 * @return OTP Message Text
	 */
	private String createOTPMessage(COConfig config, ACSRequest request,
			String strOTP, COLogger logger, String otpMessage) {
		String currencyName = null;
		String bankName = null;
		
		String cardNumber = request.getCardNumber();
		int bankId = request.getBankId();
		
		BankInfo bankInfo = ESCache.bic.getBank(bankId);
		bankName = bankInfo.BANKNAME;
		
		Map<String, String> otpMessageTextValues = new HashMap<String, String>();
		
		/*
    	 * Determine the Transaction Amount, Date Time, Currency, Merchant Code
    	 * and Merchant Name from the ACSXML Handler.
    	 */
		
		ACSPARequest paReq = ACSXMLHandler.getParsedPAReq(request, logger);
		
		/*
    	 * Determine the Transaction Currency Name by passing the currency code
    	 */
    	try {
    		currencyName = getCurrType(config, paReq.getPurchaseCurrency(), request);
    	} catch (ArcotException e) {
    		e.printStackTrace();
    		logger.error(request, "ArcotException while calulating Currency Code", e);
    	}
		
		otpMessageTextValues.put(otp, strOTP);
		otpMessageTextValues.put(last4digits, cardNumber.substring(cardNumber.length()-4, cardNumber.length()));
		otpMessageTextValues.put(transaction_amt, getPurchaseAmount(paReq));
		otpMessageTextValues.put(transaction_currency, currencyName);
		otpMessageTextValues.put(merchant_name, paReq.getMerchantName());
		otpMessageTextValues.put(bank_name, bankName);
		otpMessageTextValues.put(transaction_time, getPurchaseDate(config, paReq.getPurchaseDate()));
		otpMessageTextValues.put(cardholder_name, request.getCardholderName());
		otpMessageTextValues.put(email_images_path, config.getValue(email_images_path));
		otpMessageTextValues.put(card_type, getCardType(request,logger));
		otpMessage = StrSubstitutor.replace(otpMessage, otpMessageTextValues);
		
		return otpMessage;
	}
	
	private String getCardType(ACSRequest request, COLogger logger) {
		
		Brand brandInfo = ESCache.bric.getBrand(request.getRangeId());
		String cardType = brandInfo.getCardType();
		if(cardType.equals("1") || cardType.equals("2")){
			logger.info(request,"It is an visa card");
			return "Visa";
		} else if(cardType.equals("3") || cardType.equals("4")) {
			logger.info(request,"It is an mc card");
			return "MasterCard";
		} else if(cardType.equals("5")){
			logger.info(request,"It is an amex card");
			return "American Express";
		} else {
			logger.info(request,"Unable to detect the card type");
			return "null";
		}
		
	}

	/**
	 * This method will return the Transaction Date in Calender Type
	 * @param COConfig
	 * @param purchaseDate
	 * @return Purchase Date Time
	 */
	private String getPurchaseDate(COConfig config, String purchaseDate) {
		/*
		 * Transaction Date Time value creating the calendar value
		 */
		String transDateFormat = null;
		String purchaseDateTime = null;
		
		transDateFormat = readConfigValue(config, TRANSACTION_DATE_FORMAT, DefaultTransDateFormat);
		
		DateFormat purchaseDateFormat = new SimpleDateFormat(transDateFormat);
		purchaseDateTime = purchaseDateFormat.format(new Date());	
		
		return purchaseDateTime;
	}
	
	/**
	   * This method will return the Currency Type
	   * with the currency code provided as input
	   * @param config
	   * @param currCode
	   * @return
	   * @throws ArcotException
	   */
	  private String getCurrType(COConfig config, String currCode, ACSRequest request) throws ArcotException
		{
			COLogger logger = config.getLogger();
			DatabaseConnection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String currType = null;
			
				try
				{
					conn = dbMan.getConnection();
					if (conn != null && !conn.isClosed())
					{
						stmt = conn.prepareStatement(getCurrType);
						stmt.setInt(1, Integer.parseInt(currCode));

						rs = stmt.executeQuery();
						if(rs.next())
						{
							currType = rs.getString("CURRTYPE");
						}

					}
					else
						throw new ArcotException(ArcotException.AE_WEBADMIN_DATABASE_CONNECTION_FULL);	//E7262
				return currType;
				}
				catch (SQLException sqle)
				{
					logger.error(request,"Error in select ARCURRENCY", sqle);
					try {
						if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()));
					} catch ( SQLException sqle11 ) {
						logger.error(request, "SQLException", sqle11);
					}

					throw new ArcotException(ArcotException.AE_DATABASE_NOT_AVAILABLE);
				}
				finally
				{
					try
					{
						if ( rs != null )
							rs.close();
						if ( stmt != null )
							stmt.close();
					}
					catch ( SQLException sqle )
					{}

					if(conn != null)
						dbMan.release(conn);
				}
		}
	  
	  /**
	   * This method will update the LocaleId in AHA
	   * with the locale name provided as input
	   * @param config
	   * @param currCode
	   * @return
	   * @throws ArcotException
	   */
	  private boolean updateLocaleInAHA(COConfig config, String localeName, ACSRequest request) throws ArcotException
		{
			COLogger logger = config.getLogger();
			DatabaseConnection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			int localeID = 0;
			int rowsUpdated = 0;
			
			localeID = Integer.parseInt(COUtil.getLocaleId(localeName));
			
			logger.info(request, "updateLocaleInAHA: The Locale Name: " + localeName + " LocaleID: " + localeID);
			
			if(localeID <=0){
				logger.info(request, "The Locale Name: "+ localeName + " is Not Set up in the DB returning without updating AHA.");
				return false;
			}
			
				try
				{
					conn = dbMan.getConnection();
					if (conn != null && !conn.isClosed())
					{
						stmt = conn.prepareStatement(AdminQueryStrings.updateCHLocale);
						stmt.setInt(1, localeID);
						stmt.setString(2, EnrollmentCrypto.encrypt(request.getCardNumber(), request.getBankId()));

						rowsUpdated = stmt.executeUpdate();
						
						if(rowsUpdated > 0){
							logger.info(request, "The LocaleID of the cardholder is updated in AHA.");
							return true;
						}else{
							logger.info(request, "The LocaleID of the cardholder was not updated in AHA.");
							return false;
						}
						

					}
					else{
						throw new ArcotException(ArcotException.AE_WEBADMIN_DATABASE_CONNECTION_FULL);	//E7262
					}
				}
				catch (SQLException sqle)
				{
					logger.error(request,"Error in updateCHLocale", sqle);
					try {
						if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()));
					} catch ( SQLException sqle11 ) {
						logger.error(request, "SQLException", sqle11);
					}

					throw new ArcotException(ArcotException.AE_DATABASE_NOT_AVAILABLE);
				}
				finally
				{
					try
					{
						if ( rs != null )
							rs.close();
						if ( stmt != null )
							stmt.close();
					}
					catch ( SQLException sqle )
					{}

					if(conn != null)
						dbMan.release(conn);
				}
		}

	/**
	   * This method will provide the Purchase Amount
	   * of the cardholder
	   * @param paReq
	   * @return
	   */
	  private String getPurchaseAmount(ACSPARequest paReq) {
		  
		String purchaseRawAmount = paReq.getPurchaseRawAmount();
		String purchaseExponent = paReq.getPurchaseExponent();
		String newAmount = purchaseRawAmount.substring(0, purchaseRawAmount.length()
				- Integer.parseInt(purchaseExponent)) + "." + purchaseRawAmount.substring(purchaseRawAmount.length() - Integer.parseInt(purchaseExponent));			
		return newAmount;
	}
	
	/**
	 * 
	 * @param request
	 * @param config
	 * @param localeToSwitch 
	 * @param objChBean 
	 * @return COResponse
	 */
	public COResponse moveToOTPPage(ACSRequest request, COConfig config, String localeToSwitch, CardHolderBean objChBean, HashMap<String, Object> additionalCustomerData) {
		
		COLogger logger = config.getLogger();
		GeneratorResponse generatorResponse = new GeneratorResponse();
		String strOTP = null;
		
		logger.info(request, "moveToOTPPage()");
		
		/*
		 * Method call to create OTP
		 */
		try {
			generatorResponse = createOTP(config, request, config.getValue(OTP_TYPE), config.getIntValue(OTP_LENGTH));
		} catch (InstantiationException e) {
			logger.error(request, "InstantiationException", e);
			return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "InstantiationException",
					"InstantiationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
		} catch (IllegalAccessException e) {
			logger.error(request, "IllegalAccessException", e);
			return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "IllegalAccessException",
					"IllegalAccessException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
		} catch (ClassNotFoundException e) {
			logger.error(request, "ClassNotFoundException", e);
			return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "ClassNotFoundException",
					"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
		}
		
		logger.info(request, "Generator Response: Result[" + generatorResponse.getResult() + "] Short Error Message[" + generatorResponse.getShortErrorMessage()
				+ "] Long Error Message[" + generatorResponse.getLongErrorMessage() + "]");
		
		/*
		 * Verifying the Generator's Result, if False we will return with Pares of N.
		 */
		if(!generatorResponse.getResult()){
			/*
			 * The OTP Generation Failed.
			 */
			return generatorFailureResponse(generatorResponse);
		}
		
		strOTP = generatorResponse.getOtpValue();
		
		COResponse objCOresp;
		try {
			objCOresp = sendOTP(config, request, strOTP, 0, objChBean, false, additionalCustomerData);
		} catch (InstantiationException e) {
			logger.error(request, "InstantiationException", e);
			return COUtil.prepareACSResponse(request,COUtil.createCOResponse(true, 0, "InstantiationException",
					"InstantiationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
		} catch (IllegalAccessException e) {
			logger.error(request, "IllegalAccessException", e);
			return COUtil.prepareACSResponse(request,COUtil.createCOResponse(true, 0, "IllegalAccessException",
					"IllegalAccessException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
		} catch (ClassNotFoundException e) {
			logger.error(request, "ClassNotFoundException", e);
			return COUtil.prepareACSResponse(request,COUtil.createCOResponse(true, 0, "ClassNotFoundException",
					"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger)));
		}
		objCOresp.setCalloutMessage(setCalloutMsg(request, config, objCOresp.getCalloutMessage()));
		return objCOresp;
	}

	/*
	 * The Generator Failure Response will be created.
	 */
	private COResponse generatorFailureResponse(
			GeneratorResponse generatorResponse) {
		COResponse otpGenerationResponse = new COResponse();
		otpGenerationResponse.setAcsCRC(ACS_CALLOUT_PARES_N);
		otpGenerationResponse.setLogToDB(generatorResponse.getShortErrorMessage());
		otpGenerationResponse.setLogToFile(generatorResponse.getLongErrorMessage());
		return otpGenerationResponse;
	}
	
	/**
	 * Based on a Parameter show the mobie number
	 * 
	 * @param request
	 * @param config
	 * @param strOTP
	 * @param CardHolderBean
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public COResponse validateOTP(ACSRequest request, COConfig config, String strOTP, CardHolderBean objChBean, HashMap<String, Object> additionalCustomerData) {
		
		COLogger logger = config.getLogger();
		logger.info(request, "validateOTP()");
		String otpValidatorType = null;
		OTPValidator otpValidator = null;
		ValidatorResponse response = null;
		String localeName = null;
		String mobNumber=null;
		
		/*
		 * Determine Locale Name
		 */
		localeName = COUtil.getLocaleName(Integer.toString(objChBean.getLocaleId()));
		
		otpValidatorType = readConfigValue(config, VALIDATOR, DEFAULT_VALIDATOR);
		
		mobNumber=EnrollmentCrypto.decrypt(objChBean.getMobilePhoneEnc(), request.getBankId());
		
		/*
		 * If the Validator is DB OTP Validator we have generated the OTP and stored it
		 * in our DB hence we will be performing the validation.
		 */
		if(DEFAULT_VALIDATOR.equals(otpValidatorType)){
				return DBValidator.performDBValidation(request, config, strOTP, logger, localeName, additionalCustomerData);
			}else{
				/*
				 * The OTP Validation is external we will create a Validator Object and make the call
				 * to Validate.
				 */
				try {
					otpValidator = OTPValidator.getInstance(otpValidatorType, config.getAll());
				} catch (InstantiationException e) {
					logger.error(request, "InstantiationException", e);
					return COUtil.createCOResponse(true, 0, "InstantiationException",
							"InstantiationException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger));
				} catch (IllegalAccessException e) {
					logger.error(request, "IllegalAccessException", e);
					return COUtil.createCOResponse(true, 0, "IllegalAccessException",
							"IllegalAccessException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger));
				} catch (ClassNotFoundException e) {
					logger.error(request, "ClassNotFoundException", e);
					return COUtil.createCOResponse(true, 0, "ClassNotFoundException",
							"ClassNotFoundException",ACS_CALLOUT_PARES_N, getLocaleName(request, logger));
				}
				/*
				 * We can set other parameters based on the external call.
				 */
		
				HashMap<String, String> conf = new HashMap<String, String>();
				
				conf.putAll(customConfigData(request, config,objChBean));
				conf.putAll(config.getAll());
				conf.put(OTP, strOTP);
				conf.put(CARDNUMBER_CLEAR.toLowerCase(), request.getCardNumber());
				conf.put(mobnum, mobNumber);
				response = otpValidator.validateOTP(conf);
				
				logger.info(request, "Validator Response: Result[" + response.getResult() + "] Short Error Message[" + response.getShortErrorMessage()
						+ "] Long Error Message[" + response.getLongErrorMessage() + "]");
				
				if (response.getResult()){
					
					/*
					 * If the response is a success
					 */
					return COUtil.createCOResponse(true, 0, response.getLongErrorMessage(), response.getShortErrorMessage(),
							ACS_CALLOUT_SUCCESS, getLocaleName(request, logger), request.getFolderId());
				
				}else{
					if(additionalCustomerData == null)
					additionalCustomerData = new HashMap<String, Object>();
					
					String timeBetweenResends = ((config.getValue(TIME_BETWEEN_RESENDS) != null)) ? config.getValue(TIME_BETWEEN_RESENDS) : "";

					additionalCustomerData = DBValidator.setCalloutMsgForOTPPageOnFailure(request, config, additionalCustomerData);
					
					COResponse coResponse =  COUtil.createCOResponse(false, 0, "OTP[f]", "OTP[f]", ACS_CALLOUT_FAIL,"", 
							request.getFolderId());
					additionalCustomerData.put(GenericOTPHandler.time_period, timeBetweenResends);
					coResponse.setAdditionalCustomerData(additionalCustomerData);
					return coResponse;
					
				}
				
			}
	}
	public HashMap<String, String> customConfigData(ACSRequest request, COConfig config, CardHolderBean objChBean){
		
		return new HashMap<String, String>();
	}
    /**
     * This method's ACS response code is dependent on the Callout Config Param
     * NO_MOBILE_PRESENT_ACTION.
     * 
     * @param request
     * @param config
     * @return
     */
	public COResponse noMobilePresentAction(ACSRequest request, COConfig config, HashMap<String, Object> additionalCustomerData) {
		
		COLogger logger = config.getLogger();
		logger.info(request, "noMoBilePresentAction()");
		COResponse coResponse = null;
		
		if (config.getValue(NO_MOBILE_PRESENT_ACTION) != null){
			logger.info(request, "NO_MOBILE_PRESENT_ACTION config has been Set.");
			coResponse = COUtil.createCOResponse(false, 0, NOMOBILEPRESENT, NOMOBILEPRESENT,
					retrieveACSResponse(config.getValue(NO_MOBILE_PRESENT_ACTION)), NOMOBILEPRESENT, request.getFolderId());
			additionalCustomerData.put(mobnum, NOMOBILEPRESENT);
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		}else{
			logger.info(request, "The Default behaviour of sending PARES_N is being executed as NO_MOBILE_PRESENT_ACTION is not set.");
			coResponse = COUtil.createCOResponse(false, 0, NOMOBILEPRESENT, NOMOBILEPRESENT, ACS_CALLOUT_SEND_PARES_N,
					getLocaleName(request, logger) + NOMOBILEPRESENT, request.getFolderId());
			additionalCustomerData.put(mobnum, NOMOBILEPRESENT);
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		}
	}	
	
	/*
	 * Retrieve the ACS Response from the Value sent by the Callout Config
	 */
	public static int retrieveACSResponse(String acsResponseValue) {
		if(pares_N.equalsIgnoreCase(acsResponseValue))
			return ACS_CALLOUT_PARES_N;
		else if(pares_U.equalsIgnoreCase(acsResponseValue))
			return ACS_CALLOUT_PARES_U;
		else if(txn_No_Action.equalsIgnoreCase(acsResponseValue))
			return ACS_CALLOUT_TX_NO_ACTION;
		else if(pares_a.equalsIgnoreCase(acsResponseValue))
			return ACS_CALLOUT_PARES_A;
		else if(pares_y.equalsIgnoreCase(acsResponseValue))
			return ACS_CALLOUT_PARES_Y;
		else if(pares_S_N.equalsIgnoreCase(acsResponseValue))
			return ACS_CALLOUT_SEND_PARES_N;
		return ACS_CALLOUT_PARES_N;
	}

	/**
     * This method's ACS response code is dependent on the Callout Config Param
     * NO_EMAIL_PRESENT_ACTION.
     * 
     * @param request
     * @param config
     * @return
     */
	public COResponse noEmailPresentAction(ACSRequest request, COConfig config, HashMap<String, Object> additionalCustomerData) {
		
		COLogger logger = config.getLogger();
		logger.info(request, "noEmailPresentAction()");
		COResponse coResponse = null;
		
		if (Boolean.valueOf(config.getValue(NO_EMAIL_PRESENT_ACTION))){
			logger.info(request, "NO_EMAIL_PRESENT_ACTION config has been Set.");
			coResponse = COUtil.createCOResponse(false, 0, NOEMAILPRESENT, NOEMAILPRESENT, retrieveACSResponse(NO_EMAIL_PRESENT_ACTION),
					getLocaleName(request, logger) + NOEMAILPRESENT, request.getFolderId());
			additionalCustomerData.put(email, NOEMAILPRESENT);
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		}else{
			logger.info(request, "The Default behaviour of sending PARES_N is being executed as NO_EMAIL_PRESENT_ACTION is not set.");
			coResponse =  COUtil.createCOResponse(false, 0, NOEMAILPRESENT, NOEMAILPRESENT, ACS_CALLOUT_PARES_N,
					getLocaleName(request, logger) + NOEMAILPRESENT, request.getFolderId());
			additionalCustomerData.put(email, NOEMAILPRESENT);
			coResponse.setAdditionalCustomerData(additionalCustomerData);
			return coResponse;
		}
	}	


	/**
	 * 
	 * To be overridden by handlers ONLY IF some info needs to be passed to the page
	 * This is the case when you are moving from Landing page to the OTP page.
	 * 
	 * Called from moveToOTPPage()  
	 * 
	 * Please make sure that the 'exsisting callout msg' is not lost
	 * 
	 * @return
	 */
	public String setCalloutMsg(ACSRequest request, COConfig config, String strExistingCalloutMsg) {
		return strExistingCalloutMsg;
	}


	/**
	 * Sets the callout Message on Resend Tries Exceeded.
	 * @param request
	 * @param config
	 * @return
	 */
	public String setCalloutMessageOnResendExceed(ACSRequest request,
			COConfig config) {
		return RESENTEXCEEDED;
	}
	
	/**
	 * Sets the callout Message on OTP Expiry.
	 * @param request
	 * @param config
	 * @return
	 */
	public String setCalloutMessageOnOTPExpiry(ACSRequest request,
			COConfig config) {
		return OTPEXPIRED;
	}
	
	/**
	 * 
	 * To be overridden by handlers ONLY IF some info needs to be passed to the page {optindecline or attempts}; 
	 * after clicking on NotNow
	 * 
	 * @return
	 * 
	 */
	public String setCalloutMsgForAttempts(ACSRequest request, COConfig config) {
		return "";
	}	
	
	/**
	 * 
	 * To be overridden by handlers ONLY IF some info needs to be passed to the page 
	 * after clicking on Cancel
	 * 
	 * @return
	 * 
	 */
	public String  setCalloutMsgForCancel(ACSRequest request, COConfig config) {
		return CANCEL;
	}	
	
		/**
		 * The Latest record of Mobile Number/ Email Address is retrieved on the basis of
		 * Question ID in the DB.
		 * 
		 * @param CardNumber
		 * @param questionId
		 * @param key
		 * @param bankID
		 * @param config
		 * @return
		 * @throws ArcotException
		 */
		public String QueryByCardNumberQuestionID(String CardNumber, int questionId, String key,
				int bankID, COConfig config) throws ArcotException {

			DatabaseConnection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			COLogger logger = config.getLogger();
			String cardHolderAnswer = null;

				try
				{
					conn = dbMan.getConnection();
					if (conn!=null && !conn.isClosed())
					{
						stmt = conn.prepareStatement(getCardHolderAnswers,
								ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						stmt.setQueryTimeout(ESCache.esc.getAttributes().getJDBCReadQueryTimeout());

						stmt.setString(1, EnrollmentCrypto.encrypt(CardNumber, bankID));
						stmt.setInt(2, questionId);
						stmt.setInt(3, bankID);

						rs = stmt.executeQuery();
						if (rs.next())
							cardHolderAnswer = rs.getString("answer");
						
						cardHolderAnswer = EnrollmentCrypto.decrypt(cardHolderAnswer, bankID);

					}
					else
						throw new ArcotException(ArcotException.AE_WEBADMIN_DATABASE_CONNECTION_FULL);	//E7262
				}
				catch (SQLException sqle)
				{
					logger.fatal("Error in QueryByCardNumberCardHolderName", sqle);
					try {
						if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
							return cardHolderAnswer;
					} catch ( SQLException sqle11 ) {
						logger.fatal("Error in QueryByCardNumberCardHolderName", sqle11);
					}

					throw new ArcotException(ArcotException.AE_DATABASE_NOT_AVAILABLE);
				}
				finally
				{
					try
					{
						if ( rs != null )
							rs.close();
						if ( stmt != null )
							stmt.close();
					}
					catch ( SQLException sqle )
					{
						logger.fatal("Error in QueryByCardNumberCardHolderName", sqle);
					}

					if(conn != null)
						dbMan.release(conn);
				}
			return cardHolderAnswer;
		}

	
		/**
		 * The Email Address and Mobile Number will be masked before being rendered to the screen.
		 * This can be overridden by the BLH specific to the issuer requirements.
		 * 
		 * @param request
		 * @param config
		 * @param mobileNumClear
		 * @param emailAddress
		 * @return
		 */
		public StringBuffer maskingEmailMobNo(ACSRequest request,
	            COConfig config, String mobileNumClear, String emailAddress, HashMap<String, Object> additionalCustomerData) {
	            COLogger logger = config.getLogger();
	            MASKINGSTRING.charAt(0);
	            StringBuffer calloutMsg = new StringBuffer();
	            String maskedMobNo = "";
	            String maskedEmailAddr = "";
	            
	            if(mobileNumClear != null && !"".equals(mobileNumClear) && !"null".equals(mobileNumClear)){
	                  logger.info(request, "The Last 4 digits of the Mobile Number is extracted.");
	                  
	                  maskedMobNo = mobileNumClear.substring(mobileNumClear.length() - 4, mobileNumClear.length()); 
	                  additionalCustomerData.put(mobnum, maskedMobNo);
	                  calloutMsg.append(maskedMobNo);
	                  calloutMsg.append("||");
	            }
	            if(emailAddress != null && !"".equals(emailAddress)){
	                  logger.info(request, "The Domain Name of the Email Address is being extracted.");
	                  maskedEmailAddr = emailAddress.substring(emailAddress.indexOf("@") + 1, emailAddress.lastIndexOf("."));                        
	                  calloutMsg.append(maskedEmailAddr);
	                  additionalCustomerData.put(email, maskedEmailAddr);
	            }
	            return calloutMsg;
	}	
		
		//if label is not present it will return null. Please handle null in your client code.
	public static String parseJsonStringAndGetvalues(COLogger logger, String jsonString, String label) {
			
			String value = null;
				try {
					JSONObject jsonObject = new JSONObject(jsonString);
					value = jsonObject.getString(label);
				} catch (JSONException e) {
					logger.error("JSONException in parseJsonStringAndGetvalues()", e);
                    return value;
				}
				return value;
		
		}
	
	//if label is not present it will return null. Please handle null in your client code.
public static String parseJsonStringAndGetvaluesFromCalloutData(COLogger logger, String jsonString, String label) {
		
	JSONObject calloutMsgJsonObject = null;
	String value = "";
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				calloutMsgJsonObject = (JSONObject)jsonObject.get(callout_msg);
				value = calloutMsgJsonObject.getString(label);
				
			} catch (JSONException e) {
				logger.error("JSONException in parseJsonStringAndGetvalues()", e);
                return value;
			}
			return value;
	
	}
		
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {}

}
