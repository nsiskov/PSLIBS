package com.arcot.apps.callout.acs;

import com.arcot.apps.callout.acs.extension.indiaivr.VEReqExtension;

public class ACSVERequest {
	
	private String version = null;
	private String cardNumber = null;
	private String acquirerBin = null;
	private String merchantId = null;
	private String deviceCategory = null;
	private String accept = null;
	private String userAgent = null;
	
	private VEReqExtension vEReqExtension = null;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getAcquirerBin() {
		return acquirerBin;
	}
	public void setAcquirerBin(String acquirerBin) {
		this.acquirerBin = acquirerBin;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getDeviceCategory() {
		return deviceCategory;
	}
	public void setDeviceCategory(String deviceCategory) {
		this.deviceCategory = deviceCategory;
	}
	public String getAccept() {
		return accept;
	}
	public void setAccept(String accept) {
		this.accept = accept;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public VEReqExtension getVEReqExtension() {
		return vEReqExtension;
	}
	public void setVEReqExtension(VEReqExtension veReqExtension) {
		this.vEReqExtension = veReqExtension;
	}
}
