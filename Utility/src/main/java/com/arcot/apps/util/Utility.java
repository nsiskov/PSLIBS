package com.arcot.apps.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.Properties;

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
import org.xml.sax.SAXException;

public class Utility {
	public static String maskPan(String pan) {
		int len = pan.length();
		if (len < 4)
			return pan;
		StringBuffer temp = new StringBuffer("************");
		temp.append(pan.substring((len - 4), len));
		return temp.toString();
	}

	public static byte[] getBytesFromFile(String xmlFilePath) throws Exception {

		byte[] bytes = null;
		File file = new File(xmlFilePath);
		InputStream is = null;
		is = new FileInputStream(file);
		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// throw new Exception("Too large file");
			System.err.println("File too large!");
		}

		// Create the byte array to hold the data
		bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			// throw new IOException("Could not completely read file " +
			// file.getName());
			System.err.println("Could not completely read file "
					+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	public static Document bytesToXml(byte[] xml) throws SAXException,
			ParserConfigurationException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new ByteArrayInputStream(xml));
	}

	public static String getXMLFromDocument(Document doc) {
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = null;
		/*
		 * Simply catching the exception and logging it assuming that the
		 * exception can occur only if XML messages are wrong and hence it will
		 * be an implementation problem that WILL require implementation change
		 */
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return writer.toString();
	}

	public static byte[] decode(String base64) {

		byte[] raw;
		if (base64 != null) {
			int pad = 0;
			for (int i = base64.length() - 1; base64.charAt(i) == '='; i--)
				pad++;

			int length = base64.length() * 6 / 8 - pad;
			raw = new byte[length];
			int rawIndex = 0;

			for (int i = 0; i < base64.length(); i += 4) {
				int block = (getValue(base64.charAt(i)) << 18)
						+ (getValue(base64.charAt(i + 1)) << 12)
						+ (getValue(base64.charAt(i + 2)) << 6)
						+ (getValue(base64.charAt(i + 3)));

				for (int j = 0; j < 3 && rawIndex + j < raw.length; j++)
					raw[rawIndex + j] = (byte) ((block >> (8 * (2 - j))) & 0xff);

				rawIndex += 3;
			}
		} else
			raw = new byte[0];
		return raw;
	}

	protected static int getValue(char c) {
		if (c >= 'A' && c <= 'Z')
			return c - 'A';
		if (c >= 'a' && c <= 'z')
			return c - 'a' + 26;
		if (c >= '0' && c <= '9')
			return c - '0' + 52;
		if (c == '+')
			return 62;
		if (c == '/')
			return 63;
		if (c == '=')
			return 0;

		return -1;
	}

	public static Properties getEnvVars() throws IOException {
		Process p = null;
		Properties envVars = new Properties();
		Runtime r = Runtime.getRuntime();
		String OS = System.getProperty("os.name").toLowerCase();
		// System.out.println(OS);
		if (OS.indexOf("windows 9") > -1) {
			p = r.exec("command.com /c set");
		} else if ((OS.indexOf("nt") > -1) || (OS.indexOf("windows 2000") > -1)) {
			p = r.exec("cmd.exe /c set");
		} else {
			// our last hope, we assume Unix (thanks to H. Ware for the fix)
			p = r.exec("env");
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(p
				.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			int idx = line.indexOf('=');
			String key = line.substring(0, idx);
			String value = line.substring(idx + 1);
			envVars.setProperty(key, value);
			// System.out.println( key + " = " + value );
		}
		return envVars;
	}
	
	public static String toHexString(byte[] bytes){
		StringBuffer hash = new StringBuffer();
		for (int i = 0; i < bytes.length; i++){
			hash.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
	    }
		
		if(hash.length() == 0){
			return null;
		}
		return hash.toString();

	}
	
	public static String getRandomInt(int length){
		StringBuffer challenge = new StringBuffer();
		SecureRandom random = new SecureRandom();
		int randomNumber = random.nextInt((int)Math.pow(10, length));
		String temp = Integer.toString(randomNumber);
		int randLength = temp.length();
		for(int i=0; i < (length - randLength); i++){
			challenge = challenge.append("0");
		}
		challenge.append(randomNumber);
		return challenge.toString();
	}
	
	public static String getHexLength(int length){
		byte[] hexLen = new byte[2];
		if (length < 256){
			hexLen[0] = (byte)0;
			hexLen[1] = (byte)length;
		}else{
			hexLen[0] = (byte) (length / 256);
			hexLen[1] = (byte) (length % 256);
		}
		return new String(hexLen);
	}

	public static void main(String str[]) {
		/*
		 * String xmlStr = null; xmlStr="D:/UBS/sample.xml"; byte[] bytes =
		 * null; bytes = new byte[xmlStr.length()]; try { bytes =
		 * getBytesFromFile(xmlStr); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * Document d = null; try { d = bytesToXml(bytes); } catch (SAXException
		 * e1) { // TODO Auto-generated catch block e1.printStackTrace(); }
		 * catch (ParserConfigurationException e1) { // TODO Auto-generated
		 * catch block e1.printStackTrace(); } catch (IOException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); } if (d == null)
		 * System.out.println("doc is null"); else
		 * System.out.println(getXMLFromDocument(d));
		 */
		// System.out.println(CryptoUtil.encrypt3DES64("uccencrypt",
		// EncodingUtil.toUpperCaseUTF(""), true));
		for (int i = 0; i < 10; i++) {
			System.out.println(getRandomInt(7));
		}
	}
}
