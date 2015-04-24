package com.arcot.apps.callout.matchlibrary.field;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;

public class RegExField extends Field {

	public RegExField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
	}
	
	@Override
	public boolean matchEncrypted(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		boolean result = false;
		String splChars[] = new String[]{" ", "/"};

		if(StringUtil.isBlank(matchFormat))
			throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "MatchFormat should contain the regex expression for RegExField.");
		
		try {
			validateDataWithRegex(userData, userDataFormat, Constants.ERR_INVALID_USERDATA);
		} catch (MatchException e) {
			userData = correctData(userData, userDataFormat, splChars);
			validateDataWithRegex(userData, userDataFormat, Constants.ERR_INVALID_USERDATA);
		}
		
		log("User data vaildated. Now validating StoredData.");
		try {
			validateDataWithRegex(storedData, userDataFormat, Constants.ERR_INVALID_STOREDDATA);
		} catch (MatchException e) {
			storedData = correctData(storedData, userDataFormat, splChars);
			validateDataWithRegex(storedData, userDataFormat, Constants.ERR_INVALID_STOREDDATA);
		}
		
		log("Stored data is also vaildated. Now trying to match both values.");
		if(ignoreCase())
			result = userData.equalsIgnoreCase(storedData);
		else
			result = userData.equals(storedData);
		return result;
	}

	@Override
	public boolean matchHashed(String userData, String userDataFormat,
			String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected boolean validateDataWithRegex(String input, String regEx, String error) throws MatchException {
		if(input == null)
			throw new MatchException(error, "Input data is null.");
		
		if(!input.matches(regEx))
			throw new MatchException(error, "Input doesnt match the RegEx configured.");
		
		return true;
	}
	
	private String correctData(String input, String regExFormat, String[] splChars) {
		// Regex spl characters.. RegEx expression contains the below characters prefixed with a '\'. Do not add any other chars to this list.
		log("Validation failed for the input, trying to remove special characters from the RegEx input.");
		String regExSplChrs[] = new String[]{"[", "]", "\\", "^", "$", "%", ".", "|", "?", "*", "+", "(", ")"};
		for(int i=0; i<regExSplChrs.length; i++) {
			if(input.indexOf(regExSplChrs[i]) > 0 && regExFormat.indexOf("\\"+regExSplChrs[i]) < 0)
				input = StringUtil.removeChar(input, regExSplChrs[i].charAt(0));
		}
		//For normal characters like space and /
		for(int i=0; i<splChars.length; i++) {
			if(input.indexOf(splChars[i]) > 0 && regExFormat.indexOf(splChars[i]) < 0)
				input = StringUtil.removeChar(input, splChars[i].charAt(0));
		}
		return input;
	}
	
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
		
		return "-1";	}
	
}
