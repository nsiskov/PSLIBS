package com.arcot.apps.callout.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSPARequest;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.acs.ACSResponse;
import com.arcot.apps.callout.acs.ACSXMLHandler;
import com.arcot.apps.callout.es.ESRequest;
import com.arcot.apps.callout.es.ESResponse;
import com.arcot.crypto.CryptoUtil;
import com.arcot.database.DatabaseConnection;
import com.arcot.dboperations.DBHandler;
import com.arcot.vpas.enroll.EnrollmentCrypto;

public class COUtil extends DBHandler{
	
	private static String getCalloutData = "select calloutdata from aracctholderauth where cardnumber = ? and cardholdername = ?";
	private static String updateCalloutData = "update aracctholderauth set calloutData = ? where cardnumber = ? and cardholdername = ?";
	private static String getDBAnswers = "select questionid, answer from arissueranswers where bankid = ? and pan = ? and cardholdername = ? order by questionid";
	private static String updateCalloutDataExtension = "update aracctholderauth set CALLOUTDATAEXT = ? where cardnumber = ? and cardholdername = ?";
	private static String mergeTxCalloutData = "merge INTO ARCALLOUTXNDATA a USING    (SELECT ? tid,? txntype FROM dual) incoming ON (a.txnid = incoming.tid and a.txntype=incoming.txntype)  WHEN matched THEN    UPDATE    SET a.CALLOUTDATA = ?,      a.updatedate = sysdate      WHEN NOT matched THEN INSERT (txnid,txntype,calloutdata) VALUES (?,?,?) ";
	private static String getTxnCalloutData = "select calloutdata from  ARCALLOUTXNDATA where txnid = ? and txntype = ?";
	private static final String AMOUNT_FORMAT = "AMOUNT_FORMAT";
	private static final String AMOUNT_LOCALE = "AMOUNT_LOCALE";
	/**
	 * folderIdMap 
	 * 1 - en_US
	 */
	private static HashMap folderIdMap = new HashMap ();
	
	/**
	 * folderMap
	 * en_US - 1
	 */
	private static HashMap folderMap = new HashMap ();
	/**
	 * localeIdMap 
	 * 1 - en_US
	 */
	private static HashMap localeIdMap = new HashMap ();
	
	/**
	 * localeMap
	 * en_US - 1
	 */
	private static HashMap localeMap = new HashMap ();	
	
	
	public static ACSResponse prepareACSResponse(ACSRequest request, COResponse response){
		ACSResponse resp = new ACSResponse();
		resp.setResult(response.isResult());
		resp.setLockStatus(response.getLockStatus());
		resp.setLogToDB(response.getLogToDB());
		resp.setMessageId(request.getMessageId());
		if(resp.getVersion() == null){
			resp.setVersion(request.getVersion());
		}
		resp.setMessageType(ACSXMLHandler.getResponseMessageType(request));
		resp.setAcsCRC(response.getAcsCRC());
		resp.setCalloutMessage(response.getCalloutMessage());
		if(ACS.VERIFY_IA_REQUEST.equals(request.getMessageType())){
			if(resp.isResult()){
				resp.setAnswersStatus(ACS.STATUS_Y);
			}else{
				if(response.getAcsCRC() == ACS.ACS_CALLOUT_FAIL){
					resp.setAnswersStatus(ACS.STATUS_N);
				}else{
					resp.setAnswersStatus(ACS.STATUS_U);
				}
			}
		}
		
		if (response.getFolderId() != null)
            resp.setFolderId(response.getFolderId());
		
		resp.setAuthMethod(response.getAuthMethod());
		
		return resp;
	}
	
	public static COResponse prepareACSResponse(ACSRequest request, COResponse response, String folderId){
		COResponse resp = prepareACSResponse(request, response);
		resp.setFolderId(folderId);
		return resp;
	}
	
	public static ACSResponse createACSResponse(ACSRequest request, int acsCRC, String calloutMsg, boolean result, int lockStatus, String logToDB, String logToFile){
		ACSResponse resp = new ACSResponse();
		resp.setResult(result);
		resp.setLockStatus(lockStatus);
		resp.setLogToDB(logToDB);
		resp.setMessageId(request.getMessageId());
		resp.setVersion(request.getVersion());
		resp.setMessageType(ACSXMLHandler.getResponseMessageType(request));
		resp.setAcsCRC(acsCRC);
		resp.setCalloutMessage(calloutMsg);
		if(ACS.VERIFY_IA_REQUEST.equals(request.getMessageType())){
			if(result){
				resp.setAnswersStatus(ACS.STATUS_Y);
			}else{
				if(acsCRC == ACS.ACS_CALLOUT_FAIL){
					resp.setAnswersStatus(ACS.STATUS_N);
				}else{
					resp.setAnswersStatus(ACS.STATUS_U);
				}
			}			
		}
		return resp;

	}
	
	public static COResponse createACSResponse(ACSRequest request, int acsCRC, String calloutMsg, boolean result, int lockStatus, String logToDB, String logToFile, String folderId){
		COResponse resp = createACSResponse(request, acsCRC, calloutMsg, result, lockStatus, logToDB, logToFile);
		resp.setFolderId(folderId);
		return resp;

	}
	
	public static ESResponse prepareESResponse(ESRequest request, COResponse response){
		ESResponse resp = new ESResponse();
		resp.setLockStatus(response.getLockStatus());
		resp.setResult(response.isResult());
		resp.setLogToDB(response.getLogToDB());
		resp.setLogToFile(response.getLogToFile());
		resp.setCalloutMessage(response.getCalloutMessage());
		resp.setIvrMsgRes(response.getIvrMsgRes());
		resp.setOtp(response.getOTP());
		resp.setShowOtp(response.isShowOtp());
		resp.setAcsCRC(response.getAcsCRC());
		return resp;
	}
	
		
	public static COResponse createCOResponse(boolean result, int lockStatus, String logToDB, String logToFile, int acsCRC){
		COResponse response = new COResponse();
		response.setResult(result);
		response.setLockStatus(lockStatus);
		response.setLogToDB(logToDB);
		response.setLogToFile(logToFile);
		response.setAcsCRC(acsCRC);
		return response;
	}

	public static COResponse createCOResponse(boolean result, int lockStatus, String logToDB, String logToFile, int acsCRC, String calloutMsg, int authMethod){
		COResponse response = createCOResponse(result, lockStatus, logToDB, logToFile, acsCRC);
		response.setCalloutMessage(calloutMsg);
		response.setAuthMethod(authMethod);
		return response;
	}	

	public static COResponse createCOResponse(boolean result, int lockStatus, String logToDB, String logToFile, int acsCRC, String calloutMsg){
		COResponse response = createCOResponse(result, lockStatus, logToDB, logToFile, acsCRC);
		response.setCalloutMessage(calloutMsg);
		return response;
	}	
	
	public static COResponse createCOResponse(boolean result, int lockStatus, String logToDB, String logToFile, int acsCRC, String calloutMsg, String folderId){
		COResponse response = createCOResponse(result, lockStatus, logToDB, logToFile, acsCRC, calloutMsg);
		response.setFolderId(folderId);
		return response;
	}
	/*
	 * Added for IVR : by ranjeet
	 */
	
	public static COResponse createCOResponse(boolean result, int lockStatus, String logToDB, String logToFile, int acsCRC, String ivrmsg, String otp, boolean showOtp){
		COResponse response = createCOResponse(result, lockStatus, logToDB, logToFile, acsCRC);
		response.setIvrMsgRes(ivrmsg);
		response.setOtp(otp);
		response.setShowOtp(showOtp);
		//response.setCalloutMessage(calloutMsg);
		return response;
	}	
	
	/*
	 * Included to serve VE callout
	 * */
	public static String generateProxyPAN(String encryptedPAN)
	{
		// if the ProxyPAN is already in use, we generate a new one
		StringBuffer buf = new StringBuffer(encryptedPAN);
		byte[] cardSecret = new byte[20];
		CryptoUtil.genRandom(cardSecret);
		for (int i = 0; i < cardSecret.length; i++)
			buf.append(Integer.toHexString(cardSecret[i]));
		String proxyPAN = CryptoUtil.hashDataB64(buf.toString());
		return proxyPAN;
	}
	
	/**
     * @deprecated
     * @see #getTxnCalloutData(CORequest, COConfig)
     */
	public static String[] getCalloutData(CORequest request, COConfig config){
		COLogger logger = config.getLogger();		
		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		//Use it for migrated data, while uploading we will nesure taht we put "2" in this column
		ArrayList list = new ArrayList();
		while (true){
			try{
				conn = dbMan.getConnection();
				if (conn != null && !conn.isClosed()){
					String encCardNumber = EnrollmentCrypto.encrypt(request.getCardNumber(), request.getBankId());
					String encCHName = request.getCardholderName();
					if(encCHName == null){
						encCHName = "";
					}
					encCHName = EnrollmentCrypto.encrypt(encCHName.trim(), request.getBankId());
					
					stmt = conn.prepareStatement(getCalloutData);
					stmt.setString(1, encCardNumber);
					stmt.setString(2, encCHName);
					
					rs = stmt.executeQuery();
					while(rs.next()){
						//get the password and email
						list.add(rs.getString("calloutdata"));
					}
				}else{
					logger.log(request, "Could not get DB connection");
					return null;
				}
				break;
			}catch ( SQLException sqle ){
				try{
					if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
						continue;
				}catch ( SQLException sqle1 )
				{}

				// DB down
				logger.log(request, "SQL Error in getting AHA details", sqle);
				return null;
			}finally{
				try{
					if ( rs != null ){
						rs.close();
					}
					if ( stmt != null ){
						stmt.close();
					}
				}catch ( SQLException sqle ){}
				if(conn != null)
					dbMan.release(conn);
			}
		}		
		String[] cdatas = new String[list.size()];
		for (int i = 0; i < cdatas.length; i++) {
			cdatas[i] = (String)list.get(i);
		}
		return cdatas;
	}
	
	//added this function for IVR by Ranjeet
	
	/**
     * @deprecated
     * @see #setTxnCalloutData(CORequest, COConfig, String)
     */
	public static int setCalloutData(CORequest request, COConfig config, String calloutData){
		COLogger logger = config.getLogger();		
		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		int rowsAffected = 0;
		
		while (true){
			try{
				conn = dbMan.getConnection();
				if (conn != null && !conn.isClosed()){
					String encCardNumber = EnrollmentCrypto.encrypt(request.getCardNumber(), request.getBankId());
					String encCHName = request.getCardholderName();
					if(encCHName == null){
						encCHName = "";
					}
					encCHName = EnrollmentCrypto.encrypt(encCHName.trim(), request.getBankId());
					conn.setAutoCommit(false);
					stmt = conn.prepareStatement(updateCalloutData);
					stmt.setString(1, calloutData);
					stmt.setString(2, encCardNumber);
					stmt.setString(3, encCHName);					
					rowsAffected = stmt.executeUpdate();
					conn.commit();
				}else{
					logger.log(request, "Could not get DB connection while updating email");
					return -1;
				}
				break;
			}catch ( SQLException sqle ){
				try{
					if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
						continue;
				}catch ( SQLException sqle1 )
				{}
				// DB down
				logger.log(request, "SQL Error in setting email details", sqle);
				return -1;
			}finally{
				try{
					conn.setAutoCommit(true);
					if ( stmt != null ){
						stmt.close();
					}
				}catch ( SQLException sqle ){}
				if(conn != null)
					dbMan.release(conn);
			}
		}		
		return rowsAffected;
	}
	
	/*
	 * Returns a hashmap of DB answers such that
	 * Key: CHName 
	 * 
	 * */
	public static String[] getDBAnswers(CORequest request, COConfig config, String chName, int maxQID){
		COLogger logger = config.getLogger();
		
		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String[] answers = null;
		if(maxQID < 0){
			maxQID = 5;
		}
		
		answers = new String[maxQID];
		while (true){
			try{
				conn = dbMan.getConnection();
				if (conn != null && !conn.isClosed()){
					String encCardNumber = EnrollmentCrypto.encrypt(request.getCardNumber(), request.getBankId());
					if(chName == null){
						chName = "";
					}
					String encCHName = EnrollmentCrypto.encrypt(chName.trim(), request.getBankId());
					
					stmt = conn.prepareStatement(getDBAnswers);
					stmt.setInt(1, request.getBankId());
					stmt.setString(2, encCardNumber);
					stmt.setString(3, encCHName);
					
					rs = stmt.executeQuery();
					
					int qid = -1;
					String answer = null;
					while(rs.next()){
						qid = rs.getInt(1);
						answer = rs.getString(2);
						if(answer != null && !"".equals(answer)){
							answer = EnrollmentCrypto.decrypt(answer, request.getBankId());
						}
						
						if(qid <= maxQID){
							answers[qid - 1] = answer;
						}else{
							//refactor the array to atleast the size of qid
							maxQID = qid;
							String[] moreAnswers = new String[maxQID];
							for (int i = 0; i < answers.length; i++) {
								moreAnswers[i] = answers[i];
							}
							moreAnswers[qid - 1] = answer;
							answers = moreAnswers;
						}
					}
				}else{
					logger.log(request, "Could not get DB connection");
					return null;
				}
				break;
			}catch ( SQLException sqle ){
				try{
					if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
						continue;
				}catch ( SQLException sqle1 )
				{}

				// DB down
				logger.log(request, "SQL Error in getting AHA details", sqle);
				return null;
			}finally{
				try{
					if ( rs != null ){
						rs.close();
					}
					if ( stmt != null ){
						stmt.close();
					}
				}catch ( SQLException sqle ){}
				if(conn != null)
					dbMan.release(conn);
			}
		}		
		return answers;
	}
	
	/*right now just creating a setcalloutext function  later we have to move it into coutil function.*/ 
	public static int setCalloutDataExt(CORequest request, COConfig config, String calloutDataExtension) {
			COLogger logger = config.getLogger();			
			DatabaseConnection conn = null;
			PreparedStatement stmt = null;
			int rowsAffected = 0;
			
			while (true){
				try{
					conn = dbMan.getConnection();
					if (conn != null && !conn.isClosed()){
						String encCardNumber = EnrollmentCrypto.encrypt(request.getCardNumber(), request.getBankId());
						String encCHName = request.getCardholderName();
						if(encCHName == null){
							encCHName = "";
						}
						conn.setAutoCommit(false);
						encCHName = EnrollmentCrypto.encrypt(encCHName.trim(), request.getBankId());
						
						stmt = conn.prepareStatement(updateCalloutDataExtension);
						stmt.setString(1, calloutDataExtension);
						stmt.setString(2, encCardNumber);
						stmt.setString(3, encCHName);					
						rowsAffected = stmt.executeUpdate();
						conn.commit();
					}else{
						logger.log(request, "Could not get DB connection while updating email");
						return -1;
					}
					break;
				}catch ( SQLException sqle ){
					try{
						if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
							continue;
					}catch ( SQLException sqle1 )
					{}
					// DB down
					logger.log(request, "SQL Error in setting email details", sqle);
					return -1;
				}finally{
					try{
						conn.setAutoCommit(true);
						if ( stmt != null ){
							stmt.close();
						}
					}catch ( SQLException sqle ){}
					if(conn != null)
						dbMan.release(conn);
				}
			}		
			return rowsAffected;	
	}

	/**
	 * 	
	 * With folder change feature lets have the ArrayList containning the folderid <-> folder Mapping 
	 *
	 * @param request
	 * @param config
	 * @return ArrayList
	 * [0] - folderIdMap			::			folderId->folderName
	 * [1] - folderMap				::			folderName->folderId
	 */
	public static void getFolderMapping(COLogger logger){
		
		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		while (true){
			try{
				conn = dbMan.getConnection();
				if (conn != null && !conn.isClosed()){
					stmt = conn.prepareStatement("SELECT FOLDERID, FOLDER FROM ARFOLDER ORDER BY FOLDERID");
					rs = stmt.executeQuery();
					while(rs.next()){
						String folderId = Integer.toString(rs.getInt("FOLDERID"));
						String folderName = rs.getString("FOLDER");
						folderIdMap.put(folderId, folderName);
						folderMap.put(folderName, folderId);
					}
				}else{
					logger.log("COUtil:getFolderMapping()::Could not get DB connection");
					return;
				}
				break;
			}catch ( SQLException sqle ){
				try{
					if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
						continue;
				}catch ( SQLException sqle1 )
				{
					logger.logException(sqle1, "COUtil:getFolderMapping()::FailOver-SQL Error in creating the Folder Map");
				}

				// DB down
				logger.logException(sqle, "COUtil:getFolderMapping()::SQL Error in creating the Folder Map");
				return;
			}finally{
				try{
					if ( rs != null ){
						rs.close();
					}
					if ( stmt != null ){
						stmt.close();
					}
				}catch ( SQLException sqle ){
					logger.logException(sqle, "COUtil:getFolderMapping()::Closing - SQL Error in creating the Folder Map");
				}
				if(conn != null)
					dbMan.release(conn);
			}
		}		
		return;
	}
	
	public static void getLocaleMapping(COLogger logger){
		
		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		while (true){
			try{
				conn = dbMan.getConnection();
				if (conn != null && !conn.isClosed()){
					stmt = conn.prepareStatement("SELECT LOCALEID, LOCALE FROM ARLOCALE ORDER BY LOCALEID");
					rs = stmt.executeQuery();
					while(rs.next()){
						String localeId = Integer.toString(rs.getInt("LOCALEID"));
						String localeName = rs.getString("LOCALE").trim();
						localeIdMap.put(localeId, localeName);
						localeMap.put(localeName, localeId);
					}
				}else{
					logger.log("COUtil:getLocaleMapping()::Could not get DB connection");
					return;
				}
				break;
			}catch ( SQLException sqle ){
				try{
					if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
						continue;
				}catch ( SQLException sqle1 )
				{
					logger.logException(sqle1, "COUtil:getLocaleMapping()::FailOver-SQL Error in creating the Locale Map");
				}

				// DB down
				logger.logException(sqle, "COUtil:getLocaleMapping()::SQL Error in creating the locale Map");
				return;
			}finally{
				try{
					if ( rs != null ){
						rs.close();
					}
					if ( stmt != null ){
						stmt.close();
					}
				}catch ( SQLException sqle ){
					logger.logException(sqle, "COUtil:getFolderMapping()::Closing - SQL Error in creating the Folder Map");
				}
				if(conn != null)
					dbMan.release(conn);
			}
		}		
		return;
	}	
	
	public static String getFolderId(String folderName){
		return (String)folderMap.get(folderName);
	}

	public static String getFolderName(String folderId){
		return (String)folderIdMap.get(folderId);
	}

	public static String getLocaleId(String localeName){
		return (String)localeMap.get(localeName);
	}

	public static String getLocaleName(String localeId){
		return (String)localeIdMap.get(localeId);
	}
	
	/**
     * This method should be used in place of {@link #setCalloutData(CORequest, COConfig, String)} 
     * This method stores the callout data at a transaction level. <br>
     * If it's an ACS transaction, the key is transaction proxy pan <br>
     * If it's an ECS transaction, the key is http session id.
     */
	public static int setTxnCalloutData(CORequest request, COConfig config, String calloutdata){
		
		COLogger logger = config.getLogger();
		if(request == null){			
			throw new IllegalArgumentException("COUtil.setTxnCalloutData():: request object is null");
		}		
				
		String txnRequestType = null;
		String txid = null;		
		
		if(request instanceof ACSRequest){
			txnRequestType = "A";
			ACSRequest acsrequest = (ACSRequest) request;
			txid = acsrequest.getTxProxyPan();			
		}
		if(request instanceof ESRequest){
			txnRequestType = "E";
			ESRequest esrequest = (ESRequest) request;
			txid = esrequest.getSession().getId();			
		}
				
		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		int rowsAffected = 0;
		
		while (true){
			try{
				conn = dbMan.getConnection();
				if (conn != null && !conn.isClosed()){
					conn.setAutoCommit(false);
					stmt = conn.prepareStatement(mergeTxCalloutData);
					stmt.setString(1, txid);
					stmt.setString(2, txnRequestType);
					stmt.setString(3, calloutdata);	
					stmt.setString(4, txid);
					stmt.setString(5, txnRequestType);
					stmt.setString(6, calloutdata);
					rowsAffected = stmt.executeUpdate();
					conn.commit();
				}else{
					logger.log(request, "Could not get DB connection while setting txn calloutdata");
					return -1;
				}
				break;
			}catch ( SQLException sqle ){
				try{
					if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
						continue;
				}catch ( SQLException sqle1 ){
					logger.log(request, "Error while trying to get the DB Connection", sqle1);
				}
				// DB down
				logger.log(request, "SQL Error in setting txn calloutdata", sqle);
				try {
					conn.rollback();
				} catch (SQLException e) {					
					logger.log(request, "Error while rollback of set txncalloutdata", e);
				}
				return -1;
			}finally{
				try{
					conn.setAutoCommit(true);
					if ( stmt != null ){
						stmt.close();
					}
				}catch ( SQLException sqle ){}
				if(conn != null)
					dbMan.release(conn);
			}
		}
		
		return rowsAffected;
	}
	
	/**
	 * This method is used to format the amount from the PAReq request based on the 
	 * value of the AMOUNT_FORMAT set in the callout configuration.
	 * Example:
	 * Input		Pattern			Output
	 * ---------------------------------------
	 * 123456.789   ###,###.###    123,456.789
	 * 123456.789   ###.##         123456.79
	 * 123.78  	    000000.000     000123.780
     * 12345.67     $###,###.###   $12,345.67
     * 
     * Default Pattern:
     * #,###,###,##0.00
	 * @param Pareq Request
	 * @param config
	 * @return
	 */
	public static String pareqAmountFormatter(ACSPARequest request, COConfig config){
		COLogger logger = config.getLogger();
		double doublePurRawAmt = 0.00;
        int intExp = 0;
        double doublePurchaseAmount = 0.00;
        try{
                        // converting the Ram purchase amount to the actual double amount
                        doublePurRawAmt = Double.valueOf(request.getPurchaseRawAmount()).doubleValue();
                        intExp = Integer.valueOf(request.getPurchaseExponent()).intValue();
                        doublePurchaseAmount = doublePurRawAmt/Math.pow(10, intExp);
        }catch (Exception e){
                        logger.error("COUtil:pareqAmountFormatter :: ", e);
        }
        String amount_pattern = (config.getValue(AMOUNT_FORMAT)!=null)? config.getValue(AMOUNT_FORMAT) : "#,###,###,##0.00";
        String amount_locale = (config.getValue(AMOUNT_LOCALE)!=null)? config.getValue(AMOUNT_LOCALE) : "en";
        
        String purchaseAmount= numberFormatLocale(doublePurchaseAmount,amount_pattern,amount_locale);
        logger.debug("COUtil:pareqAmountFormatter :: Purchase Amount from PaReq : "+purchaseAmount);
        return purchaseAmount;
	}
	
	/**
	 * This method is used to format the number in internationalization format.
	 * @param value : value to be formated 
	 * @param pattern : Patter in which the out is expected.
	 * Example:
	 * ###,###.###      123,456.789     en_US
	 * ###,###.###      123.456,789     de_DE
	 * ###,###.###      123 456,789     fr_FR
	 * @param locale
	 * 
	 * @return
	 * Call 
	 * numberFormatLocale(123456.25, "###,###.##", "en")
	 * numberFormatLocale(545421.25, "###,###.##", "de")
	 */
	public static String numberFormatLocale(double value, String pattern, String locale){
		Locale aLocale = new Locale(locale);
		NumberFormat nf = NumberFormat.getNumberInstance(aLocale);
		DecimalFormat df = (DecimalFormat)nf;
		df.applyPattern(pattern);
		return df.format(value);
	}
	/**
	 * This method should be used in place of {@link #getCalloutData(CORequest, COConfig)} 
	 * This method will return the callout data based on either transaction proxy pan if it's ACS <br>
	 * or http session id if it is ES
	 * 
	 * @param request - Either ACS or ES request
	 * @param config - Call out config
	 * @return calloutdata
	 */
	public static String getTxnCalloutData(CORequest request, COConfig config){
		if(request == null){			
			throw new IllegalArgumentException("COUtil.getTxnCalloutData():: request object is null");
		}
		String retcalloutdata = null;		
		COLogger logger = config.getLogger();		
		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String txnRequestType = null;
		String txnid = null;		
		
		if(request instanceof ACSRequest){
			txnRequestType = "A";
			ACSRequest acsrequest = (ACSRequest) request;
			txnid = acsrequest.getTxProxyPan();			
		}
		if(request instanceof ESRequest){
			txnRequestType = "E";
			ESRequest esrequest = (ESRequest) request;
			txnid = esrequest.getSession().getId();			
		}
		while (true){
			try{
				conn = dbMan.getConnection();
				if (conn != null && !conn.isClosed()){
									
					stmt = conn.prepareStatement(getTxnCalloutData);
					stmt.setString(1, txnid);
					stmt.setString(2, txnRequestType);
					
					rs = stmt.executeQuery();
					if(rs.next())
						retcalloutdata = rs.getString(1);
					else {
						logger.log(request,"No txn level callout data found");
						return null;
					}
				}else{
					logger.log(request, "Could not get DB connection");
					return null;
				}
				break;
			}catch ( SQLException sqle ){
				try{
					if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
						continue;
				}catch ( SQLException sqle1 ){
					logger.log(request, "Error while trying to get the DB Connection", sqle1);
				}
				// DB down
				logger.log(request, "SQL Error in getting txn callout details", sqle);
				return null;
			}finally{
				try{
					if ( rs != null ){
						rs.close();
					}
					if ( stmt != null ){
						stmt.close();
					}
				}catch ( SQLException sqle ){}
				if(conn != null)
					dbMan.release(conn);
			}
		}
		return retcalloutdata;
	}
	
}
