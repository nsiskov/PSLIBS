package com.arcot.apps.callout.matchlibrary.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.field.impl.CreditCardLimitField;
import com.arcot.apps.callout.matchlibrary.field.impl.DrivingLicenseField;
import com.arcot.apps.callout.matchlibrary.field.impl.EmailField;
import com.arcot.apps.callout.matchlibrary.field.impl.HintAnswerField;
import com.arcot.apps.callout.matchlibrary.field.impl.LastBillAmoutField;
import com.arcot.apps.callout.matchlibrary.field.impl.OTPField;
import com.arcot.apps.callout.matchlibrary.field.impl.PhoneField;
import com.arcot.apps.callout.matchlibrary.field.impl.PostalCodeField;
import com.arcot.apps.callout.matchlibrary.field.impl.RegCodeField;
import com.arcot.apps.callout.matchlibrary.field.impl.SSNField;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;
import com.arcot.logger.ArcotLogger;

public abstract class Field {
	
	public enum FieldType {
	    ALPHANUMERIC, 
	    DATE, 
	    DEFAULT, 
	    EMAIL, 
	    NAME, 
	    NUMERIC, 
	    NUMERICDOUBLE, 
	    REGEX, 
	    SSN, 
	    CREDITCARDLIMIT, 
	    DRIVINGLICENCE, 
	    HINTANSWER, 
	    LASTBILLAMOUNT, 
	    PHONE, 
	    POSTALCODE,
	    OTP,
	    REGISTRATIONCODE;
	}
	
	public static final String FIELD_CARDHOLDERNAME 					= "CHN";
	public static final String FIELD_MOTHERMAIDENNAME 					= "MMN";
	public static final String FIELD_FATHERNAME 						= "FN";
	public static final String FIELD_DOB 								= "DOB";
	public static final String FIELD_EXPIRY_DATE 						= "ED";
	public static final String FIELD_VALID_FROM_DATE 					= "VF";
	public static final String FIELD_EMAIL 								= "EMAIL";
	public static final String FIELD_PRIMARYEMAIL 						= "PEMAIL";
	public static final String FIELD_SECONDARYEMAIL 					= "SEMAIL";
	public static final String FIELD_PHONE		 						= "PHONE";
	public static final String FIELD_MOBILE		 						= "MOBILE";
	public static final String FIELD_LANDLINE	 						= "LL";
	public static final String FIELD_HOMEPHONE							= "HOMEPHONE";
	public static final String FIELD_WORKPHONE							= "WORKPHONE";
	public static final String FIELD_CVV								= "CVV";
	public static final String FIELD_SSN		 						= "SSN";
	public static final String FIELD_POSTALCODE		 					= "POSTALCODE";
	public static final String FIELD_DRIVINGLICENCE		 				= "DRIVINGLIC";
	public static final String FIELD_ADDRESS		 					= "ADDRESS";
	public static final String FIELD_CITY		 						= "CITY";
	public static final String FIELD_STATE		 						= "STATE";
	public static final String FIELD_COUNTRY		 					= "COUNTRY";
	public static final String FIELD_HINTANSWER		 					= "HINTANSWER";
	public static final String FIELD_ACCNUM		 						= "ACCNUM";
	public static final String FIELD_CREDITLIMIT		 				= "CREDITLIMIT";
	public static final String FIELD_OTP		 						= "OTP";
	public static final String FIELD_REGCODE		 					= "REGCODE";
	public static final String FIELD_ATMPIN		 						= "ATMPIN";
	public static final String FIELD_LASTBILLAMOUNT		 				= "LASTBILLAMOUNT";

	protected CORequest request;
	protected COConfig config;
	protected String fieldName;
	protected String userData;
	protected String userDataFormat;
	protected String storedData;
	protected List<String> storedDataFormatList = new ArrayList<String>();
	protected List<String> whatIfStoredDataFormatList = new ArrayList<String>();
	protected List<String> matchFormatList = new ArrayList<String>();
	protected List<String> whatIfMatchFormatList = new ArrayList<String>();
	protected boolean isHashed = false;
	protected boolean ignoreCase = false;
	protected String logLevel = Constants.CONFVAL_LOG_INFO;
	
	public abstract boolean matchHashed(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException;
	public abstract boolean matchEncrypted(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException;
	
	public static Field createField(CORequest request, COConfig config, FieldType fieldType, String fieldName, String userData, String storedData) {
		switch(fieldType) {
			case ALPHANUMERIC :
				return new AlphaNumericField(request, config, fieldName, userData, storedData);
			case DATE :
				return new DateField(request, config, fieldName, userData, storedData);
			case DEFAULT :
				return new DefaultField(request, config, fieldName, userData, storedData);
			case EMAIL :
				return new EmailField(request, config, fieldName, userData, storedData);
			case NAME :
				return new NameField(request, config, fieldName, userData, storedData);
			case NUMERIC :
				return new NumericField(request, config, fieldName, userData, storedData);
			case NUMERICDOUBLE :
				return new NumericDoubleField(request, config, fieldName, userData, storedData);
			case REGEX : 
				return new RegExField(request, config, fieldName, userData, storedData);
			case SSN : 
				return new SSNField(request, config, fieldName, userData, storedData);
			case CREDITCARDLIMIT : 
				return new CreditCardLimitField(request, config, fieldName, userData, storedData); 
			case DRIVINGLICENCE : 
				return new DrivingLicenseField(request, config, fieldName, userData, storedData); 
			case HINTANSWER : 
				return new HintAnswerField(request, config, fieldName, userData, storedData);
			case LASTBILLAMOUNT : 
				return new LastBillAmoutField(request, config, fieldName, userData, storedData);
			case PHONE : 
				return new PhoneField(request, config, fieldName, userData, storedData);
			case POSTALCODE : 
				return new PostalCodeField(request, config, fieldName, userData, storedData);
			case REGISTRATIONCODE : 
				return new RegCodeField(request, config, fieldName, userData, storedData);
			case OTP : 
				return new OTPField(request, config, fieldName, userData, storedData);
			default :
				return new DefaultField(request, config, fieldName, userData, storedData);
		}
	}
	
	public Field(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		this.request = request;
		this.config = config;
		this.fieldName = fieldName;
		this.userData = userData;
		this.storedData = storedData;
	}
	
	public void prepare() throws MatchException {
		setDebugLevel(getDebugLevelFromConfig());
		setHashed(getHashedFlagFromConfig());
		
		try {
			if(StringUtil.isBlank(fieldName))
				throw new MatchException(Constants.ERR_BLANK_FIELDNAME, "Field name is null.");
			setUserDataFormat(getUserDataFormatFromConfig());
			setStoredDataFormatList(getStoredDataFormatFromConfig());
			setMatchFormatList(getFieldValueFromConfig(Constants.CONF_MATCH_FORMAT));
		} catch (MatchException e) {
			log("MatchException while preparing field :"+ e.getMessage());
			throw e;
		}
	}
	
	public CORequest getRequest() {
		return request;
	}

	public COConfig getConfig() {
		return config;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getUserData() {
		return userData;
	}
	
	public String getStoredData() {
		return storedData;
	}
	
	public String getUserDataFormat() {
		return userDataFormat;
	}
	
	protected boolean validateUserDataFormat(String udf) throws MatchException {
		return true;
	}
	protected void setUserDataFormat(String userDataFormat) throws MatchException {
		validateUserDataFormat(userDataFormat);
		this.userDataFormat = userDataFormat;
	}
	public List<String> getStoredDataFormatList() {
		return storedDataFormatList;
	}
	public List<String> getWhatIfStoredDataFormatList() {
		return whatIfStoredDataFormatList;
	}
	protected boolean validateSingleStoredDataFormat(String sdf) throws MatchException {
		return true;
	}

	protected void setStoredDataFormatList(String storedDataFormat) throws MatchException {
		if(StringUtil.isBlank(storedDataFormat))
			return;
		
		String[] temp = storedDataFormat.split(Constants.FORMAT_DELIM);
		if(temp == null)
			throw new MatchException(Constants.ERR_INVALID_STOREDDATAFORMAT, "Null Array returned after parsing the Format with "+ Constants.FORMAT_DELIM);
		
		for(String format : temp) {
			if(!validateSingleStoredDataFormat(format))
				throw new MatchException(Constants.ERR_INVALID_STOREDDATAFORMAT, "Invalid stored Format.");
			if(isWhatIfFormat(format)) {
				whatIfStoredDataFormatList.add(format.substring(1, format.length()-1));
			} else {
				storedDataFormatList.add(format);
			}
		}
		if(storedDataFormatList == null || storedDataFormatList.size() < 1)
			throw new MatchException(Constants.ERR_INVALID_STOREDDATAFORMAT, Constants.ERR_INVALID_STOREDDATAFORMAT);
	}
	
	public List<String> getMatchFormatList() {
		return matchFormatList;
	}
	
	public List<String> getWhatIfMatchFormatList() {
		return whatIfMatchFormatList;
	}
	
	protected boolean validateSingleMatchDataFormat(String mf) throws MatchException {
		return true;
	}
	protected void setMatchFormatList(String matchFormatFromConfig) throws MatchException {
		if(StringUtil.isBlank(matchFormatFromConfig))
			return;
		
		String[] temp = matchFormatFromConfig.split(Constants.FORMAT_DELIM);
		if(temp == null)
			throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "No value configured after parsing.");
		
		for(String format : temp) {
			if(!validateSingleMatchDataFormat(format))
				throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "Invalid MatchFormat.");
			if(isWhatIfFormat(format)) {
				whatIfMatchFormatList.add(format.substring(1, format.length()-1));
			} else {
				matchFormatList.add(format);
			}
		}
	}
	
	public boolean isHashed() {
		return isHashed;
	}
	
	protected void setHashed(boolean isHashed) {
		this.isHashed = isHashed;
	}
	
	public boolean ignoreCase() {
		return ignoreCase;
	}
	
	protected void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}	
	
	public String getUserDataFormatFromConfig() throws MatchException {
		String udf = getFieldValueFromConfig(Constants.CONF_USERDATAFORMAT);

		if(StringUtil.isBlank(udf))
			throw new MatchException(fieldName+"_"+Constants.ERR_BLANK_USERDATAFORMAT, fieldName+"_"+Constants.ERR_BLANK_USERDATAFORMAT);
		
		return udf;
	}
	
	public String getStoredDataFormatFromConfig() throws MatchException {
		String sdf = getFieldValueFromConfig(Constants.CONF_STOREDDATAFORMAT);

		if(StringUtil.isBlank(sdf))
			throw new MatchException(Constants.ERR_BLANK_STOREDDATAFORMAT, Constants.ERR_BLANK_STOREDDATAFORMAT);
		
		return sdf;
	}
	
	protected boolean isWhatIfFormat(String format) {
		return format.startsWith("[") && format.endsWith("]");
	}
	
	private String getDebugLevelFromConfig() {
		String logLevelStr = config.getValue(Constants.CONF_LOG_LEVEL);
		if(!StringUtil.isBlank(logLevelStr)) {
			if(Constants.CONFVAL_LOG_DEBUG.equals(logLevelStr))
				return Constants.CONFVAL_LOG_DEBUG;
		}
		return Constants.CONFVAL_LOG_INFO;
	}
	
	protected void setDebugLevel(String debugLevel) {
		if(!StringUtil.isBlank(debugLevel)) {
			if(Constants.CONFVAL_LOG_DEBUG.equals(debugLevel))
				logLevel = Constants.CONFVAL_LOG_DEBUG;
		}
	}

	public void info(String text) {
		log(text);
	}
	
	public void log(String message) {
		if(fieldName != null)
			message = fieldName +":"+ message;
		if(config == null || config.getLogger() == null) {
			ArcotLogger.logError(message);
		} else {
			COLogger logger = config.getLogger();
			if(request == null)
				logger.log("["+message+"]");
			else
				logger.log(request, "["+message+"]");
		}
	}
	
	public void debug(String text) {
		if(logLevel.equals(Constants.CONFVAL_LOG_DEBUG)) {
			log("DEBUG - " +text);
		}
	}
	
	public String getSuccessCalloutStatus(String formatId) {
		String successLog = getFieldValueFromConfig(Constants.CONF_SUCCESS_RESULT_LOG);
		if(StringUtil.isBlank(successLog))
			successLog = fieldName + "[s"+ formatId +"]";
		else
			successLog = successLog + formatId;
		return successLog;
	}

	public String getFailureCalloutStatus() {
		String failureLog = getFieldValueFromConfig(Constants.CONF_FAILURE_RESULT_LOG);
		if(StringUtil.isBlank(failureLog))
			failureLog = fieldName + "[f]";
		return failureLog;
	}
	
	/*
	 * <FIELD>_HASHED has highest priority, Look for ALL_HASHED iff that is not found.
	 * */
	private boolean getHashedFlagFromConfig() {
		boolean isHashed = false;
		String encFlag = getFieldValueFromConfig(Constants.CONF_HASHED);
		if(StringUtil.isBlank(encFlag)) {
			isHashed = Boolean.valueOf(config.getValue(Constants.CONF_ALL_HASHED));
		} else {
			isHashed = Boolean.valueOf(encFlag);
		}
		return isHashed;
	}
	
	public char[] getSplCharArray() {
		String splCharStr = getFieldValueFromConfig(Constants.CONF_SPLCHARS_LIST);
		if(StringUtil.isBlank(splCharStr))
			return Constants.SPECIAL_CHARS_ARRAY;
		return splCharStr.toCharArray();
	}
	
	public String getFieldValueFromConfig(String configName) {
		return config.getValue(fieldName + Constants.DELIM + configName);
	}
	
	public boolean getConfigBooleanValue(String configName, boolean defaultValue) {
		String flagStr = getFieldValueFromConfig(configName);
		if(StringUtil.isBlank(flagStr))
			flagStr = String.valueOf(defaultValue);
		return Boolean.valueOf(flagStr);
	}
	
	protected boolean getFlag(HashMap<String, String> flagsMap, String flagName, boolean defaultValue) {
		String flagValue = flagsMap.get(flagName);

		if(!StringUtil.isBlank(flagValue)) {
			if(flagValue.equalsIgnoreCase("Y") || flagValue.equalsIgnoreCase("true")) {
				return true;
			} else {
				return false;
			}
		} else {
			return defaultValue;
		}
	}
	
	protected HashMap<String, String> getMatchFormatFlagsMap(String matchFormat) {
		return StringUtil.parseStringToMap(matchFormat, Constants.DF_FLAG_DELIM, Constants.DF_KEYVALUE_DELIM);
	}
	
	protected boolean defaultIgnoreNonAlphaNumerics() {
		return false;
	}
	
	protected boolean defaultIgnoreCase() { 
		return false;
	}
	
	protected boolean defaultIgnoreSplChars() { 
		return false;
	}
	
	protected boolean defaultIgnoreNonNumerics() { 
		return false;
	}
	
	protected boolean defaultIgnoreSpaces() { 
		return false;
	}
	
	protected boolean defaultIgnoreNumerics() {
		return false;
	}
	
	protected boolean defaultIgnoreTitle() {
		return false;
	}
	
	protected boolean defaultShuffleFlag() {
		return false;
	}
}