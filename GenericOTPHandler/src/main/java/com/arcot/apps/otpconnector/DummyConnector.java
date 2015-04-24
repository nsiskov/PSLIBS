package com.arcot.apps.otpconnector;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

/*
 * The Dummy connector can be configured to exhibit the end-end flow
 * while the connectors are being developed.
 * @see com.arcot.apps.otpconnector.OTPConnector#validate(java.util.HashMap, java.lang.String, java.lang.String)
 */
public class DummyConnector extends Connector {

	
	@Override
	protected ConnectorResponse validate(HashMap<String, String> configs,
			String mobileNumber, String message) {
		ConnectorResponse res = super.validate(configs, mobileNumber, message);
/*		if(mobileNumber == null || "".equals(mobileNumber)){
			
			 * The Mobile Number has not been set return false for the OTP Delivery.
			 
			return setConnectorResponse(res, false, res.MOBILE_NUMBER_EMPTY_SHORT_ERROR, res.MOBILE_NUMBER_EMPTY_LONG_ERROR);
		}*/
		return res;
	}

	/*
	 * This connector will always respond with Success
	 */
	@Override
	public ConnectorResponse deliver(HashMap<String, String> configs,
			String mobileNumber, String otpMessage) throws MalformedURLException,
			IOException, IllegalArgumentException, NullPointerException,
			Exception {
		/*
		 * Calling the Validate Function to Verify the
		 * presence of necessary inputs like OTP, Mobile Number.
		 */
		ConnectorResponse dummyConnectorResponse = validate(configs, mobileNumber, otpMessage);
		if(!dummyConnectorResponse.getResult()){
			return dummyConnectorResponse;
		}
		
		dummyConnectorResponse.setResult(true);
		dummyConnectorResponse.setShortErrorMessage(dummyConnectorResponse.SUCCESS_SHORT_MSG);
		dummyConnectorResponse.setLongErrorMessage(dummyConnectorResponse.SUCCESS_LONG_MSG);
		dummyConnectorResponse.setMobileNumber("0826");
		return dummyConnectorResponse;
	}
	

}
