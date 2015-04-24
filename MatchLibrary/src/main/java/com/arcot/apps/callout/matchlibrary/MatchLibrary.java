package com.arcot.apps.callout.matchlibrary;

import java.util.ArrayList;
import java.util.List;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.es.ESRequest;
import com.arcot.apps.callout.matchlibrary.field.Field;
import com.arcot.apps.callout.matchlibrary.result.MatchResult;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;
import com.arcot.logger.ArcotLogger;

public class MatchLibrary {
	
	/*
	 * Returns a MatchResult object after matching all fields passed as an array in a sequence.
	 * Parameters:	 * Field[] fields : An array of Field objects. A factory method createField of Field can be used for creating Fields.
	 * Returns:	 * MatchResult : An object of MatchResult class which contains an int value which is the number of fields matched and a boolean array for getting individual field result and callout status.
	 * Throws:	 * MatchException - If the input fields array is null or contains any null elements. 
	 * */
	public static MatchResult match(Field[] fields) throws MatchException {
		if(fields == null || fields.length < 1) {
			String errorCode = Constants.ERR_NO_FIELDS_PASSED;
			log(null, null, "ERROR", errorCode);
			throw new MatchException(errorCode, "No fields passed");
		}

		MatchResult matchResult = new MatchResult();
		StringBuffer calloutStatus = new StringBuffer();
		boolean resultArray[] = new boolean[fields.length];
		int count = 0;
		CORequest request = null;
		COLogger logger = null;
		
		for(Field field : fields) {
			if(field == null) {
				String errorCode = Constants.ERR_NULL_FIELD_PASSED;
				String errorDesc = "Null field passed at "+ count;
				log(request, logger, "ERROR", errorDesc);
				throw new MatchException(errorCode, errorDesc);
			}
			
			MatchResult singleResult = null;
			try {
				field.prepare();
				if(StringUtil.isBlank(field.getUserData()))
					singleResult = createMatchResult(field, false, Constants.ERR_BLANK_USERDATA, "", "");
				else if(StringUtil.isBlank(field.getStoredData()))
					singleResult = createMatchResult(field, false, Constants.ERR_BLANK_STOREDDATA, "", "");
				else
					singleResult = matchSingleField(field);
			} catch (MatchException e) {
				e.printStackTrace();
				field.log("MatchException while processing field :"+ field.getFieldName()+ " :"+ e.getMessage()+ " - " +e.getDescription());
				singleResult = createMatchResult(field, false, e.getMessage(), "", "");
			} catch (Exception e) {
				e.printStackTrace();
				field.log("Exception while processing field :"+ field.getFieldName()+ " :"+ e.getMessage());
				singleResult = createMatchResult(field, false, Constants.ERR_MATCHLIB_ERROR, "", "");
			}
			
			if(singleResult == null) {
				String errorCode = Constants.ERR_NO_RESULT_RETURNED;
				field.log(errorCode + " - There should be an error in MatchLibrary!!!");
				singleResult = createMatchResult(field, false, Constants.ERR_MATCHLIB_NULLRESULT, "", "");
			}
			resultArray[count] = singleResult.getMatchCount() == 1 ? true : false;
			calloutStatus.append(singleResult.getCalloutStatus());
			matchResult.setMatchCount(matchResult.getMatchCount() + singleResult.getMatchCount());
			
			if(request == null && logger == null) { //For logging purpose in error cases.
				request = field.getRequest();
				logger = field.getConfig().getLogger();
			}
			count++;
		}
		matchResult.setCalloutStatus(calloutStatus.toString());
		
		log(request, logger, "FINALRESULT", matchResult.toString());
		return matchResult;
	}
	
	public static MatchResult match(ArrayList<Field> fields) throws MatchException {
		Field[] fieldsArray = fields.toArray(new Field[fields.size()]);
		return match(fieldsArray);
	}
	
	private static MatchResult matchSingleField(Field field) {
		field.log(" --- Matching class :"+ field.getClass().getSimpleName() + " --- ");
		MatchResult result = matchWithActualDataFormats(field);
		
		if(result != null && !StringUtil.isBlank(result.getFormatError()) && (result.getFormatError().equals(Constants.ERR_INVALID_USERDATA) || result.getFormatError().equals(Constants.ERR_INVALID_USERDATAFORMAT))) {
			field.log("Not trying the WhatIf formats as the User Data or User Data Format is INVALID. Please check the configuration!!!");
			return result;
		}
		
		if(result == null)
			createMatchResult(field, false, Constants.ERR_MATCHLIB_ERROR, null, null);
		if(result.getMatchCount() != 1) {
			MatchResult whatIfResult = matchWithWhatIfDataFormats(field);
			if(whatIfResult != null)
				result.setCalloutStatus(result.getCalloutStatus() + whatIfResult.getCalloutStatus());
		}
		return result;
	}
	
	private static MatchResult matchWithWhatIfDataFormats(Field field) {
		List<String> storedDataFormatList = field.getStoredDataFormatList();
		List<String> whatIfStoredDataFormatList = field.getWhatIfStoredDataFormatList();
		List<String> matchFormatList = field.getMatchFormatList();
		List<String> whatIfMatchFormatList = field.getWhatIfMatchFormatList();
		MatchResult result = null;
		
		StringBuilder calloutStatus = new StringBuilder();
		if(whatIfStoredDataFormatList.size() > 0 ) {
			field.log("Trying with WhatIfStoredDataFormat of size " + whatIfStoredDataFormatList.size() + " and actual MatchFormat of size " + matchFormatList.size());
			result = matchWithWhatIfDataFormatList(field, whatIfStoredDataFormatList, true, matchFormatList, false);
			if(result != null)
				calloutStatus.append(result.getCalloutStatus());
		}
		
		if(whatIfStoredDataFormatList.size() > 0 && whatIfMatchFormatList.size() > 0) {
			field.log("Trying with WhatIfStoredDataFormat of size " + whatIfStoredDataFormatList.size() + " and WhatIfMatchFormat of size " + whatIfMatchFormatList.size());
			result = matchWithWhatIfDataFormatList(field, whatIfStoredDataFormatList, true, whatIfMatchFormatList, true);
			if(result != null)
				calloutStatus.append(result.getCalloutStatus());
		}
		
		if(whatIfMatchFormatList.size() > 0) {
			field.log("Trying with actual StoredDataFormat of size " + storedDataFormatList.size() + " and WhatIfMatchFormat of size " + whatIfMatchFormatList.size());
			result = matchWithWhatIfDataFormatList(field, storedDataFormatList, false, whatIfMatchFormatList, true);
			if(result != null)
				calloutStatus.append(result.getCalloutStatus());
		}
		
		result = new MatchResult();
		if(!StringUtil.isBlank(calloutStatus.toString()))
			result.setCalloutStatus("{"+ calloutStatus.toString() + "}");
		
		field.log("WhatIf result :"+ calloutStatus);
		return result;
	}

	private static MatchResult matchWithWhatIfDataFormatList(Field field,
			List<String> storedDataFormatList, boolean isWhatIfSDF,
			List<String> matchFormatList, boolean isWhatIfMF) {

		MatchResult result = new MatchResult();
		StringBuilder calloutStatus = new StringBuilder();
		String userDataFormat = field.getUserDataFormat();
		
		String sdfPrevix = isWhatIfSDF ? "w" : "";
		String mfPrevix = isWhatIfMF ? "w" : "";

		if(isWhatIfSDF && storedDataFormatList.size() > 0 && matchFormatList.size() == 0) {
			int count = 1;
			for(String storedDataFormat : storedDataFormatList) {
				result = matchWithSDFAndMF(field, userDataFormat, storedDataFormat, sdfPrevix+count, null, null);
				if(result.getMatchCount() > 0)
					calloutStatus.append(result.getCalloutStatus());
				count++;
			}
		} else if(isWhatIfMF && storedDataFormatList.size() == 0 && matchFormatList.size() > 0) {
			int count = 1;
			for(String matchFormat : matchFormatList) {
				result = matchWithSDFAndMF(field, userDataFormat, null, null, matchFormat, mfPrevix+count);
				if(result.getMatchCount() > 0)
					calloutStatus.append(result.getCalloutStatus());
				count++;
			}
		} else if(storedDataFormatList.size() > 0 && matchFormatList.size() > 0) {
			int i = 1;
			for(String storedDataFormat : storedDataFormatList) {
				int j = 1;
				for(String matchFormat : matchFormatList) {
					result = matchWithSDFAndMF(field, userDataFormat, storedDataFormat, sdfPrevix+i, matchFormat, mfPrevix+j);
					if(result.getMatchCount() > 0)
						calloutStatus.append(result.getCalloutStatus());
					j++;
				}
				i++;
			}
		}
		result.setCalloutStatus(calloutStatus.toString());
		return result;
	}

	private static MatchResult matchWithActualDataFormats(Field field) {
		MatchResult result = null;
		
		String userDataFormat = field.getUserDataFormat();
		List<String> storedDataFormatList = field.getStoredDataFormatList();
		field.log("Number of Stored Data formats configured :" +storedDataFormatList.size());
		
		if(storedDataFormatList.size() == 0) {
			result = matchWithSDF(field, userDataFormat, null, null);
			if(result != null) {
				field.info("No SDF configured, Matching without SDF "+ result.getCalloutStatus() + ", User Input Data Format " +userDataFormat+ ", Stored Data Format "+ null);
				return result;
			}
		} else {
			int count = 1;
			MatchResult tempResult = null;
			for(String storedDataFormat : storedDataFormatList) {
				tempResult = matchWithSDF(field, userDataFormat, storedDataFormat, String.valueOf(count));
				
				if(tempResult != null) {
					field.info("Matched with SDF "+ count +" :"+ tempResult.getCalloutStatus() + ", User Input Data Format " +userDataFormat+ ", Stored Data Format "+ storedDataFormat);
					if(tempResult.getMatchCount() == 1) {
						return tempResult;
					} else if(!StringUtil.isBlank(tempResult.getFormatError()) && (tempResult.getFormatError().equals(Constants.ERR_INVALID_USERDATA) || tempResult.getFormatError().equals(Constants.ERR_INVALID_USERDATAFORMAT))) {
						field.log("User Data or User Data Format is INVALID. Please check the configuration!!!");
						return tempResult;
					} else if(result == null && StringUtil.isBlank(tempResult.getFormatError())) {
						result = tempResult;
					}
				}
				count++;
			}
			if(result == null)
				result = tempResult;
		}
		field.info("Match failed :"+ result.toString());
		return result;
	}
	
	
	private static MatchResult matchWithSDF(Field field, String userDataFormat, String storedDataFormat, String storedDataFormatId) {
		MatchResult result = null;
		List<String> matchFormatList = field.getMatchFormatList();
		
		field.log("No of MatchFormats configured :"+ matchFormatList.size());
		if(matchFormatList.size() == 0) {
			String matchFormat = null;
			result = matchWithSDFAndMF(field, userDataFormat, storedDataFormat, storedDataFormatId, matchFormat, null);
		} else {
			int count = 1;
			MatchResult tempResult = null;
			for(String matchFormat : matchFormatList) {
				tempResult = matchWithSDFAndMF(field, userDataFormat, storedDataFormat, storedDataFormatId, matchFormat, String.valueOf(count));
				
				if(tempResult != null) {
					field.info("Matching with MF "+ count +" :"+ tempResult.getCalloutStatus() + ", User Input Data Format " +userDataFormat+ ", Stored Data Format "+ storedDataFormat + " and Match Format :"+ matchFormat);
					if(tempResult.getMatchCount() == 1) {
						return tempResult;
					} else if(!StringUtil.isBlank(tempResult.getFormatError()) && (tempResult.getFormatError().equals(Constants.ERR_INVALID_STOREDDATA) || tempResult.getFormatError().equals(Constants.ERR_INVALID_STOREDDATAFORMAT))) {
						field.log("*** Stored Data or Stored Data Format is INVALID. Please check the configuration!!!");
						return tempResult;
					} else if(result == null && StringUtil.isBlank(tempResult.getFormatError())) {
						result = tempResult;
					}
				}
				count++;
			}
			if(result == null)
				result = tempResult;
		}
		return result;
	}
	
	private static MatchResult matchWithSDFAndMF(Field field, String userDataFormat, String storedDataFormat, String storedDataFormatNumber, String matchFormat, String matchFormatNumber) {
		String userData = field.getUserData().trim();
		String storedData = field.getStoredData().trim();
		
		field.debug("User Data = "+ userData + ", User Data Format = "+userDataFormat+ ", Stored Data = "+ storedData+ ", Stored Data Format = "+ storedDataFormat+ " and Match Format = "+ matchFormat);
		boolean result = false;
		String error = "";
		String fieldClassName = field.getClass().getSimpleName();
		
		try {
			if(field.isHashed()) {
				field.log(fieldClassName +" Stored data is hashed! Calling the matchHashed method.");
				result = field.matchHashed(userData, userDataFormat, storedData, storedDataFormat, matchFormat);
			} else {
				field.log(fieldClassName +" Stored data is encrypted! Calling the matchEncrypt method.");
				result = field.matchEncrypted(userData, userDataFormat, storedData, storedDataFormat, matchFormat);
			}
		} catch (MatchException e) {
			field.log("*** MatchException :" +e.getMessage()+" - "+ e.getDescription());
			error = e.getMessage().trim();
		} catch(Exception e) {
			error = Constants.ERR_MATCHLIB_ERROR;
			e.printStackTrace();
			field.log("Exception while processing the match logic in " +fieldClassName+ " with hash flag " +field.isHashed()+ " :"+ e.getMessage());
		}
		field.log("Match result with User data format "+ userDataFormat+ " and Stored data format "+ storedDataFormat+ " :" +result+"."+ error);
		return createMatchResult(field, result, error, storedDataFormatNumber, matchFormatNumber);
	}
	
	private static MatchResult createMatchResult(Field field, boolean result, String error, String storedDataFormatId, String matchFormatId) {
		MatchResult matchResult = new MatchResult();

		String formatId = getFormatId(storedDataFormatId, matchFormatId);

		String calloutStatus = "";
		if(result) {
			matchResult.setMatchCount(1);
			calloutStatus = field.getSuccessCalloutStatus(formatId);
		} else {
			if(StringUtil.isBlank(error)) {
				calloutStatus = field.getFailureCalloutStatus();
			} else {
				calloutStatus = field.getFieldName() + "[" +error+ "]";
				matchResult.setFormatError(error);
			}
		}
		matchResult.setCalloutStatus(calloutStatus.toString());
		return matchResult;
	}
	
	/*
	 * FormatId will be like (2,1) or (1, 3).
	 * */
	private static String getFormatId(String storedDataFormatCount, String matchFormatCount) {
		String formatId = "";

		/*if(!StringUtil.isBlank(storedDataFormatCount) && !storedDataFormatCount.equalsIgnoreCase("1")) 
			formatId = storedDataFormatCount;*/
		if(!StringUtil.isBlank(matchFormatCount) && !matchFormatCount.equalsIgnoreCase("1")) {
			if(StringUtil.isBlank(formatId))
				formatId = matchFormatCount;
			else
				formatId = formatId + "," + matchFormatCount;
		}
		if(!StringUtil.isBlank(formatId))
			formatId = "(" +formatId +")";
		
		return formatId;
	}
	
	public static boolean isEnabled(COConfig config, CORequest request) {
		boolean isEnabled = false;
		String matchLibEnabled = null;
		
		if(request instanceof ACSRequest) {
			matchLibEnabled = config.getValue(Constants.MATCHLIB_ENABLED_ACS);
			log(request, config.getLogger(), "MatchLibrary", "Reading "+ Constants.MATCHLIB_ENABLED_ACS+  " :"+ matchLibEnabled);
		} else if(request instanceof ESRequest) {
			matchLibEnabled = config.getValue(Constants.MATCHLIB_ENABLED_ES);
			log(request, config.getLogger(), "MatchLibrary", "Reading "+ Constants.MATCHLIB_ENABLED_ES+  " :"+ matchLibEnabled);
		}
		
		if(StringUtil.isBlank(matchLibEnabled)) {
			matchLibEnabled = config.getValue(Constants.MATCHLIB_ENABLED);
			log(request, config.getLogger(), "MatchLibrary", "Not configured for ACS/ES, Reading "+ Constants.MATCHLIB_ENABLED+  ":"+ matchLibEnabled);
		}
		
		if(matchLibEnabled != null) {
			isEnabled = Boolean.valueOf(matchLibEnabled.trim());
		}
		log(request, config.getLogger(), "MatchLibrary", "Enabled :"+isEnabled+ ", flag value :"+ matchLibEnabled);
		return isEnabled;
	}
	
	private static void log(CORequest request, COLogger logger, String fieldName, String message) {
		if(StringUtil.isBlank(message))
			return;
		
		if(fieldName != null)
			message = fieldName +":"+ message;
		try {
			if(logger == null) {
				ArcotLogger.logInfo(message);
			} else {
				if(request == null)
					logger.log("["+message+"]");
				else
					logger.log(request, "["+message+"]");
			}
		} catch (Exception e) {
			System.out.println("Exception while logging from MatchLibrary :"+message);
		}
	}
	
	static {
		log(null, null, "MatchLibrary Version", "1, Tag :MatchLibrary_09Feb2012");
	}
	
}
