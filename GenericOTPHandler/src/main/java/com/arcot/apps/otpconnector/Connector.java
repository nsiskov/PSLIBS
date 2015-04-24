package com.arcot.apps.otpconnector;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public abstract class Connector {

	/*
	 * Static Final Constants
	 */
	public static final String DEFAULT_CONNECTOR= "com.arcot.apps.connector.CATConnector";
	public static final int DEFAULT_CONNECTION_TIMEOUT= 20000; // 20 Seconds is the default Connection Timeout
	public static final String MOBILE_NUMBER= "MOBILE_NUMBER";
	public static final String OTP_TEXT= "OTP_TEXT";
	
	/*
	 * Input Key-Values to be retrieved from the Input HashMap 
	 */
	public static final String CONNECTION_TIMEOUT= "CONNECTION_TIMEOUT";
	
	
	//TODO: Need to decide on the generation of unique reference number which multiple clients are requesting for.
	
	/**
	 * 
	 * @param connector
	 * @param configs
	 * @return Connector Object
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static Connector getInstance(String connector, HashMap<String, String> configs)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		return (Connector)Class.forName(connector).newInstance();
	}
	
/**
 * This method will perform OTP Delivery	
 * @param configs
 * @return
 * @throws MalformedURLException
 * @throws IOException
 * @throws InvalidMessageException 
 * @throws NullPointerException 
 * @throws IllegalArgumentException 
 */
public abstract ConnectorResponse deliver(HashMap<String, String> configs, String mobileNumber, String message) throws MalformedURLException, IOException,
IllegalArgumentException, NullPointerException, Exception, ParserConfigurationException, SAXException, IOException;
	
	/**
	 * This method will perform the common validations for All the Connectors.
	 * Moved the Mobile Number Validation to the individual Connector in the 
	 * situation of the client having the mobile number at their back-end.
	 * @param configs
	 * @return Generator ConnectorResponse
	 */
	protected ConnectorResponse validate(HashMap<String, String> configs, String mobileNumber, String otpMessage){
		ConnectorResponse response = new ConnectorResponse();
		
		/*
		 * Set Successful result as Null Checks are complete
		 */
		response.setResult(true);
		
		return response;
	}
	
	/**
	 * This method will set the Connector Response Object
	 * @param connectorResponse
	 * @param result
	 * @param shortErrorMessage
	 * @param longErrorMessage
	 * @return ConnectorResponse Object
	 */
	protected ConnectorResponse setConnectorResponse(
			ConnectorResponse connectorResponse, boolean result, String shortErrorMessage, String longErrorMessage) {
		connectorResponse.setResult(result);
		connectorResponse.setShortErrorMessage(shortErrorMessage);
		connectorResponse.setLongErrorMessage(longErrorMessage);
		return connectorResponse;
	}

}
