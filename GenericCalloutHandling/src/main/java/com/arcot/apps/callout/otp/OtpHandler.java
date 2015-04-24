package com.arcot.apps.callout.otp;

/**
 * @author Sandip Bose
 * 
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
 * MOBILE_QUES_ID
 * Datatype 	:: int	
 * QUESTION ID of the mobile uploaded
 * This SHOULD be configured is Mobile Number is stored in ARISSUERANSWERS
 * 
 * OTP_VALIDITY_PERIOD
 * Datatype 	:: int
 * Time in milliseconds for the OTP to be valid
 * If not configured, we will proceed with OTP validation; and there will be no check on the VALIDITY PERIOD of OTP 
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
 * 3.2 CALLOUT_MSG on Failure to OTP page will have format		:- <TIME_TO_DISABLE_RESENT_LINK>~<MOBILE_NUMBER>~OPTFAILED~<USER_DEFINED_STRING>
 * 
 * 4. CALLOUT_MSG sent to exit pages + scenario
 *     
 *    NOMOBILEPRESENT			MOBILE NUMBER not found, althou MOBILE_NUMBER_ON_PAGE is turned on.
 *    RESENTEXCEEDED			Number of resent tries exceeded.
 *    OTPTIMEOUT				Validity of OTP expired
 *    
 */

import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.acs.ACSResponse;
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
import com.arcot.crypto.CryptoUtil;
import com.arcot.database.DatabaseConnection;
import com.arcot.dboperations.DBHandler;
import com.arcot.dboperations.admin.AdminQueryStrings;
import com.arcot.dboperations.enroll.EnrollOperations;
import com.arcot.util.ArcotException;
import com.arcot.util.EncodingUtil;
import com.arcot.vpas.enroll.EnrollmentCrypto;
import com.arcot.vpas.enroll.cache.ESCache;
import com.arcot.vpas.profile.CardHolderBean;

public class OtpHandler extends DBHandler implements BusinessLogicHandler, ACS, ES {

	/**
	 * OTP_PER_TRX_PROXY_PAN
	 * Datatype :: boolean:Default - false
	 * if otp is to be supported per transaction proxy pan		[true]
	 * [Default, if NOT configured]otp supported per cardnumber					
	 * 		
	 */
	private static final String OTP_PER_TRX_PROXY_PAN = "OTP_PER_TRX_PROXY_PAN";
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
	 * TIME_BETWEEN_RESENDS
	 * Datatype :: int
	 * Time in milliseconds between two consecutive resends
	 */
	private static final String TIME_BETWEEN_RESENDS = "TIME_BETWEEN_RESENDS";
	/**
	 * OTP_VALIDITY_PERIOD
	 * Datatype :: int
	 * Time in milliseconds for the OTP to be valid
	 */
	private static final String OTP_VALIDITY_PERIOD = "OTP_VALIDITY_PERIOD";
	
	/** MOBILE_NUMBER_ON_PAGE
	 * Datatype 	:: boolean	:Default - false
	 * To be shown on page									[true]
	 * Default, if not Configured, Not to be shown on page
	 */
	private static final String MOBILE_NUMBER_ON_PAGE = "MOBILE_NUMBER_ON_PAGE";
	
	
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
	private static final String LANDING = "LANDING";
	private static final String OTP = "OTP";
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
	//private static final String NUMERIC = "NUMERIC";
	//private static final String ALPHANUMERIC = "ALPHANUMERIC";
	//private static final String ALPHABETS = "ALPHABETS";
	/**
	 * Default supported Values
	 */
	private static String NUMERIC_RANGE = "0123456789";
	private static String ALPHANUMERIC_RANGE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static String ALPHA_RANGE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/**
	 * Used in case the Values to be used are different from the Default List provided
	 */
	private static final String NUMERIC_RANGE_SPECIFIED = "NUMERIC_RANGE_SPECIFIED";
	private static final String ALPHANUMERIC_RANGE_SPECIFIED = "ALPHANUMERIC_RANGE_SPECIFIED";
	private static final String ALPHA_RANGE_SPECIFIED = "ALPHA_RANGE_SPECIFIED";
	
	public ACSResponse process(ACSRequest request, COConfig config) {
		
		COLogger objLogger = config.getLogger();

		if (PAREQ_REQUEST.equalsIgnoreCase(request.getMessageType())){
		
			  if(checkForCardLockedState(request, objLogger)){
				  return COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, "CARDLOCKED", "CARDLOCKED",
		        			ACS.ACS_CALLOUT_PARES_N, "CARDLOCKED"));
			  }
			
			try {
				RA_ACTION raAction = performRfProcess(request, config);
				if(raAction!=null)
					return COUtil.createACSResponse(request, raAction.getAcsRetStatus(), null, raAction.getResult(), 0, "PAREQ["+raAction.getCOStatusLog()+"]", "PAREQ["+raAction.getCOStatusLog()+"]");
				else
					return doPareqFlow(request, config);
			
			} catch (ArcotException e) {
				objLogger.error(request, "doPareqFlow", e);
				return COUtil.createACSResponse(request, ACS_CALLOUT_UNSURE, "", false, 0, "PAREQExp", "PAREQExp");
			}
		
		}else if (VERIFY_IA_REQUEST.equalsIgnoreCase(request.getMessageType())){
		
			try {
				
				return doViaFlow(request, config, request.getIssuerAnswer());
			
			} catch (ArcotException e) {
				objLogger.error(request, "doViaFlow", e);
				return COUtil.createACSResponse(request, ACS_CALLOUT_UNSURE, "", false, 0, "ViaExp", "ViaExp");
			}
		
		}else if (VERIFY_PASSWORD_REQUEST.equalsIgnoreCase(request.getMessageType())) {
			
			try {
				
				return doVPFlow(request, config);
			
			} catch (ArcotException e) {
				objLogger.error(request, "doVpFlow", e);
				return COUtil.createACSResponse(request, ACS_CALLOUT_UNSURE, "", false, 0, "VpExp", "VpExp");
			}
		
		}else{
			objLogger.warn(request, "Check the configuration. Unhandled Callout Configured - "+request.getMessageType());
			return COUtil.createACSResponse(request, ACS_CALLOUT_UNHANDLED, null, false, 0, request.getMessageType() + " UNHANDLED", "Callout type not supported: " + request.getMessageType());
		
		}
	}
	
	public RA_ACTION performRfProcess(ACSRequest request, COConfig config) {
		return RiskFortRules.getActionForRiskAdvice(request, config);
	}
	
	

	/**
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
	public ACSResponse doPareqFlow(ACSRequest request, COConfig config) throws ArcotException {
		
		COLogger logger = config.getLogger();
		logger.debug(request, "doPareqFlow()");
		
		CardHolderBean objChBean = new CardHolderBean();
		EnrollOperations.getCHData(request.getCardNumber(), request.getCardholderName(), request.getBankId(), objChBean);
		
		logger.debug(request, "doPareqFlow():: Flow Configured: " +
				"UNENROLLED_FLOW ["+config.getValue(UNENROLLED_FLOW)+"] ENROLLED_FLOW ["+config.getValue(ENROLLED_FLOW)+"]");
		
		if (OTP.equalsIgnoreCase(config.getValue(UNENROLLED_FLOW)) &&
				OTP.equalsIgnoreCase(config.getValue(ENROLLED_FLOW))){
			
			/**
			 * in ARDEVICE, configure the OTP FOLDER.
			 * NO need to pass the Folder ID here
			 */
			
			logger.debug(request, "doPareqFlow():: ALWAYS_OTP - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			return COUtil.prepareACSResponse(request, sendOTP(config, request, 
					createOTP(config, request, config.getValue(OTP_TYPE), config.getIntValue(OTP_LENGTH)), 0, objChBean));
		
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

				logger.debug(request, "doPareqFlow():: ENROLLED_FLOW(OTP) - ToFolder - "+config.getValue("OTP_"+COUtil.getLocaleName(Integer.toString(objChBean.getLocaleId()))));
				return COUtil.prepareACSResponse(request, COUtil.prepareACSResponse(request, sendOTP(config, request, 
						createOTP(config, request, config.getValue(OTP_TYPE), config.getIntValue(OTP_LENGTH)), 0, objChBean), 
						COUtil.getFolderId(config.getValue("OTP_"+COUtil.getLocaleName(Integer.toString(objChBean.getLocaleId()))))));

			}else{
				
				logger.debug(request, "doPareqFlow():: UNENROLLED_FLOW(LANDING) - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
				return COUtil.createACSResponse(request, ACS_CALLOUT_SUCCESS, "", true, 0, "[L]", "[L]");
			
			}
			
		}else {
			// nothing is configured(i.e UNENROLLED_FLOW & ENROLLED_FLOW is missing from callout config), 
			// and PAREQ is plugged in by mistake, just return a success
			
			// in this scenario we should assume that we always show the LANDING PAGE
			logger.debug(request, "doPareqFlow():: ALWAYS_LANDING - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			return COUtil.createACSResponse(request, ACS_CALLOUT_SUCCESS, "", true, 0, "[L]", "[L]");
		}
	}
	
	/**
	 * this will be invoked to achieve VIA/VP flow
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
	public ACSResponse doViaFlow(ACSRequest request, COConfig config, String pinFrmPage) throws ArcotException {
		
		COLogger logger = config.getLogger();
		logger.debug(request, "doViaFlow()");
		
		if (pinFrmPage == null){
			logger.warn(request, "PIN FROM PAGE IS EMPTY !");
			return COUtil.prepareACSResponse(request, COUtil.createACSResponse(request, ACS_CALLOUT_UNSURE, "", true, 0, "PINEmpty", "PINEmpty", request.getFolderId()));
		}
		
		/**
		 * Good Practice:-
		 * 
		 * Fixed length checks done explicitly so that we know 
		 * which part of the PIN needs to be taken into consideration.
		 *
		 */
		
		logger.trace(request, "doViaFlow():: PIN_FROM_PAGE - "+pinFrmPage);
		
		if (pinFrmPage.contains("LOCALE~")){
			
			String localeToSwitch = pinFrmPage.substring(pinFrmPage.indexOf("LOCALE~")+7, pinFrmPage.length());
			
			if ((OTP.equalsIgnoreCase(config.getValue(UNENROLLED_FLOW)) && OTP.equalsIgnoreCase(config.getValue(ENROLLED_FLOW))) ||
					(OTP.equalsIgnoreCase(config.getValue(ENROLLED_FLOW)))){
					
				
				logger.debug(request, "doViaFlow():: OTP - ToFolder - "+config.getValue("OTP_"+localeToSwitch));
				return COUtil.prepareACSResponse(request, COUtil.createACSResponse(request, ACS_CALLOUT_TX_NO_ACTION, "", true, 0, "LocaleOTP["+localeToSwitch+"]", "Locale["+localeToSwitch+"]", 
					COUtil.getFolderId(config.getValue("OTP_"+localeToSwitch))));
			
			}else{
				
				logger.debug(request, "doViaFlow():: LANDING - ToFolder - "+config.getValue("LANDING_"+localeToSwitch));
				return COUtil.prepareACSResponse(request, COUtil.createACSResponse(request, ACS_CALLOUT_TX_NO_ACTION, "", true, 0, "LocaleLanding["+localeToSwitch+"]", "Locale["+localeToSwitch+"]", 
						COUtil.getFolderId(config.getValue("LANDING_"+localeToSwitch))));
				
			}
		
		}else if (pinFrmPage.contains("ToOTP~")){
			logger.debug(request, "pinFrmPage - "+pinFrmPage);
			
			String localeToSwitch = getLocale(pinFrmPage);
			logger.debug(request, "localeToSwitch - "+localeToSwitch);
			logger.debug(request, "doViaFlow():: LANDING To OTP - ToFolder - "+config.getValue("OTP_"+localeToSwitch));
			/**
			 * 
			 * moveToOTPPage() 
			 * 
			 * To be overridden by handlers ONLY IF some info needs to be passed to the page
			 * 
			 */

			CardHolderBean objChBean = new CardHolderBean();
			EnrollOperations.getCHData(request.getCardNumber(), request.getCardholderName(), request.getBankId(), objChBean);

			logger.debug(request, "doViaFlow():: LANDING To OTP - ToFolder - "+config.getValue("OTP_"+localeToSwitch));
			
			return COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request, 
					moveToOTPPage(request, config, localeToSwitch, objChBean), 
					COUtil.getFolderId(config.getValue("OTP_"+localeToSwitch))));
		
		}else if (pinFrmPage.contains("OTP~")){
			
			String strOTP = pinFrmPage.substring(pinFrmPage.indexOf("OTP~")+4, pinFrmPage.length());
			COResponse co=new COResponse();
			logger.debug(request, "doViaFlow():: OTP Validation - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			return  COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request,
					 validateOTP(request, config, strOTP),
					 request.getFolderId()));
			
		}else if (pinFrmPage.contains("RESEND")){
			
			logger.debug(request, "doViaFlow():: RESEND - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			return  COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request,
					 resendOTP(request, config),
					 request.getFolderId()));			
		
		}else if (pinFrmPage.contains("NOTNOW")){
			
			logger.debug(request, "doViaFlow():: NOTNOW - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			return  COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request,
					 attemptsProcessing(request, config),
					 request.getFolderId()));			
			
		}else if (pinFrmPage.contains("CANCEL")){
			
			logger.debug(request, "doViaFlow():: CANCEL - ToFolder - "+COUtil.getFolderName(request.getFolderId()));
			return  COUtil.prepareACSResponse(request,COUtil.prepareACSResponse(request,
					 cancelProcessing(request, config),
					 request.getFolderId()));			
			
		}else{
			
			logger.warn(request, "Unrecognized Pin Rcvd - "+pinFrmPage);
			return COUtil.prepareACSResponse(request, 
					COUtil.createCOResponse(false, 0, "UnknownPin", "UnknownPin - "+pinFrmPage, ACS_CALLOUT_UNSURE));
		}
	}
	
	public String getLocale(String pinFrmPage){
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
	 * @return
	 */
	public COResponse attemptsProcessing(ACSRequest request, COConfig config) {
		COLogger logger = config.getLogger();
		logger.debug(request, "attemptsProcessing()");
		
		return COUtil.prepareACSResponse(request, 
				COUtil.createCOResponse(true, 0, "NOTNOW", "NOTNOW", ACS_CALLOUT_PARES_A, setCalloutMsgForAttempts(request, config)), 
				request.getFolderId());
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
		logger.debug(request, "cancelProcessing()");
		
		return COUtil.prepareACSResponse(request, 
				COUtil.createCOResponse(true, 0, "CANCEL", "CANCEL", ACS_CALLOUT_PARES_N, setCalloutMsgForCancel(request, config)), 
				request.getFolderId());
	}

	/**
	 * resent functionality.
	 * Dont see a reason of a handler overwriting this.
	 * 
	 * @param request
	 * @param config
	 * @return
	 */
	public COResponse resendOTP(ACSRequest request, COConfig config) {
		
		COLogger logger = config.getLogger();
		logger.debug(request, "resendOTP()");
		
		// 1. get the OTP from calloutData
		String[] strCalloutData = fetchCalloutData(request, config);
		
		// 2. increment the counter
		int updateResendCounter = Integer.parseInt(strCalloutData[2]);
		updateResendCounter = updateResendCounter + 1;
		
		String mobileNumber = strCalloutData[3];
		if (mobileNumber.length() == 0)
			mobileNumber = null;
		
		CardHolderBean objTempBean = new CardHolderBean();
		objTempBean.setMobilePhoneClear(mobileNumber);
		
		if (!Boolean.valueOf(config.getValue(NUM_OF_RESENDS))){
			if (updateResendCounter > (config.getIntValue(NUM_OF_RESENDS)-1)){
				return actionOnResend(request, config, updateResendCounter);
			}
		}

		COResponse objCOResp = null;
		/**
		 * updateResendCounter will be updated in AHA, only if there is a delivery success.
		 */
		if (Boolean.valueOf(config.getValue(USE_NEW_OTP_ALWAYS))){

			// different otp to be used
			
			// 3. send the new OTP
			logger.debug(request, "resendOTP():: Configured to use the different OTP");
			objCOResp = sendOTP(config, request, 
							createOTP(config, request, config.getValue(OTP_TYPE), config.getIntValue(OTP_LENGTH)), 
							updateResendCounter, objTempBean);
 
		}else{
			
			// same OTP to be used
			
			// 3. send the OTP
			
			String decryptedUserOTP = EnrollmentCrypto.decrypt(strCalloutData[0], request.getBankId());
			//logger.debug(request, "resendOTP():: Configured to use the same OTP"+decryptedUserOTP);
			objCOResp = sendOTP(config, request, decryptedUserOTP, updateResendCounter, objTempBean);
		}
		
		objCOResp.setCalloutMessage(setCalloutMsgOnResend(request, config, objCOResp.getCalloutMessage()));
		return objCOResp;
	}

	
	/**
	 * override in the respective handlers if something extra needs to be done
	 * @param request
	 * @param config
	 * @return
	 */
	public COResponse actionOnResend(ACSRequest request, COConfig config, int resendNumber) {
		
		COLogger logger = config.getLogger();
		logger.debug(request, "actionOnResend()");

		if (config.getIntValue(MAX_RESEND_ACTION) == 2) {
			
			logger.warn(request, "actionOnResend():: LOCKING is configured");
			// Lock the card
			return COUtil.prepareACSResponse(request, 
					COUtil.createCOResponse(false, 0, 
							"ResendTry(L)["+ resendNumber + "]", "ResendTry(L)["+ resendNumber + "]",
							ACS_CALLOUT_BLOCK_CH, setCalloutMessageOnResendExceed(request, config), request.getFolderId()));
		} else {
			
			logger.warn(request, "actionOnResend():: BLOCKING is configured");
			// block the card
			return COUtil.prepareACSResponse(request, 
					COUtil.createCOResponse(false, 0, 
							"ResendTry(B)["+ resendNumber + "]", "ResendTry(B)["+ resendNumber + "]",
							ACS_CALLOUT_BLOCK_NO_LOCK, setCalloutMessageOnResendExceed(request, config),request.getFolderId()));
		}
	}

	/**
	 * By Default lets have the VP functionality same as the VIA functionality
	 * @param request
	 * @param config
	 * @return
	 * @throws ArcotException 
	 */
	public ACSResponse doVPFlow(ACSRequest request, COConfig config) throws ArcotException {
		
		return doViaFlow(request, config, request.getPin());
	
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
	 */
	public String createOTP( COConfig config, ACSRequest request, String otpType, int otpLength ){
		
		COLogger logger = config.getLogger();
		logger.debug(request, "createOTP()");
		
		SecureRandom random = new SecureRandom();
		StringBuilder strOTP = new StringBuilder(otpLength);
		
		/**
		 * overwriting the default values if required from the callout config
		 */
		if (config.getValue(ALPHANUMERIC_RANGE_SPECIFIED) != null)
			ALPHANUMERIC_RANGE = config.getValue(ALPHANUMERIC_RANGE_SPECIFIED);
		if (config.getValue(ALPHA_RANGE_SPECIFIED) != null)
			ALPHA_RANGE = config.getValue(ALPHA_RANGE_SPECIFIED);		
		if (config.getValue(NUMERIC_RANGE_SPECIFIED) != null)
			NUMERIC_RANGE = config.getValue(NUMERIC_RANGE_SPECIFIED);			
		
		if ("ALPHANUMERIC".equalsIgnoreCase(otpType)){
			for( int i = 0; i < otpLength; i++ ) 
				strOTP.append( ALPHANUMERIC_RANGE.charAt(random.nextInt(ALPHANUMERIC_RANGE.length())));
		
		}else if ("ALPHABETS".equalsIgnoreCase(otpType)){
			for( int i = 0; i < otpLength; i++ ) 
				strOTP.append( ALPHA_RANGE.charAt(random.nextInt(ALPHA_RANGE.length())));
		
		}else{
			for( int i = 0; i < otpLength; i++ ) 
				strOTP.append( NUMERIC_RANGE.charAt(random.nextInt(NUMERIC_RANGE.length())));
		
		}
		
		logger.trace(request, "createOTP():: OTP Type Configured ["+otpType+"] OTP ["+strOTP.toString()+"]");
		logger.debug(request, "createOTP():: OTP Type Configured ["+otpType+"] OTPLength ["+strOTP.toString().length()+"]");
		
		return strOTP.toString();
	}
	
	/**
	 * Sent the OTP
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @param objChBean 
	 * @return
	 */
	public COResponse sendOTP( COConfig config, ACSRequest request, String strOTP, int resentCounter, CardHolderBean objChBean){
		
		COLogger logger = config.getLogger();
		logger.debug(request, "sendOTP()");
		List<String> calloutData = new ArrayList<String>();
		String mobileNumber = null;
		
		
		COResponse deliveryResp = deliverOTP(config, request, strOTP, mobileNumber);
		
		// deliverOTP(config, request, strOTP, mobileNumber) is deprecated
		// deleveopers are not suppose to use this method anymore
		// this loop is for backward compatibility; for BLH's which has already used this method
		if ("UnImplementedMethod[deliverOTP()]".equals(deliveryResp.getLogToDB()) && 
				"UnImplementedMethod[deliverOTP()]".equals(deliveryResp.getLogToFile()) &&
				deliveryResp.getAcsCRC() == ACS_CALLOUT_UNSURE){

			List<Object> listFrmDeliverCall = deliverOTP(config, request, strOTP);
			
			deliveryResp = (COResponse)listFrmDeliverCall.get(0);
							
			logger.debug(request, "sendOTP():: listFrmDeliverCall size"+listFrmDeliverCall.size());
			if(listFrmDeliverCall.size()>1){
				if (listFrmDeliverCall.get(1) !=null && ((String)listFrmDeliverCall.get(1)).length()> 0)
					mobileNumber = (String)listFrmDeliverCall.get(1);
				for(int i=1;i<listFrmDeliverCall.size();i++){
					//logger.debug(request, "sendOTP():: calloutData "+i+" =>"+listFrmDeliverCall.get(i));
					calloutData.add((String)listFrmDeliverCall.get(i));
				}
				logger.debug(request, "sendOTP():: calloutData size"+calloutData.size());
			}
		}
		
		if (mobileNumber != null){
			//logger.trace(request, "sendOTP():: Mobile Number fetched from deliverOTP Method - "+mobileNumber);
			logger.debug(request, "sendOTP():: Mobile Number fetched from deliverOTP Method - "+mobileNumber.length());
		
		}else{
			
			logger.debug(request, "sendOTP():: Mobile Number NOT fetched from deliverOTP Method");
		
		}

		
		if ("1".equals(Integer.toString(deliveryResp.getAcsCRC()))){
			
			/**
			 * Check if MOBILE is supported; i.e. do we have to show 
			 */
			
			if (Boolean.valueOf(config.getValue(MOBILE_NUMBER_ON_PAGE))){
				
				if (resentCounter > 0){
					
					logger.info(request, "sendOTP():: RESENT FLOW - Counter - "+resentCounter);
					
					// this is a resent flow... we already have the mobileNumber with us.
					// even if the backend is sending in a number, we ignore that
					mobileNumber = objChBean.getMobilePhoneClear();
					
					if (mobileNumber != null){
						
						//logger.trace(request, "sendOTP():: RESENT FLOW - Mobile Number fetched from CALLOUTDATA - "+mobileNumber);
						logger.debug(request, "sendOTP():: RESENT FLOW - Mobile Number fetched from CALLOUTDATA - "+mobileNumber.length());
					
					}else{
						
						// safe Check , this should never happen
						
						logger.info(request, "sendOTP():: RESENT FLOW - Mobile Number Not present in CALLOUTDATA during RESENT FLOW");
						return COUtil.createCOResponse(false, 0, "RESENT_FLOW[NoMobileNumber]", "RESENT_FLOW[NoMobileNumber]", ACS_CALLOUT_UNSURE);
					}
					calloutData.set(0, mobileNumber);
				}else{
				
					logger.debug(request, "sendOTP():: NORMAL FLOW - Mobile Number to be Stored in CALLOUTDATA");
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
						return noMobilePresentAction(request, config);
					}else{
						calloutData.set(0, mobileNumber);
					}
				
				}
			}else{
				
				logger.debug(request, "sendOTP():: Mobile number not to be shown on page");
			
			}
			
			/**
			 * store the OTP	
			 */
			
			return storeOTP(config, request, strOTP, deliveryResp.getLogToDB(), deliveryResp.getCalloutMessage(), resentCounter, calloutData);
			
		}else{
			// if CRC is anything other than ACS_CALLOUT_SUCCESS, 
			// let the deliverOTP() method decide what needs to be done
			return deliveryResp;
		}
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
		logger.debug(request, "populateMobileNumber()");
		
		if (mobileNumber != null){
		
			return mobileNumber;
		
		}else{
			if (Boolean.valueOf(config.getValue(MOBILE_NUMBER_IN_AHA))){
				
				// GET MOBILEPHONE FROM aracctholderauth
				//logger.trace(request, "populateMobileNumber() :: Mobile Number fetched from ARACCTHOLDERAUTH - " + objChBean.getMobilePhoneClear());
				return objChBean.getMobilePhoneClear();
			
			}else {
				
				// GET MOBILEPHONE FROM ARISSUERANSWERS
				logger.debug(request, "populateMobileNumber():: Mobile Number fetched from ARISSUERANSWERS");
				
				if (config.getValue(MOBILE_QUES_ID) != null){
					
					logger.info(request, "populateMobileNumber():: MOBILE_QUES_ID configured - "+config.getValue(MOBILE_QUES_ID));

				}else{

					logger.warn(request, "populateMobileNumber():: No QuestionID configured for Mobile Number");
					return null;
					
				}
				
				Hashtable<String, Vector<String>> ht = new Hashtable<String, Vector<String>>();
				try {
					ht = QueryByCardNumberCardHolderName(request.getCardNumber(), request.getCardholderName(), ESCache.bic.getBank(request.getBankId()).BANKKEY_CLEAR, request.getBankId(), config);
				} catch (ArcotException e) {
					e.printStackTrace();
					logger.error(request, "populateMobileNumber():: ", e);
					return null;
				}
				logger.trace(request, "populateMobileNumber():: Mobile Number fetched from ARISSUERANSWERS - "
						+ (String)ht.get("IssuerAnswers").get(config.getIntValue(MOBILE_QUES_ID)));
				
				return (String)ht.get("IssuerAnswers").get(config.getIntValue(MOBILE_QUES_ID));
			
			}
		}
	}

	/**
	 * Store the OTP in Transfort DB for Validation
	 * Do not see a reason why handlers should override this, playing it safe for the time being
	 * 
	 * 
	 * CALLOUTDATA --> OTP~TIME_OF_GENERATION_in_millisecond~NUMBER_OF_RESENDS~MOBILE_NUMBER
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @param strLogToDB 
	 * @param mobileNumber 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public COResponse storeOTP(COConfig config, ACSRequest request, 
			String strOTP, String strLogToDB, String exsistingCalloutMsg, int resentCounter, 
			List<String> calloutData) {
		String mobileNumber="";
		COLogger logger = config.getLogger();
		logger.debug(request, "storeOTP()");
		
		logger.debug(request, "storeOTP():: calloutData length"+calloutData.size());
		if (calloutData.get(0) != null){
			mobileNumber = formatMobileNumber(calloutData.get(0));
			calloutData.set(0, mobileNumber);
			logger.trace(request, "storeOTP():: Formated Mobile Number to be shown on Page - "+calloutData.get(0));
		}
		
		String strCalloutData = createCalloutData(request, config, strOTP, resentCounter, calloutData);

		/**
		 * should OTP be @ 
		 * card number level 
		 * OR 
		 * trxpropxypan level
		 * 
		 * By default its false, i.e if not configured
		 * 
		 */
		if (Boolean.valueOf(config.getValue(OTP_PER_TRX_PROXY_PAN))){
			
			// otp at trxproxypan level
			logger.debug(request, "storeOTP():: CALLOUTDATA is stored in ARCALLOUTXNDATA");
			COUtil.setTxnCalloutData(request, config, strCalloutData);		
		
		}else{
			
			// otp at cardnumber level
			logger.debug(request, "storeOTP():: CALLOUTDATA is stored in ARACCTHOLDERAUTH");
			COUtil.setCalloutData(request, config, strCalloutData);
		}		
		// setting the timeout period for Resend Link
		
		String timeBetweenResends = ((config.getValue(TIME_BETWEEN_RESENDS) != null)) ? config.getValue(TIME_BETWEEN_RESENDS) : "";
		mobileNumber = (mobileNumber != null)? mobileNumber : "";
		
		return COUtil.createCOResponse(true, 0, strLogToDB, strLogToDB, 
					ACS_CALLOUT_TX_NO_ACTION, timeBetweenResends + "~" + mobileNumber + "~" + exsistingCalloutMsg + setCalloutMsgForOTPPage(request,config));
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
	 * To be overridden by all handlers in case you want to add anything else to the calloutdata.
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
		logger.debug(request,"createCalloutData()");
		
		String strCalloutData = null;
		
		if (calloutData.get(0) == null)
			calloutData.set(0,"");
		
		strCalloutData = EnrollmentCrypto.encrypt(strOTP, request.getBankId()) + "~" + 
								String.valueOf(System.currentTimeMillis()) + "~" + resendNumber + "~" + provideExtraCalloutData(request,config,calloutData);

			
		logger.trace(request,"createCalloutData():: CALLOUTDATA - "+strCalloutData);
		return strCalloutData;
	}
	
	/**
	 * this is to be overwritten by handlers in case any masaging is required on the mobile number before showing on page 
	 * presently it shows last 4 digit of mobile number
	 * 
	 * @param mobileNumber
	 * @return
	 */
	public String formatMobileNumber(String mobileNumber) {
		return mobileNumber.substring(mobileNumber.length()-4, mobileNumber.length());
	}

	/**
	 * to be overridden by all handlers.
	 * Set the Callout msg, if you need to send some data from the response , to the page.
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @param mobileNumber - this is a place holder , incase the backend is sending back the mobile number; please populate this
	 * @return
	 * 
	 * 
	 * @deprecated
	 */
	public COResponse deliverOTP( COConfig config, ACSRequest request, String strOTP, String mobileNumber){
		return COUtil.createCOResponse(false, 0, 
				"UnImplementedMethod[deliverOTP()]", "UnImplementedMethod[deliverOTP()]", 
				ACS_CALLOUT_UNSURE, "", request.getFolderId());
	}
	
	/**
	 * to be overridden by all handlers.
	 * Set the Callout msg, if you need to send some data from the response , to the page.
	 * Pass additional params if required in the calling method.
	 * 
	 * Since in OTP we can get the mobile number from backend, the sentOTP() , uses returnList[0] as 'COResponse' and returnList[1] as 'mobileNumber'
	 * 
	 * @param config
	 * @param request
	 * @param strOTP
	 * @return
	 */
	public List<Object> deliverOTP( COConfig config, ACSRequest request, String strOTP){
		List<Object> returnList = new ArrayList<Object>();
		returnList.add(COUtil.createCOResponse(false, 0, 
				"UnImplementedMethod[deliverOTP()]", "UnImplementedMethod[deliverOTP()]", 
				ACS_CALLOUT_UNSURE, "", request.getFolderId()));
		return returnList;
	}

	
	/**
	 * 
	 * @param request
	 * @param config
	 * @param localeToSwitch 
	 * @param objChBean 
	 * @return
	 */
	public COResponse moveToOTPPage(ACSRequest request, COConfig config, String localeToSwitch, CardHolderBean objChBean) {
		
		COLogger logger = config.getLogger();
		logger.debug(request, "moveToOTPPage()");
		
		COResponse objCOresp = sendOTP(config, request, 
				createOTP(config, request, config.getValue(OTP_TYPE), config.getIntValue(OTP_LENGTH)), 0, objChBean);
		objCOresp.setCalloutMessage(setCalloutMsg(request, config, objCOresp.getCalloutMessage()));
		return COUtil.prepareACSResponse(request,objCOresp);
	}
	
	/**
	 * To be overridden by handlers ONLY IF there is a different form of validation that is required
	 * 
	 * @param request
	 * @param config
	 * @param strOTP
	 * @return
	 */
	public COResponse validateOTP(ACSRequest request, COConfig config, String strOTP) {
		
		COLogger logger = config.getLogger();
		logger.debug(request, "validateOTP()");
		
		String strCalloutData[] = null;
		strCalloutData = fetchCalloutData(request, config);
		
		if (config.getValue(OTP_VALIDITY_PERIOD) != null){

			// check for validity
			COResponse objCOResp = checkExpiryOfOTP(request, config, Long.valueOf(strCalloutData[1]));
			objCOResp.setCalloutMessage(setCalloutMessageOnExpiry(request, config, objCOResp.getCalloutMessage()));
			
			if (objCOResp.getAcsCRC() == ACS_CALLOUT_TX_NO_ACTION){
				objCOResp = doesOTPMatch(request, config, strOTP, strCalloutData[0], strCalloutData[3]);
				
				return  objCOResp;
			}else{
				return objCOResp;
			}
			
		}else{

			// if validity_period is not configured proceed with the validation of OTP
			logger.warn(request, "validateOTP():: OTP_VALIDITY_PERIOD Not Configured");
			return  doesOTPMatch(request, config, strOTP, strCalloutData[0], strCalloutData[3]);
		}
	}
	
	/**
	 * fetch the calloutdata from AHA
	 * @param request
	 * @param config
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String[] fetchCalloutData(ACSRequest request, COConfig config){
		
		COLogger logger = config.getLogger();
		logger.debug(request, "fetchCalloutData()");
		
		String strCalloutData[] = null;
		
		if (Boolean.valueOf(config.getValue(OTP_PER_TRX_PROXY_PAN))){
			
			logger.debug(request, "fetchCalloutData() :: Get CALLOUTDATA from ARCALLOUTXNDATA");
			// otp at trxproxypan level
			strCalloutData = COUtil.getTxnCalloutData(request, config).split("~");
			
		}else{
			
			logger.debug(request, "fetchCalloutData() :: Get CALLOUTDATA from ARACCTHOLDERAUTH");
			// otp at cardnumber level
			strCalloutData = COUtil.getCalloutData(request, config)[0].split("~");
		}
		
		logger.trace(request, "fetchCalloutData() :: CALLOUTDATA fetched - "+strCalloutData);
		return strCalloutData;
	}
	
	/**
	 * To be overridden by handlers ONLY IF there is a different form of validation that is required {like backend validation}
	 * 
	 * @param request
	 * @param config
	 * @param userOTP
	 * @param storedOTP
	 * @param storedMobileNumber 
	 * @return
	 */
	public COResponse doesOTPMatch(ACSRequest request, COConfig config, String userOTP, String storedOTP, String storedMobileNumber){
		
		COLogger logger = config.getLogger();
		logger.debug(request, "doesOTPMatch()");
		
		String encryptedUserOTP = EnrollmentCrypto.encrypt(userOTP, request.getBankId());
		
		if (encryptedUserOTP.equals(storedOTP)){
		
			return COUtil.createCOResponse(true, 0, "OTP[s]", "OTP[s]", ACS_CALLOUT_SUCCESS, "", request.getFolderId());
		
		}else{
			
			logger.trace(request, "doesOTPMatch() :: Encrypted UserOTP [" + encryptedUserOTP + "] Encrypted StoredOTP [" + storedOTP + "]");
			
			String timeBetweenResends = ((config.getValue(TIME_BETWEEN_RESENDS) != null)) ? config.getValue(TIME_BETWEEN_RESENDS) : "";
			return COUtil.createCOResponse(false, 0, "OTP[f]", "OTP[f]", ACS_CALLOUT_FAIL, 
					timeBetweenResends + "~" + storedMobileNumber + setCalloutMsgForOTPPageOnFailure(request, config), 
					request.getFolderId());
		}
	}
	
	/**
	 * Checks whether the OTP is still valid after 'OTP_VALIDITY_PERIOD' ms
	 * 
	 * @param request
	 * @param config
	 * @param otpGenerationTime
	 * @return
	 */
	public COResponse checkExpiryOfOTP(ACSRequest request, COConfig config, long otpGenerationTime){
		
		COLogger logger = config.getLogger();
		logger.debug(request, "checkExpiryOfOTP()");
		
		long currentTime = System.currentTimeMillis();
		long result = currentTime - otpGenerationTime;
		long longConfigOtpGenerationTime = Long.parseLong(config.getValue(OTP_VALIDITY_PERIOD));
		
		logger.info(request, "checkExpiryOfOTP():: CURRENT_TIME["+currentTime+"] " +
				"OTP_GENERATION_TIME["+otpGenerationTime+"] " +
						"VALIDITY_PERIOD_CONFIGURED["+longConfigOtpGenerationTime+"]");
		
		if (result > longConfigOtpGenerationTime){
			return actionOnOTPTimeOut(request,config);
		}else{
			// otp is still valid
			return COUtil.createCOResponse(true, 0, "", "", ACS_CALLOUT_TX_NO_ACTION,"",request.getFolderId());
		}
	}
	

    /**
     * to be overwritten by handlers incase some other action is required.
     * 
     * @param request
     * @param config
     * @return
     */
	public COResponse actionOnOTPTimeOut(ACSRequest request, COConfig config) {
		
		COLogger logger = config.getLogger();
		logger.debug(request, "actionOnOTPTimeOut()");
		
		return COUtil.createCOResponse(false, 0, "OTPTIMEOUT", "OTPTIMEOUT", ACS_CALLOUT_PARES_N, "OTPTIMEOUT", request.getFolderId());
	}
	
    /**
     * to be overwritten by handlers incase some other action is required.
     * 
     * @param request
     * @param config
     * @return
     */
	public COResponse noMobilePresentAction(ACSRequest request, COConfig config) {
		
		COLogger logger = config.getLogger();
		logger.debug(request, "noMoBilePresentAction()");
		
		return COUtil.createCOResponse(false, 0, "NOMOBILEPRESENT", "NOMOBILEPRESENT", ACS_CALLOUT_PARES_N, "NOMOBILEPRESENT", request.getFolderId());
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
	 * 
	 * To be overridden by handlers ONLY IF some info needs to be passed to the page; after clicking on Resend
	 * 
	 * Please make sure that the 'exsisting callout msg' is not lost
	 * 
	 * @param exsistingCalloutMsg 
	 * @return
	 * 
	 */
	public String setCalloutMsgOnResend(ACSRequest request, COConfig config, String exsistingCalloutMsg) {
		return exsistingCalloutMsg;
	}

	/**
	 * override in the respective handlers if you want to pass a different calloutmsg to the page
	 * 
	 * @param request
	 * @param config
	 * @return
	 */
	public String setCalloutMessageOnResendExceed(ACSRequest request,
			COConfig config) {
		return "RESENTEXCEEDED";
	}
	
	/**
	 * override in the respective handlers if you want to pass a different calloutmsg to the page
	 * 
	 * Presently it will sent out OTPTIMEOUT
	 * 
	 * @param request
	 * @param config
	 * @param exsistingCalloutMsg 
	 * @return
	 */
	public String setCalloutMessageOnExpiry(ACSRequest request,
			COConfig config, String exsistingCalloutMsg) {
		return exsistingCalloutMsg;
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
		return "";
	}	
	
	/**
	 * To be overridden by handlers ONLY if some info needs to be paged to the OTP page.
	 * 
	 * @param request
	 * @param config
	 * @return
	 * 
	 */
	public String setCalloutMsgForOTPPage(ACSRequest request, COConfig config) {
		return "";
	}

	/**
	 * To be overridden by handlers ONLY if some info needs to be paged to the OTP page on failure.
	 * 
	 * @param request
	 * @param config
	 * @return
	 * 
	 */	
	public String setCalloutMsgForOTPPageOnFailure(ACSRequest request, COConfig config){
		COLogger logger = config.getLogger();
		 String calloutMsg = "~OPTFAILED~";
			String[] strCalloutData = fetchCalloutData(request, config);
			
			// 1. Not increment the counter
			int updateResendCounter = Integer.parseInt(strCalloutData[2]);
			
			if (!Boolean.valueOf(config.getValue(NUM_OF_RESENDS))){
				if (updateResendCounter > (config.getIntValue(NUM_OF_RESENDS)-1)){
					calloutMsg += setCalloutMessageOnResendExceed(request, config);
				}
			}
			logger.debug(request, "Callout Msg"+calloutMsg);
		return calloutMsg;
	}
	 
	 
	/**
	 * Product Code
	 */
	 
		@SuppressWarnings("deprecation")
		private Hashtable<String, Vector<String>> QueryByCardNumberCardHolderName(String CardNumber, String CardHolderName, String key, int bankID, COConfig config) throws ArcotException {

			DatabaseConnection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			Hashtable<String, Vector<String>> ht;
			COLogger logger = config.getLogger();

			while (true) {
				ht = new Hashtable<String, Vector<String>>();
				try
				{
					conn = dbMan.getConnection();
					if (conn!=null && !conn.isClosed())
					{
						stmt = conn.prepareStatement(AdminQueryStrings.getIssuerAnswersByCardNumberCardHolderName, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						stmt.setQueryTimeout(ESCache.esc.getAttributes().getJDBCReadQueryTimeout());

						stmt.setString(1, CryptoUtil.encrypt3DES64(key, CardNumber, dbParam.softsign));
						stmt.setString(2, CryptoUtil.encrypt3DES64(key, CardHolderName, dbParam.softsign));
						stmt.setInt(3, bankID);

						rs = stmt.executeQuery();
						Vector<String> IssuerQuestionIDs = new Vector<String>();
						Vector<String> IssuerAnswers = new Vector<String>();
						if (rs.next())
							rs.beforeFirst();
						else
							break;

						while (rs.next())
						{
							IssuerQuestionIDs.addElement("" + rs.getInt("questionid"));
							int isEncrypted = rs.getInt("type");//Column name to be added
							String answer = rs.getString("answer");
							if (answer == null)
								answer = "";
							else
							{
								if (isEncrypted == 1)
									answer = CryptoUtil.decrypt3DES64(key, answer, dbParam.softsign);
							}
							answer = EncodingUtil.toBrowserFriendlyValue(answer);
							IssuerAnswers.addElement(answer);
						}
						ht.put("IssuerQuestionIDs", IssuerQuestionIDs);
						ht.put("IssuerAnswers", IssuerAnswers);
					}
					else
						throw new ArcotException(ArcotException.AE_WEBADMIN_DATABASE_CONNECTION_FULL);	//E7262
					break;
				}
				catch (SQLException sqle)
				{
					logger.fatal("Error in QueryByCardNumberCardHolderName", sqle);
					try {
						if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
							continue;
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
			}
			return ht;
		}
		protected boolean checkForCardLockedState(ACSRequest request, COLogger logger) {
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
	 * @param args
	 */
	public static void main(String[] args) {}

}
