package com.arcot.apps.https;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

public class HttpsHandler{

	HttpURLConnection connection = null;
	private final int defaultTimeout = 60000;
	boolean additionalParams = false;

	public HttpsHandler(ConnectionProperties conProp, HttpURLConnection connection){
		this.connection = connection;
		this.additionalParams = conProp.isAdditionalParams();
	}

	public String processRequest(String postData) throws IOException{
		return processRequest(postData, defaultTimeout);
	}
	
	/*
	 * Helper method to print connection properties onece the connection is created, used for debugging
	 * */
	public static void printConnectionProperties(HttpURLConnection urlc) throws IOException{
		System.out.println(urlc);
		if(urlc instanceof HttpsURLConnection){
			System.out.println("SocketFactory: " + ((HttpsURLConnection)urlc).getSSLSocketFactory());
		}
		
		System.out.println("Connected to: " + urlc);
		System.out.println("Content Length: " + urlc.getContentLength());
		System.out.println("Response code and msg: " + urlc.getResponseCode() + " : " + urlc.getResponseMessage());
		System.out.println("getAllowUserInteraction: " + urlc.getAllowUserInteraction());
		System.out.println("getDoOutput: " + urlc.getDoOutput());
		System.out.println("getContentEncoding: " + urlc.getContentEncoding());
		System.out.println("usingProxy: " + urlc.usingProxy());
		System.out.println("getRequestMethod: " + urlc.getRequestMethod());
		System.out.println("getURL: " + urlc.getURL());

	}
	
	public String processRequest(String postData, int timeout) throws IOException{
		connection.setConnectTimeout(timeout);
		connection.setReadTimeout(timeout);
		
		System.out.println("HTTPSHandler processRequest " + additionalParams);
		if(additionalParams){
			connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			connection.setRequestProperty("Content-transfer-encoding", "UTF-8");
			connection.setDoOutput(true);
			connection.setDoInput(true);			
		}
		
		OutputStream os = connection.getOutputStream();
		os.write(postData.getBytes());
		os.flush();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		if(br == null){
			br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
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
	
	public static void main(String args[]){

	}

}
