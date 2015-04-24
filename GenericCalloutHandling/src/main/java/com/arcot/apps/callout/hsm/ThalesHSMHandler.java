package com.arcot.apps.callout.hsm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Date;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.util.Utility;

public class ThalesHSMHandler {
	
	public static final String HSM_HOST = "HSMHost";
	public static final String HSM_PORT = "HSMPort";
	public static final String BKUP_HSM_HOST = "BkupHSMHost";
	public static final String BKUP_HSM_PORT = "BkupHSMPort";
	public static final String HSM_CONN_TIMEOUT = "HSMConnTimeout";
	public static final String HSM_READ_TIMEOUT = "HSMReadTimeout";
	public static final String HSM_RETRY_COUNT = "HSMRetries";
	public static final String HSM_SVC_CODE = "HSMSvcCode";

	public static String[] doHSM(CORequest request, COConfig config, String[] commands){
		COLogger logger = config.getLogger();
		if(commands == null || commands.length == 0){
			return null;
		}
		
		String[] responses = new String[commands.length];
		Socket hsmConn = getHSMConn(request, config);
		if(hsmConn == null){
			return null;
		}
		
		OutputStream hsmOut = null;
		InputStream hsmIn = null;
		
		try {
			hsmOut = hsmConn.getOutputStream();
			hsmIn = hsmConn.getInputStream(); 
		} catch (IOException e1) {
			logger.log(request, "IOException - " + e1.getMessage() + " while opening read/write streams on HSM connection [" + config.getValue(HSM_HOST) + ":" + config.getValue(HSM_PORT) + "]");
			closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
			return null;
		}
		
		
		if(hsmConn == null || hsmOut == null || hsmIn == null){
			logger.log(request, "HSM connection failed [" + config.getValue(HSM_HOST) + ":" + config.getValue(HSM_PORT) + "]");
			closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
			return null;
		}
		
		for (int i = 0; i < commands.length; i++) {
			try {
				logger.log(request, "Trying Command[" + 0 + "]");
				hsmOut.write(commands[i].getBytes(), 0, commands[i].length());
				byte[] buffer = new byte[100];
		        int respLen = hsmIn.read(buffer);
		        responses[i] = new String(buffer, 0, respLen);		        
			} catch (IOException e) {
				//ignore this, let's continue to check fwith rest of the pairs
				//Need to decide later whether we need retries on IOException!
				logger.log(request, "IOException in command [" + i + "]", e);
				responses[i] = "IOException::" + e.getMessage();
				continue;
			} catch (Exception e) {
				//ignore this, let's continue to check fwith rest of the pairs
				//Need to decide later whether we need retries on IOException!
				logger.log(request, "Exception in command [" + i + "]", e);
				responses[i] = "Exception::" + e.getMessage();
				continue;
			}
		}
		closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
		return responses;
	}
	
	private static void closeHSMResources(Socket hsmConn, OutputStream hsmOut, InputStream hsmIn, CORequest request, COLogger logger){
		try {
			if(hsmIn  != null)			
					hsmIn.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.log(request, "Error cleaning hsm input stream", e);
		}
		
		try {
			if(hsmOut  != null)
				hsmOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.log(request, "Error cleaning output stream", e);
		}
		
		try {
			if(hsmConn  != null)
				hsmConn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.log(request, "Error cleaning hsm connection", e);
		}		
	}
	
	/*
	 * Currently use this method so taht we ensure taht we never take more tahn one HSM connection per trident
	 * */
	public static synchronized String[] doSyncHSM(CORequest request, COConfig config, String[] commands){
		return doHSM(request, config, commands);
	}
	
	public static synchronized String[] createISOPinBlock(CORequest request, COConfig config, String zpk, String atmPin, String acctNumber){
		COLogger logger = config.getLogger();
		Socket hsmConn = getHSMConn(request, config);
		OutputStream hsmOut = null;
		InputStream hsmIn = null;
		String encPinLMK  = null;
		String pinBlock = null;
		
		StringBuffer strCommand = new StringBuffer();
		strCommand.append("Enc PIN Command:BA");
		strCommand.append(atmPin + "F");
		strCommand.append(acctNumber);
		String baCmd = Utility.getHexLength(strCommand.length()) + strCommand.toString();
		
		try {
			hsmOut = hsmConn.getOutputStream();
			hsmIn = hsmConn.getInputStream(); 
		} catch (IOException e1) {
			logger.log(request, "IOException - " + e1.getMessage() + " while opening read/write stream on HSM connection [" + config.getValue(HSM_HOST) + ":" + config.getValue(HSM_PORT) + "]");
			closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
			return null;
		}
		
		try {			
			hsmOut.write(baCmd.getBytes(), 0, baCmd.length());
			byte[] buffer = new byte[100];
	        int respLen = hsmIn.read(buffer);
	        String hsmRead = new String(buffer, 0, respLen);
	        
	        if(hsmRead.length() < 22){
				closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
				return new String[]{"BadRespLMK", pinBlock};
	        }
	        
	        String hsmRetCode = hsmRead.substring(20,22);
	        if (!"00".equals(hsmRetCode)){
				closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
				return new String[]{"HSMErrLMK[" + hsmRetCode + "]", pinBlock};
	        }
	        
	        encPinLMK  = hsmRead.substring(22,27);
		} catch (IOException e1) {
			logger.log(request, "IOException - " + e1.getMessage() + " in LMK encryption on HSM connection [" + config.getValue(HSM_HOST) + ":" + config.getValue(HSM_PORT) + "]");
			closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
			return new String[]{"LMKEncEx", pinBlock};
		}
		
		try {
	        //Now Translate under ZPK
			strCommand = new StringBuffer();
			strCommand.append("Enc PIN Command:JG");
			strCommand.append(zpk);
			strCommand.append("01");
			strCommand.append(acctNumber);
			strCommand.append(encPinLMK);
			String jgCmd = Utility.getHexLength(strCommand.length()) + strCommand.toString();
			hsmOut.write(jgCmd.getBytes(), 0, jgCmd.length());
			byte[] buffer = new byte[100];
	        int respLen = hsmIn.read(buffer);
	        String hsmRead = new String(buffer, 0, respLen);
	        
	        if(hsmRead.length() < 22){
				closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
				return new String[]{"BadRespZPK", pinBlock};
	        }
	        
	        String hsmRetCode = hsmRead.substring(20,22);
	        if (!"00".equals(hsmRetCode)){
				closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
				return new String[]{"HSMErrZPK[" + hsmRetCode + "]", pinBlock};
	        }
	        pinBlock = hsmRead.substring(22,38);
		} catch (IOException e1) {
			logger.log(request, "IOException - " + e1.getMessage() + " while read/write on HSM connection [" + config.getValue(HSM_HOST) + ":" + config.getValue(HSM_PORT) + "]");
			closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
			return new String[]{"ZPKEncEx", pinBlock};
		}		
		closeHSMResources(hsmConn, hsmOut, hsmIn, request, logger);
		return new String[]{"00", pinBlock};
	}
	
	private static Socket getHSMConn(CORequest request, COConfig config){
		Socket hsmConn = null;
		COLogger logger = config.getLogger();	
		String hsmHost = config.getValue(HSM_HOST);
		int hsmPort = 0;
		int connTimeout = 0;
		int readTimeout = 0;
		int retries = 0;
		try {
			hsmPort = Integer.parseInt(config.getValue(HSM_PORT));
			connTimeout = Integer.parseInt(config.getValue(HSM_CONN_TIMEOUT));
			readTimeout = Integer.parseInt(config.getValue(HSM_READ_TIMEOUT));
			retries = Integer.parseInt(config.getValue(HSM_RETRY_COUNT));
		} catch (NumberFormatException e1) {
			hsmPort = 1000;
			connTimeout = 1000;
			readTimeout = 1000;
			retries = 2;
		}		

		for (int retry = 0; retry < retries; retry++) {
			try {
				SocketAddress hsmSA = new InetSocketAddress(hsmHost, hsmPort);
				hsmConn = new Socket();
				hsmConn.connect(hsmSA, connTimeout);
				hsmConn.setSoTimeout(readTimeout);
				break;
			} catch (UnknownHostException e) {
				logger.log(request, "HSM connection failed [" + hsmHost + ", " + hsmPort + "] for try " + (retry + 1), e);
				closeHSMResources(hsmConn, null, null, request, logger);
			} catch (IOException e) {
				logger.log(request, "HSM IO failed [" + hsmHost + ", " + hsmPort + "] for try " + (retry + 1), e);
				closeHSMResources(hsmConn, null, null, request, logger);
			} catch (Exception e) {
				logger.log(request, "Exception [" + hsmHost + ", " + hsmPort + "] for try " + (retry + 1), e);
				closeHSMResources(hsmConn, null, null, request, logger);
			}
		}
		
		if(hsmConn == null){
			logger.log(request, "HSM connection failed [" + hsmHost + ", " + hsmPort + "]");
		}
		return hsmConn;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String hsmHost = args[0];
		int hsmPort = Integer.parseInt(args[1]);
		String zpk = args[2];
		String pin = args[3];
		String actNum = args[4];
		
		Date start = new Date();
		SocketAddress hsmSA = new InetSocketAddress(hsmHost, hsmPort);
		Socket hsmConn = new Socket();
		OutputStream hsmOut = null;
		InputStream hsmIn = null;
		String encPinLMK  = null;
		String pinBlock = null;
		
		StringBuffer strCommand = new StringBuffer();
		strCommand.append("Enc PIN Command:BA");
		strCommand.append(pin + "F");
		strCommand.append(actNum);
		String baCmd = Utility.getHexLength(strCommand.length()) + strCommand.toString();
		
		try {
			hsmConn.connect(hsmSA, 10000);
			hsmConn.setSoTimeout(10000);
			hsmOut = hsmConn.getOutputStream();
			hsmIn = hsmConn.getInputStream(); 
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {			
			hsmOut.write(baCmd.getBytes(), 0, baCmd.length());
			byte[] buffer = new byte[100];
	        int respLen = hsmIn.read(buffer);
	        //while(respLen > 0){
	        String hsmRead = new String(buffer, 0, respLen);
	        	//respLen = hsmIn.read(buffer);
	        //}
	        System.out.println("LMK return "  + hsmRead);
	        if(hsmRead.length() < 22){
				System.out.println("Bad LMK return ");
	        	return;
	        }
	        
	        String hsmRetCode = hsmRead.substring(20,22);
	        if (!"00".equals(hsmRetCode)){
	        	System.out.println("Bad error code:  " + hsmRetCode);
	        	return;
	        }
	        
	        encPinLMK  = hsmRead.substring(22,27);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		try {
	        //Now Translate under ZPK
			strCommand = new StringBuffer();
			strCommand.append("Enc PIN Command:JG");
			strCommand.append(zpk);
			strCommand.append("01");
			strCommand.append(actNum);
			strCommand.append(encPinLMK);
			String jgCmd = Utility.getHexLength(strCommand.length()) + strCommand.toString();
			hsmOut.write(jgCmd.getBytes(), 0, jgCmd.length());
			byte[] buffer = new byte[100];
	        int respLen = hsmIn.read(buffer);
	        //while(respLen > 0){
	        String hsmRead = new String(buffer, 0, respLen);
	        	//respLen = hsmIn.read(buffer);
	        //}
	        
	        System.out.println("ZPK return: " + hsmRead);
	        if(hsmRead.length() < 22){
				System.out.println("Bad ZPK return ");
	        	return;
	        }
	        
	        String hsmRetCode = hsmRead.substring(20,22);
	        if (!"00".equals(hsmRetCode)){
				System.out.println("Bad ZPK error code " + hsmRetCode);
	        	return;
	        }
	        pinBlock = hsmRead.substring(22,38);
	        System.out.println("PIN Block:  " + pinBlock);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		Date end = new Date();
		System.out.println("Time taken: " + ((end.getTime() - start.getTime())));
	}

}
