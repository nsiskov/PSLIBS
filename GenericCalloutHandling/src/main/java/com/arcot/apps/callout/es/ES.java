package com.arcot.apps.callout.es;

public interface ES {
	public static final String INFO_LIST_SEPARATOR = ";";
	public static final String LOG_FILE_PATH = "LogFilePath";
	public static final String LOGIC_HANDLER = "LogicHandler";
	public static final String USERcardNumber = "USER.cardNumber";
	public static final String USERcardHolderName = "USER.cardHolderName";
	public static final String USERTEMPPASSWORD = "USER.TEMPPASSWORD";
	public static final String USERPIN1 = "USER.PIN1";
	public static final String USERPIN2 = "USER.PIN2";
	public static final String PASSWORDHINT = "passwordHint";
	public static final String USERHINTQUESTION = "USER.HINTQUESTION";
	public static final String USERHINTANSWER1 = "USER.HINTANSWER1";
	public static final String USERHINTANSWER2 = "USER.HINTANSWER2";
	public static final String USERSHOPPERID = "USER.SHOPPERID";
	public static final String USERQUESTIONS = "USER.QUESTIONS";
	public static final String USERANSWERS = "USER.ANSWERS";
	public static final String USERQUESTIONIDS = "USER.QUESTIONIDS";
	public static final String ISABRIDGED = "ISABRIDGED";
	public static final String ISRESET = "ISRESET";
	public static final String ISMINI = "ISMINI";
	public static final String ISFORGOTPWD = "ISFORGOTPWD";
	public static final String SESSION = "SESSION";
	public static final String CALLOUT_EVENT = "CALLOUT_EVENT";
	public static final String CFG_URL = "CFG_URL";
	public static final String CFG_CONNTIMEOUT = "CFG_CONNTIMEOUT";
	public static final String CFG_RESPTIMEOUT = "CFG_RESPTIMEOUT";
	public static final String CFG_CONNTRIES = "CFG_CONNTRIES";
	public static final String CFG_ENCRYPTIONCERTFILE = "CFG_ENCRYPTIONCERTFILE";
	public static final String CFG_CLIENTCERTPATH = "CFG_CLIENTCERTPATH";
	public static final String CFG_ROOTCERTPATH = "CFG_ROOTCERTPATH";
	public static final String CFG_SSLVERSION = "CFG_SSLVERSION";
	public static final String CFG_INFOLIST = "CFG_INFOLIST";
	public static final String USERdateExpired = "USER.dateExpired";
	public static final String USERverificationCode = "USER.verificationCode";
	public static final String USERaddress = "USER.address";
	public static final String USERcity = "USER.city";
	public static final String USERstate = "USER.state";
	public static final String USERzip = "USER.zip";
	public static final String USERemail = "USER.email";
	public static final String USERssn = "USER.ssn";
	public static final String USERdob = "USER.dob";
	public static final String USERDLNumber = "USER.DLNumber";
	public static final String USERDLState = "USER.DLState";
	public static final String USERhomePhone = "USER.homePhone";
	public static final String USERisPhoneListed = "USER.isPhoneListed";
	public static final String LOG_TO_DB = "LOG_TO_DB";
	public static final String LOG_TO_FILE = "LOG_TO_FILE";
	public static final String CARD_STATUS = "CARD_STATUS";
	public static final String RESULT = "result";
	public static final String USEROldemail = "USER.oldEmail";
	public static final String CardNumber = "CARDNUMBER";
	
	public static final String DisplayMsgResString = "DisplayMsgResString";
	public static final String OTP = "OTP";
	public static final String showOTP = "showOTP";

	public static final String USERUserId = "USER.userId";
	public static final String USERPassword = "USER.PIN";
	
	//DEFINE callout events
	public static final int ES_VERIFY_ISSUER_ANSWER = 1;
	public static final int ES_STEP1 = 3;
	public static final int ES_STEP2 = 4;
	public static final int ES_STEP3 = 5;
	public static final int ES_POST_IDENTIFICATION = 6;
	public static final int ES_ATTIBUTE_VERIFICATION = 7;
	public static final int ES_PRE_ISSUER_QA = 8;
	public static final int ES_POST_ISSUER_QA = 9;
	public static final int ES_BEFORE_FINISH = 10;
	public static final int ES_AFTER_FINISH = 11;
	public static final int AA_LOGIN = 12;
	public static final int AA_UPDATE_PROFILE = 13;
	public static final int AA_POST_UPDATE_PROFILE = 14;
	public static final int AA_POST_CANCEL_SERVICE = 15;
	public static final int ADMIN_POST_ACCOUNT_ASST = 51;
	public static final int ES_ACCNT_ASST_SERVICE = 16;
	
	public static final int ES_CALLOUT_TX_NO_ACTION = 500;
	public static final int AOTP_SEND_ACTIVATION_CODE = 53;
	public static final int AA_LOGIN_STEP1 = 17;
	
	
	// Session key attribute
	public static final String CALLOUT_MSG = "CALLOUT_MSG"; 
}
