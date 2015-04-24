package com.arcot.apps.callout.impl;


import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.acs.ACSResponse;
import com.arcot.apps.callout.common.BusinessLogicHandler;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.callout.es.ESRequest;
import com.arcot.apps.callout.es.ESResponse;

/*
 *  This Callout Needs to pick up the cardnumber from the configuration
 * And it should send VERES Y for only those cards rest of the cards should
 * be returned with VERES N 
 */

public class GenericVEBusinessLogicHandler implements ACS, BusinessLogicHandler{

	private static final String CARDSTOALLOW = "CardsToAllow";	
	public ACSResponse process(ACSRequest request, COConfig config) {

		if(VERIFY_ENROLLMENT_REQUEST.equalsIgnoreCase(request.getMessageType())){
			
			ACSResponse acsResp = doVEReq(request, config);
			acsResp.setVersion("0.0");
			if(acsResp.getAcsCRC() ==ACS_CALLOUT_SUCCESS){
				acsResp.setEnrolled(STATUS_Y);
			}else if(acsResp.getAcsCRC() == ACS_CALLOUT_FAIL){
				acsResp.setEnrolled(STATUS_N);
			}else{
				acsResp.setEnrolled(STATUS_U);
			}
			
			if(acsResp.getProxyPan() == null){
				//ACSXMLHandler requires this even in N and U case
				acsResp.setProxyPan("");
			}
			COLogger logger = config.getLogger();
			logger.log(request, acsResp);
			return acsResp;			
		}
		return COUtil.prepareACSResponse(request,COUtil.createCOResponse(false, 0, "Unknown Callout", "Unknown Callout", ACS_CALLOUT_FAIL));	
	}

	public ESResponse process(ESRequest arg0, COConfig arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	
	private ACSResponse doVEReq(ACSRequest request, COConfig config) {
		COLogger logger = config.getLogger();
		String cardNumber = request.getCardNumber();
		String cardNumberList = config.getValue(CARDSTOALLOW);
		
		if(cardNumberList == null || "".equals(cardNumberList)){
			logger.log(request,"Configuration Error");
			return COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, "ConfigErr", "Configuration Error", ACS.ACS_CALLOUT_FAIL));
		}
		
		if(cardNumber == null || "".equals(cardNumber)){
			logger.log(request,"CardNumber is null");
			return COUtil.prepareACSResponse(request, COUtil.createCOResponse(false, 0, "SysErr", "Cardnumber is null", ACS.ACS_CALLOUT_FAIL));
		}
		
		if(cardNumberList.indexOf(cardNumber) > -1){
			logger.log(request,"CardNumber in configured list, hence sending VERes of Y");
			return COUtil.prepareACSResponse(request, COUtil.createCOResponse(true, 0, "", "VE[Y]", ACS.ACS_CALLOUT_SUCCESS));
		}
		
		logger.log(request,"CardNumber not in configured list, hence sending VERes of N");
		return COUtil.createACSResponse(request, ACS_CALLOUT_FAIL, "", false, 0, "", "VE[N]");				
	}	
}
