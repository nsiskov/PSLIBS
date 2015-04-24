package com.arcot.apps.callout.matchlibrary.field;

import java.util.HashMap;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;

public class NumericField extends Field {
	
	protected final String PARTIAL_FIRST 		= "F";
	protected final String PARTIAL_LAST 		= "L";
	protected final String PARTIAL_ANY 			= "A";
	protected final String PARTIAL_PERCENTAGE 	= "P";
	
	public NumericField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
	}

	@Override
	public boolean matchEncrypted(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		int userDataLength = -1;
		int storedDataLength = -1;
		int partialDataLength = -1;
		String partialFormatStyle = "L";
		
		try {
			userDataLength = Integer.parseInt(userDataFormat);
//			if(userDataLength == -1)
//				userDataLength = userData.length();
		} catch (NumberFormatException e1) {
			throw new MatchException(Constants.ERR_INVALID_USERDATAFORMAT, "User data format(UDF) should be a number for Numeric fields.");
		}
		
		try {
			storedDataLength = Integer.parseInt(storedDataFormat);
//			if(storedDataLength == -1)
//				storedDataLength = storedData.length();
		} catch (NumberFormatException e1) {
			throw new MatchException(Constants.ERR_INVALID_STOREDDATAFORMAT, "Stored data format(SDF) should be a number for Numeric fields.");
		}
		
		HashMap<String, String> flagsMap = getMatchFormatFlagsMap(matchFormat);

		String partialMatchFormat = flagsMap.get("default");
		if(!StringUtil.isBlank(partialMatchFormat)) {
			partialFormatStyle = String.valueOf(partialMatchFormat.charAt(0));
			partialMatchFormat = partialMatchFormat.substring(1);
			 
			try {
				partialDataLength = Integer.parseInt(partialMatchFormat);
			} catch (NumberFormatException e1) {
				throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "Partial match configured, but MatchFormat(MF) should be have a number for Numeric fields.");
			}
		}

		log("Data length, UserDataLength :"+userDataLength+", StoredDataLength :"+storedDataLength+", PartialDataLength :"+partialDataLength);

		boolean ignoreCase = getFlag(flagsMap, Constants.CONF_IGNORECASE_FLAG, defaultIgnoreCase());
		if(ignoreCase) {
			log("Ignoring case for both userData and storedData.");
			userData = userData.toLowerCase();
			storedData = storedData.toLowerCase();
		}

		try {
			validateData(userData, userDataLength, flagsMap, Constants.ERR_INVALID_USERDATA);
		} catch (MatchException e) {
			userData = correctData(userData, flagsMap);
			validateData(userData, userDataLength, flagsMap, Constants.ERR_INVALID_USERDATA);
		}

		try {
			validateData(storedData, storedDataLength, flagsMap, Constants.ERR_INVALID_STOREDDATA);
		} catch (MatchException e) {
			storedData = correctData(storedData, flagsMap);
			validateData(storedData, storedDataLength, flagsMap, Constants.ERR_INVALID_STOREDDATA);
		}
		
		if(userData.length() != storedData.length()) {
			userData = correctData(userData, flagsMap);
			storedData = correctData(storedData, flagsMap);
		}
			
		return matchValues(userData, userDataLength, storedData, storedDataLength, partialFormatStyle, partialDataLength);
	}
	
	protected boolean matchValues(String userData, int userDataLength, String storedData, int storedDataLength, String partialFormatStyle, int partialDataLength) throws MatchException {
		if(partialDataLength > 0) { // Partial match
			
			if(userDataLength == -1)
				userDataLength = userData.length();
			if(storedDataLength == -1)
				storedDataLength = storedData.length();
			
			log("Data length, UserDataLength :"+userDataLength+", StoredDataLength :"+storedDataLength+", PartialDataLength :"+partialDataLength);
			
			if(PARTIAL_FIRST.equalsIgnoreCase(partialFormatStyle)) {
				return matchPartialFirst(userData, userDataLength, storedData, storedDataLength, partialDataLength);
			} else if(PARTIAL_LAST.equalsIgnoreCase(partialFormatStyle)) {
				return matchPartialLast(userData, userDataLength, storedData, storedDataLength, partialDataLength);
			} else if(PARTIAL_ANY.equalsIgnoreCase(partialFormatStyle)) {
				return matchPartialAny(userData, userDataLength, storedData, storedDataLength, partialDataLength);
			} else if(PARTIAL_PERCENTAGE.equalsIgnoreCase(partialFormatStyle)) {
				return matchPartialPercentage(userData, storedData, partialDataLength);
			} else {
				throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "Invalid MatchFormat(MF) "+ partialFormatStyle+ ". Only 'Ln' for last n chars, 'Fn' for first n chars, 'An' for any n characters or 'Px' for x percentage deviation is allowed.");
			}
		}
		
		if(userDataLength != storedDataLength) {
			log("No partial match configured and Length of two fields is different. UserDataLength :"+ userDataLength + ", StoredDataLength :"+ storedDataLength);
			return false;
		}
		debug("Matching complete inputData "+userData+" and storedData "+storedData);
		return userData.equals(storedData);
	}
	
	protected boolean matchPartialFirst(String userData, int userDataLength, String storedData, int storedDataLength, int partialDataLength) throws MatchException {
		if((userDataLength != -1 && userDataLength < partialDataLength) && (storedDataLength != -1 && storedDataLength < partialDataLength))
			throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "MatchFormat(MF) "+ partialDataLength +" is greater than User Data Length :"+ userDataLength + " and Stored Data Length :"+ storedDataLength);
		if((userDataLength != -1 && userDataLength < partialDataLength))
			throw new MatchException(Constants.ERR_INVALID_USERDATA, "MatchFormat(MF) "+ partialDataLength +" is greater than User Data Length :"+ userDataLength);
		if((storedDataLength != -1 && storedDataLength < partialDataLength))
			throw new MatchException(Constants.ERR_INVALID_STOREDDATA, "MatchFormat(MF) "+ partialDataLength +" is greater than Stored Data Length :"+ storedDataLength);

		log("Reading only first " +partialDataLength+ " characters. Truncating both user and stored data.");
		try {
			userData = userData.substring(0, partialDataLength);
			storedData = storedData.substring(0, partialDataLength);
		} catch (StringIndexOutOfBoundsException e) {
			throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "MatchFormat(MF) "+ partialDataLength +" should not be greater than User data format(UDF) and Stored data format(SDF).");					
		}
		
		debug("After reading first "+ partialDataLength+ " UD :"+ userData + " SD :" + storedData);
		return userData.equals(storedData);
	}

	protected boolean matchPartialLast(String userData, int userDataLength, String storedData, int storedDataLength, int partialDataLength) throws MatchException {
		if((userDataLength != -1 && userDataLength < partialDataLength) && (storedDataLength != -1 && storedDataLength < partialDataLength))
			throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "MatchFormat(MF) "+ partialDataLength +" is greater than User Data Length :"+ userDataLength + " and Stored Data Length :"+ storedDataLength);
		if((userDataLength != -1 && userDataLength < partialDataLength))
			throw new MatchException(Constants.ERR_INVALID_USERDATA, "MatchFormat(MF) "+ partialDataLength +" is greater than User Data Length :"+ userDataLength);
		if((storedDataLength != -1 && storedDataLength < partialDataLength))
			throw new MatchException(Constants.ERR_INVALID_STOREDDATA, "MatchFormat(MF) "+ partialDataLength +" is greater than Stored Data Length :"+ storedDataLength);
			
		log("Reading only last " +partialDataLength+ " characters. Truncating both user and stored data.");
		userData = userData.substring(userDataLength - partialDataLength);
		storedData = storedData.substring(storedDataLength - partialDataLength);
		
		debug("After reading last "+ partialDataLength+ " UD :"+ userData + " SD :" + storedData);
		return userData.equals(storedData);
	}

	protected boolean matchPartialPercentage(String userData, String storedData, int partialDataLength) throws MatchException {
		long storedDataLong = Long.parseLong(storedData);
		long deviation = (long)storedDataLong * partialDataLength/100;
		
		long max = storedDataLong + deviation;
		long min = storedDataLong - deviation;
		
		long userDataLong = Long.parseLong(userData);

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

	protected boolean matchPartialAny(String userData, int userDataLength, String storedData, int storedDataLength, int partialDataLength) throws MatchException {
		if((userDataLength != -1 && userDataLength < partialDataLength) && (storedDataLength != -1 && storedDataLength < partialDataLength))
			throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "MatchFormat(MF) "+ partialDataLength +" is greater than User Data Length :"+ userDataLength + " and Stored Data Length :"+ storedDataLength);
		if((userDataLength != -1 && userDataLength < partialDataLength))
			throw new MatchException(Constants.ERR_INVALID_USERDATA, "MatchFormat(MF) "+ partialDataLength +" is greater than User Data Length :"+ userDataLength);
		if((storedDataLength != -1 && storedDataLength < partialDataLength))
			throw new MatchException(Constants.ERR_INVALID_STOREDDATA, "MatchFormat(MF) "+ partialDataLength +" is greater than Stored Data Length :"+ storedDataLength);
		
		log("Validating any "+ partialDataLength + " characters.");
		int count = 0;
		for(int i = 0; i < userData.length(); i++) {
			if(i == storedData.length())
				break;
			if(userData.charAt(i) == storedData.charAt(i))
				count++;
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
	public boolean matchHashed(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		return false;
	}
	
	protected String correctData(String inputData, HashMap<String, String> flagsMap) {
		boolean ignoreSplChars = getFlag(flagsMap, Constants.CONF_IGNORE_SPLCHARS_FLAG, defaultIgnoreSplChars());
		boolean ignoreSpaces = getFlag(flagsMap, Constants.CONF_IGNORE_SPACES_FLAG, defaultIgnoreSpaces());
		boolean ignoreNonNumerics = getFlag(flagsMap, Constants.CONF_IGNORE_NONNUMERICS, defaultIgnoreNonNumerics());
		
		if(ignoreNonNumerics) {
			log("Ignoring Non Numerics.");
			inputData = StringUtil.removeNonNumerics(inputData);
		} else {
			if(ignoreSplChars)
				inputData = StringUtil.removeChars(inputData, getSplCharArray());
			if(ignoreSpaces)
				inputData = StringUtil.removeChar(inputData, ' ');
		}
		
		log("Ignore non numerics flag :"+ ignoreNonNumerics);
		debug("After removing non alpha numerics :"+inputData);
		
		return inputData;
	}

	protected boolean validateData(String input, int length, HashMap<String, String> flagsMap, String error) throws MatchException {
		try {
			Long.parseLong(input);
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
	public String getUserDataFormatFromConfig() throws MatchException {
		String udf = getFieldValueFromConfig(Constants.CONF_USERDATAFORMAT);
		if(StringUtil.isBlank(udf))
			udf = "-1";
		
		return udf;
	}
	
	@Override
	protected boolean validateUserDataFormat(String udf) throws MatchException {
		try {
			Integer.parseInt(udf);
		} catch (NumberFormatException e) {
			throw new MatchException(Constants.ERR_INVALID_USERDATAFORMAT, "Only integers are allowed as UDF for NumericField.");
		}
		return true;
	}
	
	@Override
	public String getStoredDataFormatFromConfig() throws MatchException {
		String sdf = getFieldValueFromConfig(Constants.CONF_STOREDDATAFORMAT);
		if(StringUtil.isBlank(sdf))
			sdf = "-1";
		
		return sdf;
	}
	
	@Override
	protected boolean validateSingleStoredDataFormat(String sdf) throws MatchException {
		try {
			Integer.parseInt(sdf);
		} catch (NumberFormatException e) {
			throw new MatchException(Constants.ERR_INVALID_STOREDDATAFORMAT, "Only integers are allowed as SDF for NumericField.");
		}
		return true;
	}
	
	@Override
	protected boolean validateSingleMatchDataFormat(String sdf) throws MatchException {
		return true;
	}
}
