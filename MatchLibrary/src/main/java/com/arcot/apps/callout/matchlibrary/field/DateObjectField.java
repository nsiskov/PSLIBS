package com.arcot.apps.callout.matchlibrary.field;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;

public class DateObjectField extends DateField {
	
	private static String dateFormatStr = "ddMMyyyy";
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
	
	public DateObjectField(CORequest request, COConfig config,
			String fieldName, Date userData, Date storedData) {
		super(request, config, fieldName, dateFormat.format(userData), dateFormat.format(storedData));
	}

	@Override
	public String getUserDataFormatFromConfig() throws MatchException {
		String udf = getFieldValueFromConfig(Constants.CONF_USERDATAFORMAT);

		if(!StringUtil.isBlank(udf))
			throw new MatchException(Constants.ERR_INVALID_USERDATAFORMAT, "UDF not required for "+ getClass().getSimpleName());
		
		return dateFormatStr;
	}
	
	@Override
	public String getStoredDataFormatFromConfig() throws MatchException {
		String sdf = getFieldValueFromConfig(Constants.CONF_STOREDDATAFORMAT);

		if(!StringUtil.isBlank(sdf))
			throw new MatchException(Constants.ERR_INVALID_STOREDDATAFORMAT, "SDF not required for "+ getClass().getSimpleName());
		
		return dateFormatStr;
	}

}
