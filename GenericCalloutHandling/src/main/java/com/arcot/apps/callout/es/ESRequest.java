package com.arcot.apps.callout.es;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.util.Utility;
import com.arcot.vpas.enroll.RangeIssuerQA;
import com.arcot.vpas.enroll.UserFields;

public class ESRequest extends CORequest{
	private String infolist = null;
	private String tempPassword = null;
	private String pin1 = null;
	private String pin2 = null;
	private String passwordHint = null;
	private String hintQuestion = null;
	private String hintAnswer1 = null;
	private String hintAnswer2 = null;
	private String shooperId = null;
	/*
	 * Including RangeIssuerQA as a part of ES Request
	 * Try using that instead of this as we will get
	 * mandatory and all other data because of that.
	 * */
	private ArrayList questions = null;
	private ArrayList answers = null;
	private ArrayList questionids = null;
	private RangeIssuerQA rangeIssuerQA = null;
	private boolean isabridged = false;
	private boolean isreset = false;
	private boolean ismini = false;
	private boolean isforgotpwd = false;
	private boolean isautofyp = false;
	private int calloutEvent = -1;
	private String url = null;
	private int conntimeout = 0;
	private int resptimeout = 0;
	private int conntries = 0;
	private byte[] encryptionCertFile = null;
	private String clientCertPath = null;
	private String rootCertPath = null;
	private String sSLVersion = null;
	private HttpSession session = null;
	private int numAuthFailures = 0;
	
	private HashMap userFeildsMap = null;
	private UserFields userFields = null;
	
	private String eMailAddr= null;
	private String oldEMailAddr= null;
	private String userId = null;
	private String password = null;
	private String oldMobileNumber = null;
	private String newMobileNumber = null;

	//[1] to pass on the extraAuthParams map from input hashmap to the ESRequest
	private Map<String, String> extraAuthParams;
	
	//[2] to hold the ATOP provisioning details 
	private String aotpActivationURL;
	private String aotpActivationCode;
	private String aotpCardHolderId;
	private String aotpStatus;
	

	public String getOldEMailAddr() {
		return oldEMailAddr;
	}
	public void setOldEMailAddr(String oldEMailAddr) {
		this.oldEMailAddr = oldEMailAddr;
	}
	public String getEMailAddr() {
		return eMailAddr;
	}
	public void setEMailAddr(String mailAddr) {
		eMailAddr = mailAddr;
	}
	public String getUserFeildsMap(String name) {
		String valAttMap = null;
		valAttMap = (String)this.userFeildsMap.get(name);
		return valAttMap;
	}
	public void setUserFeildsMap(HashMap userFeildsMap) {
		this.userFeildsMap = userFeildsMap;
	}
	
	/*
	 * Comments in ESCallout 
	 */
	
	
	/**
	 * @return Returns the answers.
	 */
	public ArrayList getAnswers() {
		return answers;
	}
	/**
	 * @param answers The answers to set.
	 */
	public void setAnswers(ArrayList answers) {
		this.answers = answers;
	}
	/**
	 * @return Returns the calloutEvent.
	 */
	public int getCalloutEvent() {
		return calloutEvent;
	}
	/**
	 * @param calloutEvent The calloutEvent to set.
	 */
	public void setCalloutEvent(int calloutEvent) {
		this.calloutEvent = calloutEvent;
	}
	/**
	 * @return Returns the clientCertPath.
	 */
	public String getClientCertPath() {
		return clientCertPath;
	}
	/**
	 * @param clientCertPath The clientCertPath to set.
	 */
	public void setClientCertPath(String clientCertPath) {
		this.clientCertPath = clientCertPath;
	}
	/**
	 * @return Returns the conntimeout.
	 */
	public int getConntimeout() {
		return conntimeout;
	}
	/**
	 * @param conntimeout The conntimeout to set.
	 */
	public void setConntimeout(int conntimeout) {
		this.conntimeout = conntimeout;
	}
	/**
	 * @return Returns the conntries.
	 */
	public int getConntries() {
		return conntries;
	}
	/**
	 * @param conntries The conntries to set.
	 */
	public void setConntries(int conntries) {
		this.conntries = conntries;
	}
	/**
	 * @return Returns the encryptionCertFile.
	 */
	public byte[] getEncryptionCertFile() {
		return encryptionCertFile;
	}
	/**
	 * @param encryptionCertFile The encryptionCertFile to set.
	 */
	public void setEncryptionCertFile(byte[] encryptionCertFile) {
		this.encryptionCertFile = encryptionCertFile;
	}
	/**
	 * @return Returns the hintAnswer1.
	 */
	public String getHintAnswer1() {
		return hintAnswer1;
	}
	/**
	 * @param hintAnswer1 The hintAnswer1 to set.
	 */
	public void setHintAnswer1(String hintAnswer1) {
		this.hintAnswer1 = hintAnswer1;
	}
	/**
	 * @return Returns the hintAnswer2.
	 */
	public String getHintAnswer2() {
		return hintAnswer2;
	}
	/**
	 * @param hintAnswer2 The hintAnswer2 to set.
	 */
	public void setHintAnswer2(String hintAnswer2) {
		this.hintAnswer2 = hintAnswer2;
	}
	/**
	 * @return Returns the hintQuestion.
	 */
	public String getHintQuestion() {
		return hintQuestion;
	}
	/**
	 * @param hintQuestion The hintQuestion to set.
	 */
	public void setHintQuestion(String hintQuestion) {
		this.hintQuestion = hintQuestion;
	}
	/**
	 * @return Returns the infolist.
	 */
	public String getInfolist() {
		return infolist;
	}
	/**
	 * @param infolist The infolist to set.
	 */
	public void setInfolist(String infolist) {
		this.infolist = infolist;
	}
	/**
	 * @return Returns the isabridged.
	 */
	public boolean isIsabridged() {
		return isabridged;
	}
	/**
	 * @param isabridged The isabridged to set.
	 */
	public void setIsabridged(boolean isabridged) {
		this.isabridged = isabridged;
	}
	/**
	 * @return Returns the isforgotpwd.
	 */
	public boolean isIsforgotpwd() {
		return isforgotpwd;
	}
	/**
	 * @param isforgotpwd The isforgotpwd to set.
	 */
	public void setIsforgotpwd(boolean isforgotpwd) {
		this.isforgotpwd = isforgotpwd;
	}
	/**
	 * @return Returns the ismini.
	 */
	public boolean isIsmini() {
		return ismini;
	}
	/**
	 * @param ismini The ismini to set.
	 */
	public void setIsmini(boolean ismini) {
		this.ismini = ismini;
	}
	/**
	 * @return Returns the isreset.
	 */
	public boolean isIsreset() {
		return isreset;
	}
	/**
	 * @param isreset The isreset to set.
	 */
	public void setIsreset(boolean isreset) {
		this.isreset = isreset;
	}
	/**
	 * @return Returns the passwordHint.
	 */
	public String getPasswordHint() {
		return passwordHint;
	}
	/**
	 * @param passwordHint The passwordHint to set.
	 */
	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}
	/**
	 * @return Returns the pin1.
	 */
	public String getPin1() {
		return pin1;
	}
	/**
	 * @param pin1 The pin1 to set.
	 */
	public void setPin1(String pin1) {
		this.pin1 = pin1;
	}
	/**
	 * @return Returns the pin2.
	 */
	public String getPin2() {
		return pin2;
	}
	/**
	 * @param pin2 The pin2 to set.
	 */
	public void setPin2(String pin2) {
		this.pin2 = pin2;
	}
	/**
	 * @return Returns the questionids.
	 */
	public ArrayList getQuestionids() {
		return questionids;
	}
	/**
	 * @param questionids The questionids to set.
	 */
	public void setQuestionids(ArrayList questionids) {
		this.questionids = questionids;
	}
	/**
	 * @return Returns the questions.
	 */
	public ArrayList getQuestions() {
		return questions;
	}
	/**
	 * @param questions The questions to set.
	 */
	public void setQuestions(ArrayList questions) {
		this.questions = questions;
	}
	/**
	 * @return Returns the resptimeout.
	 */
	public int getResptimeout() {
		return resptimeout;
	}
	/**
	 * @param resptimeout The resptimeout to set.
	 */
	public void setResptimeout(int resptimeout) {
		this.resptimeout = resptimeout;
	}
	/**
	 * @return Returns the rootCertPath.
	 */
	public String getRootCertPath() {
		return rootCertPath;
	}
	/**
	 * @param rootCertPath The rootCertPath to set.
	 */
	public void setRootCertPath(String rootCertPath) {
		this.rootCertPath = rootCertPath;
	}
	/**
	 * @return Returns the shooperId.
	 */
	public String getShooperId() {
		return shooperId;
	}
	/**
	 * @param shooperId The shooperId to set.
	 */
	public void setShooperId(String shooperId) {
		this.shooperId = shooperId;
	}
	/**
	 * @return Returns the sSLVersion.
	 */
	public String getSSLVersion() {
		return sSLVersion;
	}
	/**
	 * @param version The sSLVersion to set.
	 */
	public void setSSLVersion(String version) {
		sSLVersion = version;
	}
	/**
	 * @return Returns the tempPassword.
	 */
	public String getTempPassword() {
		return tempPassword;
	}
	/**
	 * @param tempPassword The tempPassword to set.
	 */
	public void setTempPassword(String tempPassword) {
		this.tempPassword = tempPassword;
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
	}
	/**
	 * @return Returns the session.
	 */
	public HttpSession getSession() {
		return session;
	}
	/**
	 * @param session The session to set.
	 */
	public void setSession(HttpSession session) {
		this.session = session;
	}
	/**
	 * @return Returns the numAuthFailures.
	 */
	public int getNumAuthFailures() {
		return numAuthFailures;
	}
	/**
	 * @param numAuthFailures The numAuthFailures to set.
	 */
	public void setNumAuthFailures(int numAuthFailures) {
		this.numAuthFailures = numAuthFailures;
	}
	
	public RangeIssuerQA getRangeIssuerQA() {
		return rangeIssuerQA;
	}
	public void setRangeIssuerQA(RangeIssuerQA rangeIssuerQA) {
		this.rangeIssuerQA = rangeIssuerQA;
	}
	
	public String toString(){
		StringBuffer myString = new StringBuffer("[BID, RID, CID, PAN]=[");
		myString.append(bankId);
		myString.append(", ");
		myString.append(rangeId);
		myString.append(", ");
		myString.append(calloutEvent);
		myString.append(", ");
		myString.append(Utility.maskPan(cardNumber));
		myString.append("]");
		return myString.toString();
	}
	public boolean isIsautofyp() {
		return isautofyp;
	}
	public void setIsautofyp(boolean isautofyp) {
		this.isautofyp = isautofyp;
	}
	public UserFields getUserFields() {
		return userFields;
	}
	public void setUserFields(UserFields userFields) {
		this.userFields = userFields;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public HashMap getUserFeildsMap() {
		return userFeildsMap;
	}
	public void setOldMobileNumber(String oldMobileNumber) {
		this.oldMobileNumber = oldMobileNumber;
	}
	public String getOldMobileNumber() {
		return oldMobileNumber;
	}
	public void setNewMobileNumber(String newMobileNumber) {
		this.newMobileNumber = newMobileNumber;
	}
	public String getNewMobileNumber() {
		return newMobileNumber;
	}
	
	public Map<String, String> getExtraAuthParams() {
		return this.extraAuthParams;
	}
	public void setExtraAuthParams(Map<String, String> extraAuthParams) {
		this.extraAuthParams = extraAuthParams;
	}
	
	public String getAotpActivationURL() {
		return aotpActivationURL;
	}
	public void setAotpActivationURL(String aotpActivationURL) {
		this.aotpActivationURL = aotpActivationURL;
	}
	public String getAotpActivationCode() {
		return aotpActivationCode;
	}
	public void setAotpActivationCode(String aotpActivationCode) {
		this.aotpActivationCode = aotpActivationCode;
	}
	public String getAotpCardHolderId() {
		return aotpCardHolderId;
	}
	public void setAotpCardHolderId(String aotpCardHolderId) {
		this.aotpCardHolderId = aotpCardHolderId;
	}
	public String getAotpStatus() {
		return aotpStatus;
	}
	public void setAotpStatus(String aotpStatus) {
		this.aotpStatus = aotpStatus;
	}
}
