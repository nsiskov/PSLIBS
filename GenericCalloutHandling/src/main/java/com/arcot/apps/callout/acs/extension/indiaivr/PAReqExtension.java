package com.arcot.apps.callout.acs.extension.indiaivr;

import java.util.List;

import com.arcot.apps.callout.acs.extension.elements.ExtensionAttributes;

public class PAReqExtension extends IndiaIVRExtension {

	List<ExtensionAttributes> authUserData = null;

	public List<ExtensionAttributes> getAuthUserData() {
		return authUserData;
	}

	public void setAuthUserData(List<ExtensionAttributes> authUserData) {
		this.authUserData = authUserData;
	}
	
}
