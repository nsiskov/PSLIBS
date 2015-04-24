package com.arcot.apps.callout.acs;

import java.util.HashMap;

import com.arcot.apps.callout.acs.extension.indiaivr.PAReqExtension;

public class ACSPARequest {
	/*
	 * Currently just including the basic required fields
	 * Will enhance as we go along
	 * Remember, parsing will be done in ACSXMLHandler  
	 */
	
	private String version = null;
	private String purchaseXID = null;
	private HashMap extensions = null;
	private String extensionID = null;
	private String extensionCritical = null;
	
	//Merchant fields
	private String merchantAcqBin = null;
	private String merchantId = null;
	private String merchantName = null;
	private String merchantCountry = null;
	private String merchantURL = null;
	
	//Purchase fields	
	private String purchaseOrderNum = null;	
	private String purchaseDate = null;
	private String purchaseAmount = null;
	private String purchaseRawAmount = null;
	private String purchaseCurrency = null;
	private String purchaseExponent = null;
	private String purchaseDesc = null;
	
	//CH Fields
	private String chAccountID = null;
	private String chPan = null;
	private String chCardExpiry = null;
	
	//version < 1.0 fields
	private String merchantGMTOffset = null;
	private String merchantBrands = null;
	private PAReqExtension pAReqExtension = null;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public HashMap getExtensions() {
		return extensions;
	}
	public void setExtensions(HashMap extensions) {
		this.extensions = extensions;
	}
	public String getExtensionID() {
		return extensionID;
	}
	public void setExtensionID(String extensionID) {
		this.extensionID = extensionID;
	}
	public String getExtensionCritical() {
		return extensionCritical;
	}
	public void setExtensionCritical(String extensionCritical) {
		this.extensionCritical = extensionCritical;
	}
	public String getMerchantAcqBin() {
		return merchantAcqBin;
	}
	public void setMerchantAcqBin(String merchantAcqBin) {
		this.merchantAcqBin = merchantAcqBin;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantCountry() {
		return merchantCountry;
	}
	public void setMerchantCountry(String merchantCountry) {
		this.merchantCountry = merchantCountry;
	}
	public String getMerchantURL() {
		return merchantURL;
	}
	public void setMerchantURL(String merchantURL) {
		this.merchantURL = merchantURL;
	}
	public String getPurchaseOrderNum() {
		return purchaseOrderNum;
	}
	public void setPurchaseOrderNum(String purchaseOrderNum) {
		this.purchaseOrderNum = purchaseOrderNum;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getPurchaseAmount() {
		return purchaseAmount;
	}
	public void setPurchaseAmount(String purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	public String getPurchaseRawAmount() {
		return purchaseRawAmount;
	}
	public void setPurchaseRawAmount(String purchaseRawAmount) {
		this.purchaseRawAmount = purchaseRawAmount;
	}
	public String getPurchaseCurrency() {
		return purchaseCurrency;
	}
	public void setPurchaseCurrency(String purchaseCurrency) {
		this.purchaseCurrency = purchaseCurrency;
	}
	public String getPurchaseExponent() {
		return purchaseExponent;
	}
	public void setPurchaseExponent(String purchaseExponent) {
		this.purchaseExponent = purchaseExponent;
	}
	public String getPurchaseDesc() {
		return purchaseDesc;
	}
	public void setPurchaseDesc(String purchaseDesc) {
		this.purchaseDesc = purchaseDesc;
	}
	public String getChAccountID() {
		return chAccountID;
	}
	public void setChAccountID(String chAccountID) {
		this.chAccountID = chAccountID;
	}
	public String getChPan() {
		return chPan;
	}
	public void setChPan(String chPan) {
		this.chPan = chPan;
	}
	public String getChCardExpiry() {
		return chCardExpiry;
	}
	public void setChCardExpiry(String chCardExpiry) {
		this.chCardExpiry = chCardExpiry;
	}
	public String getMerchantGMTOffset() {
		return merchantGMTOffset;
	}
	public void setMerchantGMTOffset(String merchantGMTOffset) {
		this.merchantGMTOffset = merchantGMTOffset;
	}
	public String getMerchantBrands() {
		return merchantBrands;
	}
	public void setMerchantBrands(String merchantBrands) {
		this.merchantBrands = merchantBrands;
	}
	public String getPurchaseXID() {
		return purchaseXID;
	}
	public void setPurchaseXID(String purchaseXID) {
		this.purchaseXID = purchaseXID;
	}
	public PAReqExtension getPAReqExtension() {
		return pAReqExtension;
	}
	public void setPAReqExtension(PAReqExtension pAReqExtension) {
		this.pAReqExtension = pAReqExtension;
	}

}
