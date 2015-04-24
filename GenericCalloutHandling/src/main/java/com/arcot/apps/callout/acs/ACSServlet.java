/**
 * 
 */
package com.arcot.apps.callout.acs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.arcot.apps.callout.receiptauthconfig.ReceiptAuthConfig;
import com.arcot.apps.callout.common.BusinessLogicHandler;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.callout.generic.LoadCalloutConfig;
import com.arcot.apps.callout.currencyconverter.CurrencyConverter;
import com.arcot.callout.CallOutsConfig;
import com.arcot.logger.ArcotLogger;
import com.arcot.util.ESConfig;
import com.arcot.vpas.enroll.EnrollmentCrypto;


/**
 * @author abhi
 *
 */
public class ACSServlet extends HttpServlet implements ACS {
	
	private HashMap myCalloutConfigs = null;
	private boolean reInitRuntime = false;
	private COLogger logger = null; 
	private Map recieptConfig = null;
	
	public void init(ServletConfig conf) throws ServletException{
		ArcotLogger.logError("Initilizing ACSServlet : - "+GENERIC_CALLOUT_VERSION);
		super.init(conf);
		String runtimeReinit = this.getInitParameter(RUNTIME_REINITILIZATION);
		if(runtimeReinit != null && ("1".equals(runtimeReinit.trim()) || "true".equalsIgnoreCase(runtimeReinit) ||
				"y".equalsIgnoreCase(runtimeReinit))){
			reInitRuntime = true;
		}
		String debugFlag = this.getInitParameter("debug");
		if(debugFlag != null && ("true".equalsIgnoreCase(debugFlag) ||
				"y".equalsIgnoreCase(debugFlag))){
		}
		String logFilePath = this.getInitParameter(ACS_SERVLET_LOGGER);
		if(logFilePath == null)
			logFilePath = "logs/acsservlet";
		try {
			if(logFilePath.indexOf("/") > -1){
				logger = new COLogger(logFilePath, true,"ACS_SERVLET","INFO");
			}else{
				logger = new COLogger(logFilePath, false,"ACS_SERVLET","INFO");
			}
			/*
			 * Initialize the callout configurations
			 * */
			initialize();
			
			logPSJarsInfo();
			
		} catch (IOException e) {
			e.printStackTrace();
			ArcotLogger.logError("ACSServlet::Issue in initializing a few componenets in ACSServlet, it might affect the callout functionality. " +
					"PLEASE CHECK THE SYSTEM CONFIGURATIONS", e);
		} catch (Exception e) {
			e.printStackTrace();
			ArcotLogger.logError("ACSServlet::Issue in initializing a few componenets in ACSServlet, it might affect the callout functionality. " +
					"PLEASE CHECK THE SYSTEM CONFIGURATIONS", e);
		}
	}
	


	private void initialize(){
		String urlPattern = this.getInitParameter(ACS_SERVLET_NAME);
		myCalloutConfigs = new HashMap();
		Vector ccConfigs = new Vector();
		LoadCalloutConfig.getInstance(ccConfigs);

		/**
		 * with folder change feature lets have the hashmap containning the folderid <-> folder Mapping & localeid <-> locale Mapping
		 * Intent of this piece of code is to make a single DB Call to fetch the complete folder list. 
		 */
		COUtil.getFolderMapping(logger);
		COUtil.getLocaleMapping(logger);
		
		/**
		 * Creating a cache of ARRFCURRCONVRATES table 
		 *  
		 */
		try{
			int currConvVersion = 1;
			ESConfig escConfig = null;
			escConfig = (ESConfig)((this.getServletContext()).getAttribute("ESC"));
			if (escConfig !=null){
				if (escConfig.getValue("CurrencyConversionRate") !=null && escConfig.getValue("CurrencyConversionRate").length()>0)
					currConvVersion = Integer.parseInt(escConfig.getValue("CurrencyConversionRate"));
			}
			CurrencyConverter.fetchCurrencyConvRate(logger, currConvVersion);
		}catch(Exception e){
			logger.warn("Issue caching the ARRFCURRCONVRATES table. It might not be created in the Database. Ignoring it.");
		}

		try{
			ReceiptAuthConfig.populateAuthMethodToAuthChallengeMap(logger);
		}catch(Exception e){
			logger.warn("Issue caching the ARRECEIPTCONFIG table. It might not be created in the Database. Ignoring it.");
		}
		
		int ccSize = ccConfigs.size();
		CallOutsConfig ccConf = null;
		String url = null;
		COConfig myConfig = null;
		
		for(int i = 0; i < ccSize; i++){
			ccConf = (CallOutsConfig)ccConfigs.get(i);
			url = ccConf.getURL();
			if(url != null && url.endsWith(urlPattern)){
				myConfig = new COConfig(ccConf);
				myCalloutConfigs.put(new Integer(ccConf.getConfID()), myConfig);
				logger.info("Configuring [" + ccConf.getName() + "] - [ "+myConfig.getBLHClass()+" ] to be served by ACSServlet");
				ArcotLogger.logError("Configuring [" + ccConf.getName() + "] - [ "+myConfig.getBLHClass()+" ] to be served by ACSServlet");
			}
		}
	}
		
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{
			Date startTime = new Date();
			String reInitialize = request.getParameter("REINITIALIZE");
			if(reInitialize != null && "y".equalsIgnoreCase(reInitialize) && reInitRuntime){
				synchronized(this){
					initialize();
				}
				sendResponse(response, "REINITIALIZE done");
				return;
			}
			String strBank = request.getParameter("BANKID");
			String strRange = request.getParameter("RANGEID");
			String strCCID = request.getParameter("CCID");
			String reqXML = request.getParameter("ACSREQUEST");
			////ACS does not give clear password in request XML!!!
			String clearPIN = request.getParameter("PIN");
			String chIP = request.getParameter("CARDHOLDERIP");
			String strTXType = request.getParameter("TXTYPE");
			
			String txProxyPan = request.getParameter("TXPROXYPAN");
			String dbProxyPan = request.getParameter("DBPROXYPAN");
			// encrypted cardholderName
			String chCardName = request.getParameter("ENCCHNAME");
			
			HttpSession session = request.getSession();
			
			// CALLOUTDATA
			String strCalloutdata = request.getParameter("CALLOUTDATA");
			
			
			
			
			int bankId, rangeId, ccId, txType = -1;
			
			if(logger == null || myCalloutConfigs == null){
				//Problem with configuration, log appropriate message and send back the response
				ArcotLogger.logError("BAD ACS SERVLET CONFIGURATION: DID not initialized properly, check web.xml for log file path");
				sendResponse(response, "ERROR=BAD ACS SERVLET CONFIGURATION" + session.getId());
				return;
			}
			
			logger.info("Request recived for [BankId, RangeId, CCId, sessionId, TXNPAN] = [" + strBank + 
					", " + strRange + ", " + strCCID + ", " + session.getId() + ", "+txProxyPan+ "]");
			
			try {
				bankId = Integer.parseInt(strBank);
			} catch (NumberFormatException e) {
				logger.error("Invalid bank: " + strBank + " sessionId: " + session.getId(), e);
				sendResponse(response, "ERROR=INVALID BANK: " + strBank + " sessionId: " + session.getId());
				return;
			}
			
			try {
				rangeId = Integer.parseInt(strRange);
			} catch (NumberFormatException e) {
				logger.error("Invalid range: " + strRange + " sessionId: " + session.getId(), e);
				sendResponse(response, "ERROR=INVALID RANGE: " + strRange + " sessionId: " + session.getId());
				return;
			}
			
			try {
				ccId = Integer.parseInt(strCCID);
			} catch (NumberFormatException e) {
				logger.error("Invalid callout configuration: " + strCCID + " sessionId: " + session.getId(), e);
				sendResponse(response, "ERROR=INVALID CALLOUTCONFIG: " + strCCID + " sessionId: " + session.getId());
				return;
			}
			
			try {
				txType = Integer.parseInt(strTXType);
			} catch (NumberFormatException e) {
				logger.error("Invalid TX Type: " + strTXType + " sessionId: " + session.getId(), e);
			}
			
			if(reqXML == null){
				logger.error("ACS XML not found in request" + " sessionId: " + session.getId(), null);
				sendResponse(response, "ERROR=NO_REQUEST_XML" + " sessionId: " + session.getId());
				return;
			}
			
			logger.trace("##### ACS reqXML ::: " + reqXML);
			
			ACSRequest acsReq = ACSXMLHandler.parseACSRequest(reqXML, logger);
			if(acsReq == null){
				logger.error("Error in parsing ACS request XML" + " sessionId: " + session.getId(), null);
				sendResponse(response, "ERROR=INVALID_REQUEST_XML" + " sessionId: " + session.getId());
				return;
			}
			
			acsReq.setBankId(bankId);
			acsReq.setRangeId(rangeId);
			acsReq.setCalloutConfigId(ccId);
			acsReq.setPin(clearPIN);//Added for OTP
			acsReq.setChIP(chIP);
			acsReq.setIntTXType(txType);
			
			if (acsReq.getTxProxyPan() == null)
				acsReq.setTxProxyPan(txProxyPan);
	
			if (strCalloutdata != null)
				acsReq.setCalloutData(strCalloutdata);
			
			if (dbProxyPan != null)
				acsReq.setDbProxyPan(dbProxyPan);
			
			if (chCardName != null)
				acsReq.setCardholderName(EnrollmentCrypto.decrypt(chCardName,bankId));
			
			logger.debug(acsReq, "Successfully parsed the ACS request");
			
			/**
			 * 	PrePares Generation Callout
			 * 	'UserAction' Variable 
			 * 	enum ACSUserAction
			 	{
					USER_ACTION_DEF,
					USER_ACTION_OPTIN,
					USER_ACTION_DECLINE,
					USER_ACTION_CANCEL,
					USER_ACTION_PURCH,
					USER_ACTION_OPTIN_CANCEL,
					USER_ACTION_REENROLL
				};
			 **/
			String strUserAction = request.getParameter("USERACTION");
			int userAction = -1;
			if (strUserAction != null){
				try{
					userAction = Integer.parseInt(strUserAction);
				}catch (NumberFormatException ne){
					logger.error("Error Parsing UserAction", ne);
				}
			}
			acsReq.setUserAction(userAction);
			
			/**
			 *  End Of 'UserAction' Variable
			 */
			
			
			/*
			 * Get the relevant myconfig and extract BLH from there
			 * */
			COConfig myConfig = (COConfig)myCalloutConfigs.get(new Integer(ccId));
			
			
			if(myConfig == null && reInitRuntime){
				initialize();
				myConfig = (COConfig)myCalloutConfigs.get(new Integer(ccId));
			}
			
			if(myConfig == null){
				logger.fatal(acsReq, "Could not find configuration details, returning bad response", null);
				sendResponse(response, "ERROR=MISSING_CONFIGURATION_DETAILS" + " sessionId: " + session.getId());
				return;
			}
			
			BusinessLogicHandler acs = myConfig.getBusinessLogicHandler();
			
			if(acs == null){
				logger.fatal(acsReq, "Could not load business logic handler[" + myConfig.getValue(COConfig.LOGIC_HANDLER) + "], returning bad response", null);
				sendResponse(response, "ERROR=MISSING_BUSINESS_HANDLER: " + myConfig.getValue(COConfig.LOGIC_HANDLER) + " sessionId: " + session.getId());
				return;
			}
			
			COLogger coLogger = myConfig.getLogger();
			
			if(coLogger == null){
				logger.fatal(acsReq, "Could not create logger [" + myConfig.getValue(COConfig.LOG_FILE_PATH) + "], returning bad response", null);
				sendResponse(response, "ERROR=MISSING_LOGGER: " + myConfig.getValue(COConfig.LOG_FILE_PATH) + " sessionId: " + session.getId());
				return;
			}
			
			
			ACSResponse acsResp = acs.process(acsReq, myConfig);
			
			
			logger.info(acsReq, " completed with result " + acsResp);
			String acsreponse=ACSXMLHandler.createACSResponse(acsReq, acsResp, logger);
			logger.trace(acsReq, acsreponse);
			sendResponse(response, acsreponse);
			Date endTime = new Date();
			logger.info(acsReq, "TIME[" + (endTime.getTime() - startTime.getTime()) + "]");
			return;
		
		}catch(Exception e){
			logger.error("ACSServlet :: Exception - " + e.getMessage(), e);
			sendResponse(response, "ACSServlet :: Uncaught Exception Rcvd");
			return;
		}
	}
	
	private void sendResponse(HttpServletResponse response, String data) throws IOException{
		OutputStream os = response.getOutputStream();
		os.write(data.getBytes());
		os.flush();
		return;
	}	
	
	private void logPSJarsInfo() throws IOException
	{
		Pattern pat = Pattern.compile(".*\\/(ps.*\\.jar)!.*");
		
		Enumeration<URL> mfs = Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF");
		
		
		ArcotLogger.logInfo("");
		ArcotLogger.logInfo("*************************************************************");
		ArcotLogger.logInfo("             Deployed PS Components Information");
		ArcotLogger.logInfo("*************************************************************");
		
		ArcotLogger.logInfo(String.format("%-40s%-40s%-40s%-40s","  PS Components","Implementation Version","Product-Version","PSLibs-Version"));
		
		while(mfs.hasMoreElements())
		{
			URL u = mfs.nextElement();
			Matcher matcher = pat.matcher(u.getFile());
			
			if(matcher.matches())
			{				
				Manifest mf = new Manifest(u.openStream());
				
				String implementationVersion = mf.getMainAttributes().getValue("Implementation-Version");
				String productVersion = mf.getMainAttributes().getValue("Product-Version");
				if(null != implementationVersion && implementationVersion.contains("-") && null == productVersion){
					String implProdversions[] = implementationVersion.split("-");
					implementationVersion = implProdversions[0];
					productVersion = implProdversions[1];
				}
				String psLibsVersion = mf.getMainAttributes().getValue("PSLibs-Version");				
				ArcotLogger.logInfo(String.format("%-40s%-40s%-40s%-40s", matcher.group(1),
						(implementationVersion == null ? "N/A":implementationVersion),
						(productVersion == null ? "N/A":productVersion),
						(psLibsVersion == null ? "N/A":psLibsVersion)) );				
			}			
			
		}
		ArcotLogger.logInfo("");
		ArcotLogger.logInfo("*************************************************************");
		ArcotLogger.logInfo("");
		
	}
}
