package com.arcot.apps.callout.currencyconverter;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ArRFCurrConvRates{
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getCurr_from() {
		return curr_from;
	}
	public void setCurr_from(int curr_from) {
		this.curr_from = curr_from;
	}
	public int getCurr_to() {
		return curr_to;
	}
	public void setCurr_to(int curr_to) {
		this.curr_to = curr_to;
	}
	public String getCurr_from_str() {
		return curr_from_str;
	}
	public void setCurr_from_str(String curr_from_str) {
		this.curr_from_str = curr_from_str;
	}
	public String getCurr_to_str() {
		return curr_to_str;
	}
	public void setCurr_to_str(String curr_to_str) {
		this.curr_to_str = curr_to_str;
	}
	public BigDecimal getConv_rate() {
		return conv_rate;
	}
	public void setConv_rate(BigDecimal conv_rate) {
		this.conv_rate = conv_rate;
	}
	public Timestamp getDtcreated() {
		return dtcreated;
	}
	public void setDtcreated(Timestamp dtcreated) {
		this.dtcreated = dtcreated;
	}
	public String getCurr_name_and_notes() {
		return curr_name_and_notes;
	}
	public void setCurr_name_and_notes(String curr_name_and_notes) {
		this.curr_name_and_notes = curr_name_and_notes;
	}
	
	public String toString(){
		StringBuilder strBuilder = new StringBuilder("[ VERSION, CURR_FROM, CURR_TO, CONV_RATE, CURR_NAME_AND_NOTES ] [ ");
		strBuilder.append(version);
		strBuilder.append(", ");
		strBuilder.append(curr_from_str+" ( "+ curr_from+" )");
		strBuilder.append(", ");
		strBuilder.append(curr_to_str+" ( "+ curr_to+" )");
		strBuilder.append(", ");
		strBuilder.append(conv_rate);
		strBuilder.append(", ");
		strBuilder.append(curr_name_and_notes);
		strBuilder.append(" ]");
		return strBuilder.toString();
	}
	
	private int version;
	private int curr_from;
	private int curr_to;
	private String curr_from_str;
	private String curr_to_str;
	private BigDecimal conv_rate;
	private Timestamp dtcreated;
	private String curr_name_and_notes;
	
}
