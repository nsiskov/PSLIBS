package com.arcot.apps.logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.arcot.logger.ArcotLogger;

/**
 * 
 * @author Gunjan Kuwadia
 * @version 1.1
 *  
 */

public class Log4JLoggerAdmin {
	
	static MyComparator mycomp = new MyComparator();
	public static String CALLOUT_LOG_PREFIX = "callout_";
	private static TreeMap<String, org.apache.log4j.Logger> calloutLoggers = new TreeMap<String, org.apache.log4j.Logger>(mycomp);
	
	public static boolean reConfigureLogger(String logCatgeory, String logLevel, String sendEmailonError){
		
		if(LogManager.exists(logCatgeory) == null)
			return false;
		org.apache.log4j.Logger loggerToChange = LogManager.getLogger(logCatgeory);
		if("Y".equalsIgnoreCase(sendEmailonError)){
			if(!"ON".equalsIgnoreCase(logLevel) && !"OFF".equalsIgnoreCase(logLevel))
				return false;
			
			try{ 
				Log4jWrapper.getInstance().toggleEmailTriggerConfig(logCatgeory, "ON".equalsIgnoreCase(logLevel)?true:false);				
			} catch(Exception _ex)
			{
				ArcotLogger.logError("Error updating email trigger map", _ex);
				return false;
			}
			 return true;
			
		} else if("N".equalsIgnoreCase(sendEmailonError)){ //This is for file logging
			Level log4jLevel = determineLevel(logLevel);
			Level currentLogLevel = loggerToChange.getLevel();
			if(log4jLevel == null)
				return false;
			if(log4jLevel == Level.OFF)
				loggerToChange.log(currentLogLevel, "************* Logging turned OFF for this category *************");
			loggerToChange.setLevel(log4jLevel);
			if(currentLogLevel == Level.OFF && log4jLevel != Level.OFF)
				loggerToChange.log(log4jLevel, "+++++++++++++ Logging is back ON for this category +++++++++++++");
			return true;
		}
		return false;
	}
	
	public static boolean reConfigureLoggerParams(String paramName, String paramValue){
		
		if("DATA_MASK_TOGGLE".equalsIgnoreCase(paramName)){
			 Log4jWrapper.getInstance().toggleDataMasking();
			 return true;
		}
		return false;
	}
	
	public static Map<String, org.apache.log4j.Logger> getAllCalloutLoggers(){
		
		Map<String, org.apache.log4j.Logger> transfortLoggers = Log4jWrapper.getInstance().getAllTransfortLoggers();
		
		if(transfortLoggers.size()>calloutLoggers.size()){
			System.out.println("Repopulating logger list as new loggers are added");
			calloutLoggers.clear();
			for(Logger log4Logger: transfortLoggers.values()){
				String logCategoryName = log4Logger.getName();
				calloutLoggers.put(logCategoryName, log4Logger);
			}
		}
		return Collections.unmodifiableMap(calloutLoggers);
		
	}

	private static Level determineLevel(String logLevel) {		
		if("trace".equalsIgnoreCase(logLevel))
			return Level.TRACE;
		if("debug".equalsIgnoreCase(logLevel))
			return Level.DEBUG;
		if("info".equalsIgnoreCase(logLevel))
			return Level.INFO;
		if("warn".equalsIgnoreCase(logLevel))
			return Level.WARN;
		if("error".equalsIgnoreCase(logLevel))
			return Level.ERROR;
		if("fatal".equalsIgnoreCase(logLevel))
			return Level.FATAL;
		if("off".equalsIgnoreCase(logLevel))
			return Level.OFF;
		System.out.println("******************* LOGLEVEL NOT RECOGNIZED[" + logLevel + "]. ***********************");
		
		return null;
	}
	
	private static class MyComparator implements Comparator<String>{

		public int compare(String s1, String s2) {
			if(s1.contains(CALLOUT_LOG_PREFIX))
				s1 = s1.substring(CALLOUT_LOG_PREFIX.length());
			if(s2.contains(CALLOUT_LOG_PREFIX))
				s2 = s2.substring(CALLOUT_LOG_PREFIX.length());
			return s1.toLowerCase().compareTo(s2.toLowerCase());
		}
		
	}
	
}