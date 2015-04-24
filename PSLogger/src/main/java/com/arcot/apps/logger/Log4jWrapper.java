package com.arcot.apps.logger;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.AsyncAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SMTPAppender;
import org.apache.log4j.rolling.RollingFileAppender;
import org.apache.log4j.rolling.TimeBasedRollingPolicy;

import com.arcot.logger.ArcotLogger;
import com.arcot.util.GenericValidator;
import com.arcot.vpas.enroll.cache.ESCache;

/**
 * 
 * @author Gunjan Kuwadia
 * @version 1.1
 *  
 */
public class Log4jWrapper {
	
	public static Log4jWrapper log4jNew;
	private static Object lock = new Object();
	private static String LOGFILE_BASE_LOCATION = ESCache.esc.getValue("LOG_BASE_LOCATION");// From ESConfig			
	private static String DEFAULT_LOG_FILE = ESCache.esc.getValue("DEFAULT_LOG_FILE"); // From ESConfig
	private static String DEFAULT_LOG_LEVEL = ESCache.esc.getValue("DEFAULT_LOG_LEVEL"); // From ESConfig
	private static String LOG_FORMAT = ESCache.esc.getValue("LOG_FORMAT");	
	private static ArcotPatternLayout layout;
	private static String hn = "Err";
	private static Pattern sensitiveDataPattern;	
	private static boolean maskSensitiveData;
	private static String EMAIL_FROM = ESCache.esc.getValue("LOG_EMAIL_FROM"); // From ESConfig
	private static String EMAIL_TO = ESCache.esc.getValue("LOG_EMAIL_TO"); // From ESConfig
	private static String EMAIL_HOST = ESCache.esc.getValue("LOG_EMAIL_HOST"); // From ESConfig
	private static String EMAIL_PORT = ESCache.esc.getValue("LOG_EMAIL_PORT"); // From ESConfig
	private static String EMAIL_PROTOCOL = ESCache.esc.getValue("LOG_EMAIL_PROTOCOL"); // From ESConfig
	private static String EMAIL_HOST_USERNAME = ESCache.esc.getValue("LOG_EMAIL_HOST_USERNAME"); // From ESConfig
	private static String EMAIL_HOST_PASSWORD = ESCache.esc.getValue("LOG_EMAIL_HOST_PASSWORD"); // From ESConfig	
	private static String LOG_MASK_PATTERN = ESCache.esc.getValue("LOG_MASK_PATTERN"); // From ESConfig
	private static String LOG_MASK_DATA = ESCache.esc.getValue("LOG_MASK_DATA"); // From ESConfig
	private static String LOG_MASK_REPLACE_TEXT = ESCache.esc.getValue("LOG_MASK_REPLACE_TEXT"); // From ESConfig
	private static AsyncAppender asyncAppender;
	private static String version = "$Revision: 1.1 $";
	private Map<String, Logger> listOfLoggers = new HashMap<String, Logger>();
	private Map<String, Boolean> emailTriggerSwitchMap = new Hashtable<String, Boolean>();
	
	private Log4jWrapper(){
		
	}
	
	public static Log4jWrapper getInstance(){
		
		if (log4jNew == null) {
			synchronized (lock) {
				if (log4jNew == null) {
					log4jNew = new Log4jWrapper();
					log4jNew.initLogger();
					ArcotLogger.logInfo("Log4J wrapper initialized........SCM version-->" + version);
				}
			}
		}
		return log4jNew;
	}
	
	/**
	 * 
	 */
	private void initLogger() {
		createLogsBaseLocationIfReqd();	
		if(GenericValidator.isBlankOrNull(DEFAULT_LOG_FILE))
			DEFAULT_LOG_FILE = "transfort_default_log.txt";
		if(GenericValidator.isBlankOrNull(LOG_FORMAT ))
			LOG_FORMAT = "%d{MM-dd-yyyy HH:mm:ss z} %h [%t] %p %c - %m%n";
		layout = new ArcotPatternLayout(LOG_FORMAT);
		//layout = new PatternLayout(LOG_FORMAT);
		InetAddress host;
		try {
			host = InetAddress.getLocalHost();
			hn = host.getHostName();
		} catch (UnknownHostException e) {
			ArcotLogger.logError("Error getting hostname for log4j");		
		}
		if(!GenericValidator.isBlankOrNull(LOG_MASK_PATTERN)){
			try{
				sensitiveDataPattern = Pattern.compile(LOG_MASK_PATTERN);
			} catch (PatternSyntaxException  pttrnSyntaxEx){ // This unchecked exception has to be caught so the logger functionality remains intact
				ArcotLogger.logError("************** ERROR IN DATA MASKING PATTERN. PLEASE CHECK THE PATTERN SYNTAX ***************");
				sensitiveDataPattern = null;
			}
		}		
		ArcotLogger.logInfo("LOG FILE(S) LOCATION IS --> " + LOGFILE_BASE_LOCATION);
		setMaskingData();
		createAysncAppender();
	}
	
	private int getEffectiveSMTPPortNumber() {
		if(GenericValidator.isBlankOrNull(EMAIL_PORT))
			return 25;
		try{
			return Integer.parseInt(EMAIL_PORT);
			
		} catch(Exception _ex){
			ArcotLogger.logError("Email Port from ESConfig is not a valid number. Defaulting to 25");
			return 25;			
		}
		
	}

	private void setMaskingData() {
		maskSensitiveData = isMaskingEnabled();
		if(maskSensitiveData){
			ArcotLogger.logInfo("************* DATA MASKING IS ENABLED *************");			
		}
		else
			ArcotLogger.logInfo("************* DATA MASKING IS DISABLED *************");
	}

	synchronized Logger getLog4jLogger(String fileName, String category, String logLevel){		
		Logger log4jLogger = null;
		String logFileName = getLogFileName(fileName);	
		
		if(GenericValidator.isBlankOrNull(logFileName)){
			ArcotLogger.logInfo("Log path [" + fileName + "] does NOT have file name. Default filename [" + DEFAULT_LOG_FILE +  "] will be used for category [" + category + "]");
			logFileName = DEFAULT_LOG_FILE;
		}
		
		if(GenericValidator.isBlankOrNull(category)){
			ArcotLogger.logInfo("Category is BLANK. Logfile name will be used as category");
			category = logFileName;
		}
		
		Logger existingLogger = returnLoggerIfPresent(category, logFileName);
		if(existingLogger != null){
			log4jLogger = existingLogger;
		} else 
			log4jLogger = configureLogger(category, logLevel, logFileName);
		
		ArcotLogger.logInfo("Category [" + category + "] will be logged in file [" + LOGFILE_BASE_LOCATION + logFileName + "] with level [" + log4jLogger.getLevel() + "] " + listOfLoggers.size());
		
		return log4jLogger;		
	}
	
	private Logger returnLoggerIfPresent(String category, String logFileName) {
		
		Logger loggerFromMap = listOfLoggers.get(logFileName);
		if(loggerFromMap != null){
			ArcotLogger.logInfo("Returning existing category [" + loggerFromMap.getName() + "] for [" + category  + "] as the log file[" + logFileName + "] is same ");
			return loggerFromMap;
		}
		return null;
	}

	private void createLogsBaseLocationIfReqd() {		
		if(LOGFILE_BASE_LOCATION == null){
			LOGFILE_BASE_LOCATION = getDefaultLogLocation();
		} else if(!new File(LOGFILE_BASE_LOCATION).exists()){
			File logLocationFromESConfig = new File(LOGFILE_BASE_LOCATION);
			boolean dirCreated = logLocationFromESConfig.mkdirs();
			if(!dirCreated)
				LOGFILE_BASE_LOCATION = getDefaultLogLocation();
			else 
				LOGFILE_BASE_LOCATION = logLocationFromESConfig.getAbsolutePath() + File.separator ;
		} else 
			LOGFILE_BASE_LOCATION = LOGFILE_BASE_LOCATION + File.separator;
	}
	
	private String getDefaultLogLocation(){
		String defaultLogLoc = System.getenv("ARCOT_HOME") + File.separator + "logs" + File.separator;
		ArcotLogger.logError("############ WARNING::USING DEFAULT LOCATION FOR LOG FILES !!!!  ############");
		return defaultLogLoc;
	}

	private Logger configureLogger(String category, String logLevel,
			String logFileName) {
		Logger log4jLogger  = LogManager.getLogger(category);
		log4jLogger.setAdditivity(false);
		Level returnedLevel = getLogLevel(logLevel);
		log4jLogger.setLevel(returnedLevel);		 
		RollingFileAppender rolling = createDailyRollingFileAppender(logFileName);
		//SMTPAppender smtpAppender = createEmailAppender();	
		
		log4jLogger.addAppender(rolling);	
		log4jLogger.log(returnedLevel, "Logger Initialized..........");
		if(asyncAppender != null){
			log4jLogger.addAppender(asyncAppender);
			emailTriggerSwitchMap.put(category, true);
		} else 
			emailTriggerSwitchMap.put(category, false);
		listOfLoggers.put(logFileName, log4jLogger);
		//log4jLogger.info("Logger [" + category + "] intialized with LOG LEVEL " + returnedLevel + "........" + listOfLoggers.size());
		return log4jLogger;
	}
	
	private void createAysncAppender(){
		SMTPAppender smtpAppender = createEmailAppender();
			if(smtpAppender != null){
			asyncAppender = new AsyncAppender();
			asyncAppender.addAppender(smtpAppender);
			asyncAppender.activateOptions();
		}
	}

	/**
	 * @return
	 */
	private SMTPAppender createEmailAppender() {
		SMTPAppender smtpAppender = null;
		if(sendEmailOnFatalErrors()){
		    smtpAppender = new SMTPAppender();
			smtpAppender.setName("transfortemaillog");
			smtpAppender.setTo(EMAIL_TO);
			smtpAppender.setBufferSize(1);
			smtpAppender.setFrom(EMAIL_FROM);
			smtpAppender.setLayout(layout);
			smtpAppender.setSMTPHost(EMAIL_HOST);
			smtpAppender.setSMTPPort(getEffectiveSMTPPortNumber());
			smtpAppender.setSMTPProtocol(EMAIL_PROTOCOL);
			//smtpAppender.setSMTPDebug(true);
			smtpAppender.setThreshold(Level.FATAL);		
			smtpAppender.setEvaluator(new TransfortEmailTriggerEvaluator());
			smtpAppender.setSubject(hn + " : ERROR in Transfort");
			if(!GenericValidator.isBlankOrNull(EMAIL_HOST_USERNAME) && !GenericValidator.isBlankOrNull(EMAIL_HOST_PASSWORD)){
				smtpAppender.setSMTPUsername(EMAIL_HOST_USERNAME);
				smtpAppender.setSMTPPassword(EMAIL_HOST_PASSWORD);
			}
			smtpAppender.activateOptions();
		}
		return smtpAppender;
	}	

	/**
	 * @param logFileName
	 * @return
	 */
	private RollingFileAppender createDailyRollingFileAppender(String logFileName) {
		TimeBasedRollingPolicy tbrp = new TimeBasedRollingPolicy();
		tbrp.setFileNamePattern(LOGFILE_BASE_LOCATION + logFileName + "_%d{yyyyMMdd}.log"); // Files will roll over daily.
		tbrp.activateOptions();		
		RollingFileAppender rolling = new RollingFileAppender();
		rolling.setName("transfortdailylog");
		rolling.setLayout(layout);
		rolling.setRollingPolicy(tbrp);
		//rolling.setFile(LOGFILE_BASE_LOCATION + logFileName);		
		rolling.activateOptions();
		return rolling;
	}

	private String getLogFileName(String fileName) {
		if(fileName != null){
			File f = new File(fileName);			
			String logFileName = f.getName();
			if("".equals(logFileName))
				return null;
			else
				return logFileName;
		}
		return null;
	}

	private Level getLogLevel(String logLevel) {
		if(!GenericValidator.isBlankOrNull(logLevel)){
			Level level = getLog4jLevel(logLevel);
			if(level == null){
				level = getDefaultLevel();				
			}
			return level;
		} else {
			ArcotLogger.logInfo("Log level not supplied. Default log level [" + DEFAULT_LOG_LEVEL + "] will be set.");
			return getDefaultLevel();
		}
	}
	
	private Level getDefaultLevel() {
		Level dl = getLog4jLevel(DEFAULT_LOG_LEVEL);
		return (dl==null?Level.DEBUG:dl);
	}

	private Level getLog4jLevel(String level){
		if("INFO".equalsIgnoreCase(level))
			return Level.INFO;
		if("DEBUG".equalsIgnoreCase(level))
			return Level.DEBUG;
		if("WARN".equalsIgnoreCase(level))
			return Level.WARN;
		if("ERROR".equalsIgnoreCase(level))
			return Level.ERROR;
		if("TRACE".equalsIgnoreCase(level))
			return Level.TRACE;
		if("FATAL".equalsIgnoreCase(level))
			return Level.FATAL;
		return null;
	}
	
	Map<String, Logger> getAllTransfortLoggers(){		
		return Collections.unmodifiableMap(listOfLoggers);
	}
	
	public boolean maskSensitiveData(){		
		return maskSensitiveData; 
	}
	
	Pattern getMaskSensitiveDataPatern(){
		return sensitiveDataPattern;
	}
	
	String getMaskSensitiveDataReplacmentText(){
		return LOG_MASK_REPLACE_TEXT;
	}
	
	static String getDefaultLogLevel(){
		return DEFAULT_LOG_LEVEL;
	}
	 
	public boolean sendEmailOnFatalErrors() {
		if (GenericValidator.isBlankOrNull(EMAIL_TO)
				|| GenericValidator.isBlankOrNull(EMAIL_FROM)
				|| GenericValidator.isBlankOrNull(EMAIL_HOST)) {
			ArcotLogger
					.logError("############ WARNING!!! - EMAIL CONFIG PARAMETERS FOR LOGGER NOT FOUND. EMAIL WILL NOT BE SENT ON FATAL ERRORS  #############");
			return false;
		}
		return true;
	}
	
	public Map<String, Boolean> getEmailTriggerMap(){
		return Collections.unmodifiableMap(emailTriggerSwitchMap);
	}
	
	void toggleEmailTriggerConfig(String logCatgeory, boolean emailOnOff){
		emailTriggerSwitchMap.put(logCatgeory, emailOnOff);
	}
	
	void toggleDataMasking(){
		maskSensitiveData = !maskSensitiveData;
		ArcotLogger.logInfo("Toggling data masking. Data masking is now " + (maskSensitiveData?"ENABLED":"DISABLED"));
	}
	
	public boolean isMaskingEnabled(){
		
		boolean isMaskingOn = (sensitiveDataPattern != null && !GenericValidator.isBlankOrNull(LOG_MASK_DATA)
				&& ("Y".equalsIgnoreCase(LOG_MASK_DATA) || "true"
						.equalsIgnoreCase(LOG_MASK_DATA))
				&& !GenericValidator.isBlankOrNull(LOG_MASK_PATTERN) && !GenericValidator
				.isBlankOrNull(LOG_MASK_REPLACE_TEXT));	
		return isMaskingOn;
	}
}
