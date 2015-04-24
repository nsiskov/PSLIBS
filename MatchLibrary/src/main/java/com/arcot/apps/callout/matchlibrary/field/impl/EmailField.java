package com.arcot.apps.callout.matchlibrary.field.impl;

import java.util.HashMap;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.field.Field;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;

public class EmailField extends Field {

	public EmailField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
	}

	@Override
	public boolean matchEncrypted(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		HashMap<String, String> flagsMap = getMatchFormatFlagsMap(matchFormat);
		
		try {
			validateEmailWithRegex(userData, matchFormat, Constants.ERR_INVALID_USERDATA);
		} catch (MatchException e) {
			userData = correctEmail(userData, flagsMap, Constants.ERR_INVALID_USERDATA);
			validateEmailWithRegex(userData, matchFormat, Constants.ERR_INVALID_USERDATA);
		}
		
		log("User data vaildated. Now validating StoredData.");
		try {
			validateEmailWithRegex(storedData, matchFormat, Constants.ERR_INVALID_STOREDDATA);
		} catch (MatchException e) {
			storedData = correctEmail(storedData, flagsMap, Constants.ERR_INVALID_STOREDDATA);
			validateEmailWithRegex(storedData, matchFormat, Constants.ERR_INVALID_STOREDDATA);
		}
		
		log("Stored data is also vaildated. Now trying to match both values.");
		if(ignoreCase())
			return storedData.equalsIgnoreCase(userData);
		else
			return storedData.equals(userData);
	}

	@Override
	public boolean matchHashed(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		// TODO Auto-generated method stub
		return false;
	}

	
	protected void validateEmailWithRegex(String email, String regex, String error) throws MatchException {
		debug("Validating email :"+ email);
		
		try {
			if(!email.matches(Constants.EMAIL_REGEX))
				throw new MatchException(error, "Email doesnt match the regex pattern :"+ regex);
		} catch (Exception e) {
			throw new MatchException(error, "Error while trying to match the email with given regex :"+ regex);
		}
	}
	
	protected String correctEmail(String email, HashMap<String, String> flagsMap, String error) throws MatchException {
		log("Validation failed for the input, trying to correct the email");
		if(email == null)
			throw new MatchException(error, "Email is blank or null before correction");
		
		email = email.replace("@@", "@");
		
		boolean ignoreSplChars = getFlag(flagsMap, Constants.CONF_IGNORE_SPLCHARS_FLAG, defaultIgnoreSplChars());
		boolean ignoreNonAlphaNumerics = getFlag(flagsMap, Constants.CONF_IGNORE_NONALPHANUMERICS, defaultIgnoreNonAlphaNumerics());
		boolean ignoreSpaces = getFlag(flagsMap, Constants.CONF_IGNORE_SPACES_FLAG, defaultIgnoreSpaces());
		
		if(ignoreSplChars) {
			email = StringUtil.removeChars(email, getSplCharArray());
		}
		if(ignoreNonAlphaNumerics) {
			email = StringUtil.removeNonAlphaNumerics(email, ALLOWED_SPLCHARS);
		}
		if(ignoreSpaces)
			email = StringUtil.removeChar(email, ' ');
		
		if(StringUtil.isBlank(email))
			throw new MatchException(error, "Email is blank or null.");
		return email;
	}
	
	private char[] ALLOWED_SPLCHARS = new char[]{'.', '_', '%', '+', '-', '@'};
	
	@Override
	public String getUserDataFormatFromConfig() throws MatchException {
		String udf = getFieldValueFromConfig(Constants.CONF_USERDATAFORMAT);
		if(!StringUtil.isBlank(udf))
			throw new MatchException(Constants.ERR_INVALID_USERDATAFORMAT, getClass().getSimpleName() + " field doesn't need UDF.");
		
		return "-1";
	}
	
	@Override
	public String getStoredDataFormatFromConfig() throws MatchException {
		String sdf = getFieldValueFromConfig(Constants.CONF_STOREDDATAFORMAT);
		if(!StringUtil.isBlank(sdf))
			throw new MatchException(Constants.ERR_INVALID_STOREDDATAFORMAT, getClass().getSimpleName() + " field doesn't need SDF.");
		
		return "-1";
	}
	
	@Override
	public char[] getSplCharArray() {
		String splCharStr = getFieldValueFromConfig(Constants.CONF_SPLCHARS_LIST);
		
		if(StringUtil.isBlank(splCharStr))
			return new char[]{'\\', '/'};
		
		return splCharStr.toCharArray();
	}
	
	@Override
	protected boolean defaultIgnoreSpaces() {
		return true;
	}
	
	@Override
	protected boolean defaultIgnoreCase() {
		return true;
	}

}
