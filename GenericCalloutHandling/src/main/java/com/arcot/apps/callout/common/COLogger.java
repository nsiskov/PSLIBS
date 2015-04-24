package com.arcot.apps.callout.common;

import java.io.IOException;

import com.arcot.apps.logger.Logger;

public class COLogger extends Logger {

	public COLogger(String fileName, boolean pathProvided, String category, String logLevel)throws IOException, Exception{		
		super(fileName,pathProvided,category==null?category:"callout_"+category,logLevel);
	}
	
	public COLogger(String fileName) throws IOException, Exception {
		super(fileName);
	}
	
	public COLogger(String fileName, boolean pathProvided) throws IOException, Exception {		
		this(fileName, pathProvided, null, null);				
	}
	
	@ Deprecated
	/**
	 * Use logger methods with their level i.e. trace, debug, info, warn, error or fatal
	 * @param request
	 * @param msg
	 * @param e
	 */
	public void log(CORequest request, String msg){
		super.log(request.toString() + ":: " + msg);
		return;
	}
	
	@ Deprecated
	/**
	 * Use logger methods with their level i.e. trace, debug, info, warn, error or fatal
	 * @param request
	 * @param msg
	 * @param e
	 */
	public void log(CORequest request, COResponse response){
		super.log(request.toString() + " completed with result " + response);
		return;
	}
	
	@ Deprecated
	/**
	 * Use logger methods with their level i.e. trace, debug, info, warn, error or fatal
	 * @param request
	 * @param msg
	 * @param e
	 */
	public void log(CORequest request, String msg, Exception e){
		super.logException(e, request.toString() + ":: " + msg);
		return;
	}
	
	public void trace(CORequest request, String msg){
		trace(request.toString() + ":: " + msg);
	}
	
	public void debug(CORequest request, String msg){
		debug(request.toString() + ":: " + msg);
		
	}
	
	public void info(CORequest request, String msg){
		info(request.toString() + ":: " + msg);
	}
	
	public void warn(CORequest request, String msg) {
		warn(request.toString() + ":: " + msg);

	}

	/**
	 * 
	 * @param request
	 * @param msg
	 * @param e - This is optional.
	 */
	public void error(CORequest request, String msg, Exception e) {
		error(request.toString() + ":: " + msg, e);

	}

	/**
	 * Please use this with caution as for every fatal log, an email will be
	 * sent out.
	 * 
	 * @param request
	 * @param msg
	 * @param e	- This is optional.
	 */
	public void fatal(CORequest request, String msg, Exception e) {
		fatal(request.toString() + ":: " + msg, e);

	}
}
