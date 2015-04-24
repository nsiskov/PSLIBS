package com.arcot.apps.callout.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;


import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.acs.ACSResponse;
import com.arcot.apps.callout.common.BusinessLogicHandler;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.common.COResponse;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.callout.es.ES;
import com.arcot.apps.callout.es.ESRequest;
import com.arcot.apps.callout.es.ESResponse;
import com.arcot.database.DatabaseConnection;
import com.arcot.dboperations.DBHandler;
import com.arcot.vpas.enroll.EnrollmentCrypto;

public class GenericDBCheckBLH  extends DBHandler implements ACS, BusinessLogicHandler{

	private static final String checkIssuerAnswer = "SELECT ANSWER,TYPE FROM ARISSUERANSWERS WHERE PAN =? AND BANKID = ?";
	
	public ACSResponse process(ACSRequest request, COConfig config) {
		// TODO Auto-generated method stub
		if(VERIFY_ENROLLMENT_REQUEST.equalsIgnoreCase(request.getMessageType())){			
			COResponse coResp = doVEReq(request, config);
			ACSResponse acsResp = COUtil.prepareACSResponse(request, coResp);
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
	/**
	 * @param request
	 * @param config
	 * @return CO Response (ACS or ES response)
	 * Functionality: If the answers are uploaded for the cardholder returns success or failure.
	 */
	private COResponse doVEReq(CORequest request, COConfig config){
		
		boolean issuerAnsStatus = updateAHA(request, config);
		if(issuerAnsStatus)
			return COUtil.createCOResponse(true, 0, "Ans[F]", "Answers Found In DB", ACS.ACS_CALLOUT_SUCCESS);
		else return COUtil.createCOResponse(false, 0, "Ans[NF]", "Answers NOT Found In DB", ACS.ACS_CALLOUT_FAIL);
	}

	 private boolean updateAHA(CORequest request, COConfig config){
			COLogger logger = config.getLogger();
			
			DatabaseConnection conn = null;
			PreparedStatement stmt = null;
			int rowsSelected = 0;
			
			while (true){
				try{
					conn = dbMan.getConnection();
					if (conn != null && !conn.isClosed()){
						String encCardNumber = EnrollmentCrypto.encrypt(request.getCardNumber(), request.getBankId());
						stmt = conn.prepareStatement(checkIssuerAnswer);
						stmt.setString(1, encCardNumber);
						stmt.setInt(2,request.getBankId());
						rowsSelected = stmt.executeUpdate();
					}else{
						logger.log(request, "Could not get DB connection while selecting Issuer Answers");
						return false;
					}
					break;
				}catch ( SQLException sqle ){
					try{
						if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
							continue;
					}catch ( SQLException sqle1 )
					{}
					// DB down
					logger.log(request, "SQL Error in selecting Issuer Answers", sqle);
					return false;
				}finally{
					try{
						if ( stmt != null ){
							stmt.close();
						}
					}catch ( SQLException sqle ){}
					if(conn != null)
						dbMan.release(conn);
				}
			}		
			if (rowsSelected <= 0)
				return false;
			else return true;
		}
	
	public ESResponse process(ESRequest request, COConfig config) {
		if(ES.ES_POST_IDENTIFICATION == request.getCalloutEvent()){
			COResponse esResp = doVEReq(request, config);			
			COLogger logger = config.getLogger();
			logger.log(request, esResp);
			return COUtil.prepareESResponse(request, esResp);			
		}
		return COUtil.prepareESResponse(request,COUtil.createCOResponse(false, 0, "Unknown Callout", "Unknown Callout", ACS_CALLOUT_FAIL));
	}

}
