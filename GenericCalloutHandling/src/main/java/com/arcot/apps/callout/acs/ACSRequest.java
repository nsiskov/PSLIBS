package com.arcot.apps.callout.acs;

import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.util.Utility;

/*
 * This is superset bean for all kind of ACS requests
 * */
public class ACSRequest extends CORequest implements ACS {

	int calloutConfigId = -1;
	String chIP = null;
	/*
	 * CHProfile is pin value as constructed on multipwdbase.htm ACS has its own
	 * format mandated: <pin>,CHName=<chname>,ShopperId=<shopperid> Leave it on
	 * BusinessLogicHandler to have the same format or define a new format for
	 * the particular customer
	 */
	String cHProfile = null;
	String email = null;
	String encPAN = null;
	// Can only be one hintq
	String hint = null;
	// Can only be one hinta
	String hintAnswer = null;
	/*
	 * Isssuer Answer is pin value as constructed on optin.htm This is
	 * particular to each CAP folder/customer Leave it on BusinessLogicHandler
	 * to format for the particular customer
	 */
	String issuerAnswer = null;
	String messageId = null;
	String messageType = null; // VerifyPasswordReq etc
	String newName = null;
	int numFailuresInSession = -1;
	int numTotalFailures = -1;
	String vEReq = null;
	String pAReq = null;
	String pARes = null;
	String password = null;
	String pin = null;
	String proxyPan = null;
	// RiskFort integration parameters
	int riskAdvice = 0;
	String secAuthPin = null;
	String status = null;
	String timestamp = null;
	String tXid = null;
	String type = null;
	String version = null;
	String userId = null;

	int isAbridged = -1;
	String slotInfo = null;
	String tXType = null;
	int intTXType = -1;

	String httpUAId = null;
	String folderId = null;

	String calloutData = null;
	String txProxyPan = null;
	String matchedRuleMnemonic = null;
	String fypRAMatchedRuleMnemonic = null;
	int fypRARiskAdvice = 0;
	int autoFYP = 0;
	int fypRAIncAuthAction = 0;

	// Addding for the ArcotID OTP feature support
	// Only used in PostAuthReq callout
	String activationCode = null;
	String activationURL = null;
	String cardHolderID = null;

	public String getMatchedRuleMnemonic() {
		return matchedRuleMnemonic;
	}

	public void setMatchedRuleMnemonic(String matchedRuleMnemonic) {
		this.matchedRuleMnemonic = matchedRuleMnemonic;
	}

	public String getFypRAMatchedRuleMnemonic() {
		return fypRAMatchedRuleMnemonic;
	}

	public void setFypRAMatchedRuleMnemonic(String fypRAMatchedRuleMnemonic) {
		this.fypRAMatchedRuleMnemonic = fypRAMatchedRuleMnemonic;
	}

	public int getFypRARiskAdvice() {
		return fypRARiskAdvice;
	}

	public void setFypRARiskAdvice(int fypRARiskAdvice) {
		this.fypRARiskAdvice = fypRARiskAdvice;
	}

	public int getAutoFYP() {
		return autoFYP;
	}

	public void setAutoFYP(int autoFYP) {
		this.autoFYP = autoFYP;
	}

	public int getFypRAIncAuthAction() {
		return fypRAIncAuthAction;
	}

	public void setFypRAIncAuthAction(int fypRAIncAuthAction) {
		this.fypRAIncAuthAction = fypRAIncAuthAction;
	}

	public String getTxProxyPan() {
		return txProxyPan;
	}

	public void setTxProxyPan(String txProxyPan) {
		this.txProxyPan = txProxyPan;
	}

	public String getDbProxyPan() {
		return dbProxyPan;
	}

	public void setDbProxyPan(String dbProxyPan) {
		this.dbProxyPan = dbProxyPan;
	}

	String dbProxyPan = null;

	public String getCalloutData() {
		return calloutData;
	}

	public void setCalloutData(String calloutData) {
		this.calloutData = calloutData;
	}

	// used in PrePares Callout
	// Required in case of "Banque Laurentienne du Canada"
	/**
	 * enum ACSUserAction { USER_ACTION_DEF, USER_ACTION_OPTIN,
	 * USER_ACTION_DECLINE, USER_ACTION_CANCEL, USER_ACTION_PURCH,
	 * USER_ACTION_OPTIN_CANCEL, USER_ACTION_REENROLL };
	 */
	
	String isReplayPareq = null;
	int userAction = -1;

	public void setIsReplayPareq(String isReplayPareq) {
		this.isReplayPareq = isReplayPareq;
	}

	public String getIsReplayPareq() {
		return this.isReplayPareq;
	}

	public int getUserAction() {
		return userAction;
	}

	public void setUserAction(int userAction) {
		this.userAction = userAction;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return Returns the calloutConfigId.
	 */
	public int getCalloutConfigId() {
		return calloutConfigId;
	}

	public String getChIP() {
		return chIP;
	}

	/**
	 * @return Returns the cHProfile.
	 */
	public String getCHProfile() {
		return cHProfile;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return Returns the encPAN.
	 */
	public String getEncPAN() {
		return encPAN;
	}

	/**
	 * @return Returns the hint.
	 */
	public String getHint() {
		return hint;
	}

	/**
	 * @return Returns the hintAnswer.
	 */
	public String getHintAnswer() {
		return hintAnswer;
	}

	/**
	 * @return Returns the issuerAnswer.
	 */
	public String getIssuerAnswer() {
		return issuerAnswer;
	}

	/**
	 * @return Returns the messageId.
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @return Returns the messageType.
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @return Returns the newName.
	 */
	public String getNewName() {
		return newName;
	}

	/**
	 * @return Returns the numFailuresInSession.
	 */
	public int getNumFailuresInSession() {
		return numFailuresInSession;
	}

	/**
	 * @return Returns the numTotalFailures.
	 */
	public int getNumTotalFailures() {
		return numTotalFailures;
	}

	/**
	 * @return Returns the pAReq.
	 */
	public String getPAReq() {
		return pAReq;
	}

	/**
	 * @return Returns the pARes.
	 */
	public String getPARes() {
		return pARes;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return Returns the pin.
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * @return Returns the proxyPan.
	 */
	public String getProxyPan() {
		return proxyPan;
	}

	/**
	 * @return Returns the rangeId.
	 */
	public int getRangeId() {
		return rangeId;
	}

	public int getRiskAdvice() {
		return riskAdvice;
	}

	public String getSecAuthPin() {
		return secAuthPin;
	}

	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return Returns the timestamp.
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @return Returns the tXid.
	 */
	public String getTXid() {
		return tXid;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param calloutConfigId
	 *            The calloutConfigId to set.
	 */
	public void setCalloutConfigId(int calloutConfigId) {
		this.calloutConfigId = calloutConfigId;
	}

	public void setChIP(String chIP) {
		this.chIP = chIP;
	}

	/**
	 * @param profile
	 *            The cHProfile to set.
	 */
	public void setCHProfile(String profile) {
		cHProfile = profile;
	}

	/**
	 * @param email
	 *            The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param encPAN
	 *            The encPAN to set.
	 */
	public void setEncPAN(String encPAN) {
		this.encPAN = encPAN;
	}

	/**
	 * @param hint
	 *            The hint to set.
	 */
	public void setHint(String hint) {
		this.hint = hint;
	}

	/**
	 * @param hintAnswer
	 *            The hintAnswer to set.
	 */
	public void setHintAnswer(String hintAnswer) {
		this.hintAnswer = hintAnswer;
	}

	/**
	 * @param issuerAnswer
	 *            The issuerAnswer to set.
	 */
	public void setIssuerAnswer(String issuerAnswer) {
		this.issuerAnswer = issuerAnswer;
	}

	/**
	 * @param messageId
	 *            The messageId to set.
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @param messageType
	 *            The messageType to set.
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @param newName
	 *            The newName to set.
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}

	/**
	 * @param numFailuresInSession
	 *            The numFailuresInSession to set.
	 */
	public void setNumFailuresInSession(int numFailuresInSession) {
		this.numFailuresInSession = numFailuresInSession;
	}

	/**
	 * @param numTotalFailures
	 *            The numTotalFailures to set.
	 */
	public void setNumTotalFailures(int numTotalFailures) {
		this.numTotalFailures = numTotalFailures;
	}

	/**
	 * @param req
	 *            The pAReq to set.
	 */
	public void setPAReq(String req) {
		pAReq = req;
	}

	/**
	 * @param res
	 *            The pARes to set.
	 */
	public void setPARes(String res) {
		pARes = res;
	}

	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param pin
	 *            The pin to set.
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * @param proxyPan
	 *            The proxyPan to set.
	 */
	public void setProxyPan(String proxyPan) {
		this.proxyPan = proxyPan;
	}

	/**
	 * @param rangeId
	 *            The rangeId to set.
	 */
	public void setRangeId(int rangeId) {
		this.rangeId = rangeId;
	}

	public void setRiskAdvice(int riskAdvice) {
		this.riskAdvice = riskAdvice;
	}

	public void setSecAuthPin(String secAuthPin) {
		this.secAuthPin = secAuthPin;
	}

	/**
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param timestamp
	 *            The timestamp to set.
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param xid
	 *            The tXid to set.
	 */
	public void setTXid(String xid) {
		tXid = xid;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	public String toString() {
		StringBuffer myString = new StringBuffer(
				"[BID, RID, CCID, Type, Time, PAN, TXNPAN]=[");
		myString.append(bankId);
		myString.append(", ");
		myString.append(rangeId);
		myString.append(", ");
		myString.append(calloutConfigId);
		myString.append(", ");
		myString.append(messageType);
		myString.append(", ");
		myString.append(timestamp);
		myString.append(", ");
		myString.append(Utility.maskPan(cardNumber));
		myString.append(", ");
		myString.append(txProxyPan);
		myString.append("]");
		return myString.toString();
	}

	public int getIsAbridged() {
		return isAbridged;
	}

	public void setIsAbridged(int isAbridged) {
		this.isAbridged = isAbridged;
	}

	public String getSlotInfo() {
		return slotInfo;
	}

	public void setSlotInfo(String slotInfo) {
		this.slotInfo = slotInfo;
	}

	public String getTXType() {
		return tXType;
	}

	public void setTXType(String type) {
		tXType = type;
	}

	public int getIntTXType() {
		return intTXType;
	}

	public void setIntTXType(int intTXType) {
		this.intTXType = intTXType;
	}

	public String getHttpUAId() {
		return httpUAId;
	}

	public void setHttpUAId(String httpUAId) {
		this.httpUAId = httpUAId;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getVEReq() {
		return vEReq;
	}

	public void setVEReq(String req) {
		vEReq = req;
	}

	/**
	 * @return the activationCode
	 */
	public String getActivationCode() {
		return activationCode;
	}

	/**
	 * @param activationCode
	 *            the activationCode to set
	 */
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	/**
	 * @return the activationURL
	 */
	public String getActivationURL() {
		return activationURL;
	}

	/**
	 * @param activationURL
	 *            the activationURL to set
	 */
	public void setActivationURL(String activationURL) {
		this.activationURL = activationURL;
	}

	/**
	 * @return the cardHolderID
	 */
	public String getCardHolderID() {
		return cardHolderID;
	}

	/**
	 * @param cardHolderID
	 *            the cardHolderID to set
	 */
	public void setCardHolderID(String cardHolderID) {
		this.cardHolderID = cardHolderID;
	}

}