package com.arcot.apps.callout.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.arcot.apps.callout.es.ESRequest;
import com.arcot.callout.CallOutsConfig;
import com.arcot.logger.ArcotLogger;
import com.arcot.vpas.enroll.cache.ESCache;

public class COConfig{
	private CallOutsConfig cCConfig = null;
	private HashMap infolist = null;
	private COLogger logger = null;
	private String businessLogicHandlerClass = null;
	
	public static final String INFO_LIST_SEPARATOR = ";";
	public static final String LOG_FILE_PATH = "LogFilePath";
	public static final String LOG_LEVEL = "LogLevel";
	public static final String LOGIC_HANDLER = "LogicHandler";
	public static final String ARCOT_HOME_VAR = "$ARCOT_HOME";
	private static String ARCOT_HOME = null; 
	
	
	public void init(String il, String logFile){
		infolist = new HashMap();		
		if(il != null){
			StringTokenizer stkPair = new StringTokenizer(il, INFO_LIST_SEPARATOR);
			StringTokenizer stkKeyVal = null;
			String keyVal = null;
			while(stkPair.hasMoreTokens()){
				keyVal = stkPair.nextToken();
				stkKeyVal = new StringTokenizer(keyVal, "=");
				if(stkKeyVal.countTokens() == 2){
					//key value recieved store it
					infolist.put(stkKeyVal.nextToken().trim().toLowerCase(), stkKeyVal.nextToken().trim());
				}
			}
		}
		//Initilize logger for this configuration
		String logFilePath = getValue(LOG_FILE_PATH);
		String logLevel = getValue(LOG_LEVEL);		
		if(logFilePath == null){
			logFilePath = logFile;
		}
		
		try {			
			if(logFilePath.startsWith(ARCOT_HOME_VAR)){
				if(ARCOT_HOME != null){
					logFilePath = logFilePath.replaceFirst(ARCOT_HOME_VAR, ARCOT_HOME);
				}
			}
			
			if(logFilePath.indexOf("/") > -1){				
				logger = new COLogger(logFilePath, true, ""+cCConfig.getName(), logLevel);
			}else{
				logger = new COLogger(logFilePath, false, ""+cCConfig.getName(), logLevel);
			}
		} catch (IOException e) {
			ArcotLogger.logException(e, "COConfig:Logger initializtion failed for [LogFilePath], [" + logFilePath + "]");
		} catch (Exception e) {
			ArcotLogger.logException(e, "COConfig:Logger initializtion failed for [LogFilePath], [" + logFilePath + "]");
		}
		
		businessLogicHandlerClass = getValue(LOGIC_HANDLER);

	}
	
	/*
	 * TODO Might want to enhance it to log in servlet logger later
	 * */
	public COConfig(CallOutsConfig ccConfig){
		//set all necessary variables here, specially parse infolist
		this.cCConfig = ccConfig;
		String il = ccConfig.getInfoList();
		init(il, ccConfig.getName());
	}
	
	@Deprecated
	public COConfig(ESRequest esr){
		//set all necessary variables here, specially parse infolist
		String il = esr.getInfolist();
		CallOutsConfig coConfig = new CallOutsConfig();
		coConfig.setName("DEF_COCONFIG");
		
		setCCConfig(coConfig );
		// if no bank id is set in request
		if(esr.getBankId() == 0)
		{
			// for log file, lets fallback on com.arcot.apps.logger.Log4JWrapper to write logs to default log file.
			init(il, "");
		}
		else
		{
			if(ESCache.bic != null)
				init(il, ESCache.bic.getBank(esr.getBankId()).BANKDIRNAME);
			else
				init(il, "default");
		}
	}
	
	public COConfig(ESRequest esr, CallOutsConfig coConfig){
		//set all necessary variables here, specially parse infolist
		String il = esr.getInfolist();
		setCCConfig(coConfig);
		if(ESCache.bic != null)
			init(il, ESCache.bic.getBank(esr.getBankId()).BANKDIRNAME);
		else
			init(il, "default");
	}
	
	public String getValue(String key){
		if(infolist == null || key == null)
			return null;
		return (String)infolist.get(key.toLowerCase());
	}
	
	public int getIntValue(String key){
		int intVal = -1;
		if(infolist == null || key == null)
			return intVal;
		String val = (String)infolist.get(key.toLowerCase());
		
		try{
			intVal = Integer.parseInt(val);
		}catch (NumberFormatException e) {
			// Do nothing, let it return -1
		}
		return intVal;
	}
	
	public COLogger getLogger(){
		return logger;
	}
	
	public String getBLHClass(){
		return businessLogicHandlerClass;
	}
	
	
	public BusinessLogicHandler getBusinessLogicHandler(){
		if(businessLogicHandlerClass == null){
			return null;
		}
		Class blh = null;
		BusinessLogicHandler blhInstance = null;
		try {
			blh = Class.forName(businessLogicHandlerClass);
			blhInstance = (BusinessLogicHandler)blh.newInstance();
		} catch (ClassNotFoundException e) {
			if(logger != null){
				logger.logException(e, "Unable to load class: " + businessLogicHandlerClass);
			}else{
				ArcotLogger.logException(e, "Unable to load class: " + businessLogicHandlerClass);
			}
		} catch (InstantiationException e) {
			if(logger != null){
				logger.logException(e, "Unable to initialize class: " + businessLogicHandlerClass + " with defualt constructor");
			}else{
				ArcotLogger.logException(e, "Unable to load class: " + businessLogicHandlerClass + " with defualt constructor");
			}
		} catch (IllegalAccessException e) {
			if(logger != null){
				logger.logException(e, "Unable to Access class: " + businessLogicHandlerClass);
			}else{
				ArcotLogger.logException(e, "Unable to Access class: " + businessLogicHandlerClass);
			}
		}		
		return blhInstance;
	}

	public void setCCConfig(CallOutsConfig cCConfig) {
		this.cCConfig = cCConfig;
	}

	public CallOutsConfig getCCConfig() {
		return cCConfig;
	}
	
	public HashMap getAll(){
		return infolist;
	}
}
