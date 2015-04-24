package com.arcot.apps.callout.matchlibrary.field.impl;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.util.StringUtil;

/*
 * No UDF/SDF to be configured.
 * PartialFirst, PartialAny and PartialPercentage not allowed.
 * Ignoring spaces and special characters by default.
 * Adding '+' to special characters array.
 * */
public class PhoneField extends SSNField {

	public PhoneField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
	}
	
	@Override
	public char[] getSplCharArray() {
		String splCharStr = getFieldValueFromConfig(Constants.CONF_SPLCHARS_LIST);
		
		if(StringUtil.isBlank(splCharStr))
			return new char[]{'+', '-', '.'};
		return splCharStr.toCharArray();
	}

	@Override
	protected boolean defaultIgnoreSpaces() { 
		return true;
	}
	
	@Override
	protected boolean defaultIgnoreSplChars() { 
		return true;
	}
}
