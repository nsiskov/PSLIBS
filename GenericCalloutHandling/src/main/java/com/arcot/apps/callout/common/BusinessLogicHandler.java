package com.arcot.apps.callout.common;

import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.acs.ACSResponse;
import com.arcot.apps.callout.es.ESRequest;
import com.arcot.apps.callout.es.ESResponse;

public interface BusinessLogicHandler {	
	public ACSResponse process(ACSRequest request, COConfig config);
	
	public ESResponse process(ESRequest request, COConfig config);
}
