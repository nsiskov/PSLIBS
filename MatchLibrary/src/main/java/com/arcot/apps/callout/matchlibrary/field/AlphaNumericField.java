package com.arcot.apps.callout.matchlibrary.field;

import java.util.HashMap;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;

public class AlphaNumericField extends NumericField {
	
	public AlphaNumericField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
		// TODO Auto-generated constructor stub
	}

	//Similar to numeric string.. only difference is in validation.
	@Override
	protected boolean validateData(String input, int length, HashMap<String, String> flagsMap, String error) throws MatchException {
		if(length != -1 && input.length() != length) {
			throw new MatchException(error, "Invalid Data length. Not matching with configured length.");
		}
		
		if(length == -1)
			input = correctData(input, flagsMap);
	
		debug("Data is a valid Alpha Numeric and length matches. Returning true.");
		return true;
	}
	
	//Case sensitive match to be handled here..
	@Override
	protected String correctData(String inputData, HashMap<String, String> flagsMap) {
		boolean ignoreSplChars = getFlag(flagsMap, Constants.CONF_IGNORE_SPLCHARS_FLAG, defaultIgnoreSplChars());
		boolean ignoreSpaces = getFlag(flagsMap, Constants.CONF_IGNORE_SPACES_FLAG, defaultIgnoreSpaces());
		boolean ignoreNonAlphaNumerics = getFlag(flagsMap, Constants.CONF_IGNORE_NONALPHANUMERICS, defaultIgnoreNonAlphaNumerics());
		boolean ignoreNumerics = getFlag(flagsMap, Constants.CONF_IGNORE_NUMERICS, defaultIgnoreNumerics());
		
		debug("Ignore non Alpha Numerics flag :"+ ignoreNonAlphaNumerics);
		if(ignoreNonAlphaNumerics) {
			log("Ignoring Non Alpha Numerics.");
			inputData = StringUtil.removeNonAlphaNumerics(inputData);
			debug("After removing non alpha numerics :"+inputData);
		} else {
			if(ignoreSplChars) {
				log("Ignoring Special characters.");
				inputData = StringUtil.removeChars(inputData, getSplCharArray());
			}
			if(ignoreSpaces) {
				log("Ignoring Spaces.");
				inputData = StringUtil.removeChar(inputData, ' ');
			}
			if(ignoreNumerics) {
				log("Ignoring Numerics.");
				inputData = StringUtil.removeNumerics(inputData);
			}
		}
		return inputData;
	}
	
	@Override
	protected boolean matchPartialPercentage(String userData, String storedData, int partialDataLength) throws MatchException {
		throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "Alpha Numeric fields doesnt support partial percentage match.");
	}

	@Override
	protected boolean defaultIgnoreCase() { 
		return true;
	}
	
}
