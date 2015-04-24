package test.com.arcot.callout.matchlibrary;

import java.util.ArrayList;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.MatchLibrary;
import com.arcot.apps.callout.matchlibrary.field.Field;
import com.arcot.apps.callout.matchlibrary.field.Field.FieldType;
import com.arcot.apps.callout.matchlibrary.result.MatchResult;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.callout.CallOutsConfig;

public class TestAll {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*testName();
		testSSN();
		testMobile();
		testPostalCode();
		testDrivingLicenseNumber();
		testHintAnswer();
		testRegistrationCode();
		testLastBillAmount();
		testCreditLimit();
		testEmail();
		testDOB();*/
		testAll();
	}
	
	public static void testAll() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
			"LogFilePath=C:\\TestBlh;" +
			"MATCHLIB_ENABLED=YES;" +
			"CHN_MF=p80|IGNORESPACES-N|IGNORETITLE-y~[p70|IGNORESPACES-y|IGNORETITLE-y];" +
//			"SSN_UDF=6;" +
			"SSN_MF=P6~L4~[L4];" +
			"MOBILE_MF=L4~[L10];" + // Matching last 4 ditits only, and checking WhatIf scenario mobile number without country code is correct.
			"POSTALCODE_MF=L6|IGNORESPACES-y|IGNORESPLCHARS-y;" + // Optional
			"HINTANSWER_MF=IGNORESPACES-y|IGNORESPLCHARS-y;" + //Optional. To do a case sensitive match this can be used.
			"CREDITLIMIT_MF=P20;" + //P20 for 20% deviation. To do a case sensitive match this can be used.
			"LASTBILLAMOUNT_MF=P20;" + //P20 is 20% deviation allowed in the number. To do a case sensitive match this can be used.
			"REGCODE_MF=[A5|IGNORECASE-N]~A5|IGNORECASE-Y;" + //A5 is for matching Any 5 characters. To do a case sensitive match this can be used.
			"EMAIL_MF=IGNORESPLCHARS-N~[IGNORESPLCHARS-Y];" + //Optional. To do a case sensitive match this can be used.
			"DOB_UDF=ddMMyyyy;" +
			"DOB_SDF=MMddyy~ddMMyyyy~MMddyyyy;" +
			"DOB_MF=MMddyy~MMyyyy;" +
			"VALIDATOR_LOG_LEVEL=DEBUG;";
		CORequest request = null;
		COConfig config = createConfig(infoList);
		
		ArrayList<Field> fields = new ArrayList<Field>();
		
		fields.add(Field.createField(request, config, FieldType.NAME, Field.FIELD_CARDHOLDERNAME, "AJay kumar", "AJAY kumar"));
		fields.add(Field.createField(request, config, FieldType.SSN, Field.FIELD_SSN, "234323", "4323"));
		fields.add(Field.createField(request, config, FieldType.PHONE, Field.FIELD_MOBILE, "0919880543210", "+9880 543 210"));
		fields.add(Field.createField(request, config, FieldType.POSTALCODE, Field.FIELD_POSTALCODE, "D3701G", "D37 01G"));
		fields.add(Field.createField(request, config, FieldType.DRIVINGLICENCE, Field.FIELD_DRIVINGLICENCE, "AP375537", "AP37 5537"));
		fields.add(Field.createField(request, config, FieldType.HINTANSWER, Field.FIELD_HINTANSWER, "HDFCBANK", "HDFC BANK"));
		fields.add(Field.createField(request, config, FieldType.CREDITCARDLIMIT, Field.FIELD_CREDITLIMIT, "120000", "100000"));
		fields.add(Field.createField(request, config, FieldType.LASTBILLAMOUNT, Field.FIELD_LASTBILLAMOUNT, "5800", "5758"));
		fields.add(Field.createField(request, config, FieldType.REGISTRATIONCODE, Field.FIELD_REGCODE, "243sdf3243", "232SDF324"));
		fields.add(Field.createField(request, config, FieldType.EMAIL, Field.FIELD_EMAIL, "asdf%@gmail.com", "asdf %@gmail.com"));
		fields.add(Field.createField(request, config, Field.FieldType.DATE, Field.FIELD_DOB, "15071947", "07161947"));
		
		try {
			MatchResult result = MatchLibrary.match(fields);
			System.out.println("Success ::"+ (result.getMatchCount()==fields.size()) +", Callout Status :"+ result);
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}
	
	public static void testName() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"CHN_MF=p80|IGNORESPACES-N|IGNORETITLE-y~[p70|IGNORESPACES-y|IGNORETITLE-y];" +
		"MATCHLIB_LOG_LEVEL=DEBUG;";
	
		try {
			Field field = Field.createField(null, createConfig(infoList), FieldType.NAME, Field.FIELD_CARDHOLDERNAME, "AJay kumar", "AJAY kumar");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}
	
	public static void testSSN() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"SSN_MF=L6~L4;" +
		"MATCHLIB_LOG_LEVEL=DEBUG;";
	
		try {
			Field field = Field.createField(null, createConfig(infoList), FieldType.SSN, Field.FIELD_SSN, "234323", "4323");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			System.out.println(e.getMessage()+ " ******** "+ e.getDescription());
			e.printStackTrace();
		}
	}
	
	public static void testMobile() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"MOBILE_MF=L4~[L10];" + // Matching last 4 ditits only, and checking WhatIf scenario mobile number without country code is correct.
		"MATCHLIB_LOG_LEVEL=DEBUG;";
		
		try {
			Field field = Field.createField(null, createConfig(infoList), FieldType.PHONE, Field.FIELD_MOBILE, "0919880543210", "+9880 543 210");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}
	
	public static void testPostalCode() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"POSTALCODE_MF=L6|IGNORESPACES-y|IGNORESPLCHARS-y;" + // Optional
		"MATCHLIB_LOG_LEVEL=DEBUG;";
	
		try {
			Field field = Field.createField(null, createConfig(infoList), FieldType.POSTALCODE, Field.FIELD_POSTALCODE, "D3701G", "D37 01G");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}

	public static void testDrivingLicenseNumber() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"MATCHLIB_LOG_LEVEL=DEBUG;";
	
		try {
			Field field = Field.createField(null, createConfig(infoList), FieldType.DRIVINGLICENCE, Field.FIELD_DRIVINGLICENCE, "AP375537", "AP37 5537");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}
	
	public static void testHintAnswer() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"POSTALCODE_MF=IGNORESPACES-y|IGNORESPLCHARS-y;" + //Optional. To do a case sensitive match this can be used.
		"MATCHLIB_LOG_LEVEL=DEBUG;";
	
		try {
			Field field = Field.createField(null, createConfig(infoList), FieldType.HINTANSWER, Field.FIELD_HINTANSWER, "HDFCBANK", "HDFC BANK");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}
	
	public static void testCreditLimit() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"CREDITLIMIT_MF=P20;" + //P20 for 20% deviation. To do a case sensitive match this can be used.
		"MATCHLIB_LOG_LEVEL=DEBUG;";
	
		try {
			Field field = Field.createField(null, createConfig(infoList), FieldType.CREDITCARDLIMIT, Field.FIELD_CREDITLIMIT, "120000", "100000");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}

	public static void testLastBillAmount() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"LASTBILLAMOUNT_MF=P20;" + //P20 is 20% deviation allowed in the number. To do a case sensitive match this can be used.
		"MATCHLIB_LOG_LEVEL=DEBUG;";
	
		try {
			Field field = Field.createField(null, createConfig(infoList), FieldType.LASTBILLAMOUNT, Field.FIELD_LASTBILLAMOUNT, "5800", "5758");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}
	
	public static void testRegistrationCode() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"REGCODE_MF=A5|IGNORECASE-N~[A5|IGNORECASE-Y];" + //A5 is for matching Any 5 characters. To do a case sensitive match this can be used.
		"MATCHLIB_LOG_LEVEL=DEBUG;";
	
		try {
			Field field = Field.createField(null, createConfig(infoList), FieldType.REGISTRATIONCODE, Field.FIELD_REGCODE, "243sdf3243", "232SDF324");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}
	
	public static void testEmail() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"EMAIL_MF=IGNORESPLCHARS-N~[IGNORESPLCHARS-Y];" + //Optional. To do a case sensitive match this can be used.
		"MATCHLIB_LOG_LEVEL=DEBUG;";
	
		try {
			Field field = Field.createField(null, createConfig(infoList), FieldType.EMAIL, Field.FIELD_EMAIL, "asdf%@gmail.com", "asdf %@gmail.com");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}
	
	public static void testDOB() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"DOB_UDF=ddMMyyyy;" +
		"DOB_SDF=MMddyy~MMddyyyy;" +
		"DOB_MF=MMddyy~[MMyyyy];" +
		"MATCHLIB_LOG_LEVEL=DEBUG;";
		
		try {
			Field field = Field.createField(null, createConfig(infoList), Field.FieldType.DATE, Field.FIELD_DOB, "15071947", "07161947");
			System.out.println(MatchLibrary.match(new Field[]{field}));
		} catch (MatchException e) {
			e.printStackTrace();
		}
	}

	/*public static void testAllFields() {
		COConfig config = createConfig();
		CORequest request = null;
		
		String fieldName[] = new String[11];
		String storedData[] = new String[11];
		String userData[] = new String[11];
		
		fieldName[0] = Field.FIELD_DOB;
		fieldName[1] = Field.FIELD_EXPIRY_DATE;
		fieldName[2] = Field.FIELD_SSN;
		fieldName[3] = Field.FIELD_POSTALCODE;
		fieldName[4] = Field.FIELD_CARDHOLDERNAME;
		fieldName[5] = Field.FIELD_MOBILE;
		fieldName[6] = Field.FIELD_LANDLINE;
		fieldName[7] = Field.FIELD_EMAIL;
		fieldName[8] = Field.FIELD_SECONDARYEMAIL;
		fieldName[9] = "FAVOURITECITY";
		fieldName[10] = Field.FIELD_LASTBILLAMOUNT;
		
		userData[0] = "22072011";
		userData[1] = "1208";
		userData[2] = "9876";
		userData[3] = "543-210";
		userData[4] = "Mr. M S DHONI";
		userData[5] = "919880123456";
		userData[6] = "08023456789";
		userData[7] = "asfd. lkj@gmail.com";
		userData[8] = "asfd.\\ lkj@gmail.com";
		userData[9] = "Hyderabad-17";
		userData[10] = "100.10";
		
		storedData[0] = "02011.JULY.22";
		storedData[1] = "0812";
		storedData[2] = "9876";
		storedData[3] = "543-210";
		storedData[4] = "Mr. M S Dhoni";
		storedData[5] = "9880123456";
		storedData[6] = "08023456789";
		storedData[7] = "asfd.lkj@gmail.com";
		storedData[8] = "asfd.lkj@gmail.com";
		storedData[9] = "HYDERABAD";
		storedData[10] = "100.10";
		
		Field fields[] = new Field[11];
		
		MatchResult matchResult = null;
		try {
			fields[0] = Field.createField(request, config, Field.FieldType.DATE, fieldName[0], userData[0], storedData[0]);
			fields[1] = Field.createField(request, config, Field.FieldType.DATE, fieldName[1], userData[1], storedData[1]);
			fields[2] = Field.createField(request, config, Field.FieldType.NUMERIC, fieldName[2], userData[2], storedData[2]);
			fields[3] = Field.createField(request, config, Field.FieldType.REGEX, fieldName[3], userData[3], storedData[3]);
			fields[4] = Field.createField(request, config, Field.FieldType.NAME, fieldName[4], userData[4], storedData[4]);
			fields[5] = Field.createField(request, config, Field.FieldType.NUMERIC, fieldName[5], userData[5], storedData[5]);
			fields[6] = Field.createField(request, config, Field.FieldType.NUMERIC, fieldName[6], userData[6], storedData[6]);
			fields[7] = Field.createField(request, config, Field.FieldType.EMAIL, fieldName[7], userData[7], storedData[7]);
			fields[8] = Field.createField(request, config, Field.FieldType.REGEX, fieldName[8], userData[8], storedData[8]);
			fields[9] = Field.createField(request, config, Field.FieldType.DEFAULT, fieldName[9], userData[9], storedData[9]);
			fields[10] = Field.createField(request, config, Field.FieldType.NUMERICDOUBLE, fieldName[10], userData[10], storedData[10]);
			
			matchResult = MatchLibrary.match(fields);
		} catch (MatchException e) {
			e.printStackTrace();
		}
		System.out.println("MatchResult :"+ matchResult);
	}
	
	public static ACSRequest createRequest() {
		ACSRequest request = new ACSRequest();
		request.setCardNumber("9999999999999999");
		request.setMessageType(ACS.VERIFY_IA_REQUEST);
		request.setIssuerAnswer("cvv=123:expiry=1017:zip=560017:dob=10121983");
		
		return request;
	}
	
	public static COConfig createConfig() {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestBlh;" +
		"MATCHLIB_ENABLED=YES;" +
		"DOB_SDF=yy~yyyyy.MMMMM.dd;DOB_UDF=ddMMyy~ddMMyyyy~[MMddyyyy]~[yyyy]~[MM]~[dd];" +
		"ED_SDF=MMyy~[yyMM];ED_UDF=yyMM;ED_PDF=yy;" +
		"SSN_DF=4;" +
		"POSTALCODE_DF=\\d{3}-\\d{3};" +
		"MOBILE_SDF=10;MOBILE_UDF=12;MOBILE_PDF=10;" +
		"LL_SDF=11;LL_UDF=11;LL_PDF=8;" +
		"POSTALCODE_TYPE=REGEX;" +
		"VALIDATOR_LOG_LEVEL=DEBUG;" +
		"SEMAIL_DF=^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$;" +
		"FAVOURITECITY_DF=IGNORENUM-y|IGNORENONALPHANUM-y;" +
		"LASTBILLAMOUNT_DF=-1;" +
		"CN_DF=EXACTMATCH-N|IGNORESPACES-Y|SHUFFLE-Y|IGNORETITLE-Y;" +
		"VALIDATOR_LOG_LEVEL=DEBUG;";
		
		return createConfig(infoList);
	}*/

	public static COConfig createConfig(String infoList) {
		CallOutsConfig ccConfig = new CallOutsConfig();
		ccConfig.setInfoList(infoList);
		COConfig config = new COConfig(ccConfig);
		
		return config;
	}
	
}
