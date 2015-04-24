/**
 * 
 */
package com.arcot.apps.callout.pinparser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;

/**
 * @author Thejas P Murthy (murte02)
 *
 */

/** API USER GUIDE
 * <p>PreRequisites : 
 * 	 
 * 		<p>1)The Callout config associated to the issuer should have the Pin format specifications with a key "<i>ADS_QUESTION_MAP</i>"
 * 	
 * 		<p>2)The Callout configuration entry should be in the format "<i>CVV:3:3:N~MMN:6:30:A~PINCODE:6:6:N</i>"
 * 
 * 		<p>3)The Cap Pin should be constructed in the format <i>question1=answer1~question2=answer2~question3=answer3 (CVV=777~MMN=Hello World~PINCPDE=570020)</i>
 * 			(To Discuss= any possibility of = or ~ being in the answer? they are both supported special characters in TransFort)
 * 
 * <p>Disecting each entry of the Format:
 * 		<p>Input String =       <i>CVV:3:3:N</i>
 * 		<p>Output Index Order = 	0---1-2-3
 * 
 * 		 <p> Index
 * 			<p>0		The Question Name(Same to be used in CAP PIN)
 *  		<p>1		The lowerBound Length of Answer
 * 			<p>2		The UpperBound Length of Answer
 *  		<p>3		The Format of allowed characters (Find the list below)
 * 
 * 		<p>Exhaustive list of Valid Format Tags, to be published to DEV's and PM's
 * 
 * 		<p>'A' = Alphabets Only + case sensitive
 * 		<p>'B' = AlpaNumeric only + case Sensitive
 *  	<p>'C' = AlphaNumeric and Special chars ( hopefully except &<>"%;+\? )	
 *  	<p>'N' = Numeric only
 *  	<p>'a' = alphabets only + case insensitive
 *  	<p>'b' = alphaNumeric only + case insensitive
 * 
 *********************************************************************************************************************************************/
public class AcsPinParser {

	//Constants
	private static String ADS_QUESTION_MAP = "ADS_QUESTION_MAP";
	
	/**
	 * Input:
	 * 	ACSRequest object got from ACS servlet.
	 *  COConfig object got from ACS servlet.
	 *  StringBuffer (cant use a Boolean or Integer :( - they are immutable and autoBoxing occurs) 
	 *  	(Used as return by reference)true if there was an exception or error.
	 *  StringBuffer  (Used as return by reference) error code corresponding to what wrong happened - to be used to logToDB
	 *  -- Might combine above two into an array --
	 *  
	 *  Output:
	 *  HashMap<String, String>(synchronization is not an issue here) - Key value pairs of <questions from callout config,answers from cap pin> .
	 */
	synchronized public static HashMap<String, String> getAnswers(ACSRequest request,COConfig config,StringBuffer isError,StringBuffer errCode)
	{
		COLogger logger = config.getLogger();
		
		String pinFrmCap = request.getIssuerAnswer();
		if(null == pinFrmCap || "".equals(pinFrmCap) )
		{
			/*IssuerCAPFolderCache icfc = (IssuerCAPFolderCache) application.getAttribute("ICFC");
			IssuerCallOutConfigCache icocfc = (IssuerCallOutConfigCache) application.getAttribute("ICOCFC");*/
			
			logger.log(request,"AcsPinParser: The Pin from CAP is Null of Blank - CAP dir folderid = "+request.getFolderId()/*"Log folder from above code"*/);
			isError.append("1");
			errCode.append("CapPin[Emp]");
			return null;
		}
		
		String configStruct = config.getValue(ADS_QUESTION_MAP);
		if(null == configStruct || "".equals(configStruct))
		{
			/*IssuerCAPFolderCache icfc = (IssuerCAPFolderCache) application.getAttribute("ICFC");
			IssuerCallOutConfigCache icocfc = (IssuerCallOutConfigCache) application.getAttribute("ICOCFC");*/
			logger.log(request,"AcsPinParser: The Callout Config  from config is Null of Blank - check ADS_QUESTION_MAP entry in the config "/*  "Log Config from above code"*/);
			isError.append("1");
			errCode.append("ConfigMap[Emp]");
			return null;
		}
		String regEx = "~";
		
		String configQuestion[] = splitString(regEx,configStruct);
		String QnAfrmCap[] = splitString(regEx,pinFrmCap);
		logger.log(request,"AcsPinParser: Number of Qestions from Config = "+configQuestion.length + "& Number of QnA pairs from Config = "+ QnAfrmCap.length);
		/**
		 * The length of above two array should be same ideally, 
		 * considering the developer might want to send something extra for saving in his/her callout lets not check on this.
		 * So should the pairs from CAP should always be greater or equal to pairs from Config?
		 * 	No what if there are non mandatory Questions- lets leave it to the joy of developer to handle.
		 *  populate a Null string if we don't find an answer from cap, |||ly don't populate whats extra on CAP.
		 */
		
		//Store Answers from CAP in a MAP
		HashMap<String, String> answerTable = new HashMap<String, String>();
		for(int i=0;i<QnAfrmCap.length;++i)
		{
			if(null != QnAfrmCap[i] && "".equals(QnAfrmCap[i]))
			{
				String keyVal[] = splitString("=", QnAfrmCap[i]);
				if(null != keyVal[0] && "".equals(keyVal[0]) && (null != keyVal[0] && "".equals(keyVal[0])) )
				{
					answerTable.put(keyVal[0], keyVal[1]);
				}
			}
			
		}
		
		/**
		 * For every question in Config check if corresponding QnA pair has come from CAP.
		 * 	If such an entry exists in the answersTable do the Format checks.
		 */
		
		for(int i=0;i<configQuestion.length;++i)
		{
			
			if(null != QnAfrmCap[i] && "".equals(QnAfrmCap[i]))
			{
				/**
				 * Input String =       CVV:3:3:N
				 * Output Index Order = 0---1-2-3
				 * 
				 * Index
				 * 	0		The Question Name
				 *  1		The lowerBound Length of Answer
				 *  2		The UpperBound Length of Answer
				 *  3		The Format of allowed characters (i.e A= Alphabets,B= AlphaNumeric,N=Numbers,C=AlphaNumericSpecialChars,....)
				 */
				String questionFormatPair[] = splitString(":", QnAfrmCap[i]);
				if(null != questionFormatPair[0] && "".equals(questionFormatPair[0]))
				{
					if(answerTable.containsKey(questionFormatPair[0]))
					{
						/*
						 * 
						 */
						boolean resultFormatCheck = checkFormat(questionFormatPair,answerTable.get(questionFormatPair[0]));
						if(!resultFormatCheck)
						{
							logger.log(request,"Failure in FormatCheck for the value of \" "+questionFormatPair[0]+" \"");
							isError.append("1");
							errCode.append(questionFormatPair[0]+"[IncFormat]");
							return null;
						}
					}
				}
			}
		}
		
		return answerTable;
		
	}

	synchronized private static boolean checkFormat(String FormatArr[],String value) 
	{
		
		////(Just for Safety - future proof )
		if(isBlankOrNull(value))
		{
			// answer cannot be null or blank
			return false;
		}
		if(((isBlankOrNull(FormatArr[1])) || (isBlankOrNull(FormatArr[2]))))
		{
			//not performing data field length validations
		}
		else
		{
			int lowerBound = Integer.parseInt(FormatArr[1]);
			int upperBound = Integer.parseInt(FormatArr[2]);
			int ansLength = value.length();
			if(ansLength < lowerBound || ansLength > upperBound)
			{
				return false;
			}
		}
		
		char format = FormatArr[3].charAt(0);
		
		/***************
		 * 
		 * Exhaustive list of Valid Format Tags, to be published to DEV's and PM's
		 * 
		 * 	'A' = Alphabets Only + case sensitive
		 *  'B' = AlpaNumeric only + case Sensitive
		 *  'C' = AlphaNumeric and Special chars ( hopefully except &<>"%;+\? )	
		 *  'N' = Numeric only
		 *  'a' = alphabets only + case insensitive
		 *  'b' = alphaNumeric only + case insensitive
		 * 
		 ***************/
		
		boolean returnValue = false;
		switch(format)
		{
			//Alphabets Only + case sensitive
			case 'A':
			{
				returnValue =  correctAnsFormat(value,"[^a-z]+");
				break;
			}
			//AlpaNumeric only + case Sensitive
			case 'B':
			{
				returnValue =  correctAnsFormat(value,"[^a-z0-9]+");
				break;
			}
			//AlphaNumeric and Special chars ( hopefully except &<>"%;+\? )
			case 'C':
			{
				/**** 
				 	\" \\ are escape sequences for " and \
					To test if needed (Tested and Needed) - as we are blacklisting characters just a small tweak to use the correctAnsFormat - negate its response
				 	Basically the function returns true if it finds any of the blacklisted chars-  negate the response to send format failure :).
				 */
				returnValue = !(correctAnsFormat(value,"[&<>\"%;+\\?]+"));
				break;
			}
			//Numeric only
			case 'N':
			{
				returnValue =  correctAnsFormat(value,"[^0-9]+");
				break;
			}
			//alphabets only + case insensitive
			case 'a':
			{
				returnValue =  correctAnsFormat(value,"[^a-zA-Z]+");
				break;
			}
			//alphaNumeric only + case insensitive
			case 'b':
			{
				returnValue =  correctAnsFormat(value,"[^a-zA-Z0-9]+");
				break;
			}
			//no valid format tag, so assume all is well.
			default:
			{
				returnValue = true;
			}
		}
		
		return returnValue;
	}

	synchronized private static String[] splitString(String REGEX, String input)
	{
		// TODO Auto-generated method stub
		Pattern p = Pattern.compile(REGEX);
		String[] result = p.split(input);
		return result;
	}
	
	synchronized private static boolean correctAnsFormat(String sPassed, String regex) 
	{

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sPassed);
        //If u found a regEx voilation as per regex
        if(matcher.find())
        {
        	return false;
        }
        return true;

   }
	
	synchronized public static boolean isBlankOrNull(String str) 
	{
		return (str == null || "".equals(str));
	}
}
