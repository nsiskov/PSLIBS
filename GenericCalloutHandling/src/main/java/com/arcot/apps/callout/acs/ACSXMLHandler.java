package com.arcot.apps.callout.acs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.arcot.apps.callout.acs.extension.ACSExtension;
import com.arcot.apps.callout.acs.extension.elements.ExtensionAttributes;
import com.arcot.apps.callout.acs.extension.indiaivr.IndiaIVRXMLConstants;
import com.arcot.apps.callout.acs.extension.indiaivr.PAReqExtension;
import com.arcot.apps.callout.acs.extension.indiaivr.VEReqExtension;
import com.arcot.apps.callout.acs.extension.indiaivr.VEResExtension;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.logger.Logger;
import com.arcot.vpas.enroll.EnrollmentCrypto;

public class ACSXMLHandler implements ACS {

	/*
	 * This method receives ACS CALLOUT requet XML as input and creates
	 * ACSRequest object out of it.
	 * 
	 * TODO: We need to enhance this in future to get the data out more effectively, 
	 * currently for two nodes with same name teh first one will be picked, fortunately taht's not teh case with relevant nodes today
	 * */
	
	public static ACSRequest parseACSRequest(String requestXML, Logger logger){
		/**
		 * Parse the XML and fill in the ACSRequest
		 * */
		/**
		 * To create the request Object   
		 */
		ACSRequest acsReq = new ACSRequest();
		/**
		 * Create the Document from the Input XML.
		 */
		Document doc = createDocument(requestXML, logger);
		if(doc == null){
			return null;
		}
		String temp=null;

		
		/**
		 * Get the value from XML and populate into ACS request Object. 
		 */
		if((temp=getNodeAttrData(doc,"Message","id" )) !=null){
			acsReq.setMessageId(temp);
		}
		
		if((temp=getMessageType(requestXML)) !=null)
			acsReq.setMessageType(temp);
		
		if((temp=getNodeValue(doc, "version" )) != null)
			acsReq.setVersion(temp);
		
		if((temp=getNodeValue(doc, "TimeStamp" )) != null)
			acsReq.setTimestamp(temp);
		//for VE handling
		if((temp=getNodeValue(doc, "pan" )) != null)
			acsReq.setCardNumber(temp);
		
		if((temp=getNodeValue(doc, "Pan" )) != null)
			acsReq.setCardNumber(temp);
		
		//Only in PsotEnroll/PostAuth callout ->
		if((temp=getNodeValue(doc, "Name" )) != null)
			acsReq.setCardholderName(temp);

		if((temp=getNodeValue(doc, "ProxyPan" )) != null)
			acsReq.setProxyPan(temp);
		
		if((temp=getNodeValue(doc, "Password" )) != null)
			acsReq.setPassword(temp);
		
		if((temp=getNodeValue(doc, "xid" )) != null)
			acsReq.setTXid(temp);
		
		if((temp=getNodeValue(doc, "HintAnswer" )) != null)
			acsReq.setHintAnswer(temp);

		//specially processing to store PAReq
		if(requestXML.indexOf("Pareq") > 0){
			acsReq.setPAReq(requestXML.substring(requestXML.indexOf("<PAReq>"), requestXML.indexOf("</PAReq>") + 8));
		}
			
		
		//specially processing to store VEReq
		if(requestXML.indexOf("<VEReq>") > 0){
			acsReq.setVEReq(requestXML.substring(requestXML.indexOf("<VEReq>"), requestXML.indexOf("</VEReq>") + 8));
		}
		
		//specially processing to store PARes
		if(requestXML.indexOf("<PARes") > 0){
			acsReq.setPARes(requestXML.substring(requestXML.indexOf("<PARes"), requestXML.indexOf("</PARes>") + 8));
		}

		if((temp=getNodeValue(doc, "Hint" )) != null)
			acsReq.setHint(temp);
		
		if((temp=getNodeValue(doc, "IssuerAnswer" )) != null)
			acsReq.setIssuerAnswer(temp);
		
		if((temp=getNodeValue(doc, "NumTotalFailures" )) != null)
			acsReq.setNumTotalFailures(Integer.parseInt(temp));
		
		if((temp=getNodeValue(doc, "NumFailuresInSession" )) != null)
			acsReq.setNumFailuresInSession(Integer.parseInt(temp));
		
		if((temp=getNodeValue(doc, "EmailAddress" )) != null)
			acsReq.setEmail(temp);
		
		if((temp=getNodeValue(doc, "UserID" )) != null)
			acsReq.setUserId(temp);
		
		if((temp=getNodeValue(doc, "Status" )) !=null)
			acsReq.setStatus(temp);
		
		if((temp=getNodeValue(doc, "Pares" )) !=null)
			acsReq.setPARes(temp);
	
		if((temp=getNodeValue(doc, "Type" )) !=null)
			acsReq.setType(temp);
		
		if((temp=getNodeValue(doc, "CHProfile" )) !=null)
			acsReq.setCHProfile(temp);
		
		//Added for RiskFort integration
		if((temp=getNodeValue(doc, "RiskAdvise" )) !=null)
			acsReq.setRiskAdvice(Integer.parseInt(temp));
		
		if((temp=getNodeValue(doc, "FYPRARiskAdvice" )) !=null)
			acsReq.setFypRARiskAdvice(Integer.parseInt(temp));
		
		if((temp=getNodeValue(doc, "MatchedRuleMnemonic" )) !=null)
			acsReq.setMatchedRuleMnemonic(temp);
		
		if((temp=getNodeValue(doc, "FYPRAMatchedRuleMnemonic" )) !=null)
			acsReq.setFypRAMatchedRuleMnemonic(temp);
		
		if((temp=getNodeValue(doc, "FYPRAIncAuthAction" )) !=null)
			acsReq.setFypRAIncAuthAction(Integer.parseInt(temp));
		
		if((temp=getNodeValue(doc, "IsAutoFYP" )) !=null)
			acsReq.setAutoFYP(Integer.parseInt(temp));
		
		if((temp=getNodeValue(doc, "SecAuth" )) !=null)
			acsReq.setSecAuthPin(temp);
		
		if((temp=getNodeValue(doc, "IsAbridged" )) != null){
			int isAbridged = -1;
			try {
				isAbridged = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
				//ignore
			}
			acsReq.setIsAbridged(isAbridged);
		}
		
		//SlotInfo
		if((temp=getNodeValue(doc, "SlotInfo" )) !=null)
			acsReq.setSlotInfo(temp);
		
		if((temp=getNodeValue(doc, "TxType" )) !=null)
			acsReq.setTXType(temp);
		
		if((temp=getNodeValue(doc, "HTTPUAId" )) !=null)
			acsReq.setHttpUAId(temp);
		
		if((temp=getNodeValue(doc, "FolderId" )) !=null)
			acsReq.setFolderId(temp);
		
		//Below three Only in PsotEnroll/PostAuth callout ->
		if((temp=getNodeValue(doc, "ActivationCode" )) !=null)
			acsReq.setActivationCode(temp);
		
		if((temp=getNodeValue(doc, "ActivationURL" )) !=null)
			acsReq.setActivationURL(temp);
		
		if((temp=getNodeValue(doc, "CardHolderID" )) !=null)
			acsReq.setCardHolderID(temp);
		
	    if ((temp = getNodeValue(doc, "Replay")) != null) {
	        acsReq.setIsReplayPareq(temp);
	    }
		
		return acsReq;
	}
	
	/*
	 * This method creates the response XML to be send to ACS.
	 * message id etc can be used directly out of request itself.
	 * */
	public static String createACSResponse(ACSRequest request, ACSResponse response, Logger logger){
		/*
		 * Create the response as 
		 * ACSREPONSE=<respxml>&LOGTODB=<logToDB>&LOGTOFILE=<logToFile>
		 * */
		
		/**
		 * To create the document of the Response XML.   
		 */
		Document doc = createDocument(null, logger);
		Element root = null;
		String rootElementValue = "VPAS";
		if(VERIFY_ENROLLMENT_REQUEST.equals(request.getMessageType()) || GET_FYP_REQUEST.equals(request.getMessageType())){
			root = doc.createElementNS(null, "ThreeDSecure");
			rootElementValue = "ThreeDSecure";
		}else{
			root = doc.createElementNS(null, "VPAS");
			rootElementValue = "VPAS";
		}
		
		Element elem = null;
		Element elem2 = null;
		Element elem1 = null;
		Node node = null;
	
		/**
		 * Extract the values from ACS response Object and construct response XML.    
		 */
		//header command
		
		if (request.getMessageId() != null) {
			elem1 = doc.createElementNS(null, "Message");
			elem1.setAttribute("id", request.getMessageId());	
			root.appendChild(elem1);
		}
		
		if (request.getMessageType() != null) {
			elem2 = doc.createElementNS(null, getResponseMessageType(request)); 
			elem1.appendChild(elem2);
		}
		
		if (response.getVersion() != null) {
			elem = doc.createElementNS(null, "version");
			node = doc.createTextNode(response.getVersion());				
			elem.appendChild(node);
			elem2.appendChild(elem);
		}
		
		if (response.getMatchedName() != null) {
			elem = doc.createElementNS(null, "MatchedName");
			node = doc.createTextNode(response.getMatchedName());
			elem.appendChild(node);
			elem2.appendChild(elem);
		}
		
		if (response.getPWDStatus() != null) {
			elem = doc.createElementNS(null, "PWDStatus");
			node = doc.createTextNode(response.getPWDStatus());
			elem.appendChild(node);
			elem2.appendChild(elem);
		}
		
		if (response.getAnswersStatus() != null) {
			elem = doc.createElementNS(null, "AnswersStatus");
			node = doc.createTextNode(response.getAnswersStatus());
			elem.appendChild(node);
			elem2.appendChild(elem);
		}
		
		if (response.getBlockCH() != null) {
			elem = doc.createElementNS(null, "BlockCH");
			node = doc.createTextNode(response.getBlockCH());
			elem.appendChild(node);
			elem2.appendChild(elem);
		}
		
		if (response.getNewName() != null) {
			elem = doc.createElementNS(null, "NewName");
			node = doc.createTextNode(response.getNewName());
			elem.appendChild(node);
			elem2.appendChild(elem);
		}
		
		/**
		 * Support for Folder ID, the Callout instructs ACS to render the following folder
		 * Supported in: PAREQ, VIA, VP, UCP
		 * 
		 */
		if (response.getFolderId() != null){
			elem = doc.createElementNS(null, "FolderId");
			node = doc.createTextNode(response.getFolderId());
			elem.appendChild(node);
			elem2.appendChild(elem);
			
			elem = doc.createElementNS(null, "CalloutResponseCode");
			node = doc.createTextNode(String.valueOf(response.getAcsCRC()));
			elem.appendChild(node);
			elem2.appendChild(elem);			
		}
		
		
		
		//for sending back VERes
		// this will set <CH><enrolled></enrolled><pan></pan></CH>
		if (VERIFY_ENROLLMENT_RESPONSE.equals(response.getMessageType())) {
			elem = doc.createElementNS(null, "CH");
			node = doc.createTextNode("");
			elem.appendChild(node);
			elem2.appendChild(elem);

			Element enrollElem = doc.createElementNS(null, "enrolled");
			Node enrollNode = doc.createTextNode(response.getEnrolled());
			enrollElem.appendChild(enrollNode);
			elem.appendChild(enrollElem);			
			
			String dummyCapUrl = "http://localhost/acspage/cap?RID=1"; //Required for IVR.. ACS expects a dummy url.
			Element urlElem = doc.createElementNS(null, "url");
			Node urlNode = doc.createTextNode(dummyCapUrl);
			urlElem.appendChild(urlNode);
			elem2.appendChild(urlElem);
			
			Element protocolElem = doc.createElementNS(null, "protocol");
			Node protocolNode = doc.createTextNode(rootElementValue);
			protocolElem.appendChild(protocolNode);
			elem2.appendChild(protocolElem);
			/*
			Element panElem = doc.createElementNS(null, "pan");
			Node panNode = doc.createTextNode(response.getProxyPan());
			panElem.appendChild(panNode);
			elem.appendChild(panElem);
			*/
			Element acctIDElem = doc.createElementNS(null, "acctID");
			Node acctIDNode = null;
			if(request.getDbProxyPan() == null || (request.getDbProxyPan()).length() > 28){				
				//This is to override the ACS generated Deterministic PP that causes length check to fail on VERES Acct ID
				String encCardNum = EnrollmentCrypto.encrypt(request.getCardNumber(), request.getBankId());
				acctIDNode = doc.createTextNode(COUtil.generateProxyPAN(encCardNum));
			}else{
				acctIDNode = doc.createTextNode(request.getDbProxyPan());
			}
			acctIDElem.appendChild(acctIDNode);
			elem.appendChild(acctIDElem);
			
			/*
			Element elemCR = doc.createElementNS(null, "CR");
			Node nodeCR = doc.createTextNode("");
			elemCR.appendChild(nodeCR);
			elem2.appendChild(elemCR);
			
			Element enrollElemCR = doc.createElementNS(null, "enrolled");
			Node enrollNodeCR = doc.createTextNode("N");
			enrollElemCR.appendChild(enrollNodeCR);
			elemCR.appendChild(enrollElemCR);
			*/

			ACSExtension ext = response.getExtension();
			if(ext != null) {
				if(ext instanceof VEResExtension) {
					VEResExtension vrResExt = (VEResExtension)ext;
					
					Element extNode = getVRResExtRootElement(vrResExt, logger, doc);
					elem2.appendChild(extNode);
				}
			}
		}
		
		//finish VERes handling
		if (response.getUpdateStatus() != null) {
			elem = doc.createElementNS(null, "UpdateStatus");
			node = doc.createTextNode(response.getUpdateStatus());
			elem.appendChild(node);
			elem2.appendChild(elem);
		}
		
		root.appendChild(elem1);
		doc.appendChild(root);
		StringBuffer acsRespString = new StringBuffer("ACSRESPONSE=");
		if(VERIFY_ENROLLMENT_RESPONSE.equalsIgnoreCase(response.getMessageType())){
			acsRespString.append(getXMLFromDocument(doc, logger));
		}
		
		if(response.getFolderId() != null){
			acsRespString.append(getXMLFromDocument(doc, logger));
		}
		
		if(VERIFY_IA_RESPONSE.equalsIgnoreCase(response.getMessageType()) || UPDATE_CHPROFILE_RESPONSE.equalsIgnoreCase(response.getMessageType()) || VERIFY_PASSWORD_RESPONSE.equalsIgnoreCase(response.getMessageType())) {
			if(response.getAuthMethod() != -1) {
				elem = doc.createElementNS(null, "AuthMethod");
				node = doc.createTextNode(Integer.toString(response.getAuthMethod()));
				elem.appendChild(node);
				elem2.appendChild(elem);
				acsRespString.append(getXMLFromDocument(doc, logger));
			}
		}
		
		acsRespString.append("&CRC=");
		acsRespString.append(response.getAcsCRC());
		
		acsRespString.append("&COMSG=");
		if(response.getCalloutMessage() != null){			
			acsRespString.append(response.getCalloutMessage());
		}
		
		if(response.getLogToDB() != null){
			acsRespString.append("&LOGTODB="); 
			acsRespString.append(response.getLogToDB());
		}
		
		if(response.getLogToFile() != null){
			acsRespString.append("&LOGTOFILE=");
			acsRespString.append(response.getLogToFile());
		}
		
		
		logger.log("ACSResponse created :" + acsRespString.toString());
		return acsRespString.toString();
	}
	
	private static Document getVEResIvrExtensionRootDocument(VEResExtension extension, Logger logger) {
		Document doc = createDocument(null, logger);
		Element root = getVRResExtRootElement(extension, logger, doc);
		doc.appendChild(root);
		return doc;
	}
	
	private static Element getVRResExtRootElement(VEResExtension extension, Logger logger, Document doc) {
		Element root = doc.createElementNS(null, IndiaIVRXMLConstants.EXTENSION);
		
		addAttributeToNode(root, IndiaIVRXMLConstants.ID, extension.getId());
		addAttributeToNode(root, IndiaIVRXMLConstants.CRITICAL, new Boolean(extension.isCritical()).toString());
		
		Element authData = doc.createElementNS(null, IndiaIVRXMLConstants.AUTH_DATA);
		root.appendChild(authData);
		List<ExtensionAttributes> attrList = extension.getAuthData();
		if(attrList != null) {
			Element[] attrNodes = new Element[attrList.size()];
			int i = 0;
			for(ExtensionAttributes attribute : attrList) {
				attrNodes[i] = doc.createElementNS(null, IndiaIVRXMLConstants.ATTRIBUTE);
				addAttributeToNode(attrNodes[i], IndiaIVRXMLConstants.NAME, (attribute.getName() == null)?"":attribute.getName());
				addAttributeToNode(attrNodes[i], IndiaIVRXMLConstants.LENGTH, attribute.getLength());
				addAttributeToNode(attrNodes[i], IndiaIVRXMLConstants.TYPE, attribute.getType());
				addAttributeToNode(attrNodes[i], IndiaIVRXMLConstants.LABEL, attribute.getLabel());
				addAttributeToNode(attrNodes[i], IndiaIVRXMLConstants.PROMPT, attribute.getPrompt());
				// Added for ICICI Movida - to add the attribute ENCRYPTED to TRUE or FALSE
				addAttributeToNode(attrNodes[i], IndiaIVRXMLConstants.ENCRYPTED, attribute.getEncrypted());
				//End of modification for ICICI Movida
				authData.appendChild(attrNodes[i]);
				i++;
			}
		}
		
		Element authStatusMessage = doc.createElementNS(null, IndiaIVRXMLConstants.AUTH_STATUS_MESSAGE);
		authStatusMessage.appendChild(doc.createTextNode((extension.getAuthStatusMessage() == null)?"":extension.getAuthStatusMessage()));
		root.appendChild(authStatusMessage);
		
		//ACS needs the madatory tag at least, hence we cannot completely drop this tag even if it is false.
		Element authDataEncrypt = doc.createElementNS(null, IndiaIVRXMLConstants.AUTH_DATA_ENCRYPT);
		authDataEncrypt.setAttribute(IndiaIVRXMLConstants.AUTH_DATA_ENC_MANDATORY_ATTR, Boolean.toString(extension.isDataEncMandatory()).toUpperCase());
		root.appendChild(authDataEncrypt);	
		
		Element authDataEncryptType = doc.createElementNS(null, IndiaIVRXMLConstants.AUTH_DATA_ENCRYPT_TYPE);
		if(extension.getAuthDataEncType() != null){			
			authDataEncryptType.appendChild(doc.createTextNode(extension.getAuthDataEncType()));
		}else{
			authDataEncryptType.appendChild(doc.createTextNode(""));
		}
		root.appendChild(authDataEncryptType);
		
		Element authDataEncryptKeyValue = doc.createElementNS(null, IndiaIVRXMLConstants.AUTH_DATA_ENCRYPT_KEY_VALUE);
		if(extension.getAuthDataEncKeyValue() != null){			
			authDataEncryptKeyValue.appendChild(doc.createTextNode(extension.getAuthDataEncKeyValue()));
		}else{
			authDataEncryptKeyValue.appendChild(doc.createTextNode(""));
		}
		root.appendChild(authDataEncryptKeyValue);
		
		
		Element itpStatus = doc.createElementNS(null, IndiaIVRXMLConstants.ITP_STATUS);
		itpStatus.appendChild(doc.createTextNode(extension.getITPStatus()));
		root.appendChild(itpStatus);		
		return root;
	}
	
	private static void addAttributeToNode(Element attrNode, String name, String value) {
		if(attrNode != null && value != null)
			attrNode.setAttribute(name, value);
	}
 	
	public static String getVEResIvrExtensionXML(VEResExtension extension, Logger logger) {
		Document doc = getVEResIvrExtensionRootDocument(extension, logger);
		return getXMLFromDocument(doc, logger);
	}
	
	public static String getResponseMessageType(ACSRequest request){
		String msgType = null;
		String reqMsgType = request.getMessageType();
		if(PAREQ_REQUEST.equalsIgnoreCase(reqMsgType)){
			msgType = PAREQ_RESPONSE;
		}else if(POST_AUTH_REQUEST.equalsIgnoreCase(reqMsgType)){
			msgType = POST_AUTH_RESPONSE;
		}else if(POST_ENROLL_REQUEST.equalsIgnoreCase(reqMsgType)){
			msgType = POST_ENROLL_RESPONSE;
		}else if(UPDATE_CHPROFILE_REQUEST.equalsIgnoreCase(reqMsgType)){
			msgType = UPDATE_CHPROFILE_RESPONSE;
		}else if(VERIFY_ENROLLMENT_REQUEST.equalsIgnoreCase(reqMsgType)){
			msgType = VERIFY_ENROLLMENT_RESPONSE;
		}else if(VERIFY_HINT_REQUEST.equalsIgnoreCase(reqMsgType)){
			msgType = VERIFY_HINT_RESPONSE;
		}else if(VERIFY_IA_REQUEST.equalsIgnoreCase(reqMsgType)){
			msgType = VERIFY_IA_RESPONSE;
		}else if(VERIFY_PASSWORD_REQUEST.equalsIgnoreCase(reqMsgType)){
			msgType = VERIFY_PASSWORD_RESPONSE;
		}else if(PRE_PARES_REQUEST.equalsIgnoreCase(reqMsgType)){
			msgType = PRE_PARES_RESPONSE;
		}else if(GET_FYP_REQUEST.equalsIgnoreCase(reqMsgType)){
			msgType = GET_FYP_RESPONSE;
		}
		return msgType;
	}
	
	private static Document createDocument(String xml, Logger logger){
		Document doc = null;
		/*
		 * Simply catching the exception and logging it assuming that the exception
		 * can occur only if XML messages are wrong and hence it will be an implementation
		 * problem that WILL require implementation change
		 * */
		try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			if(xml == null){
				doc = docBuilder.newDocument();
			}else{
				//Need to parse the xml provided in the string
				InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
				doc = docBuilder.parse(in);
			}
		} catch (ParserConfigurationException e) {
			//TODO Handle it well
			e.printStackTrace();
			logger.logException(e, "ACSXMLHandler: ");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.logException(e, "ACSXMLHandler: ");
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.logException(e, "ACSXMLHandler: ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.logException(e, "ACSXMLHandler: ");
		}
		return doc;
	}
	
	private static String getNodeValue(Document doc, String name) {
		//System.out.println("Node Name = "+name);
		NodeList nodelist = doc.getElementsByTagName(name);
		Node node = null;
		String value = null;
		if(nodelist != null)
			node = nodelist.item(0);
		if (node != null) {
			if (node.getFirstChild() != null) {
				value = node.getFirstChild().getNodeValue();
			}
		}
		return value;
	}
	
	private static String getNodeAttrData(Document doc, String tagName, String attributeName){
		NodeList nodelist = doc.getElementsByTagName(tagName);
		Node node = null;
		String value = null;
		if(nodelist != null)
		{
		node = nodelist.item(0);
		
		NamedNodeMap nodeMap = node.getAttributes();
		Node childNode = null;
		if(nodeMap != null)
			childNode = nodeMap.getNamedItem(attributeName);
		if(childNode != null)
			value = childNode.getNodeValue();
		}
		return value;
	}	
	
	private static String getXMLFromDocument(Document doc, Logger logger){
		DOMSource domSource = new DOMSource(doc);
	    StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = null;
		/*
		 * Simply catching the exception and logging it assuming that the exception
		 * can occur only if XML messages are wrong and hence it will be an implementation
		 * problem that WILL require implementation change
		 * */
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.logException(e, "ACSXMLHandler::getXMLFromDocument: ");
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.logException(e, "ACSXMLHandler::getXMLFromDocument: ");
		} catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.logException(e, "ACSXMLHandler::getXMLFromDocument: ");
		}

	   return writer.toString();
	}

	private static String getMessageType(String xml)
	{
		if(xml.indexOf(VERIFY_PASSWORD_REQUEST)!=-1)
			return VERIFY_PASSWORD_REQUEST;
		else if (xml.indexOf(VERIFY_HINT_REQUEST)!=-1)
			return VERIFY_HINT_REQUEST;
		else if (xml.indexOf(VERIFY_IA_REQUEST)!=-1)
			return VERIFY_IA_REQUEST;
		else if (xml.indexOf(UPDATE_CHPROFILE_REQUEST)!=-1)
			return UPDATE_CHPROFILE_REQUEST;
		else if (xml.indexOf(POST_AUTH_REQUEST)!=-1)
			return POST_AUTH_REQUEST;
		else if (xml.indexOf(VERIFY_ENROLLMENT_REQUEST)!=-1)
			return VERIFY_ENROLLMENT_REQUEST;
		else if (xml.indexOf(PAREQ_REQUEST) != -1)
			return PAREQ_REQUEST;
		else if (xml.indexOf(POST_ENROLL_REQUEST) != -1)
			return POST_ENROLL_REQUEST;
		else if (xml.indexOf(PRE_PARES_REQUEST) != -1)
			return PRE_PARES_REQUEST;
		else if (xml.indexOf(GET_FYP_REQUEST) != -1)
			return GET_FYP_REQUEST;
		else return "UNKNOWN_REQUEST";	
	}

	public static ACSPARequest getParsedPAReq(ACSRequest request, Logger logger){
		/*
		 * Parse the incoming XML PAReq as per #DS instructions
		 * and set corresponding fields in the PAReq object
		 * We are not by default calling this method for all PAReq requests
		 * as many of the might not need PAReq internals. 
		 * 
		 * Handle PAReq version < 1.0 separately
		 * */
		String paReqStr = request.getPAReq();
		if(paReqStr == null || "".equals(paReqStr.trim())){
			logger.log("Null Pareq");
			return null;
		}
		
		Document doc = createDocument(paReqStr, logger);
		if(doc == null){
			logger.log("Bad PAReq, could not parse [" + request.getPAReq() + "]");
			return null;
		}
		
		String temp=null;		
		ACSPARequest paReq = new ACSPARequest();
		
		if((temp=getNodeValue(doc, "version")) !=null){
			paReq.setVersion(temp);
		}
		
		if((temp=getNodeValue(doc, "acqBIN")) !=null){
			paReq.setMerchantAcqBin(temp);
		}
		
		if((temp=getNodeValue(doc, "merID")) !=null){
			paReq.setMerchantId(temp);
		}
		
		if((temp=getNodeValue(doc, "name")) !=null){
			paReq.setMerchantName(temp);
		}
		
		if((temp=getNodeValue(doc, "country")) !=null){
			paReq.setMerchantCountry(temp);
		}
		
		if((temp=getNodeValue(doc, "url")) !=null){
			paReq.setMerchantURL(temp);
		}
		
		if((temp=getNodeValue(doc, "xid")) !=null){
			paReq.setPurchaseXID(temp);
		}
		
		if((temp=getNodeValue(doc, "date")) !=null){
			paReq.setPurchaseDate(temp);
		}
		
		if((temp=getNodeValue(doc, "amount")) !=null){
			paReq.setPurchaseAmount(temp);
		}
		
		if((temp=getNodeValue(doc, "purchAmount")) !=null){
			paReq.setPurchaseRawAmount(temp);
		}
		
		if((temp=getNodeValue(doc, "currency")) !=null){
			paReq.setPurchaseCurrency(temp);
		}
		
		if((temp=getNodeValue(doc, "exponent")) !=null){
			paReq.setPurchaseExponent(temp);
		}
		
		if((temp=getNodeValue(doc, "desc")) !=null){
			paReq.setPurchaseDesc(temp);
		}
		
		if((temp=getNodeValue(doc, "acctID")) !=null){
			paReq.setChAccountID(temp);
		}
		
		if((temp=getNodeValue(doc, "expiry")) !=null){
			paReq.setChCardExpiry(temp);
		}
		
		NodeList nodelist = doc.getElementsByTagName(IndiaIVRXMLConstants.EXTENSION);
		Node node = null;
		if(nodelist != null) {
			logger.log("Found Extensions :"+nodelist.getLength());
			for(int i = 0; i < nodelist.getLength(); i++) {
				node = nodelist.item(i);
				if(getNodeAttribute(node, IndiaIVRXMLConstants.ID).equals(ACS.INDIA_IVR_EXTENSION_ID)) {
					logger.log("Found an India IVR Extension :"+i);
					PAReqExtension extension = getParsedIndiaIvrPAReqExtension(node, logger);
					paReq.setPAReqExtension(extension);
					logger.log("Added India IVR extensions element to PAREQ object.");
				}
			}
		}
		return paReq;
	}
	
	public static ACSVERequest getParsedVEReq(ACSRequest request, Logger logger){
		String veReqStr = request.getVEReq();
		if(veReqStr == null || "".equals(veReqStr.trim())){
			logger.log("Null VEReq element.");
			return null;
		}
		
		Document doc = createDocument(veReqStr, logger);
		if(doc == null){
			logger.log("Bad VEReq, could not parse [" + request.getVEReq() + "]");
			return null;
		}
		
		String temp=null;		
		ACSVERequest veReq = new ACSVERequest();
		
		if((temp=getNodeValue(doc, "version")) !=null){
			veReq.setVersion(temp);
		}
		if((temp=getNodeValue(doc, "pan")) !=null){
			veReq.setCardNumber(temp);
		}
		if((temp=getNodeValue(doc, "acqBIN")) !=null){
			veReq.setAcquirerBin(temp);
		}
		if((temp=getNodeValue(doc, "merID")) !=null){
			veReq.setMerchantId(temp);
		}
		if((temp=getNodeValue(doc, "deviceCategory")) !=null){
			veReq.setDeviceCategory(temp);
		}
		if((temp=getNodeValue(doc, "accept")) !=null){
			veReq.setAccept(temp);
		}
		if((temp=getNodeValue(doc, "userAgent")) !=null){
			veReq.setUserAgent(temp);
		}
		if(veReqStr.indexOf("<Extension") > 0){
			String extensionsXML = veReqStr.substring(veReqStr.indexOf("<Extension"), veReqStr.lastIndexOf("</Extension>") + 12);
			VEReqExtension extension = getParsedIndiaIvrVEReqExtension(extensionsXML, logger);
			veReq.setVEReqExtension(extension);
		}
		return veReq;
	}
	
	public static ACSPAResponse getParsedPARes(ACSRequest request, Logger logger){
		String paResStr = request.getPARes();
		if(paResStr == null || "".equals(paResStr.trim())){
			logger.log("Null PARes element.");
			return null;
		}
		
		Document doc = createDocument(paResStr, logger);
		if(doc == null){
			logger.log("Bad PARes, could not parse [" + request.getPARes() + "]");
			return null;
		}
		
		String temp=null;		
		ACSPAResponse paRes = new ACSPAResponse();
		
		if((temp=getNodeValue(doc, "version")) !=null){
			paRes.setVersion(temp);
		}
		
		if((temp=getNodeValue(doc, "acqBIN")) !=null){
			paRes.setAcquirerBin(temp);
		}
		
		if((temp=getNodeValue(doc, "merID")) !=null){
			paRes.setMerchantId(temp);
		}
		
		if((temp=getNodeValue(doc, "xid")) !=null){
			paRes.setXId(temp);
		}
		
		if((temp=getNodeValue(doc, "date")) !=null){
			paRes.setDate(temp);
		}
		
		if((temp=getNodeValue(doc, "purchAmount")) !=null){
			paRes.setAmount(temp);
		}
		
		if((temp=getNodeValue(doc, "currency")) !=null){
			paRes.setCurrency(temp);
		}
		
		if((temp=getNodeValue(doc, "exponent")) !=null){
			paRes.setExponent(temp);
		}
		
		if((temp=getNodeValue(doc, "time")) !=null){
			paRes.setTime(temp);
		}
		
		if((temp=getNodeValue(doc, "status")) !=null){
			paRes.setStatus(temp);
		}
		
		if((temp=getNodeValue(doc, "eci")) !=null){
			paRes.setECI(temp);
		}
		
		if((temp=getNodeValue(doc, "cavv")) !=null){
			paRes.setCAVV(temp);
		}
		
		if((temp=getNodeValue(doc, "cavvAlgorithm")) !=null){
			paRes.setCAVVAlgo(temp);
		}
		
		return paRes;
	}
	
	
	/**
	 * <Extension id="visa.3ds.india_ivr" critical="false">
	 * <npc356chphoneidformat>I</npc356chphoneidformat>
	 * <npc356chphoneid>16505551234</npc356chphoneid>
	 * <npc356pareqchannel >DIRECT</ npc356pareqchannel>
	 * <npc356shopchannel>IVR|WAP|J2ME|native-app|USSD|TTP|PAT</npc356shopchannel>
	 * <npc356availauthchannel>SMS</npc356availauthchannel>
	 * </Extension>
	 * @param request
	 * @param logger
	 * @return
	 */
	public static VEReqExtension getParsedIndiaIvrVEReqExtension(String extXml, Logger logger){
		if(extXml == null || "".equals(extXml.trim())){
			logger.log("Null Extensions element.");
			return null;
		}
		
		Document doc = createDocument(extXml, logger);
		if(doc == null){
			logger.log("Bad Extensions element, could not parse [" + extXml + "]");
			return null;
		}
		
		if(extXml.indexOf(INDIA_IVR_EXTENSION_ID) < 0) {
			logger.log("Not India IVR Extension, not parsing [" + extXml + "]");
			return null;
		}

		String temp = null;
		VEReqExtension extension = new VEReqExtension();
		if((temp = getNodeAttrData(doc, IndiaIVRXMLConstants.EXTENSION, IndiaIVRXMLConstants.CRITICAL)) != null)
			extension.setCritical(new Boolean(temp));
		if((temp=getNodeValue(doc, IndiaIVRXMLConstants.PHONE_ID_FORMAT)) !=null)
			extension.setPhoneIdFormat(temp);
		if((temp=getNodeValue(doc, IndiaIVRXMLConstants.PHONE_ID)) !=null)
			extension.setPhoneId(temp);
		if((temp=getNodeValue(doc, IndiaIVRXMLConstants.PAREQ_CHANNEL)) !=null)
			extension.setPaReqChannel(temp);
		if((temp=getNodeValue(doc, IndiaIVRXMLConstants.SHOP_CHANNEL)) !=null)
			extension.setShopChannel(temp);
		if((temp=getNodeValue(doc, IndiaIVRXMLConstants.AUTH_CHANNEL)) !=null)
			extension.setAvailableAuthChannel(temp);
		
		//TODO Dirty fix for current VISA requirement, need to revisit again
		if(extXml.indexOf(IndiaIVRXMLConstants.ITP_CREDENTIAL) > -1){
			if((temp=getNodeValue(doc, IndiaIVRXMLConstants.ITP_CREDENTIAL)) != null)
				extension.setITPCredential(temp);
			else
				extension.setITPCredential("");
		}		
		return extension;
	}
	
	public static PAReqExtension getParsedIndiaIvrPAReqExtension(Node node, Logger logger){
		logger.log("Adding PAREQ extensions to PAREQ object.");
		PAReqExtension extension = new PAReqExtension();
		extension.setId(getNodeAttribute(node, IndiaIVRXMLConstants.ID));
		String isCritical = getNodeAttribute(node, IndiaIVRXMLConstants.CRITICAL);
		extension.setCritical(Boolean.valueOf(isCritical));
		
		NodeList nodeList = node.getChildNodes();
		List<ExtensionAttributes> attributes = new ArrayList<ExtensionAttributes>();
		if(nodeList != null && nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				NodeList attrList = nodeList.item(i).getChildNodes();
				if(attrList != null) {
					attributes.addAll(getExtensionAttribute(attrList));
				}
			}			
		}
		extension.setAuthUserData(attributes);
		return extension;
	}
	
	private static List<ExtensionAttributes> getExtensionAttribute(NodeList nodelist) {
		List<ExtensionAttributes> attributes = new ArrayList<ExtensionAttributes>();

		Node node = null;
		if(nodelist != null) {
			for(int i = 0; i < nodelist.getLength(); i++) {
				ExtensionAttributes attribute = new ExtensionAttributes();
				node = nodelist.item(i);
				
				attribute.setName(getNodeAttribute(node, IndiaIVRXMLConstants.NAME));
				attribute.setLength(getNodeAttribute(node, IndiaIVRXMLConstants.LENGTH));
				attribute.setType(getNodeAttribute(node, IndiaIVRXMLConstants.TYPE));
				attribute.setLabel(getNodeAttribute(node, IndiaIVRXMLConstants.LABEL));
				
				attribute.setPrompt(getNodeAttribute(node, IndiaIVRXMLConstants.PROMPT));
				attribute.setValue(getNodeAttribute(node, IndiaIVRXMLConstants.VALUE));
				attribute.setEncrypted(getNodeAttribute(node, IndiaIVRXMLConstants.ENCRYPTED));
				attribute.setStatus(getNodeAttribute(node, IndiaIVRXMLConstants.STATUS));
				attribute.setAuthenticated(getNodeAttribute(node, IndiaIVRXMLConstants.ITP_PA_AUTHENTICATED));
				attribute.setIdentifier(getNodeAttribute(node, IndiaIVRXMLConstants.ITP_PA_IDENTIFIER));
				attributes.add(attribute);
			}
		}
		return attributes;
	}
	
	private static String getNodeAttribute(Node node, String attributeName) {
		String value = null;
		NamedNodeMap nodeMap = node.getAttributes();
		Node childNode = null;
		if(nodeMap != null)
			childNode = nodeMap.getNamedItem(attributeName);
		if(childNode != null)
			value = childNode.getNodeValue();
		return value;
	}
	
	public static void main(String[] args) {
		VEResExtension ext = new VEResExtension();
		ext.setId(ACS.INDIA_IVR_EXTENSION_ID);
		ext.setCritical(false);
		
		ExtensionAttributes attr1 = new ExtensionAttributes();
		attr1.setName("OTP1");
		attr1.setLength("6");
		attr1.setType("N");
		attr1.setLabel("OTP");
		attr1.setPrompt("Please enter OTP sent by your bank");
		ext.addAuthData(attr1);
		
		ext.setAuthStatusMessage("OTP has been sent to your mobile");
		ext.setDataEncMandatory(true);
		ext.setAuthDataEncKeyValue("1234567890123456");
		ext.setAuthDataEncType("RSAAES256eRSARSARSAA");
		
		Logger logger = null;
		System.out.println(getVEResIvrExtensionXML(ext, logger));
		String xml = getVEResIvrExtensionXML((VEResExtension)ext, logger);
		Document extDoc = createDocument(xml, logger);
	}
}
