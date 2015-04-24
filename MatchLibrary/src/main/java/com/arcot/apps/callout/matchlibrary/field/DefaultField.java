package com.arcot.apps.callout.matchlibrary.field;

import java.util.HashMap;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;

public class DefaultField extends Field {
	
	public DefaultField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matchEncrypted(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		if(storedData.equals(userData))
			return true;

		String tempUserData = null;
		String tempStoredData = null;
		
		HashMap<String, String> flagsMap = getMatchFormatFlagsMap(matchFormat);
		
		boolean ignoreCase = getFlag(flagsMap, Constants.CONF_IGNORECASE_FLAG, defaultIgnoreCase());
		debug("Exact values didn't match. Trying with IgnoreCase flag :"+ ignoreCase);
		if(ignoreCase) {
			log("Ignoring case.");
			if(storedData.equalsIgnoreCase(userData))
				return true;

			tempUserData = userData.toLowerCase();
			tempStoredData = storedData.toLowerCase();
		}
		
		boolean ignoreSpaces = getFlag(flagsMap, Constants.CONF_IGNORE_SPACES_FLAG, defaultIgnoreSpaces());
		debug("Trying with IgnoreSpaces flag :"+ ignoreSpaces);
		if(ignoreSpaces) {
			log("Ignoring spaces.");
			tempUserData = StringUtil.removeChar(tempUserData, ' ');
			tempStoredData = StringUtil.removeChar(tempStoredData, ' ');
			
			if(tempStoredData.equals(tempUserData))
				return true;
		}
		
		boolean ignoreSplChars = getFlag(flagsMap, Constants.CONF_IGNORE_SPLCHARS_FLAG, defaultIgnoreSplChars());
		debug("Trying with IgnoreSplChars flag :"+ ignoreSplChars);
		if(ignoreSplChars) {
			log("Ignoring special characters.");
			char[] splCharArray = getSplCharArray();
			tempUserData = StringUtil.removeChars(tempUserData, splCharArray);
			tempStoredData = StringUtil.removeChars(tempStoredData, splCharArray);
			
			if(tempStoredData.equals(tempUserData))
				return true;
		}
		
		boolean ignoreNonAlphaNumerics = getFlag(flagsMap, Constants.CONF_IGNORE_NONALPHANUMERICS, defaultIgnoreNonAlphaNumerics());
		debug("Trying with IgnoreNonAlphaNumerics flag :"+ ignoreNonAlphaNumerics);
		if(ignoreNonAlphaNumerics) {
			log("Ignoring Non AlphaNumerics.");
			tempUserData = StringUtil.removeNonAlphaNumerics(tempUserData);
			tempStoredData = StringUtil.removeNonAlphaNumerics(tempStoredData);
			
			if(tempStoredData.equals(tempUserData))
				return true;
		}
		
		boolean ignoreNonNumerics = getFlag(flagsMap, Constants.CONF_IGNORE_NONNUMERICS, defaultIgnoreNonNumerics());
		debug("Trying with IgnoreNonNumerics flag :"+ ignoreNonNumerics);
		if(ignoreNonNumerics) {
			log("Ignoring Non numerics.");
			String udAfterRemovingNonNumerics = StringUtil.removeNonNumerics(tempUserData);
			String sdAfterRemovingNonNumerics = StringUtil.removeNonNumerics(tempStoredData);
			
			if(sdAfterRemovingNonNumerics.equals(udAfterRemovingNonNumerics))
				return true;
		}
		
		boolean ignoreNumerics = getFlag(flagsMap, Constants.CONF_IGNORE_NUMERICS, defaultIgnoreNumerics());
		debug("Trying with IgnoreNumerics flag :"+ ignoreNumerics);
		if(ignoreNumerics) {
			log("Ignoring Numerics.");
			tempUserData = StringUtil.removeNumerics(tempUserData);
			tempStoredData = StringUtil.removeNumerics(tempStoredData);
			
			if(tempStoredData.equals(tempUserData))
				return true;
		}
		
		return false;
	}
	
	@Override
	public boolean matchHashed(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		return false;
	}
	
	@Override
	public String getUserDataFormatFromConfig() throws MatchException {
		return "-1";
	}
	
	@Override
	public String getStoredDataFormatFromConfig() throws MatchException {
		return "-1";
	}

}
