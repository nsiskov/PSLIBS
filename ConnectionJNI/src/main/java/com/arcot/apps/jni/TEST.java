package com.arcot.apps.jni;

/*public class TEST {

	public static void main(String []args){
		
		String request = "BANKID=261&RANGEID=13471&CCID=1&ACSREQUEST=<VPAS><Message id=\"msg.17\"><VerifyIssuerAnswerReq><version>0.0</version><TimeStamp>5/10/2010 4:44:24 AM (UTC/GMT)</TimeStamp><CardHolderData><Pan>4377500000000006</Pan><IssuerAnswer>ExpiryDate=1212:DOB=12122009:CVV2=888:CreditCardLimit=88888888</IssuerAnswer><NumTotalFailures>0</NumTotalFailures><NumFailuresInSession>1</NumFailuresInSession><RiskAdvise>0</RiskAdvise><FolderId>266</FolderId></CardHolderData><TransactionData><Pareq><ThreeDSecure><Message id=\"msg.18\"><PAReq><version>1.0.2</version><Merchant><acqBIN>1234567890</acqBIN><merID>123456789012345</merID><name>GolfShop</name><country>840</country><url>https://www.dummy-arcot.com</url></Merchant><Purchase><xid>CDGI2FXpaky3/LS0zBhzrQAAAAY=</xid><date>20100510 04:43:01</date><amount>10.00</amount><purchAmount>1000</purchAmount><currency>840</currency><exponent>2</exponent><desc>M22 </desc></Purchase><CH><acctID>LdUsBolVrkmHDC9s2ZM2GAAAAAY=</acctID><expiry>1005</expiry></CH></PAReq></Message></ThreeDSecure></Pareq></TransactionData></VerifyIssuerAnswerReq></Message></VPAS>&cardnumber=4377500000000006";
		String CACert = "/opt/arcot/rahul/konarksoftroot.cer";
		String issuerCert = "/opt/arcot/rahul/hwsslClientcertkonark.cer";
		String pvtKey = "/opt/arcot/rahul/softpvtkey.key";
		String response = Connection.TwoWaySSLConnection(request, CACert, issuerCert, "", "https://10.150.1.198:8843/vpas/printxml.jsp", 10000, 10000);
		System.out.println(response);
	}
}

*/

public class TEST {

	public static void main(String []args){
		
		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"> <soapenv:Header><wsa:Action>VerifyRegistration</wsa:Action><wsa:To>urn:NetCodeForCardService</wsa:To></soapenv:Header><soapenv:Body>  <VerifyRegistration xmlns=\"http://www.commbank.com.au/2009/12/ServiceGateway.Services.ServiceContracts/NfcServices\"><request><ns1:card xmlns:ns1=\"http://www.commbank.com.au/2009/12/ServiceGateway.Services/Nfc\"><ns1:number>5430497000007879</ns1:number><ns1:type>SPA</ns1:type></ns1:card><ns2:id xmlns:ns2=\"http://www.commbank.com.au/2009/12/ServiceGateway.Services/Nfc\">test_123456</ns2:id></request></VerifyRegistration></soapenv:Body></soapenv:Envelope>";
		String CACert = "/opt/arcot/sandy/jni/VeriSignRoot_CBAROOT.pem.cer";
		String issuerCert = "/opt/arcot/sandy/jni/konark_Issuer_Cert.cer";
		String pvtKey = "/opt/arcot/sandy/jni/konarkCBAKey.key";
		
		//String CACert = "D:/India_CVS/profsvc/transfort/INHOUSE/MASTERCARD/CBA/DATA/SSLCertsKeysKonark/VeriSignRoot_CBAROOT.pem.cer";
		//String issuerCert = "D:/India_CVS/profsvc/transfort/INHOUSE/MASTERCARD/CBA/DATA/SSLCertsKeysKonark/konark_Issuer_Cert.cer";
		//String pvtKey = "D:/India_CVS/profsvc/transfort/INHOUSE/MASTERCARD/CBA/DATA/SSLCertsKeysKonark/konarkCBAKey.key";
		
		//String CACert = "/opt/arcot/rahul/konarksoftroot.cer";
		//String issuerCert = "/opt/arcot/rahul/hwsslClientcertkonark.cer";
		//String issuerCert = "/opt/arcot/rahul/konarksoftclient.cer";
		//String pvtKey = "/opt/arcot/rahul/softpvtkey.key";

		
		
		System.out.println("JNI req - "+request);
		String response = Connection.TwoWaySSLConnection(request, CACert, issuerCert, pvtKey, "https://140.168.76.180:443/", 40000, 40000);
		//String response = Connection.TwoWaySSLConnection(request, CACert, issuerCert, pvtKey, "https://10.150.1.198:8843/vpas/printxml.jsp", 10000, 10000);
		System.out.println("JNI res - "+response);
	}
}

