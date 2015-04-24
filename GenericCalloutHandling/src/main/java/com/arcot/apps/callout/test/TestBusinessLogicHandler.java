package com.arcot.apps.callout.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.acs.ACSResponse;
import com.arcot.apps.callout.common.BusinessLogicHandler;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.common.COResponse;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.callout.es.ESRequest;
import com.arcot.apps.callout.es.ESResponse;
import com.arcot.crypto.CryptoUtil;
import com.arcot.dboperations.DBHandler;
import com.arcot.util.BankInfo;
import com.arcot.vpas.enroll.cache.ESCache;

public class TestBusinessLogicHandler extends DBHandler implements BusinessLogicHandler {
	
	public static final String FILE_NAME = "TestFilePath";
	public static final String CACHE_TEST_DATA = "CacheTestData";
	
	private static ArrayList testData = null;

	/*
	 * This test callout assumes the format of the TestFile to be:
	 * CardNumber,ESResponse(Y/N or true/false),ACSResponse(Y/N/A/U),lock(N/lock/block)
	 * Default values for ESResponse and ACSResponse are N and U
	 * Default value for lock is false
	 * Lines begining with # are ignored
	 * */
	
	public ACSResponse process(ACSRequest request, COConfig config) {
		ACSResponse response = COUtil.prepareACSResponse(request, processTestCallout(request, config));

		/*
		 * Need special handling for VE as it does not work on ACS CRC alone!
		 * */
		if(ACS.VERIFY_ENROLLMENT_REQUEST.equalsIgnoreCase(request.getMessageType())){	
			BankInfo bank = ESCache.bic.getBank(request.getBankId());
			String encCardNumber = CryptoUtil.encrypt3DES64(bank.BANKKEY_CLEAR, request.getCardNumber(), dbParam.softsign);
			response.setProxyPan(generateProxyPAN(encCardNumber));
			
			if(response.getAcsCRC() ==ACS.ACS_CALLOUT_SUCCESS){
				response.setEnrolled(ACS.STATUS_Y);
			}else if(response.getAcsCRC() == ACS.ACS_CALLOUT_FAIL){
				response.setEnrolled(ACS.STATUS_N);
			}else{
				response.setEnrolled(ACS.STATUS_U);
			}
		}
		return response;
	}

	public ESResponse process(ESRequest request, COConfig config) {
		return COUtil.prepareESResponse(request, processTestCallout(request, config));
	}
	
	private COResponse processTestCallout(CORequest request, COConfig config){
		COResponse response = null;
		Date startTime = new Date();
		COLogger logger = config.getLogger();
		String fileName = config.getValue(FILE_NAME);
		boolean cacheData = (config.getValue(CACHE_TEST_DATA) != null);
		
		if(fileName == null || "".equals(fileName.trim())){
			logger.log(request, "No test file to read");
			return COUtil.createCOResponse(false, 0, "NoTestFile", "Test file is null or empty", ACS.ACS_CALLOUT_ABORT);
		}
		
		BufferedReader br = null;
		String line = null;
		boolean cachePopulationCycle = false;
		int index = 0;
		
		try {
			if(!cacheData || (testData == null)){
				//Do the file processing
				br = new BufferedReader(new FileReader(fileName));
				line = br.readLine();
				if(cacheData){
					testData = new ArrayList();
					testData.add(line);
					cachePopulationCycle = true;					
				}
			}else{
				if(index < testData.size()){
					line =  (String)testData.get(index++);	
				}else{
					line = null;
				}
			}
			
			while(line != null){
				line = line.trim();
				if(line.startsWith("#")){
					//This is a comment line, skip
				}else if(line.startsWith(request.getCardNumber())){
					//read this line and fill the result
					String esRes = "N";
					String acsRes = "U";
					String lock = "N";
					StringTokenizer stk = new StringTokenizer(line, ",");
					int numTokens = stk.countTokens();
					if(numTokens > 1){
						//only cardNum and esRes
						stk.nextToken();
						esRes = stk.nextToken();
						if(esRes != null && ("y".equalsIgnoreCase(esRes.trim()) || "true".equalsIgnoreCase(esRes))){
							esRes = "Y";
						}
					}
					
					if(numTokens > 2){
						acsRes = stk.nextToken();
						if(acsRes == null || "".equals(acsRes.trim())){
							acsRes = "U";
						}
					}
					
					if(numTokens > 3){
						lock = stk.nextToken();
					}
					
					boolean esr = false;
					if("y".equalsIgnoreCase(esRes) || "true".equalsIgnoreCase(esRes)){
						esr = true;
					}
					
					int acsr = ACS.ACS_CALLOUT_UNSURE;
					if("n".equalsIgnoreCase(acsRes)){
						acsr = ACS.ACS_CALLOUT_FAIL;
					}else if("a".equalsIgnoreCase(acsRes)){
						acsr = ACS.ACS_CALLOUT_ATTEMPTS;
					}else if("y".equalsIgnoreCase(acsRes)){
						acsr = ACS.ACS_CALLOUT_SUCCESS;
					}
					
					int lockStatus = 0;
					lock = lock.trim();
					if("block".equalsIgnoreCase(lock)){
						//check with product
						lockStatus = 1;
					}else if("lock".equalsIgnoreCase(lock)){
						lockStatus = 2;
					}
					response = COUtil.createCOResponse(esr, lockStatus, "TestCallout", "Test Callout [" 
							+ esr + ", " + acsr + "]", acsr);
				}else{
					//do nothing!
				}
				
				if(!cacheData || cachePopulationCycle){
					line = br.readLine();
					if(cacheData && line != null){
						testData.add(line);
					}
				}else{
					if(index < testData.size()){
						line =  (String)testData.get(index++);	
					}else{
						line = null;
					}
				}
			}
			Date endTime = new Date();
			logger.log(request, "TIME[" + (endTime.getTime() - startTime.getTime()) + "]");
		} catch (FileNotFoundException e) {
			response = COUtil.createCOResponse(false, 0, "TestCalloutFNF", "Test Callout File Not Found", ACS.ACS_CALLOUT_UNSURE);
			logger.log(request, "Missing test file", e);
		} catch (IOException e) {
			response = COUtil.createCOResponse(false, 0, "TestCalloutIO", "Test Callout IO Error", ACS.ACS_CALLOUT_UNSURE);
			logger.log(request, "Could not read test file", e);
		}finally{
			try {
				if(br != null)
					br.close();
			} catch (IOException e) {
				logger.log(request, "Error closing file reader", e);
			}
		}
		
		if(response == null){
			response = COUtil.createCOResponse(false, 0, "TestCardNotFound", "Test Card Not found in the test file", ACS.ACS_CALLOUT_UNSURE);
		}
		return response;
	}
	
	private String generateProxyPAN(String encryptedPAN)
	{
		// if the ProxyPAN is already in use, we generate a new one
		StringBuffer buf = new StringBuffer(encryptedPAN);
		byte[] cardSecret = new byte[20];
		CryptoUtil.genRandom(cardSecret);
		for (int i = 0; i < cardSecret.length; i++)
			buf.append(Integer.toHexString(cardSecret[i]));
		String proxyPAN = CryptoUtil.hashDataB64(buf.toString());
		return proxyPAN;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ESRequest request = new ESRequest();
		
		if(args.length < 3){
			System.out.println("Usage: java com.arcot.ps.callout.test.TestBusinessLogicHandler <cardnumber> <datafilepath> <logfilepath>");
			return;			
		}
		
		String cardNumber = args[0];
		if(cardNumber == null){
			System.out.println("Usage: java com.arcot.ps.callout.test.TestBusinessLogicHandler <cardnumber> <filepath>");
			return;
		}
		
		String filePath = args[1];
		if(filePath == null){
			System.out.println("Usage: java com.arcot.ps.callout.test.TestBusinessLogicHandler <cardnumber> <filepath>");
			return;
		}
		
		String logFilePath = args[2];
		StringBuffer infolist = new StringBuffer(COConfig.LOGIC_HANDLER);
		infolist.append("=com.arcot.ps.callouts.test.TestCallout;");
		infolist.append(COConfig.LOG_FILE_PATH);
		infolist.append("=");
		infolist.append(logFilePath);
		infolist.append(";");
		infolist.append(FILE_NAME);
		infolist.append("=");
		infolist.append(filePath);
		infolist.append(";");
		infolist.append(CACHE_TEST_DATA);
		infolist.append("=1");
		
		request.setCardNumber(cardNumber);
		request.setInfolist(infolist.toString());
		request.setCardNumber(args[0]);
/*		COConfig config = new COConfig(request);
		TestBusinessLogicHandler tc = new TestBusinessLogicHandler();
		COResponse response = tc.process(request, config);
		System.out.println(response);*/
	}

}
