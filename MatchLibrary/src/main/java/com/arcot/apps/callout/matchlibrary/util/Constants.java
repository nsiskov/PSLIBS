package com.arcot.apps.callout.matchlibrary.util;

public class Constants {

	public static final String MATCHLIB_ENABLED							= "MATCHLIB_ENABLED";
	public static final String MATCHLIB_ENABLED_ACS						= "MATCHLIB_ENABLED_ACS";
	public static final String MATCHLIB_ENABLED_ES						= "MATCHLIB_ENABLED_ES";
	
	public static final String CONF_ALL_HASHED 							= "ALL_HASHED";
	public static final String CONF_HASHED 								= "HASHED";
	
	
	public static final String CONF_USERDATAFORMAT 						= "UDF";
	public static final String CONF_STOREDDATAFORMAT 					= "SDF";
	public static final String CONF_MATCH_FORMAT 						= "MF";
	
	public static final String CONF_SUCCESS_RESULT_LOG 					= "SRL";
	public static final String CONF_FAILURE_RESULT_LOG 					= "FRL";
	
	public static final String CONF_SPLCHARS_LIST						= "SPLCHARSLIST";
	public static final String CONF_EXACT_MATCH_FLAG 					= "EXACTMATCH";
	public static final String CONF_IGNORECASE_FLAG 					= "IGNORECASE";
	public static final String CONF_SHUFFLE_FLAG 						= "SHUFFLE";
	public static final String CONF_IGNORE_SPACES_FLAG 					= "IGNORESPACES";
	public static final String CONF_IGNORE_TITLE_FLAG 					= "IGNORETITLE";
	public static final String CONF_IGNORE_SPLCHARS_FLAG 				= "IGNORESPLCHARS";
	public static final String CONF_IGNORE_NUMERICS 					= "IGNORENUM";
	public static final String CONF_IGNORE_NONNUMERICS 					= "IGNORENONNUM";
	public static final String CONF_IGNORE_NONALPHANUMERICS 			= "IGNORENONALPHANUM";
	
	public static final String CONF_LOG_LEVEL							= "MATCHLIB_LOG_LEVEL";
	public static final String CONFVAL_LOG_DEBUG						= "DEBUG";
	public static final String CONFVAL_LOG_INFO							= "INFO";

	public static final String ERR_NO_FIELDS_PASSED						= "NO_FIELDS_PASSED";
	public static final String ERR_NULL_FIELD_PASSED 					= "NULL_FIELD_PASSED";
	public static final String ERR_NO_RESULT_RETURNED 					= "NO_RESULT_RETURNED";
	public static final String ERR_MATCHLIB_ERROR 						= "MATCHLIB_ERROR";
	public static final String ERR_MATCHLIB_NULLRESULT					= "MATCHLIB_NULLRESULT";
	public static final String ERR_INVALID_FLAGNAME 					= "INVALID_FLAGNAME";
	
	public static final String ERR_INVALID_DATA_FORMAT 					= "INVALID_DF";
	public static final String ERR_INVALID_STOREDDATAFORMAT 			= "INVALID_SDF";
	public static final String ERR_INVALID_USERDATAFORMAT 				= "INVALID_UDF";
	public static final String ERR_INVALID_MATCHFORMAT 					= "INVALID_MF";
	public static final String ERR_DATAFORMAT_MISMATCH 					= "DATAFORMAT_MISMATCH";
	public static final String ERR_BLANK_FIELDNAME	 					= "BLANK_FIELDNAME";
	public static final String ERR_BLANK_USERDATA 						= "BLANK_UD";
	public static final String ERR_BLANK_STOREDDATA 					= "BLANK_SD";
	public static final String ERR_BLANK_STOREDDATAFORMAT 				= "BLANK_SDF";
	public static final String ERR_BLANK_USERDATAFORMAT 				= "BLANK_UDF";
	public static final String ERR_HASHING_FAILED 						= "HASHING_FAILED";
	public static final String ERR_REGEX_FORMAT_ERROR 					= "REGEX_FORMAT_MISMATCH";
	public static final String ERR_INVALID_EMAIL 						= "INVALID_EMAIL";
	
	public static final String ERR_INVALID_USERDATA 					= "INVALID_UD";
	public static final String ERR_INVALID_STOREDDATA 					= "INVALID_SD";
	public static final String ERR_INVALID_DATA 						= "INVALID_DATA";
	
	public static final String ERR_INVALID_CONFIG 						= "INVALID_CONFIG";
	public static final String ERR_INVALID_FLAG_DELIM_PASSED 			= "INVALID_FLAG_DELIM_PASSED";
	public static final String ERR_INVALID_KEYVALUE_DELIM_PASSED 		= "INVALID_KEYVALUE_DELIM_PASSED";

	public static final String DF_FLAG_DELIM 							= "|";
	public static final String DF_KEYVALUE_DELIM 						= "-";
	public static final String DELIM									= "_";
	public static final String FORMAT_DELIM								= "~";
	public static final String STORED_DATA_DELIM 						= "~";
	public static final String NAME_DELIM 								= " ";
	
	public static final char[] SPECIAL_CHARS_ARRAY 						= new char[]{'\\', '/', '-', '.'};

	public static final String EMAIL_REGEX 								= "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

}
