package com.arcot.apps.callout.matchlibrary.field.impl;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;

/*
 * No UDF/SDF to be configured.
 * PartialPercentage not allowed.
 * Ignoring spaces.
 * */
public class DrivingLicenseField extends PostalCodeField {

	public DrivingLicenseField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
	}
	
}
