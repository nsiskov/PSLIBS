package com.arcot.apps.callout.matchlibrary.field;

import java.util.HashMap;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;

public class NumericDoubleField extends NumericField {

	public NumericDoubleField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
	}

	@Override
	protected String correctData(String inputData, HashMap<String, String> flagsMap) {
		boolean ignoreSplChars = getFlag(flagsMap, Constants.CONF_IGNORE_SPLCHARS_FLAG, defaultIgnoreSplChars());
		boolean ignoreSpaces = getFlag(flagsMap, Constants.CONF_IGNORE_SPACES_FLAG, defaultIgnoreSpaces());
		boolean ignoreNonNumerics = getFlag(flagsMap, Constants.CONF_IGNORE_NONNUMERICS, defaultIgnoreNonNumerics());
		
		if(ignoreNonNumerics) {
			log("Ignoring Non Numerics.");
			inputData = removeNonNumericsIgnoreDot(inputData);
		} else {
			if(ignoreSplChars)
				inputData = StringUtil.removeChars(inputData, new char[]{'\\', '/', '-'});
			if(ignoreSpaces)
				inputData = StringUtil.removeChar(inputData, ' ');
		}
		
		log("Ignore non numerics flag :"+ ignoreNonNumerics);
		debug("After removing non alpha numerics :"+inputData);
		
		return inputData;
	}

	@Override
	protected boolean validateData(String input, int length, HashMap<String, String> flagsMap, String error) throws MatchException {
		try {
			Double.parseDouble(input);
		} catch (NumberFormatException e1) {
			debug("Data is not numeric :" +input);
			throw new MatchException(error, "Data is not numeric.");
		}

		if(length != -1 && input.length() != length) {
			throw new MatchException(error, "Invalid Data length. Not matching with configured length.");
		}
	
		debug("Data is a valid numeric and length matches. Returning true.");
		return true;
	}
	
	@Override
	protected boolean matchPartialAny(String userData, int userDataLength, String storedData, int storedDataLength, int partialDataLength) throws MatchException {
		log("Validating any "+ partialDataLength + " characters.");
		int count = 0;
		for(int i = 0; i < userData.length(); i++) {
			if(i == storedData.length())
				break;

			if(userData.charAt(i) == storedData.charAt(i)) {
				count++;
			} else if(userData.charAt(i) == '.' || storedData.charAt(i) == '.') {
				//Checking the position of '.'
				log("Dot is not in same position. Returning false.");
				return false;
			}
		}
		if(count >= partialDataLength) {
			log("PartialDataMatched :"+ count);
			return true;
		} else {
			log("PartialDataMatch failed. Only "+ count+ " matched.");
			return false;
		}
	}
	
	@Override
	protected boolean matchPartialPercentage(String userData, String storedData, int partialDataLength) {
		double storedDataLong = Long.parseLong(storedData);
		double deviation = (double)storedDataLong * partialDataLength/100;
		
		double max = storedDataLong + deviation;
		double min = storedDataLong - deviation;
		
		double userDataLong = Double.parseDouble(userData);

		log("Deviation allowed :"+ deviation);
		debug("UserData should be greater than "+ min + " and less than "+ max);
		if(userDataLong == storedDataLong) {
			return true;
		} else {
			if(max >= userDataLong && userDataLong >= min)
				return true;
		}
		return false;
	}

	
    public static String removeNonNumericsIgnoreDot(String str) {
    	if(str == null)
    		return null;
        StringBuilder sb = new StringBuilder();
        char[] charAr = str.toCharArray();
        for (int i = 0; i < charAr.length; i++) {
            if (Character.isDigit(charAr[i]) || charAr[i] == '.')
                sb.append(charAr[i]);
        }
        return sb.toString();
    }
    
    @Override
    public char[] getSplCharArray() {
    	String splCharStr = getFieldValueFromConfig(Constants.CONF_SPLCHARS_LIST);
		if(StringUtil.isBlank(splCharStr))
			return new char[]{'\\', '/', '-'};
		return splCharStr.toCharArray();
    }

}
