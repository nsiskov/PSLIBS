package com.arcot.apps.callout.es;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;

import com.arcot.apps.callout.common.BusinessLogicHandler;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.generic.LoadCalloutConfig;
import com.arcot.apps.logger.Logger;
import com.arcot.callout.CallOutDetails;
import com.arcot.callout.CallOutFrameWork;
import com.arcot.callout.CallOutsConfig;
import com.arcot.dboperations.enroll.EnrollOperations;
import com.arcot.logger.ArcotLogger;
import com.arcot.util.AcctHolderAuth;
import com.arcot.util.ArcotConstants;
import com.arcot.util.CardHolderInfoForCHInquiryCallout;
import com.arcot.util.GenericValidator;
import com.arcot.vpas.enroll.BaseAttribute;
import com.arcot.vpas.enroll.EnrollmentForm;
import com.arcot.vpas.enroll.EnrollmentVariables;
import com.arcot.vpas.enroll.IssuerQA;
import com.arcot.vpas.enroll.RangeIssuerQA;
import com.arcot.vpas.enroll.UserField;
import com.arcot.vpas.enroll.UserFields;
import com.arcot.vpas.enroll.cache.ESCache;
import com.arcot.vpas.profile.LogonForm;

public class ESCallout implements CallOutFrameWork , ES{
	
	COConfig myConfig = null;
	//[3] check if the product is upgraded to 7.3 
	private static boolean tf73 = ArcotConstants.VERSION_NAME.contains("7.3");
	
	public boolean init(Hashtable context) {
		// TODO Auto-generated method stub
		return true;
	}
	private int findRangeLevelData(ESRequest req){
		String[] cardType = {"1","2","3","4","5","6","7"};
		BaseAttribute attr = new BaseAttribute();
		try{
			EnrollOperations.getRangeIdBankIdForLandingPage(attr, req.getCardNumber(), cardType);
		} catch(Exception e){
			ArcotLogger.logError("Exception while retrieving the bankid and rangeid", e);
		}
		return attr.getRangeId();
	}
	public boolean process(Hashtable input, Hashtable output) {		
		ESRequest req = createESRequest(input);
		int targetRangeId = findRangeLevelData(req);
		// 53 : Send AOTP Activation Code Callout
		if(req.getCalloutEvent() == 51 || req.getCalloutEvent() == 52 || req.getCalloutEvent() == 53){
			String[] cardType = {"1","2","3","4","5","6","7"};
			BaseAttribute attr = new BaseAttribute();
			try{
				EnrollOperations.getRangeIdBankIdForLandingPage(attr, req.getCardNumber(), cardType);
			} catch(Exception e){
				ArcotLogger.logError("Exception while retrieving the bankid and rangeid", e);
			}
			req.setBankId(attr.getBankId());
			req.setRangeId(attr.getRangeId()); 
		}
		if(myConfig == null){
			CallOutDetails coDetails = null;
			// Logic for Callout fall back
			if(targetRangeId > 0){
			 coDetails = ESCache.cac.getDetails(req.getCalloutEvent(), targetRangeId, ESCache.bric.getBrand(req.getRangeId()).getConfigId(), req.getBankId());
			 if(coDetails == null){
				 // falling back to Range Conf Level
				 coDetails = ESCache.cac.getDetails(req.getCalloutEvent(), req.getRangeId(), ESCache.bric.getBrand(req.getRangeId()).getConfigId(), req.getBankId()); 
			 }
			}else
				coDetails = ESCache.cac.getDetails(req.getCalloutEvent(), req.getRangeId(), ESCache.bric.getBrand(req.getRangeId()).getConfigId(), req.getBankId()); 
			CallOutsConfig coConfig = LoadCalloutConfig.getInstance(null).getCalloutConfigFromId(coDetails.getConfID());
			myConfig = new COConfig(req, coConfig);
			/*CallOutDetails coDetails = ESCache.cac.getDetails(req.getCalloutEvent(), req.getRangeId(), ESCache.bric.getBrand(req.getRangeId()).getConfigId(), req.getBankId());
			CallOutsConfig coConfig = new CallOutsConfig();
			coConfig.setConfID(coDetails.getConfID());
			myConfig.setCCConfig(coConfig);*/
		}
		
		Logger logger = myConfig.getLogger();
		if(logger == null){			
			output.put(LOG_TO_DB, "BAD_CONFIG_L");
			output.put(LOG_TO_FILE, "Could not get the logger [" + myConfig.getValue(COConfig.LOG_FILE_PATH) +  "] configured for " + req);
			output.put(com.arcot.apps.callout.es.ES.RESULT, new Boolean(false));
			return false;
		}
		
		logger.log("Processing: " + req);
		BusinessLogicHandler es = myConfig.getBusinessLogicHandler();
		if(es == null){
			output.put(LOG_TO_DB, "BAD_CONFIG_BLH");
			output.put(LOG_TO_FILE, "Could not get the business logic handler [" + myConfig.getValue(COConfig.LOGIC_HANDLER) +  "] configured for " + req);
			output.put(com.arcot.apps.callout.es.ES.RESULT, new Boolean(false));
			return false;
		}
		
		HttpSession session = (HttpSession)input.get(SESSION);
		if ( null != session )
		{
			if ( null != session.getAttribute(CALLOUT_MSG) )
				session.removeAttribute(CALLOUT_MSG);
		}
		ESResponse response = es.process(req, myConfig);

		if ( null != session && !GenericValidator.isBlankOrNull(response.getCalloutMessage()) ) 
		{
			
			session.setAttribute(CALLOUT_MSG, response.getCalloutMessage());
		}

		logger.log("Request: " + req + "returning with result:: " + response);
		
		output.put(LOG_TO_DB, response.getLogToDB());
		output.put(LOG_TO_FILE, response.getLogToFile());
		output.put(com.arcot.apps.callout.es.ES.RESULT, new Boolean(response.isResult()));
		if(response.getLockStatus() > 0){
			output.put(CARD_STATUS, new Integer(response.getLockStatus()));
		}
		//added for IVR: by ranjeet
		
		if((response.getIvrMsgRes() != null)){
			output.put(DisplayMsgResString, response.getIvrMsgRes());
		}
		if ((response.getOTP() != null)) {
			output.put(OTP, response.getOTP());	
		}
		if (new Boolean(response.isShowOtp()) != null) {
			output.put(showOTP, new Boolean(response.isShowOtp()));
		}
		
		
		//[2] New Action introduced - that is : NO_ACTION
		int responseCode = response.getAcsCRC();		
		if ( responseCode == ES.ES_CALLOUT_TX_NO_ACTION ) {
			output.put("RESPONSECODE", "NO_ACTION");
		}
		
		return true;
	}

	public boolean cleanup() {
		// TODO Auto-generated method stub
		return false;
	}

	public void log(String toLog) {
		// TODO Auto-generated method stub
	}
	
	private ESRequest createESRequest(Hashtable input){
		HttpSession session = (HttpSession)input.get(SESSION);
		
		EnrollmentVariables ev = (EnrollmentVariables)session.getAttribute("EV");
		ESRequest req = new ESRequest();
		
		//[1] If Transfort version is 7.3.x we are taking the extraAuthParams map from input hashmap and putting them 
		// into the ESRequest 
		// This is temporary arrangement, to let this class be used with 7.2.x or earlier versions...
		if ( tf73 )
		{
			if ( null != session.getAttribute("logonForm") )
			{
				LogonForm logonForm = (LogonForm) session.getAttribute("logonForm");
				Map<String, String> esExtraAuthParams = new HashMap<String, String>();
				
				try {
					
					esExtraAuthParams.putAll((Map<? extends String, ? extends String>) PropertyUtils.getProperty(logonForm, "extraAuthParams"));
				} catch (NoSuchMethodException e) {
					ArcotLogger.logError("Error in getting extraAuthParams from logonForm", e );
				}
				catch (Exception e) {					
					ArcotLogger.logError("Error in getting extraAuthParams from logonForm, returning null.", e );
					return null;
				}
				
				req.setExtraAuthParams(esExtraAuthParams);
				
				// Auth fail
				Object nStrikesLogon = session.getAttribute("NSTRIKESFORLOGON");
				if ( nStrikesLogon != null )
				{
					try 
					{
						req.setNumAuthFailures(Integer.parseInt((String)nStrikesLogon));
					}
					catch(Exception ex)
					{
						//
					}
				}
			}
		}
		
		
		//[4] to pass on the ATOP details from input hashmap to the ESRequest //
		if ( input.containsKey("ACTIVATION_URL"))
			req.setAotpActivationURL((String)input.get("ACTIVATION_URL"));
		
		if ( input.containsKey("ACTIVATION_CODE"))
			req.setAotpActivationCode((String)input.get("ACTIVATION_CODE"));
		
		if ( input.containsKey("CARDHOLDERID"))
			req.setAotpCardHolderId((String)input.get("CARDHOLDERID"));

		if ( input.containsKey("AOTP_STATUS"))
			req.setAotpStatus(String.valueOf(input.get("AOTP_STATUS")));
		
		
		
		if(((Integer)input.get(CALLOUT_EVENT)).intValue() == 51){
			Boolean isCardCancelled = (Boolean)input.get("CARDCANCELLED");
			if(isCardCancelled != null && !("".equals(isCardCancelled)))
				ArcotLogger.logInfo("createESRequest: isCardCancelled => " + isCardCancelled.booleanValue());
			else{
				//This is not a Cancel CH request so proceed to populate request parameters with AHA details
				Map cardHolderInfoMap = new HashMap();
				cardHolderInfoMap = (Map)input.get("CARDHOLDERINFOMAP");
				
				// getting the chname from AHADetails Object
				String chNameEnc = null;
				Vector ahaDetails = (Vector)session.getAttribute("AHADETAILS");
				AcctHolderAuth aha = new AcctHolderAuth();
				for ( int i=0; i<ahaDetails.size();i++)
				{
					aha = (AcctHolderAuth)ahaDetails.elementAt(i);
					chNameEnc = aha.EncCardholderName;
				}
				

				CardHolderInfoForCHInquiryCallout cardHolderInfo = new CardHolderInfoForCHInquiryCallout();
				if(cardHolderInfoMap != null){
					cardHolderInfo = (CardHolderInfoForCHInquiryCallout)cardHolderInfoMap.get(chNameEnc);	
					if(cardHolderInfo != null){
						String newMobNum = (String)cardHolderInfo.getMobilePhoneClear();
						String oldMobNum = (String)cardHolderInfo.getOldMobilePhoneClear();
										
						req.setOldMobileNumber(oldMobNum);
						req.setNewMobileNumber(newMobNum);						
					}else{
						ArcotLogger.logInfo("createESRequest: cardHolderInfo is NULL");
					}

				}else{
					ArcotLogger.logInfo("createESRequest: cardHolderInfoMap is NULL");
				}
				

			}
		}
		
		

		if(ev != null){
			req.setBankId(ev.getBankId());
			req.setRangeId(ev.getRangeId());
		}
		req.setCardNumber((String)input.get(USERcardNumber));
		if(req.getCardNumber() == null){
			req.setCardNumber((String)input.get(CardNumber));
		}
		EnrollmentForm eForm = (EnrollmentForm)session.getAttribute("enrollmentForm");
		if(eForm != null){
			req.setIsabridged(eForm.getIsAbridged());
			//req.setIsautofyp(eForm.getA)
			req.setIsforgotpwd(eForm.getIsPwdForgot());
			req.setIsreset(eForm.getIsReset());
			/*
			 * Take the questions and answers from Enrollment Form as input hashmap
			 * will not contain Q/A if it was optional and CH did not fill in the Q/A (not even Q!)
			 * We do not want every callout to take care of this scenario
			 * 		req.setQuestions((ArrayList)input.get(USERQUESTIONS));
					req.setAnswers((ArrayList)input.get(USERANSWERS));
					req.setQuestionids((ArrayList)input.get(USERQUESTIONIDS));
			 * */
			ArrayList questions = new ArrayList();
			ArrayList questionIds = new ArrayList();
			ArrayList answers = new ArrayList();

			RangeIssuerQA rangeIssuerQA = eForm.getRangeIssuerQA();
			req.setRangeIssuerQA(rangeIssuerQA);
			if ( rangeIssuerQA != null ){
	        	int numQuestions = rangeIssuerQA.getQuestionCount();
	        	IssuerQA qa = null;
	        	for ( int i = 0; i < numQuestions; i++ ){
					qa = ((IssuerQA)rangeIssuerQA.getIssuerQA().get(i));
					questions.add(qa.getQuestion());
					answers.add(qa.getAnswer());
					questionIds.add(new Integer(qa.getQuestionId()));
				}
			}
			req.setQuestions(questions);
			req.setAnswers(answers);
			req.setQuestionids(questionIds);
			
			/**
			 * sandy
			 * Done for all purposes, presently used for ING NWE
			 * Done so that the Generic infrastructure 
			 * can be configured as Post Attributes Issuer Callout as well
			 * 
			 */
			UserFields userFields = eForm.getUserFields();
			//for getting complete user fields
			req.setUserFields(userFields);
			
			Iterator iter = userFields.getFieldNames();
			UserField field = null;
			String name = null;
			String value = null;
			HashMap userFeildsMap = new HashMap();
			while ( iter.hasNext() ){
				name = (String)iter.next();
				field = userFields.getFieldByName(name);
				if ( field != null )
				{
					name = field.getName();
					value = field.getValue();
				}
				if ( !GenericValidator.isBlankOrNull(value) ){
					userFeildsMap.put("USER." + name, value);
				}
			}
			req.setUserFeildsMap(userFeildsMap);
			
			// Auth fail
			req.setNumAuthFailures(rangeIssuerQA.getNumAuthFailures());
		}
		
		req.setCalloutEvent(((Integer)input.get(CALLOUT_EVENT)).intValue());
		req.setInfolist((String)input.get(CFG_INFOLIST));
		req.setSession((HttpSession)input.get(SESSION));
		req.setCardholderName((String)input.get(USERcardHolderName));
		req.setUserId((String)input.get(USERUserId));
		req.setPassword((String)input.get(USERPassword));
		
		
		// Sandy - 
		// CAP ONE - need this for acct assitant profile callout
		// TODO need to find an elegant way for this 
	
		if (!GenericValidator.isBlankOrNull((String)input.get(ES.USERemail))){
			req.setEMailAddr((String)input.get(ES.USERemail));
		}
		
		if (!GenericValidator.isBlankOrNull((String)input.get(ES.USEROldemail))){
			req.setOldEMailAddr((String)input.get(ES.USEROldemail));
		}
		
		if ( GenericValidator.isBlankOrNull(req.getNewMobileNumber() ) &&  !GenericValidator.isBlankOrNull((String)input.get(ES.USERhomePhone)) ) {
			req.setNewMobileNumber((String)input.get(ES.USERhomePhone));
		}
		
		

		
		return req;
	}
}
