package com.arcot.apps.callout.acs.extension.indiaivr;

public interface IndiaIVRXMLConstants {

	public static final String EXTENSION 					= "Extension";
	public static final String ID 							= "id";
	public static final String CRITICAL 					= "critical";
	public static final String AUTH_DATA 					= "npc356authdata";
	public static final String AUTH_STATUS_MESSAGE 			= "npc356authstatusmessage";
	public static final String AUTH_DATA_ENCRYPT 			= "npc356authdataencrypt";
	public static final String AUTH_DATA_ENCRYPT_TYPE 		= "npc356authdataencrypttype";
	public static final String AUTH_DATA_ENCRYPT_KEY_VALUE 	= "npc356authdataencryptkeyvalue";
	public static final String AUTH_DATA_ENC_MANDATORY_ATTR = "mandatory";
	//ITP
	public static final String ITP_STATUS 					= "npc356itpstatus";
	public static final String ITP_CREDENTIAL 				= "npc356itpcredential";
	public static final String ITP_PA_AUTHENTICATED 		= "authenticated";
	public static final String ITP_PA_IDENTIFIER 			= "identifier";
	
	public static final String PHONE_ID_FORMAT 				= "npc356chphoneidformat";
	public static final String PHONE_ID 					= "npc356chphoneid";
	public static final String PAREQ_CHANNEL 				= "npc356pareqchannel";
	public static final String SHOP_CHANNEL 				= "npc356shopchannel";
	public static final String AUTH_CHANNEL 				= "npc356availauthchannel";	
	
	public static final String ATTRIBUTE 					= "attribute";
	public static final String NAME 						= "name";
	public static final String LENGTH 						= "length";
	public static final String TYPE 						= "type";
	public static final String LABEL 						= "label";
	public static final String PROMPT 						= "prompt";
	public static final String VALUE 						= "value";
	public static final String ENCRYPTED 					= "encrypted";
	public static final String STATUS 						= "status";
	
	public static final String AUTH_DATA_NAME_OTP1 = "OTP1";
	public static final String AUTH_DATA_NAME_OTP2 = "OTP2";
	public static final String AUTH_DATA_NAME_TTP = "TTP";
	public static final String AUTH_DATA_NAME_ICB = "ICB";
	public static final String AUTH_DATA_NAME_ITP = "ITP";
	public static final String AUTH_DATA_NAME_OTHER = "Other";
	
	public static final String AUTH_DATA_TYPE_TEXT = "A";
	public static final String AUTH_DATA_TYPE_NUMERIC = "N";
	
	//ITP possible values
	public static final String ITP_INVALID_CRED = "01";
	public static final String ITP_INVALID_KEY = "02";
	public static final String ITP_CRED_EXP_REVOKED = "03";
	public static final String ITP_INVALID_SYNTAX = "04";
	public static final String ITP_INVALID_PAN_PHONE = "90";
	public static final String ITP_INVALID_PHONE = "91";
	public static final String ITP_INVALID_PAN = "92";
	public static final String ITP_OTHER_CH_ERROR = "93";
	public static final String ITP_UNDEFINED_ERROR = "98";
	public static final String ITP_OTHER_ERROR = "99";
	
}
