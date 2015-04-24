package com.arcot.apps.https;

import java.util.StringTokenizer;

public class ConnectionProperties{

	private String url = null;
	/**
	 * can be a certificate or JKS
	 */
	private String certFilePath = null;
	private String clientKeyCertPath = null;
	private String keyFilePassPhrase = "";
	private boolean additionalParams = false;
	/**
	 * Passphrase to access the truststore in case its a JKS bundle
	 */
	private String trustFilePassPhrase = null;
	private String requestMethod = "POST";
	private String pairSeparator = ";";
	private String disableServerCertCheck = null;
	private String query = null;
	private boolean isSSL = false;
	public static final String URL = "URL";
	public static final String CERT_FILE_PATH = "CERT_FILE_PATH";
	public static final String CLIENT_KEY_CERT_PATH = "CLIENT_KEY_CERT_PATH";
	public static final String KEY_FILE_PASS_PHRASE = "KEY_FILE_PASS_PHRASE";
	public static final String TRUST_FILE_PASS_PHRASE = "TRUST_FILE_PASS_PHRASE";
	public static final String REQUEST_METHOD = "REQUEST_METHOD";
	public static final String DISABLE_SERVER_CERT_CHECKING = "DISABLE_SERVER_CERT_CHECKING";
	public static final String CONFIGURE_ADDITIONAL_PARAMS = "CONFIGURE_ADDITIONAL_PARAMS";

	public ConnectionProperties(){

	}

	public ConnectionProperties(String infolist){
		setParamsFromInfoList(infolist, pairSeparator);
	}

	public ConnectionProperties(String infolist, String pairSeparator){
		this.pairSeparator = pairSeparator;
		setParamsFromInfoList(infolist, pairSeparator);
	}

	/**
	 * @return Returns the certFilePath.
	 */
	public String getCertFilePath() {
		return certFilePath;
	}

	/**
	 * @param certFilePath The certFilePath to set.
	 */
	public void setCertFilePath(String certFilePath) {
		this.certFilePath = certFilePath;
	}

	/**
	 * @return Returns the clientKeyCertPath.
	 */
	public String getClientKeyCertPath() {
		return clientKeyCertPath;
	}

	/**
	 * @param clientKeyCertPath The clientKeyCertPath to set.
	 */
	public void setClientKeyCertPath(String clientKeyCertPath) {
		this.clientKeyCertPath = clientKeyCertPath;
	}

	/**
	 * @return Returns the keyFilePassPhrase.
	 */
	public String getKeyFilePassPhrase() {
		return keyFilePassPhrase;
	}

	/**
	 * @param keyFilePassPhrase The keyFilePassPhrase to set.
	 */
	public void setKeyFilePassPhrase(String keyFilePassPhrase) {
		this.keyFilePassPhrase = keyFilePassPhrase;
	}

	/**
	 * @return Returns the requestMethod.
	 */
	public String getRequestMethod() {
		return requestMethod;
	}

	/**
	 * @param requestMethod The requestMethod to set.
	 */
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
		if(url != null && url.length() > 5)
			if("https".equalsIgnoreCase(url.substring(0,5)))
				isSSL = true;
			else
				isSSL = false;
	}

	public boolean isSSL() {
		return isSSL;
	}

	public String toString(){
		StringBuffer buf = new StringBuffer("ConnectionProperties \n" + URL + ": " + this.url + "\n");
		buf.append(CERT_FILE_PATH + ": " + this.certFilePath + "\n");
		buf.append(CLIENT_KEY_CERT_PATH + ": " + this.clientKeyCertPath + "\n");
		buf.append(KEY_FILE_PASS_PHRASE + ": " + this.keyFilePassPhrase + "\n");
		if (this.trustFilePassPhrase != null){
			buf.append(TRUST_FILE_PASS_PHRASE + ": " + this.trustFilePassPhrase + "\n");
		}
		buf.append(REQUEST_METHOD + ": " + this.requestMethod + "\n");
		buf.append(CONFIGURE_ADDITIONAL_PARAMS + ": " + this.isAdditionalParams() + "\n");
		return buf.toString();
	}

	private void setParamsFromInfoList(String infoList, String separator){
		if(infoList == null)
			return;
		StringTokenizer stk = new StringTokenizer(infoList, separator);
		String key = null;
		StringTokenizer stkPair = null;
		while(stk.hasMoreTokens()){
			stkPair = new StringTokenizer(stk.nextToken(), "=");
			while(stkPair.hasMoreTokens()){
				key = stkPair.nextToken().trim();
				if(URL.equalsIgnoreCase(key)){
					setUrl(stkPair.nextToken().trim());
				}else if(CERT_FILE_PATH.equalsIgnoreCase(key)){
					setCertFilePath(stkPair.nextToken().trim());
				}else if(CLIENT_KEY_CERT_PATH.equalsIgnoreCase(key)){
					setClientKeyCertPath(stkPair.nextToken().trim());
				}else if(KEY_FILE_PASS_PHRASE.equalsIgnoreCase(key)){
					setKeyFilePassPhrase(stkPair.nextToken().trim());
				}else if(REQUEST_METHOD.equalsIgnoreCase(key)){
					setRequestMethod(stkPair.nextToken().trim());
				}else if(DISABLE_SERVER_CERT_CHECKING.equalsIgnoreCase(key)){
					setDisableServerCertCheck(stkPair.nextToken().trim());
				}else if(TRUST_FILE_PASS_PHRASE.equalsIgnoreCase(key)){
					setTrustFilePassPhrase(stkPair.nextToken().trim());
				}else if(CONFIGURE_ADDITIONAL_PARAMS.equalsIgnoreCase(key)){
					setAdditionalParams(this.isAdditionalParams());
				}
			}
		}
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setDisableServerCertCheck(String disableServerCertCheck) {
		this.disableServerCertCheck = disableServerCertCheck;
	}

	public String getDisableServerCertCheck() {
		return disableServerCertCheck;
	}

	public String getTrustFilePassPhrase() {
		return trustFilePassPhrase;
	}

	public void setTrustFilePassPhrase(String trustFilePassPhrase) {
		this.trustFilePassPhrase = trustFilePassPhrase;
	}

	public void setAdditionalParams(boolean additionalParams) {
		this.additionalParams = additionalParams;
	}

	public boolean isAdditionalParams() {
		return additionalParams;
	}

}
