package com.arcot.apps.callout.acs.extension.indiaivr;

public class VEReqExtension extends IndiaIVRExtension {
	
	String phoneIdFormat = null;
	String phoneId = null;
	String paReqChannel = null;
	String shopChannel = null;
	String availableAuthChannel = null;
	String iTPCredential = null;
	
	public String getPhoneIdFormat() {
		return phoneIdFormat;
	}
	public void setPhoneIdFormat(String phoneIdFormat) {
		this.phoneIdFormat = phoneIdFormat;
	}
	public String getPhoneId() {
		return phoneId;
	}
	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}
	public String getPaReqChannel() {
		return paReqChannel;
	}
	public void setPaReqChannel(String paReqChannel) {
		this.paReqChannel = paReqChannel;
	}
	public String getShopChannel() {
		return shopChannel;
	}
	public void setShopChannel(String shopChannel) {
		this.shopChannel = shopChannel;
	}
	public String getAvailableAuthChannel() {
		return availableAuthChannel;
	}
	public void setAvailableAuthChannel(String availableAuthChannel) {
		this.availableAuthChannel = availableAuthChannel;
	}
	public String getITPCredential() {
		return iTPCredential;
	}
	public void setITPCredential(String credential) {
		iTPCredential = credential;
	}
}
