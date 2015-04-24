package com.arcot.apps.callout.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.arcot.apps.callout.matchlibrary.util.MatchException;

public class StringUtil {
	
	public static HashMap<String, String> parseStringToMap(String input, String tokenDelim, String pairDelim) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("default", "");
		if(input == null)
			return map;
		
    	if(StringUtil.isBlank(tokenDelim))
    		return null;
    	
    	if(StringUtil.isBlank(pairDelim))
    		return null;

		StringTokenizer tokens = new StringTokenizer(input, tokenDelim);
		while(tokens.hasMoreTokens()) {
			String property = tokens.nextToken();
			StringTokenizer pair = new StringTokenizer(property, pairDelim);
			if(pair.countTokens() == 2)
				map.put(pair.nextToken(), pair.nextToken());
			else if(pair.countTokens() == 1)
				map.put("default", pair.nextToken());
			else {
				System.out.println(pair.countTokens()+ pairDelim + tokenDelim);// +" - "+ pair[0]+" - "+ pair[1]);
				throw new RuntimeException("Input not proper.");
			}
		}
		return map;
	}
	
	public static HashMap<String, String> parseMapStringToMap(String input) {
		if(input == null || input.length() <2)
			return null;
		input = input.substring(1, input.length()-1);
		return parseStringToMap(input, ", ", "=");		
	}
	
	/**
     * Concatenates two byte arrays (array1 and array2)
     * @param array1
     * @param beginIndex1
     * @param length1
     * @param array2
     * @param beginIndex2
     * @param length2
     * @return the concatenated array
     */
    public static byte[] concat(byte[] array1, int beginIndex1, int length1, byte[] array2,
            int beginIndex2, int length2) {
        byte[] concatArray = new byte[length1 + length2];
        System.arraycopy(array1, beginIndex1, concatArray, 0, length1);
        System.arraycopy(array2, beginIndex2, concatArray, length1, length2);
        return  concatArray;
    }
    
    /**
     * @param   b       source byte array
     * @param   offset  starting offset
     * @param   len     number of bytes in destination (processes len*2)
     * @return  byte[len]
     */
    public static byte[] hex2byte (byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i=0; i<len*2; i++) {
            int shift = i%2 == 1 ? 0 : 4;
            d[i>>1] |= Character.digit((char) b[offset+i], 16) << shift;
        }
        return d;
    }
    
    /**
     * Bitwise XOR between corresponding bytes
     * @param op1 byteArray1
     * @param op2 byteArray2
     * @return an array of length = the smallest between op1 and op2
     */
    public static byte[] xor (byte[] op1, byte[] op2) {
        byte[] result = null;
        // Use the smallest array
        if (op2.length > op1.length) {
            result = new byte[op1.length];
        }
        else {
            result = new byte[op2.length];
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte)(op1[i] ^ op2[i]);
        }
        return  result;
    }
    
    /**
     * @param s source string (with Hex representation)
     * @return byte array
     */
    public static byte[] hex2byte (String s) {
        return hex2byte (s.getBytes(), 0, s.length() >> 1);
    }
    
	public static String getHexString(byte[] b){
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result.toUpperCase();
	}

    private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
 
    public static String generateSha1Hash(String text) throws MatchException {
    	if(StringUtil.isBlank(text))
    		return null;
    	String sha1;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] sha1hash = new byte[40];
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			sha1hash = md.digest();
			sha1 = convertToHex(sha1hash);
			if(sha1 != null)
				sha1 = sha1.toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			throw new MatchException(e.getMessage(), e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new MatchException(e.getMessage(), e.getMessage());
		}
		if(isBlank(sha1))
			throw new MatchException("SHA1_HASH_FAILED", "Blank after hashing.");
	    return sha1;
    }
    
    public static boolean isBlank(String s) {
    	if(s == null || "".equals(s.trim()))
    		return true;
    	return false;
    }
    
    public static boolean isBlankWithoutTrim(String s) {
    	if(s == null || "".equals(s))
    		return true;
    	return false;
    }
    
    public static String removeChars(String str, char splChrs[]) {
    	if(str == null || splChrs == null || splChrs.length < 1)
    		return str;
    	for(char ch:splChrs) {
    		str = removeChar(str, ch);
    	}
    	return str;
    }
    
    public static String removeChar(String str, char splChr) {
    	if(str == null)
    		return null;
        StringBuilder sb = new StringBuilder();
        char[] charAr = str.toCharArray();
        for (int i = 0; i < charAr.length; i++) {
            if (charAr[i] != splChr)
                sb.append(charAr[i]);
        }
        return sb.toString();
    }
    
    public static String removeNumerics(String str) {
    	if(str == null)
    		return null;
        StringBuilder sb = new StringBuilder();
        char[] charAr = str.toCharArray();
        for (int i = 0; i < charAr.length; i++) {
            if (!Character.isDigit(charAr[i]))
                sb.append(charAr[i]);
        }
        return sb.toString();
    }
    
    public static String removeNonNumerics(String str) {
    	if(str == null)
    		return null;
        StringBuilder sb = new StringBuilder();
        char[] charAr = str.toCharArray();
        for (int i = 0; i < charAr.length; i++) {
            if (Character.isDigit(charAr[i]))
                sb.append(charAr[i]);
        }
        return sb.toString();
    }
    
    public static String removeNonAlphaNumerics(String str) {
    	if(str == null)
    		return null;
        StringBuilder sb = new StringBuilder();
        char[] charAr = str.toCharArray();
        for (int i = 0; i < charAr.length; i++) {
            if (Character.isLetterOrDigit(charAr[i]))
                sb.append(charAr[i]);
        }
        return sb.toString();
    }
    
    public static String removeNonAlphaNumerics(String str, char[] ignoreChars) {
    	if(str == null)
    		return null;
    	
        StringBuilder sb = new StringBuilder();
        char[] charAr = str.toCharArray();
        for (int i = 0; i < charAr.length; i++) {
            if (Character.isLetterOrDigit(charAr[i])) {
                sb.append(charAr[i]);
            } else {
            	for(char ch : ignoreChars) {
            		if(ch == charAr[i])
            			sb.append(charAr[i]);
            	}
            }
        }
        return sb.toString();
    }
    
    public static String generateRandomStan(int length) {
    	StringBuilder random = new StringBuilder();
    	random.append(String.valueOf(System.currentTimeMillis()));
    	while(length > random.length())
    		random.append(random);
    	return random.substring(random.length() - length, random.length());
    }

}
