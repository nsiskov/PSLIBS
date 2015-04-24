package com.arcot.apps.callout.acs.extension.indiaivr;

import java.util.ArrayList;
import java.util.List;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSXMLHandler;
import com.arcot.apps.callout.acs.extension.elements.ExtensionAttributes;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.logger.Logger;

public class VEResExtension extends IndiaIVRExtension {
	
	List<ExtensionAttributes> authData = null;
	private String authStatusMessage = null;

	private boolean isDataEncMandatory = false;
	private String authDataEncType = null;
	private String authDataEncKeyValue= null;
	private String iTPStatus = "";
	
	public VEResExtension(){
		setId(ACS.INDIA_IVR_EXTENSION_ID);
		setCritical(false);
	}
	
	public VEResExtension(COConfig config, String value){
		setId(ACS.INDIA_IVR_EXTENSION_ID);
		setCritical(false);
		authStatusMessage = config.getValue(IndiaIVRXMLConstants.AUTH_STATUS_MESSAGE);
		String dataEncMand = config.getValue(IndiaIVRXMLConstants.AUTH_DATA_ENC_MANDATORY_ATTR);
		isDataEncMandatory = false;
		if(dataEncMand != null && ("y".equalsIgnoreCase(dataEncMand) || "true".equalsIgnoreCase(dataEncMand))){
			isDataEncMandatory = true;
		}
		authDataEncType = config.getValue(IndiaIVRXMLConstants.AUTH_DATA_ENCRYPT_TYPE);
		authDataEncKeyValue = config.getValue(IndiaIVRXMLConstants.AUTH_DATA_ENCRYPT_KEY_VALUE.substring(4));
		//Fill in the authData attribute now
		authData = new ArrayList<ExtensionAttributes>();
		ExtensionAttributes veResAttr = new ExtensionAttributes();
		veResAttr.setLabel(config.getValue(IndiaIVRXMLConstants.LABEL));
		veResAttr.setLength(config.getValue(IndiaIVRXMLConstants.LENGTH));
		veResAttr.setName(config.getValue(IndiaIVRXMLConstants.NAME));
		veResAttr.setPrompt(config.getValue(IndiaIVRXMLConstants.PROMPT));
		veResAttr.setType(config.getValue(IndiaIVRXMLConstants.TYPE));
		veResAttr.setValue(value);
		authData.add(veResAttr);
	}

	
	public List<ExtensionAttributes> getAuthData() {
		return authData;
	}
	
	public void addAuthData(ExtensionAttributes attribute) {
		if(authData == null)
			authData = new ArrayList<ExtensionAttributes>();
		authData.add(attribute);
	}
	
	public String getAuthStatusMessage() {
		return authStatusMessage;
	}
	public void setAuthStatusMessage(String authStatusMessage) {
		this.authStatusMessage = authStatusMessage;
	}
	public boolean isDataEncMandatory() {
		return isDataEncMandatory;
	}
	public void setDataEncMandatory(boolean isDataEncMandatory) {
		this.isDataEncMandatory = isDataEncMandatory;
	}
	public String getAuthDataEncType() {
		return authDataEncType;
	}
	public void setAuthDataEncType(String authDataEncType) {
		this.authDataEncType = authDataEncType;
	}
	public String getAuthDataEncKeyValue() {
		return authDataEncKeyValue;
	}
	public void setAuthDataEncKeyValue(String authDataEncKeyValue) {
		this.authDataEncKeyValue = authDataEncKeyValue;
	}
	
	public String getvEResIvrExtensionXml(Logger logger) {
		return ACSXMLHandler.getVEResIvrExtensionXML(this, logger);
	}

	public String getITPStatus() {
		return iTPStatus;
	}

	public void setITPStatus(String status) {
		iTPStatus = status;
	}
	
}
