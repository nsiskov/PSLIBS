package com.arcot.apps.callout.matchlibrary.field.impl;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.field.AlphaNumericField;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;

/*
 * No UDF/SDF to be configured.
 * PartialPercentage not allowed.
 * Ignoring spaces.
 * */
public class PostalCodeField extends AlphaNumericField {

	public PostalCodeField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
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
	protected boolean matchPartialPercentage(String userData, String storedData,
			int partialDataLength) throws MatchException {
		throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "Partial Percentage Match not allowed for "+ getClass().getSimpleName());
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
