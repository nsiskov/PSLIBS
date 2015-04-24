package com.arcot.apps.callout.acs;

import com.arcot.apps.callout.acs.extension.ACSExtension;
import com.arcot.apps.callout.common.COResponse;

/*
 * This is superset bean for all kind of ACS responses
 * */
public class ACSResponse  extends COResponse implements ACS{
	String messageId = null;//
	String messageType = null; //VerifyPasswordRes etc//
	String version = null;//
	String matchedName = null;
	String pWDStatus = null;
	String answersStatus = null;
	String blockCH = null;
	String newName = null;
	String updateStatus = null;
	String encPan = null;
	String proxyPan = null;
	//String chTag = null;
	String enrolled = null;
	String capURL = null;
	String protocol = "ThreeDSecure";
	ACSExtension extension = null;
	
	/**
	 * @return Returns the answersStatus.
	 */
	public String getAnswersStatus() {
		return answersStatus;
	}
	/**
	 * @param answersStatus The answersStatus to set.
	 */
	public void setAnswersStatus(String answersStatus) {
		this.answersStatus = answersStatus;
	}
	/**
	 * @return Returns the blockCH.
	 */
	public String getBlockCH() {
		return blockCH;
	}
	/**
	 * @param blockCH The blockCH to set.
	 */
	public void setBlockCH(String blockCH) {
		this.blockCH = blockCH;
	}
	/**
	 * @return Returns the matchedName.
	 */
	public String getMatchedName() {
		return matchedName;
	}
	/**
	 * @param matchedName The matchedName to set.
	 */
	public void setMatchedName(String matchedName) {
		this.matchedName = matchedName;
	}
	/**
	 * @return Returns the messageId.
	 */
	public String getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId The messageId to set.
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return Returns the messageType.
	 */
	public String getMessageType() {
		return messageType;
	}
	/**
	 * @param messageType The messageType to set.
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	/**
	 * @return Returns the newName.
	 */
	public String getNewName() {
		return newName;
	}
	/**
	 * @param newName The newName to set.
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}
	/**
	 * @return Returns the pWDStatus.
	 */
	public String getPWDStatus() {
		return pWDStatus;
	}
	/**
	 * @param status The pWDStatus to set.
	 */
	public void setPWDStatus(String status) {
		pWDStatus = status;
	}
	/**
	 * @return Returns the updateStatus.
	 */
	public String getUpdateStatus() {
		return updateStatus;
	}
	/**
	 * @param updateStatus The updateStatus to set.
	 */
	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String toString(){
		StringBuffer myString = new StringBuffer("[ID, TYPE, VER, PWDSTAT, ANSTAT, UPDSTAT, CRC, LOCKSTAT]=[");
		myString.append(messageId);
		myString.append(", ");
		myString.append(messageType);
		myString.append(", ");
		myString.append(version);
		myString.append(", ");
		myString.append(pWDStatus);
		myString.append(", ");
		myString.append(answersStatus);
		myString.append(", ");
		myString.append(updateStatus);
		myString.append(",");
		myString.append(acsCRC);
		myString.append(",");
		myString.append(getLockStatus());
		myString.append("]");
		return myString.toString();
	}
	/**
	 * @return Returns the enrolled.
	 */
	public String getEnrolled() {
		return enrolled;
	}
	/**
	 * @param enrolled The enrolled to set.
	 */
	public void setEnrolled(String enrolled) {
		this.enrolled = enrolled;
	}
	/**
	 * @return Returns the encPan.
	 */
	public String getEncPan() {
		return encPan;
	}
	/**
	 * @param encPan The encPan to set.
	 */
	public void setEncPan(String encPan) {
		this.encPan = encPan;
	}
	/**
	 * @return Returns the proxyPan.
	 */
	public String getProxyPan() {
		return proxyPan;
	}
	/**
	 * @param proxyPan The proxyPan to set.
	 */
	public void setProxyPan(String proxyPan) {
		this.proxyPan = proxyPan;
	}
	/**
	 * @return Returns the capURL.
	 */
	public String getCapURL() {
		return capURL;
	}
	/**
	 * @param capURL The capURL to set.
	 */
	public void setCapURL(String capURL) {
		this.capURL = capURL;
	}
	/**
	 * @return Returns the protocol.
	 */
	public String getProtocol() {
		return protocol;
	}
	/**
	 * @param protocol The protocol to set.
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public ACSExtension getExtension() {
		return extension;
	}
	public void setExtension(ACSExtension extension) {
		this.extension = extension;
	}
}
