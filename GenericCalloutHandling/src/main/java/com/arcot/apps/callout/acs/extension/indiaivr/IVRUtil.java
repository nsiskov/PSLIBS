package com.arcot.apps.callout.acs.extension.indiaivr;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.acs.ACSResponse;
import com.arcot.apps.callout.acs.ACSVERequest;
import com.arcot.apps.callout.acs.extension.elements.ExtensionAttributes;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.COUtil;

public class IVRUtil implements ACS, IndiaIVRXMLConstants{
	
	public static ACSResponse checkMandatoryVETags(COConfig config, COLogger logger, ACSRequest request, ACSVERequest veReq){
		ACSResponse veRes = new ACSResponse();
		veRes.setVersion("1.0.2");
		veRes.setProxyPan("");
		veRes.setMessageType(VERIFY_ENROLLMENT_RESPONSE);
		
		VEReqExtension ext = veReq.getVEReqExtension();
		//If ITP credential tag missing send U without any extensions
		if(!ext.getShopChannel().equalsIgnoreCase(AUTH_DATA_NAME_ITP) && ext.getITPCredential() == null){
			veRes.setEnrolled(STATUS_U);
			veRes.setAcsCRC(ACS_CALLOUT_UNSURE);
			veRes.setLogToDB("ITPTagMissing");
			veRes.setLogToFile("ITPTagMissing");
			logger.log(request, "ITPTagMissing, sending U without extension");
			return veRes;
		}
		
		veRes.setAcsCRC(ACS_CALLOUT_SUCCESS);
		return veRes;
	}
	
	public static ACSResponse getDefaultITPVEResponse(COConfig config, COLogger logger, ACSRequest request, ACSVERequest veReq){
		ACSResponse veRes = new ACSResponse();
		veRes.setVersion("1.0.2");
		veRes.setProxyPan("");
		veRes.setMessageType(VERIFY_ENROLLMENT_RESPONSE);
		
		VEReqExtension ext = veReq.getVEReqExtension();
		
		VEResExtension veResExt = new VEResExtension();
		//if tag empty, then send U with itpStatus as 01
		if(ext.getITPCredential() == null || "".equals(ext.getITPCredential().trim())){
			veRes.setEnrolled(STATUS_U);
			veRes.setAcsCRC(ACS_CALLOUT_UNSURE);
			veResExt.setITPStatus(ITP_INVALID_CRED);
			veRes.setExtension(veResExt);
			veRes.setLogToDB("ITPTagEmpty");
			veRes.setLogToFile("ITPTagEmpty");
			logger.log(request, "ITPTagEmpty, sending U with " + ITP_INVALID_CRED);
			return veRes;
		}
		
		//send N if no phone id value is null, status 99
		if(ext.getPhoneId() == null || "".equals(ext.getPhoneId().trim())){
			veRes.setEnrolled(STATUS_N);
			veRes.setAcsCRC(ACS_CALLOUT_PARES_N);
			veResExt.setITPStatus(ITP_OTHER_ERROR);
			veRes.setExtension(veResExt);
			veRes.setLogToDB("PhoneIDMissing");
			veRes.setLogToFile("PhoneIDMissing");
			logger.log(request, "PhoneIDMissing, sending U with " + ITP_OTHER_ERROR);
			return veRes;
		}
		
		String configuredITPCred = config.getValue(ITP_CREDENTIAL);
		//if tag present but not equal to configured value or no value configured send U with 99
		if(configuredITPCred == null || configuredITPCred.indexOf(ext.getITPCredential()) < 0){
			veRes.setEnrolled(STATUS_U);
			veRes.setAcsCRC(ACS_CALLOUT_UNSURE);
			veResExt.setITPStatus(ITP_OTHER_ERROR);
			veRes.setExtension(veResExt);
			veRes.setLogToDB("CredNotMatch");
			veRes.setLogToFile("CredNotMatch");
			logger.log(request, "CredNotMatch, sending U with " + ITP_OTHER_ERROR);
			return veRes;
		}
		
		//if tag present and value matches configured, send VE Y, no extensions
		veRes.setEnrolled(STATUS_Y);
		veRes.setAcsCRC(ACS_CALLOUT_SUCCESS);
		veRes.setLogToDB("CredMatch");
		veRes.setLogToFile("CredMatch");
		logger.log(request, "CredMatch, sending Y with no extensions");
		return veRes;
	}
	
	public static ACSResponse getDefaultITPPAResponse(COConfig config, COLogger logger, ACSRequest request, ExtensionAttributes attr){		
		//return success as tehre were no extension so this must be non-ivr request, let ACS proceed normally for e comm
		String configuredITPCred = config.getValue(ITP_CREDENTIAL);
		if(configuredITPCred == null || configuredITPCred.indexOf(attr.getIdentifier()) < 0){
			//ITP identifier did not match, return N
			logger.log(request, "ITP identifier did not match as configured, returning N");
			return COUtil.createACSResponse(request, ACS_CALLOUT_PARES_N, null, false, 0, "[InvalidITPId]", "[InvalidITPId]");
		}
		
		if(!"TRUE".equalsIgnoreCase(attr.getAuthenticated())){
			logger.log(request, "Authenticated is not TRUE returning N");
			return COUtil.createACSResponse(request, ACS_CALLOUT_PARES_N, null, false, 0, "[InvalidITPId]", "[InvalidITPId]");
		}
		
		logger.log(request, "Authenticated is TRUE returning Y");
		return COUtil.createACSResponse(request, ACS_CALLOUT_PARES_Y, null, true, 0, "[ITPMatch]", "[ITPMatch]");
	}
}
