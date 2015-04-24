package com.arcot.apps.callout.matchlibrary.field;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;

public class DateField extends Field {
	
	public DateField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
	}
	
	@Override
	public boolean matchEncrypted(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		boolean result = false;

		SimpleDateFormat userDateFormat = getSimpleDateFormat(userDataFormat, Constants.ERR_INVALID_USERDATAFORMAT);
		SimpleDateFormat storedDateFormat = getSimpleDateFormat(storedDataFormat, Constants.ERR_INVALID_STOREDDATAFORMAT);
		
		Date userDate = getDate(correctDate(userData, userDataFormat), userDateFormat, Constants.ERR_INVALID_USERDATA);
		
		if(StringUtil.isBlank(matchFormat)) {
			log("Converting the user entered date in "+ userDataFormat+ " format to stored date format :"+ storedDataFormat);
			String userDataInSDF = changeFormat(userDate, storedDateFormat, Constants.ERR_INVALID_STOREDDATA);
			result = userDataInSDF.equalsIgnoreCase(storedData);
		} else {
			info("Match Format enabled. Changing both input and stored dates to the Match Format :" + matchFormat);
			SimpleDateFormat matchDateFormat = getSimpleDateFormat(matchFormat, Constants.ERR_INVALID_MATCHFORMAT);
			String userDateInMF = changeFormat(userDate, matchDateFormat, Constants.ERR_INVALID_USERDATA);
			
			Date storedDate = getDate(correctDate(storedData, storedDataFormat), storedDateFormat, Constants.ERR_INVALID_STOREDDATA);
			String storedDateInMF = changeFormat(storedDate, matchDateFormat, Constants.ERR_INVALID_STOREDDATA);
			result = storedDateInMF.equalsIgnoreCase(userDateInMF);
		}
		
		return result;
	}
	
	@Override
	public boolean matchHashed(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		boolean result = false;
		
		if(!StringUtil.isBlank(matchFormat)) {
			throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "MatchFormat is not applicable for Hashed data scenario.");
		}
		SimpleDateFormat userInputDateFormat = getSimpleDateFormat(userDataFormat, Constants.ERR_INVALID_USERDATAFORMAT);
		SimpleDateFormat storedDateFormat = getSimpleDateFormat(storedDataFormat, Constants.ERR_INVALID_STOREDDATAFORMAT);
		
		Date userInputDate = getDate(correctDate(userData, userDataFormat), userInputDateFormat, Constants.ERR_INVALID_USERDATA);
		String userInputDateNew = changeFormat(userInputDate, storedDateFormat, Constants.ERR_INVALID_USERDATA);
		
		String hashedUserInput = StringUtil.generateSha1Hash(userInputDateNew);
		result = hashedUserInput.equals(storedData);
		
		return result;
	}

/*	private String correctDateFormat(String format) {
		String correctedFormat = StringUtil.removeChars(format, getSplCharArray());
		correctedFormat = StringUtil.removeNumerics(correctedFormat);
		log("Corrected date format :"+ correctedFormat+ ". Format before correction :"+format);
		return correctedFormat;
	}
*/	
	private String correctDate(String dateStr, String dateFormat) {
		StringBuilder dateSB = new StringBuilder();
		for(char ch : dateStr.toCharArray()) {
			if(Character.isLetterOrDigit(ch)) {
				dateSB.append(ch);
			} else {
				if(dateFormat.indexOf(ch) > -1) {
					dateSB.append(ch);
				}
			}
		}
		log("Removing Special characters from Date");
		return dateSB.toString();
	}
	
	// Only 'y', 'd' or 'M' are allowed in a date format. It's y for years, d for day of month and M for month.
	private SimpleDateFormat getSimpleDateFormat(String dateFormat, String error) throws MatchException {
		if(error == null)
			error = Constants.ERR_INVALID_DATA_FORMAT;
		
		debug("Converting the string date format "+dateFormat+" to java DateFormat.");
		if(StringUtil.isBlank(dateFormat))
			throw new MatchException(error, "Date format is null.");

		if(!isValid(dateFormat, error))
			throw new MatchException(error, "Invalid Date format :"+ dateFormat);

		SimpleDateFormat df = null;
		try {
			df = new SimpleDateFormat(dateFormat);
		} catch (Exception e) {
			throw new MatchException(error, "Not a valid Date Format. Error while converting string date format "+dateFormat+" to java DateFormat." +e.getMessage());
		}
		if(df == null)
			throw new MatchException(error, "Error while converting string date format "+dateFormat+" to java DateFormat.");
		return df;
	}
	
	private boolean isValid(String dateFormat, String error) throws MatchException {
		int dateCount = 0, monthCount = 0, yearCount = 0;
		for(char ch : dateFormat.toCharArray()) {
			if(ch == 'd')
				dateCount++;
			if(ch == 'M')
				monthCount++;
			if(ch == 'y')
				yearCount++;
			
			if(ch == 'm')
				throw new MatchException(error, "Use 'M' for month. 'm' is for minutes.");
			if(ch == 'D')
				throw new MatchException(error, "Use 'd' for the day of month. 'D' is for day of the year which is not handled here!");
			if(ch == 'Y')
				throw new MatchException(error, "Use 'y' for years, 'Y' doesn't have any meaning.");
		}
		
		if(dateCount >= 2 && monthCount < 2 && yearCount < 2) {
			throw new MatchException(error, "Invalid Configuration. DateFormat has only one component Date. Need atleast two components :"+ dateFormat);
		} else if(dateCount < 2 && monthCount >= 2 && yearCount < 2) {
			throw new MatchException(error, "Invalid Configuration. DateFormat has only one component Month. Need atleast two components :"+ dateFormat);
		} else if(dateCount < 2 && monthCount < 2 && yearCount >= 2) {
			throw new MatchException(error, "Invalid Configuration. DateFormat has only one component Year. Need atleast two components :"+ dateFormat);
		} else if(dateCount < 2 && monthCount < 2 && yearCount < 2) {
			throw new MatchException(error, "Invalid Configuration. DateFormat has only zero components. Need atleast two components :"+ dateFormat);
		}
		
		/*if(!(dateCount == 0 || dateCount == 2)) {
			throw new MatchException(error, "Date should be like dd.");
		} else if(monthCount != 0 && monthCount < 2) {
			throw new MatchException(error, "Month should be like MM.");
		} else if(yearCount != 0 && yearCount < 2) {
			throw new MatchException(error, "Year should be like yy or yyyy.");
		}*/
		return true;
	}
	
	private Date getDate(String input, SimpleDateFormat format, String error) throws MatchException {
		debug("Converting string date to java Date object.");
		if(StringUtil.isBlank(error))
			error = Constants.ERR_INVALID_DATA;
		
		if(input == null)
			throw new MatchException(error, "Null date provided.");
		
		Date parsedDate = null;
		try {
			parsedDate = format.parse(input);
		} catch (ParseException e) {
			throw new MatchException(error, "Exception while parsing string date with configured date format :" +e.getMessage());
		}
		
		String pds = format.format(parsedDate);
		if(StringUtil.isBlank(pds))
			throw new MatchException(error, error);
		
		if(!input.equalsIgnoreCase(pds)) {
			throw new MatchException(error, "Given date is not in the configured format.");
		}

		return parsedDate;
	}
	
	private String changeFormat(Date date, SimpleDateFormat newFormat, String error) throws MatchException {
		debug("Changing the format of date.");
		if(StringUtil.isBlank(error))
			error = Constants.ERR_INVALID_DATA_FORMAT;
		
		if(date == null)
			throw new MatchException(error, "Date is null. Convertion to new format failed.");
		
		String result = newFormat.format(date);
		if(StringUtil.isBlank(result)) {
			throw new MatchException(error, "Error while converting the date to new format.");
		}
		return result;
	}
	
}
