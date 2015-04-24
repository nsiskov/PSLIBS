package com.arcot.apps.callout.acs;

import com.arcot.apps.callout.acs.extension.ACSExtension;

public class ACSPAResponse {

	private String version = null;
	private String acquirerBin = null;
	private String merchantId = null;
	
	private String xId = null;
	private String date = null;
	private String amount = null;
	private String currency = null;
	private String exponent = null;
	private String time = null;
	private String status = null;
	private String eCI = null;
	private String cAVV = null;
	private String cAVVAlgorithm = null;
	private ACSExtension extension = null;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public String getXId() {
		return xId;
	}
	public void setXId(String id) {
		this.xId = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getExponent() {
		return exponent;
	}
	public void setExponent(String exponent) {
		this.exponent = exponent;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getECI() {
		return eCI;
	}
	public void setECI(String eci) {
		this.eCI = eci;
	}
	public String getCAVV() {
		return cAVV;
	}
	public void setCAVV(String cavv) {
		this.cAVV = cavv;
	}
	public String getCAVVAlgo() {
		return cAVVAlgorithm;
	}
	public void setCAVVAlgo(String cAVVAlgorithm) {
		this.cAVVAlgorithm = cAVVAlgorithm;
	}
	public ACSExtension getExtension() {
		return extension;
	}
	public void setExtension(ACSExtension extension) {
		this.extension = extension;
	}
	
}
