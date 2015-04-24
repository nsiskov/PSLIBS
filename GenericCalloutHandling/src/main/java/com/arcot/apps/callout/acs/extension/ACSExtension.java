package com.arcot.apps.callout.acs.extension;

public class ACSExtension {
	
	private String id = null;
	private boolean critical = false;
	
	public String getId() {
		return id;
	}
	
	public boolean isCritical() {
		return critical;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCritical(boolean critical) {
		this.critical = critical;
	}
}
