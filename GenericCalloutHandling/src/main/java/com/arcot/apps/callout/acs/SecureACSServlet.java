/**
 * 
 */
package com.arcot.apps.callout.acs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.arcot.apps.callout.common.BusinessLogicHandler;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.callout.generic.LoadCalloutConfig;
import com.arcot.apps.callout.currencyconverter.CurrencyConverter;
import com.arcot.callout.CallOutsConfig;
import com.arcot.dboperations.admin.AdminInsertOperations;
import com.arcot.logger.ArcotLogger;
import com.arcot.util.ArcotException;
import com.arcot.util.ESConfig;
import com.arcot.util.GenericValidator;
import com.arcot.util.StaticMessages;
import com.arcot.vpas.enroll.EnrollmentCrypto;
import com.arcot.vpas.enroll.cache.ESCache;


/**
 * @author Soham Datta
 *
 */
public class SecureACSServlet extends HttpServlet implements ACS {
	
	private HashMap myCalloutConfigs = null;
	private boolean reInitRuntime = false;
	private COLogger logger = null; 
	private ResourceBundle staticRes;
	private List<String> pinMessagesList = null;
	
	private final List<Integer> failedResponses = Arrays.asList(ACS_CALLOUT_PARES_N,
			ACS_CALLOUT_SEND_PARES_N,
			ACS_CALLOUT_FAIL,
			ACS_CALLOUT_INVALID_DATA); 
	
	private final List<String> eventsToCheck = Arrays.asList(VERIFY_IA_REQUEST, VERIFY_PASSWORD_REQUEST, UPDATE_CHPROFILE_REQUEST);
	
	private COLogger blankPinLogger = null;
	
	
	public void init(ServletConfig conf) throws ServletException{
		ArcotLogger.logError("Initilizing SecureACSServlet : - "+GENERIC_CALLOUT_VERSION);
		super.init(conf);
		ServletContext ctx = conf.getServletContext();
		String runtimeReinit = this.getInitParameter(RUNTIME_REINITILIZATION);
		if(runtimeReinit != null && ("1".equals(runtimeReinit.trim()) || "true".equalsIgnoreCase(runtimeReinit) ||
				"y".equalsIgnoreCase(runtimeReinit))){
			reInitRuntime = true;
		}
		String debugFlag = this.getInitParameter("debug");
		if(debugFlag != null && ("true".equalsIgnoreCase(debugFlag) ||
				"y".equalsIgnoreCase(debugFlag))){
		}
		/*get the initilization parameters
		 * TODO 1. debug flag (optional, default false)
		 * 2. log file name (optional, default europayacscallout.log)
		 */
		String logFilePath = this.getInitParameter(SECURE_ACS_SERVLET_LOGGER);
		if(logFilePath == null)
			logFilePath = "logs/SecureACSServlet";
		//create logfile
		try {
			if(logFilePath.indexOf("/") > -1){
				logger = new COLogger(logFilePath, true,"SECURE_ACS_SERVLET","INFO");
			}else{
				logger = new COLogger(logFilePath, false,"SECURE_ACS_SERVLET","INFO");
			}
			
			
			// no need to give full path, as logger automatically takes from ESConfig
			blankPinLogger = new COLogger("blankPinPwdLog", false, "SECURE_ACS_BLNK_PINPWD", "INFO");
			/*
			 * Initialize the callout configurations
			 * */
			initialize();
			
			logPSJarsInfo();
			
		} catch (IOException e) {
			e.printStackTrace();
			ArcotLogger.logError("SecureACSServlet::Failed to initialize acscallout logger due to IOException, it might affect the callout functionality. " +
					"PLEASE CHECK THE SYSTEM CONFIGURATIONS", e);
		} catch (Exception e) {
			e.printStackTrace();
			ArcotLogger.logError("SecureACSServlet::Failed to initialize acscallout logger, it might affect the callout functionality. " +
					"PLEASE CHECK THE SYSTEM CONFIGURATIONS", e);
		}
	}
	


	private void initialize(){
		String urlPattern = this.getInitParameter(SECURE_ACS_SERVLET_NAME);
		String masterKey = ESCache.bic.getBank(0).BANKKEY;
		myCalloutConfigs = new HashMap();
		Vector ccConfs = new Vector();
		Collection<CallOutsConfig> ccConfigs=null;
		boolean retVal = false;
		/*try {
			retVal = AdminSelectOperations.getAllCallOutConfig(ccConfigs, masterKey);
		} catch (ArcotException e) {
			e.printStackTrace();
		}*/
		LoadCalloutConfig confdetels = LoadCalloutConfig.getInstance(ccConfs);
		ccConfigs = confdetels.getAllCalloutConfigs();
		//ccConfs = new Vector(ccConfigs);
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
			logger.error("Issue in fetching 'CurrencyConversionRate' from ARESCONFIG table", e);
		}
		//Get static messages
		
		staticRes = StaticMessages.getStaticResource(null, null, null);
		pinMessagesList = new ArrayList<String>();
		
		if(staticRes!=null){

			try
			{
				String totalSting[] =staticRes.getString("CALLOUTRESPONSELOGTTODB").toLowerCase().split("~~");
				for(String pinMessages : totalSting){
					pinMessagesList.add(pinMessages);
				}
				logger.info("Static Pin Message(CALLOUTRESPONSELOGTTODB) List Size: "+pinMessagesList.size());
			}
			catch(MissingResourceException mrex)
			{
				logger.warn(mrex.getMessage() + " No response conversion will be performed.");
			}
		}else{
			logger.info("No Static Resource");
		}
		int ccSize = ccConfigs.size();
		
		logger.info("Callout Configuration List Size: " + ccSize);
		
		//CallOutsConfig ccConf = null;
		String url = null;
		COConfig myConfig = null;
		for(CallOutsConfig ccConfigsList: ccConfigs){
			url = ccConfigsList.getURL();
			if(url != null && url.endsWith(urlPattern)){
				//logger.info("Inside for loop");
				//This means this servlet has to serve that config... do as necessary
				myConfig = new COConfig(ccConfigsList);
				myCalloutConfigs.put(new Integer(ccConfigsList.getConfID()), myConfig);
				logger.info("Configuring [" + ccConfigsList.getName() + "] - [ "+myConfig.getBLHClass()+" ] to be served by SecureACSServlet");
				ArcotLogger.logError("Configuring [" + ccConfigsList.getName() + "] - [ "+myConfig.getBLHClass()+" ] to be served by SecureACSServlet");
			}
		}
	}
		
	private String logCalloutInfo(String calloutClassPath, String calloutConfig){
		ClassLoader loader = getClass().getClassLoader();
		Class cl = null;
		String classMD5 = null;
		try {
			cl = loader.loadClass(calloutClassPath);
			java.net.URL url = cl.getResource(cl.getSimpleName()+".class");
			File f = new File(new URI(url.getPath().substring(1)).getPath());
			MessageDigest digest = MessageDigest.getInstance("MD5");
			InputStream is = new FileInputStream(f);				
			byte[] buffer = new byte[8192];
			int read = 0;
			while( (read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}		
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			classMD5 = bigInt.toString(16);
			// using Display ID instead of callout Path
			AdminInsertOperations.logCalloutInfo(InetAddress.getLocalHost().getHostName(), calloutConfig, classMD5);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArcotException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} catch(Throwable t){
            t.printStackTrace();
            //ArcotLogger.logException(new Exception(t), "generic error");
		}
		return classMD5;
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
		/*
		 * TODO Need to enhance error handling after the functionality if achieved
		 * This means ACS has to send the XML as ACSREQUEST=<actual xml>
		 * BANKID, RANGEID, CALLOUTCONFIGID
		 * So the request will come like
		 * BANKID=<bankid>&RANGEID=<rangeid>&CCID=<calloutconfigid>&ACSREQUEST=<actual xml>
		 * */
		
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
				logger.fatal(acsReq, "SecureACSServlet: Could not find configuration details, returning bad response", null);
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
			
			
			boolean pinBlank = false;
			boolean pwdBlank = false;
			
			if(GenericValidator.isBlankOrNull(acsReq.getPin()) && GenericValidator.isBlankOrNull(acsReq.getCHProfile()) && GenericValidator.isBlankOrNull(acsReq.getIssuerAnswer()) )
			{
				pinBlank = true;
			}
					
			if(GenericValidator.isBlankOrNull(acsReq.getPassword()))
			{
				pwdBlank = true;
			}
				
			
			ACSResponse acsResp = acs.process(acsReq, myConfig);
			
			// checking for UNSURE Condition
			if(acsResp.getAcsCRC() == ACS_CALLOUT_UNSURE){
				logger.info(acsReq, "Callout response ACS UNSURE and LogtoDB is "+ acsResp.getLogToDB());
				
				if(pinMessagesList.size()!=0){
		            if(acsResp.getLogToDB()!=null){
		            	if(pinMessagesList.contains(acsResp.getLogToDB().toLowerCase())){
		            		logger.info(acsReq, "Changing the response to ACS_CALLOUT_PARES_N, LogToDB match is: "+acsResp.getLogToDB());
		            		acsResp.setAcsCRC(ACS_CALLOUT_PARES_N);
					     }		            	
		            }else{
		            	logger.info(acsReq, "LogToDB is null. Not Changing the response");
		            }
				}
				else
				{
					logger.info(acsReq, "Static Resource is empty");
				}
				
				if(pinBlank && ACS_CALLOUT_PARES_N != acsResp.getAcsCRC())
        		{
        			blankPinLogger.info(acsReq,"For BLANK PIN BLH [ "+acs.getClass().getName()+" ] returned U with logMsg : " + acsResp.getLogToDB());
        		}		            		
        		
			}
			else
			{
				if(eventsToCheck.contains(acsReq.getMessageType()))
				{
					if(pinBlank && !failedResponses.contains(acsResp.getAcsCRC()))
					{
						blankPinLogger.info(acsReq,"For BLANK PIN BLH [ "+acs.getClass().getName()+" ] returned non-FAIL response : " + acsResp.getAcsCRC());
					}
					
				}
				
				if(pwdBlank && VERIFY_PASSWORD_REQUEST.equals(acsReq.getMessageType()) && !failedResponses.contains(acsResp.getAcsCRC()))
				{
					blankPinLogger.info(acsReq,"For BLANK PWD BLH [ "+acs.getClass().getName()+" ] returned non-FAIL response : " + acsResp.getAcsCRC());
				}
				
			}
			
			logger.info(acsReq, " completed with result " + acsResp);
			String acsreponse=ACSXMLHandler.createACSResponse(acsReq, acsResp, logger);
			logger.trace(acsReq, acsreponse);
			sendResponse(response, acsreponse);
			Date endTime = new Date();
			logger.info(acsReq, "TIME[" + (endTime.getTime() - startTime.getTime()) + "]");
			return;
		
		}catch(Exception e){
			logger.error("SecureACSServlet :: Exception - " + e.getMessage(), e);
			sendResponse(response, "SecureACSServlet :: Uncaught Exception Rcvd");
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
		
		ArcotLogger.logInfo(String.format("%-25s%8s%-10s%-20s","  PS Components","Build Id","","Build Job"));
		ArcotLogger.logInfo(String.format("%-25s%8s%-8s%-20s","-----------------","--------","","--------------"));
		
		while(mfs.hasMoreElements())
		{
			URL u = mfs.nextElement();
			Matcher matcher = pat.matcher(u.getFile());
			
			if(matcher.matches())
			{				
				Manifest mf = new Manifest(u.openStream());
				
				String buildId = mf.getMainAttributes().getValue("BUILD-ID");
				
				String jenkinsJob = mf.getMainAttributes().getValue("Jenkins-Job");
				
				ArcotLogger.logInfo(String.format("%-25s%5s%10s%-20s", matcher.group(1),(buildId == null ? "N/A":buildId),"",(jenkinsJob == null ? "N/A":jenkinsJob)) );				
				
			}			
			
		}
		ArcotLogger.logInfo("");
		ArcotLogger.logInfo("*************************************************************");
		ArcotLogger.logInfo("");
		
	}
	
	@Override
	public void destroy()
	{
		blankPinLogger.closeFile();
		super.destroy();
	}
}
