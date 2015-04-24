package com.arcot.apps.callout.impl;

import java.util.StringTokenizer;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.acs.ACSResponse;
import com.arcot.apps.callout.common.BusinessLogicHandler;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.callout.es.ESRequest;
import com.arcot.apps.callout.es.ESResponse;

public class GenericVIABusinessLogicHandler implements ACS, BusinessLogicHandler {

	private static final String numberOfAnswers = "NumberOfAnswers";
	private static final String nameKey = "NameKey";
	private static final String minMatch = "MinMatch";
	private static final String ignoreCase = "IgnoreCase";
	private static final String mandatoryQuestions = "MandatoryQuestions";
	
	public ACSResponse process(ACSRequest request, COConfig config) {
		// TODO Auto-generated method stub
		if(VERIFY_IA_REQUEST.equalsIgnoreCase(request.getMessageType())){			
			ACSResponse acsResp = doVIAReq(request, config);			
			COLogger logger = config.getLogger();
			logger.log(request, acsResp);
			return acsResp;			
		}
		return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "Unknown Callout", "Unknown Callout", ACS_CALLOUT_FAIL));		
	}
	/*
	 * doVIAReq function: 
	 * Input : request and config objects
	 * Output: Acs response suggesting Success, Failure or Unsure(when the configuration is not correct)
	 * Functionality: Matches the Answers provided by the card holder to the answers in DB. 
	 * 				  Min Match, Ignore Case, Card holder name required and Mandatory questions are configurable 
	 * Assumption : i) Pin from Cap comes in this format : <Key1>=<Value1>:<Key2>=<Value2>  and so on 
	 * 				ii) The answers passed in the pin are in the order of their qid in database.
	 */
	private ACSResponse doVIAReq(ACSRequest request, COConfig config){
		COLogger logger = config.getLogger();		
		String pinFromOptin = request.getIssuerAnswer();
		if(pinFromOptin == null || "".equals(pinFromOptin.trim())){
			logger.log(request, "PIN received from CAP is empty");
			return COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, "EmptyPin", "PIN received is empty", ACS.ACS_CALLOUT_UNSURE));
		}
		StringTokenizer stkEquals = null;
		StringTokenizer stkColon = new StringTokenizer(pinFromOptin, ":");
		
		String chName = "";
		String isIgnoreCase = config.getValue(ignoreCase);
		String[] answers = new String[stkColon.countTokens()];
		int numMatch = 0;
		int count = 0;
		int answersCount = config.getIntValue(numberOfAnswers);
		/*
		 * The number of answers expected from cap is configurable.
		 * If the number of answers are not equal to the number of answers received then Return U.		 
		 */
		if(stkColon.countTokens() < answersCount){
			logger.log(request, "Number of required answers are : " + answersCount + ", But the number of answers from Cap are : " +  stkColon.countTokens());
			return COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, "InvalidPin1", "All the answers not populated correctly Reqd[" + numberOfAnswers + "], Recd[" + stkColon.countTokens() +  "]", ACS.ACS_CALLOUT_UNSURE));
		}	
		// Loop through the answers
		while (stkColon.hasMoreTokens()) {
			stkEquals = new StringTokenizer(stkColon.nextToken(),"=");
			String key= stkEquals.nextToken();
			if(key == null || "".equals(key)){
				continue;
			}
			//If the namekey is provided then card holder name is required 
			// the card holder name is parsed from the pin with the nameKey 
			//configurable in the key value pair			
			if(config.getValue(nameKey) != null && key.equals(config.getValue(nameKey))){
					chName =  stkEquals.nextToken();
			} else {
				answers[count++] = stkEquals.nextToken(); 
			}			
		}
		//Getting the answers for this card number and card holder name from DB.
		String[] dbAnswers = COUtil.getDBAnswers(request, config, chName, answersCount);
		if(dbAnswers == null){
			logger.log(request, "Could not get DB answers");
			return COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, "DBErr", "Could not fetch answers from DB", ACS.ACS_CALLOUT_UNSURE));
		}
		String mandatoryQuestion =  config.getValue(mandatoryQuestions);
		boolean mandatoryQuestionFail = false;
		//Loop through the answers and match the answers
		StringBuffer result = new StringBuffer();		
		for(int i = 0; i < dbAnswers.length; i++){
			if(dbAnswers[i] == null){
				logger.log(request, "Could not get DB answers");
				return COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, "ans[NF]", "No answers found in DB", ACS.ACS_CALLOUT_UNSURE));
			}
			//In the configuration if ignore case for the answers is true then match by ignoring the case else match case.
			result.append((i+1) + ":");
			if(isIgnoreCase != null && isIgnoreCase.equalsIgnoreCase("true")){
				if(dbAnswers[i].equalsIgnoreCase(answers[i])){
					numMatch++;
					result.append("S ");
				} else {
					mandatoryQuestionFail = isMandatoryQuestion(mandatoryQuestion, i+1);
					result.append("F ");
				}
			} else {
				if(dbAnswers[i].equals(answers[i])){
					numMatch++;
					result.append("S ");
				} else {
					mandatoryQuestionFail = isMandatoryQuestion(mandatoryQuestion, i);
					result.append("F ");
				}
			}			
		}
		if(mandatoryQuestionFail){
			logger.log(request, "Mandatory Question Failed num of Answers matched[" + numMatch + "], Result : " + result.toString());
			return COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, result.toString()+"ManQ[F]", "Mandatory Question Match Failed", ACS.ACS_CALLOUT_FAIL));
		}
		//If min match is configured then if the answers matched are more than the min match then we return Success
		// Otherwise all the answers should be matched
		int configMinMatch = config.getIntValue(minMatch);		
		if(configMinMatch != -1){
			if(numMatch < config.getIntValue(minMatch)){
				logger.log(request, "Min Match Failed Answers matched[" + numMatch + "], MinMatch[" + config.getIntValue(minMatch) + "], Result : " + result.toString());
				return COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, result.toString()+"MinM[F]", "Min Match Failed", ACS.ACS_CALLOUT_FAIL));
			} else {
				logger.log(request, "Answers Matched, Result :" + result.toString());
				return COUtil.prepareACSResponse(request, COUtil.createCOResponse(true, 0, result.toString(), "Success", ACS.ACS_CALLOUT_SUCCESS));
			}		
		} else {
			//All the answers should be matched
			if(numMatch != dbAnswers.length){
				logger.log(request, "Answers Failed matched[" + numMatch + "], Number of Answers[" + dbAnswers.length + "], Result : " + result.toString());
				return COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, result.toString(), "Match Failed", ACS.ACS_CALLOUT_FAIL));
			} else {
				logger.log(request, "Answers Matched , Result : " + result.toString());
				return COUtil.prepareACSResponse(request, COUtil.createCOResponse(true, 0, result.toString(), "Success", ACS.ACS_CALLOUT_SUCCESS));
			}
		}
	}

	private boolean isMandatoryQuestion(String mandatoryQuestion, int qID){
		if(mandatoryQuestion == null)
			return false;
		if(mandatoryQuestion.indexOf(Integer.toString(qID + 1)) != -1)
			return true;
		
		return false;
		
	}
	public ESResponse process(ESRequest arg0, COConfig arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
