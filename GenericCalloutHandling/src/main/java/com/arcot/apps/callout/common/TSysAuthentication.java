package com.arcot.apps.callout.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import com.arcot.util.ArcotException;

public class TSysAuthentication {
	private String merchantID = null;
	private String deviceID = null;
	private String txKey = null;
	private String adminUID = null;
	private String adminPwd = null;

	public String doZeroDollarAuth(String url, String backupURL, int numTriesAllowed, String deviceID, String txKey, String cardNumber, String cvv, String expMonth, String expYear, String zip)  throws IOException
	{
		
		StringBuffer maskedString = new StringBuffer();
		if(numTriesAllowed == -1){
			numTriesAllowed = 3;
		}
		StringBuffer postData = new StringBuffer();
		String tmpExpYear = expYear;
		if(expYear.length() == 4)
			tmpExpYear = expYear.substring(2);

		/*
		 * Request to TSys is with the following format:
		 * <CardAuthentication>
		 * <deviceID>0</deviceID>
		 * <transactionKey>a</transactionKey>
		 * <cardDataSource>INTERNET</cardDataSource>
		 * <currencyCode>USD</currencyCode>
		 * <cardNumber>1234567890123456</cardNumber>
		 * <expirationDate>MMYY</expirationDate>
		 * <zip>12345</zip>
		 * </CardAuthentication>
		 */
		 String authRequestStr = "<CardAuthentication><deviceID>" + deviceID + "</deviceID>" +
		 											 "<transactionKey>" + txKey + "</transactionKey>" +
		 											 "<cardDataSource>INTERNET</cardDataSource>" +
		 											 "<currencyCode>USD</currencyCode>" +
		 											 "<cardNumber>" + cardNumber + "</cardNumber>" +
		 											 "<expirationDate>" + expMonth + tmpExpYear + "</expirationDate>" +
		 											 "<cvv2>" + cvv +"</cvv2>";

		String authRequestMaskedStr = "<CardAuthentication><deviceID>" + "XXXXXXXXXXXXXXXX" + "</deviceID>" +
		 											 "<transactionKey>XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX</transactionKey>" +
		 											 "<cardDataSource>INTERNET</cardDataSource>" +
		 											 "<currencyCode>USD</currencyCode>" +
		 											 "<cardNumber>XXXXXXXXXXXXXXXX</cardNumber>" +
		 											 "<expirationDate>MMYY</expirationDate>" +
		 											 "<cvv2>XXX</cvv2>";
		if (zip != null)
		{
			authRequestStr += "<zip>" + zip + "</zip>";
			authRequestMaskedStr += "<zip>XXX</zip>";
		}

		authRequestStr += "</CardAuthentication>";
		authRequestMaskedStr += "</CardAuthentication>";

		postData.append(authRequestStr);
		maskedString.append(authRequestMaskedStr);

		//Including the Fallback URL functionality.
		int primaryTries = 1;
		boolean secondryTry = false;
		//If no back Up URL is configured then use all the tries with primary URL
		// If the num tries allowed is just 1 then only the primary needs to be used
		if(backupURL == null || "".equals(backupURL) || numTriesAllowed == 1){
			primaryTries = numTriesAllowed;
		} else {
			//Else save last try for the Back up URL
			primaryTries = numTriesAllowed - 1;
			secondryTry = true;
		}

		int numTries = 0;
		String response = null;
		URLConnection conn = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		//Make a socket Connection and post the request and get the Response for the primary URL
		String authReversalResponse = null;
		do
		{
			conn = (HttpURLConnection)(new URL(url)).openConnection();
			((HttpURLConnection)conn).setRequestMethod("POST");
			response = processRequest(postData.toString(), conn);
			numTries++;
		}
		while(numTries < primaryTries && response == null);

		//If backup Configured try for the Back UP url
		if(secondryTry == true && response == null){
			conn = (HttpURLConnection)(new URL(backupURL)).openConnection();
			((HttpURLConnection)conn).setRequestMethod("POST");
			response = processRequest(postData.toString(), conn);
			numTries++;
		}

		return response;
	}

	public String doAuth(String url, String backupURL, int numTriesAllowed, String deviceID, String txKey, String authRequestStr) throws IOException{

		StringBuffer maskedString = new StringBuffer();
		if(numTriesAllowed == -1){
			numTriesAllowed = 3;
		}
		StringBuffer postData = new StringBuffer();
		postData.append(authRequestStr);

		//Including the Fallback URL functionality.
		int primaryTries = 1;
		boolean secondryTry = false;
		//If no back Up URL is configured then use all the tries with primary URL
		// If the num tries allowed is just 1 then only the primary needs to be used
		if(backupURL == null || "".equals(backupURL) || numTriesAllowed == 1){
			primaryTries = numTriesAllowed;
		} else {
			//Else save last try for the Back up URL
			primaryTries = numTriesAllowed - 1;
			secondryTry = true;
		}

		int numTries = 0;
		String response = null;
		URLConnection conn = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		//Make a socket Connection and post the request and get the Response for the primary URL
		String authReversalResponse = null;
		do
		{
			conn = (HttpURLConnection)(new URL(url)).openConnection();
			((HttpURLConnection)conn).setRequestMethod("POST");
			response = processRequest(postData.toString(), conn);
			numTries++;
		}
		while(numTries < primaryTries && response == null);

		//If backup Configured try for the Back UP url
		if(secondryTry == true && response == null){
			conn = (HttpURLConnection)(new URL(backupURL)).openConnection();
			((HttpURLConnection)conn).setRequestMethod("POST");
			response = processRequest(postData.toString(), conn);
			numTries++;
		}

		return response;
	}

	private String processRequest(String postData, URLConnection connection) throws IOException{

		connection.setDoOutput(true);
		connection.addRequestProperty("Content-Type", "text/xml");
		OutputStream os = connection.getOutputStream();
		os.write(postData.getBytes());
		os.flush();

		//BufferedReader br = new BufferedReader(new InputStreamReader(((HttpURLConnection)connection).getInputStream()));
		InputStream is = connection.getInputStream();
		InputStreamReader in = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(in);

		if(br == null){
			br = new BufferedReader(new InputStreamReader(((HttpURLConnection)connection).getErrorStream()));
		}
		StringBuffer response = new StringBuffer();

		String temp = br.readLine();

		while(temp != null){
			response.append(temp);
			response.append("\n");
			temp = br.readLine();
		}
		os.close();
		br.close();

		return response.toString().trim();
	}

	public boolean isAuthSuccessful(String response){
		String statusCode = getResponseValue(response, "responseCode");
		if (statusCode != null && statusCode.equalsIgnoreCase("A0000"))
			return true;

		return false;
	}

	public boolean isCVV2Successful(String response){
		String statusCode = getResponseValue(response, "cvvVerificationCode");
		if (statusCode != null && statusCode.equalsIgnoreCase("M"))
			return true;

		return false;
	}

	public boolean isZipSuccessful(String response){
		String statusCode = getResponseValue(response, "addressVerificationCode");
		if (statusCode != null && statusCode.equalsIgnoreCase("Z"))
			return true;

		return false;
	}

	public boolean isFieldSuccessful(String response, String fieldName,  String matchCode){
		String statusCode = getResponseValue(response, fieldName);
		if (statusCode != null && statusCode.equalsIgnoreCase(matchCode))
			return true;

		return false;
	}

	public String getResponseValue(String response, String fieldName){
		int startIndex = response.indexOf("<" +  fieldName + ">");
		int endIndex = response.indexOf("</" +  fieldName + ">");
		if (startIndex == -1 || endIndex == -1)
		    return null;

		startIndex = startIndex + 2 + fieldName.length();

		return response.substring(startIndex, endIndex);
	}

	public String generateKey(String url,String MerchantID, String DeviceID, String UserID, String Password, String txKey) throws ArcotException{
		String genKeyReqStr = "<GenerateKey><mid>" + MerchantID + "</mid><userID>" + UserID + "</userID><password>" + Password + "</password>";
		String response = null;

		if (txKey != null)
			genKeyReqStr += "<transactionKey>" + txKey + "</transactionKey>";

		genKeyReqStr += "</GenerateKey>";

		try{
			URLConnection conn = (HttpURLConnection)(new URL(url)).openConnection();
			((HttpURLConnection)conn).setRequestMethod("POST");
			response = processRequest(genKeyReqStr, conn);
		//}catch(IOException e){
		}catch(Exception e){
			throw new ArcotException(ArcotException.AE_GENERAL_ERROR,e);
		}
		return response;
	}

}
