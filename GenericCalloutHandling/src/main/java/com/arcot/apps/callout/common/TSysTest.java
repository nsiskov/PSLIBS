package com.arcot.apps.callout.common;

import java.io.*;
import java.util.*;
import com.arcot.apps.callout.acs.*;
import com.arcot.apps.callout.common.*;
import com.arcot.callout.*;
import com.arcot.util.ArcotException;
import com.arcot.util.ESConfig;
import com.arcot.vpas.enroll.cache.ESCache;

public class TSysTest
{
    public static void main (String args[])
    {
		String url = null;
		String mid = null;
		String did = null;
		String uid = null;
		String pwd = null;
		String txKey = null;
		String cn = null;
		String exp = null;
		String cvv2 = null;
		String zip = null;

		if (args.length == 0)
			printUsage();

        // read arguments
        String op = args[0];
        if (!op.equalsIgnoreCase("-genKey") &&
            !op.equalsIgnoreCase("-renewKey") &&
            !op.equalsIgnoreCase("-0auth") &&
            !op.equalsIgnoreCase("-authobj")
           )
            printUsage();

        if ((op.equalsIgnoreCase("-genKey") && args.length != 6) ||
            (op.equalsIgnoreCase("-renewKey") && args.length != 7) ||
            (op.equalsIgnoreCase("-0auth") && (args.length != 7 && args.length != 8)) ||
            (op.equalsIgnoreCase("-authobj") && args.length != 7)
           )
        {
            System.out.println("\nProvide all arguments");
            printUsage();
        }

		if (op.equalsIgnoreCase("-genKey"))
		{
			url = args[1];
			mid = args[2];
			did = args[3];
			uid = args[4];
			pwd = args[5];

			System.out.println("url = " + url);
			System.out.println("mid = " + mid);
			System.out.println("did = " + did);
			System.out.println("uid = " + uid);
			System.out.println("pwd = " + pwd);

			TSysAuthentication tsa = new  TSysAuthentication();
			String genKeyResponseStr=null;
			try {
				genKeyResponseStr = tsa.generateKey(url, mid, did, uid, pwd, null);
			} catch (ArcotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("GenerateKeyResponse = \n" + genKeyResponseStr);
			System.out.println("Transaction Key = \n" + tsa.getResponseValue(genKeyResponseStr, "transactionKey"));
		}
		else
		if (op.equalsIgnoreCase("-renewKey"))
		{
			url = args[1];
			mid = args[2];
			did = args[3];
			uid = args[4];
			pwd = args[5];
			txKey = args[6];

			System.out.println("url = " + url);
			System.out.println("mid = " + mid);
			System.out.println("did = " + did);
			System.out.println("uid = " + uid);
			System.out.println("pwd = " + pwd);
			System.out.println("txKey = " + txKey);

			TSysAuthentication tsa = new  TSysAuthentication();
			String genKeyResponseStr=null;
			try {
				genKeyResponseStr = tsa.generateKey(url, mid, did, uid, pwd, txKey);
			} catch (ArcotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String genKeyResponseStr = "<GenerateKeyResponse><status>PASS</status><responseCode>A0000</responseCode><responseMessage>Success</responseMessage><transactionKey>UG083Q56K89YYW45UA6VJ4X47IZRDXS4</transactionKey></GenerateKeyResponse>";

			System.out.println("GenerateKeyResponse = \n" + genKeyResponseStr);
			System.out.println("Transaction Key = \n" + tsa.getResponseValue(genKeyResponseStr, "transactionKey"));
		}
		else
		if (op.equalsIgnoreCase("-0auth"))
		{
			url = args[1];
			did = args[2];
			txKey = args[3];
			cn = args[4];
			exp = args[5];
			cvv2 = args[6];

			System.out.println("url = " + url);
			System.out.println("did = " + did);
			System.out.println("txKey = " + txKey);
			System.out.println("cn = " + cn);
			System.out.println("exp = " + exp);
			System.out.println("cvv2 = " + cvv2);

			if (args.length == 8)
			{
			    zip = args[7];
				System.out.println("zip = " + zip);
			}
			else
				zip = null;

			ESCache ec = new ESCache();
			ec.esc = new ESConfig();
			ec.esc.addRow("LOG_BASE_LOCATION", ".", "");

			ACSRequest request = new ACSRequest();
			request.setCardNumber(cn);

			CallOutsConfig calloutsConfig = new CallOutsConfig();

			String infoListStr = "LogFilePath=Test0Auth.log;LogLevel=1;LogicHandler=Test;";
			infoListStr += "TSysURL=" + url;
			infoListStr += ";TSysDeviceID=" + did;
			infoListStr += ";TSysTxKey=" + txKey;
			infoListStr += ";ZeroDollarVendor=TSYS";

			calloutsConfig.setName("Test0Auth");
			calloutsConfig.setInfoList(infoListStr);

			COConfig config  = new COConfig(calloutsConfig);
			com.arcot.apps.callout.common.ZeroDollarAuth.setZeroDollarAuthTestConfig(config);
			com.arcot.apps.callout.common.TSysZeroDollarAuth.setZeroDollarAuthTestConfig(config);


			//String cardAuthResponse = com.arcot.apps.callout.common.TSysZeroDollarAuth.doZeroDollarAuth(request, config, cvv2, exp.substring(0,2), exp.substring(2), zip);
			String cardAuthResponse = com.arcot.apps.callout.common.ZeroDollarAuth.doZeroDollarAuth(request, config, cvv2, exp.substring(0,2), exp.substring(2), zip);
			System.out.println("CardAuthenticationKeyResponse = \n" + cardAuthResponse);

			StringBuffer logToDB = new StringBuffer();
			boolean matchZip = false;
			if(zip !=null){
				matchZip = true;
			}
			if (com.arcot.apps.callout.common.ZeroDollarAuth.parseResponse(request, config, cardAuthResponse, logToDB, matchZip))
				System.out.println("Zero Dollar Authentication Successful.");
			else
				System.out.println("Zero Dollar Authentication Failed.");

			System.out.println("Message to be logged into database: " + logToDB.toString() + "\n");

		}
		else
		if (op.equalsIgnoreCase("-authobj"))
		{
			url = args[1];
			did = args[2];
			txKey = args[3];
			String authRequestStr = args[4];
			String fieldName = args[5];
			String matchCode = args[6];

			System.out.println("url = " + url);
			System.out.println("did = " + did);
			System.out.println("txKey = " + txKey);
			System.out.println("authRequestStr = " + authRequestStr);
			System.out.println("fieldName = " + fieldName);
			System.out.println("Authentication Request = \n" + authRequestStr);

			TSysAuthentication tsa = new  TSysAuthentication();
			String authResponseStr =  null;
			try
			{
				authResponseStr = tsa.doAuth(url, url, 3, did, txKey, authRequestStr);
			}
			catch(IOException e)
			{
				System.out.println("IOException Caught: " + e.getMessage());
			}
			System.out.println("Authentication Response = \n" + authResponseStr);

			if (authResponseStr != null && tsa.isFieldSuccessful(authResponseStr, fieldName, matchCode))
			{
				System.out.println(fieldName + " = " + tsa.getResponseValue(authResponseStr, fieldName));
				System.out.println(fieldName + " status is successful\n");
			}
			else
				System.out.println(fieldName + " status is failure\n");
		}

    } // end of method - main


    private static void printUsage()
    {
        System.out.println("\n\nTool Usage:");
        System.out.println("\n\tTSysTest -genKey <url> <MerchantID> <DeviceID> <Admin UID> <Admin PWD>\n");
        System.out.println("\n\tTSysTest -renewKey <url> <MerchantID> <DeviceID> <Admin UID> < Admin PWD> <Transaction Key>\n");
        System.out.println("\n\tTSysTest -0auth <url> <DeviceID> <Transaction Key> <Card Number> <MMYY> <CVV2> [<ZIP>]\n");
        System.out.println("\n\tTSysTest -authobj <url> <DeviceID> <Transaction Key> <Authenticate Request Message> <FieldName to check> <Successful Status Code>\n");
        System.exit(0);
    } // end of method - printUsage

} // end of class - AuthTest
