package com.arcot.apps.callout.matchlibrary.result;

public class MatchResult {
	
	private int matchCount = 0;
	boolean[] matchResults = null;
	private String calloutStatus = "";
	private String formatError = "";
	
	public int getMatchCount() {
		return matchCount;
	}
	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}
	public boolean[] getMatchResults() {
		return matchResults;
	}
	public void setMatchResults(boolean[] matchResults) {
		this.matchResults = matchResults;
	}
	public String getCalloutStatus() {
		return calloutStatus;
	}
	public void setCalloutStatus(String calloutStatus) {
		this.calloutStatus = calloutStatus;
	}
	
	public String toString() {
		return "MatchCount ="+matchCount+ ", Callout Status =" + calloutStatus;
	}
	public String getFormatError() {
		return formatError;
	}
	public void setFormatError(String formatError) {
		this.formatError = formatError;
	}

}
