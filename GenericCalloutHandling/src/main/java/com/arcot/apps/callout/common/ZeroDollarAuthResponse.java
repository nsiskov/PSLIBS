package com.arcot.apps.callout.common;

public class ZeroDollarAuthResponse {
	String completeResponse = null;
	String zeroDollarResponseCode = null;
	String zipResponse = null;
	String cvvResponse = null;
	String arcotError = null;

	public void setResponse(COLogger logger, CORequest request, String response){
		completeResponse = response;
		zeroDollarResponseCode = response.substring(26, 29);
		zipResponse = response.substring(41, 43);
		cvvResponse = response.substring(43, 44);
	}

}
