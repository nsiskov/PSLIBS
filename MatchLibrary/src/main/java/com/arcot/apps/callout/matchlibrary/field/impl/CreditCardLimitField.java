package com.arcot.apps.callout.matchlibrary.field.impl;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.field.NumericField;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;

/*
 * No SDF/UDF required.
 * PartialAny, PartialLast not allowed as this is an amount field.
 * */
public class CreditCardLimitField extends NumericField {

	public CreditCardLimitField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
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
		
		return "-1";
	}
	
	@Override
	protected boolean matchPartialFirst(String userData, int userDataLength, String storedData, int storedDataLength, int partialDataLength) throws MatchException {
		throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "Partial First Match not allowed for "+ getClass().getSimpleName());
	}

	@Override
	protected boolean matchPartialLast(String userData, int userDataLength, String storedData, int storedDataLength, int partialDataLength) throws MatchException {
		throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "Partial Last Match not allowed for "+ getClass().getSimpleName());
	}
	
	@Override
	protected boolean matchPartialAny(String userData, int userDataLength, String storedData, int storedDataLength, int partialDataLength) throws MatchException {
		throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "Partial Any Match not allowed for "+ getClass().getSimpleName());
	}
	
	@Override
	protected boolean defaultIgnoreNonAlphaNumerics() {
		return true;
	}
	
	@Override
	protected boolean defaultIgnoreSplChars() { 
		return true;
	}
	
	@Override
	protected boolean defaultIgnoreSpaces() { 
		return true;
	}
	
}
