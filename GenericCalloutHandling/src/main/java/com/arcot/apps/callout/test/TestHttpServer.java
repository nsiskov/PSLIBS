package com.arcot.apps.callout.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.arcot.apps.logger.Logger;
import com.arcot.logger.ArcotLogger;

public class TestHttpServer extends HttpServlet {
	
	public static final String RESPONSE_FILE_PATH = "RESPONSE_FILE_PATH";
	public static final String LOG_FILE_PATH = "TestHTTPLogFilePath";
	
	private Logger logger = null; 
	
	public void init(ServletConfig conf) throws ServletException{
		super.init(conf);
		String logFilePath = this.getInitParameter(LOG_FILE_PATH);
		if(logFilePath == null)
			logFilePath = "testhttpserver";
		//create log file
		try {
			if(logFilePath.indexOf("/") > -1){
				logger = new Logger(logFilePath, true);
			}else{
				logger = new Logger(logFilePath, false);
			}
		} catch (IOException e) {
			e.printStackTrace();
			ArcotLogger.logError("HTTPTestServer::Failed to initialize logger due to IOException, it might affect the test functionality. " +
					"PLEASE CHECK THE SYSTEM CONFIGURATIONS", e);
		} catch (Exception e) {
			e.printStackTrace();
			ArcotLogger.logError("HTTPTestServer::Failed to initialize acscallout logger, it might affect the test functionality. " +
					"PLEASE CHECK THE SYSTEM CONFIGURATIONS", e);
		}
		return;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * This server stub expects the request file to contain response as:
	 * <Response>
	 * Response can be any free flowing string/xml/key as expected by the callout from a backend
	 * ResponseFilePath needs to be send as the request parameter (can be removed later when stub testing is over).
	 * Servlet does not need a restart on changing the response file as file is read for every request
	 * Lines starting with # are considered comments and will not be returned
	 * Logs are written to system log
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String responseFilePath = request.getParameter(RESPONSE_FILE_PATH);
		if(responseFilePath == null){
			logger.log("Request does not contain correct logfile path" + RESPONSE_FILE_PATH);
			sendResponse(response, "Missing response file path");
		}else{
			logger.log("READING response file: " + RESPONSE_FILE_PATH);
		}
		
		BufferedReader br = new BufferedReader(new FileReader(responseFilePath));
		String line = br.readLine();
		StringBuffer resp = new StringBuffer();
		while(line != null){
			if(line.startsWith("#")){
				//This is a comment line, skip
			}else{
				resp.append(line);
			}
			line = br.readLine();
		}
		br.close();
		logger.log("sending res[" + resp.toString() + "]\n");
		sendResponse(response, resp.toString());
	}
	
	private void sendResponse(HttpServletResponse response, String data) throws IOException{
		OutputStream os = response.getOutputStream();
		os.write(data.getBytes());
		os.flush();
		return;
	}
}
