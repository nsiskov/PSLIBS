package com.arcot.apps.jni;


public class Connection {
	
	static {
    	System.loadLibrary("ArcotConnectionJNI");
	}
	public static native String TwoWaySSLConnection(String requestData, String CACert, String issuerCert, String url, String privateKey, int connTimeout, int respTimeout);
	
	// added for CTVS, as the web service expects SOAPAction to be passed as "" in the header
	public static native String TwoWaySSLConnectionSOAPAction(String requestData, String CACert, String issuerCert, String privateKey, String url, int connTimeout, int respTimeout, String soapAction);
	
	// http header can be passed in now
	// the headers should be ~ separated
	// SOAPAction=XXXXX~Content-Type=text/xml; charset=XXXX~........
	public static native String TwoWaySSLConnectionHttpHeaders(String requestData, String CACert, String issuerCert, String privateKey, String url, int connTimeout, int respTimeout, String httpHeaders);

}
