package com.arcot.apps.logger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.arcot.logger.ArcotLogger;

/**
 * 
 * @author Gunjan Kuwadia
 * @version 1.1
 *  
 */

public class Logger {
	    
		private static Pattern sensitiveDataPattern;				
		private String logMaskReplacementText; 
		private org.apache.log4j.Logger log4jLogger;
	    
	    /**
	     * 
	     * @param fileName - Name of the logfile. Don't give the full path.
	     * @param category - Log category name. Generally this is the fully qualified class name
	     * @param logLevel - One of the six log levels i.e. TRACE, DEBUG, INFO, WARN, ERROR, FATAL
	     * 	<br>
	     *  Avoid using the other constructors of this class. Those are retained for backward compatibility.
	     */
		public Logger(String fileName, String category, String logLevel){
	    	Log4jWrapper log4jWrapper = Log4jWrapper.getInstance();
	    	log4jLogger = log4jWrapper.getLog4jLogger(fileName, category, logLevel);
	    	sensitiveDataPattern = log4jWrapper.getMaskSensitiveDataPatern();
	    	logMaskReplacementText = log4jWrapper.getMaskSensitiveDataReplacmentText();	
	    }
		
		/**
		 * 
		 * @param fileName
		 * @param pathProvided
		 * @param category
		 * @param logLevel
		 * This constructor is for backward compatibility. Wherever possible use {@link #Logger(String, String, String)}
		 */
		public Logger(String fileName, boolean pathProvided, String category, String logLevel){	  
			this(fileName,category, logLevel);
	    }
		
	    /**
	     * 
	     * @param fileName
	     * @throws IOException
	     * @throws Exception
	     * This constructor is for backward compatibility. Wherever possible use {@link #Logger(String, String, String)}
	     */
		public Logger(String fileName) throws IOException,Exception {	
	    	this(fileName, true, null, Log4jWrapper.getDefaultLogLevel());
	    }
	    
	    /**
	     * 
	     * @param fileName
	     * @param pathProvided
	     * @throws IOException
	     * @throws Exception
	     * This constructor is for backward compatibility. Wherever possible use {@link #Logger(String, String, String)}
	     */
		public Logger(String fileName, boolean pathProvided) throws IOException,Exception {
	        this(fileName);	    	
	    }
	    
	    @Deprecated
	    public void log(String str) {
	    	debug(str);
	    }
	    
	    @Deprecated
	    public void logException(Exception e, String text) {
			error(text,e);
	    }
	    
	    public void trace(String msg){
			
			if(log4jLogger != null){
				if(log4jLogger.isTraceEnabled())
					if(Log4jWrapper.getInstance().maskSensitiveData())
						log4jLogger.trace(getMaskedMsg(msg));
					else
						log4jLogger.trace(msg);
			}
			else { 
				logToArcotLogger(msg, null);
			}
		}
		
		public void debug(String msg){
			
			if(log4jLogger != null){
				if(log4jLogger.isDebugEnabled())
					if(Log4jWrapper.getInstance().maskSensitiveData())
						log4jLogger.debug(getMaskedMsg(msg));
					else
						log4jLogger.debug(msg);
			}
			else 
				logToArcotLogger(msg, null);
		}
		
		public void info(String msg){
			
			if(log4jLogger != null){
				if(log4jLogger.isInfoEnabled())
					if(Log4jWrapper.getInstance().maskSensitiveData())
						log4jLogger.info(getMaskedMsg(msg));
					else
						log4jLogger.info(msg);
			}
			else 
				logToArcotLogger(msg, null);
		}
		
		public void warn(String msg){
			
			if(log4jLogger != null){	
				if(Log4jWrapper.getInstance().maskSensitiveData())
					log4jLogger.warn(getMaskedMsg(msg));
				else
					log4jLogger.warn(msg);
			}
			else 
				logToArcotLogger(msg, null);
		}
		
		/**
		 * @param msg
		 * @param e - This is optional.
		 */
		public void error(String msg, Exception e){
			
			if(log4jLogger != null){	
				if(e!=null){
					if(Log4jWrapper.getInstance().maskSensitiveData())
						log4jLogger.error(getMaskedMsg(msg), e);
					else
						log4jLogger.error(msg, e);
				}
				else {
					if(Log4jWrapper.getInstance().maskSensitiveData())
						log4jLogger.error(getMaskedMsg(msg));
					else
						log4jLogger.error(msg);
				}				
			}
			else 
				logToArcotLogger(msg, e);
		}
		
		/** Please use this with caution as for every fatal log, an email will be sent out.
		 * @param msg
		 * @param e - This is optional.
		 */
		public void fatal(String msg, Exception e){
			
			if(log4jLogger != null){	
				if(e!=null){
					if(Log4jWrapper.getInstance().maskSensitiveData())
						log4jLogger.fatal(getMaskedMsg(msg), e);
					else
						log4jLogger.fatal(msg, e);
				}
				else {
					if(Log4jWrapper.getInstance().maskSensitiveData())
						log4jLogger.fatal(getMaskedMsg(msg));
					else
						log4jLogger.fatal(msg);
				}				
			}
			else 
				logToArcotLogger(msg, e);
		}
		
		public boolean isTraceEnabled(){
			return log4jLogger.isTraceEnabled();
		}
		
		public boolean isDebugEnabled(){
			return log4jLogger.isDebugEnabled();
		}
		
		public boolean isInfoEnabled(){
			return log4jLogger.isInfoEnabled();
		}
		
		private String getMaskedMsg(String msg) {
			Matcher maskedDataMatcher = sensitiveDataPattern.matcher(msg);
			return maskedDataMatcher.replaceAll(logMaskReplacementText);
		}
		
		private void logToArcotLogger(String msg, Exception e){
			if(e==null){
				if(Log4jWrapper.getInstance().maskSensitiveData()){
					ArcotLogger.logInfo(getMaskedMsg(msg));
				} else {
					ArcotLogger.logInfo(msg);
				}
			} else {
				if(Log4jWrapper.getInstance().maskSensitiveData()){
					ArcotLogger.logError(getMaskedMsg(msg), e);
				} else {
					ArcotLogger.logError(msg, e);
				}
			}
		}
		
		/**
		 * This method does nothing. Retained for backward compatibility. 
		 */
		public void closeFile(){
			// Do nothing as this method is just for backward compatibility.
		}
	}
