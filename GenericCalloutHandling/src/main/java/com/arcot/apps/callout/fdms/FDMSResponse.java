package com.arcot.apps.callout.fdms;

import java.io.IOException;

import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;

public class FDMSResponse
{

	String avs = null;
	String ordernum = null;
	String error = null;
	String approved = null;
	String code = null;
	String message = null;
	String time = null;
	String ref = null;
	String tdate = null;
	String tax = null;
	String shipping = null;
	String score = null;

	String completeResponse = null;
	String addrNumResponse = null;
	String zipResponse = null;
	String cvvResponse = null;
	String rawAVSResponse = null;
	boolean isValidAVSResponse = false;
	String arcotError = null;

	public static final String FDMS_APPROVED = "APPROVED";

	public FDMSResponse()
	{
	}

	public void setResponse(COLogger logger, CORequest request, String response)
	{
		completeResponse = response;
		if (response != null && response.indexOf("<r_fdms_custom>") > -1)
		{
			int index = response.indexOf("<r_avs>");
			int endIndex = -1;
			if (index > -1)
			{
				endIndex = response.indexOf("</r_avs>");
				avs = response.substring(index + 7, endIndex);

				if (avs == null || "".equals(avs))
				{
					logger.log(request, "AVS reponse is blank, checking r_error tag for AVS");
					avs = parseFromErrorTag(response);
				}

				if (avs != null)
				{
					avs = avs.trim();
					if (avs.length() == 1)
					{
						cvvResponse = avs;
						isValidAVSResponse = true;
					}
					else if (avs.length() == 4)
					{
						addrNumResponse = String.valueOf(avs.charAt(0));
						zipResponse = String.valueOf(avs.charAt(1));
						rawAVSResponse = String.valueOf(avs.charAt(2));
						cvvResponse = String.valueOf(avs.charAt(3));
						isValidAVSResponse = true;
					}
					else
					{
						// Invalid AVS response, do not fill the three values
					}
				}

			}

			index = response.indexOf("<r_ordernum>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_ordernum>");
				ordernum = response.substring(index + 12, endIndex);
			}

			index = response.indexOf("<r_error>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_error>");
				error = response.substring(index + 9, endIndex);
			}

			index = response.indexOf("<r_approved>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_approved>");
				approved = response.substring(index + 12, endIndex);
			}
			else
			{
				arcotError = "-FDMSERR-FDMS_RESP_NOAPPRCODE";
			}

			index = response.indexOf("<r_code>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_code>");
				code = response.substring(index + 8, endIndex);
			}

			index = response.indexOf("<r_message>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_message>");
				message = response.substring(index + 11, endIndex);
			}

			index = response.indexOf("<r_time>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_time>");
				time = response.substring(index + 8, endIndex);
			}

			index = response.indexOf("<r_ref>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_ref>");
				ref = response.substring(index + 7, endIndex);
			}

			index = response.indexOf("<r_tdate>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_tdate>");
				tdate = response.substring(index + 9, endIndex);
			}

			index = response.indexOf("<r_tax>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_tax>");
				tax = response.substring(index + 7, endIndex);
			}

			index = response.indexOf("<r_shipping>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_shipping>");
				shipping = response.substring(index + 12, endIndex);
			}

			index = response.indexOf("<r_score>");
			if (index > -1)
			{
				endIndex = response.indexOf("</r_score>");
				score = response.substring(index + 9, endIndex);
			}
		}
		else
		{
			logger.log(request, "FDMS response Incorrect");
			arcotError = "-FDMSERR-FDMS_RESP_XML_Incorrect";
		}

	}

	public String getAvs()
	{
		return avs;
	}

	public String getOrdernum()
	{
		return ordernum;
	}

	public String getError()
	{
		return error;
	}

	public String getApproved()
	{
		return approved;
	}

	public String getCode()
	{
		return code;
	}

	public String getMessage()
	{
		return message;
	}

	public String getTime()
	{
		return time;
	}

	public String getRef()
	{
		return ref;
	}

	public String getTdate()
	{
		return tdate;
	}

	public String getTax()
	{
		return tax;
	}

	public String getShipping()
	{
		return shipping;
	}

	public String getScore()
	{
		return score;
	}

	public String getCompleteResponse()
	{
		return completeResponse;
	}

	public String getAddrNumResponse()
	{
		return addrNumResponse;
	}

	public String getZipResponse()
	{
		return zipResponse;
	}

	public String getCvvResponse()
	{
		return cvvResponse;
	}

	public String getRawAVSResponse()
	{
		return rawAVSResponse;
	}

	public boolean isValidAVSResponse()
	{
		return isValidAVSResponse;
	}

	public String getArcotError()
	{
		return arcotError;
	}

	public void setArcotError(String arcotError)
	{
		this.arcotError = arcotError;
	}

	public String toString()
	{
		return "FDMSResponse[" + completeResponse + "]";
	}

	private String parseFromErrorTag(String response)
	{

		int errind = response.indexOf("<r_error>");

		if (errind < 0)
			return null;

		String pattern = "D:Declined";

		int msgind = response.indexOf(pattern, errind);

		if (msgind < 0)
			return null;

		return response.substring(msgind + pattern.length(),
				response.indexOf(":</r_error>", errind));

	}

	/**
	 * @param args
	 * @throws Exception
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, Exception
	{
/*
		CORequest coreq = new CORequest();

		COLogger logger = new COLogger("C:\\arcot\\work\\MAR12\\01Mar\\FDMS\\logs\\fdms.log", true);

		coreq.setBankId(101);
		coreq.setCardholderName("Test Card");
		coreq.setCardNumber("4358800000000234");
		coreq.setRangeId(202);

		FDMSResponse response = new FDMSResponse();
		response.setResponse(
				logger,
				coreq,
				"<r_fdms_custom><r_csp>FDMS</r_csp><r_time>Wed Feb 29 11:00:49 2012</r_time><r_ref>0983976862</r_ref><r_error></r_error><r_ordernum>D888973E-4F4E4BB0-699-51108</r_ordernum><r_message>APPROVED</r_message><r_code>0012780983976862:   M:</r_code><r_tdate>1330531248</r_tdate><r_score></r_score><r_authresponse>X07</r_authresponse><r_approved>APPROVED</r_approved><r_avs>M</r_avs><r_avs_addrnum_custom> </r_avs_addrnum_custom><r_avs_zip_custom> </r_avs_zip_custom><r_avs_ipgs_custom> </r_avs_ipgs_custom><r_avs_cvmvalue_custom>M</r_avs_cvmvalue_custom><r_error_num_custom>0</r_error_num_custom><r_error_msg_custom>Success</r_error_msg_custom></r_fdms_custom>");

		
		
		printResponse(response);

		response = new FDMSResponse();
		response.setResponse(
				logger,
				coreq,
				"<r_fdms_custom><r_csp></r_csp><r_time>Tue Feb 28 19:53:02 2012</r_time><r_ref></r_ref><r_error>SGS-000001: D:DeclinedNNNN:</r_error><r_ordernum>D888973E-4F4D76ED-704-4FB6A</r_ordernum><r_message>DECLINED</r_message><r_code></r_code><r_tdate>1330476781</r_tdate><r_score></r_score><r_authresponse>107</r_authresponse><r_approved>DECLINED</r_approved><r_avs></r_avs></r_fdms_custom>");

		System.out.println("\n-------------------------------------------------");
		printResponse(response);
		
		
		response = new FDMSResponse();
		response.setResponse(
				logger,
				coreq,
				"<r_fdms_custom><r_csp></r_csp><r_time>Tue Feb 28 21:40:16 2012</r_time><r_ref></r_ref><r_error>SGS-000001: D:DeclinedNYWN:</r_error><r_ordernum>D888973E-4F4D900F-318-4FB6A</r_ordernum><r_message>DECLINED</r_message><r_code></r_code><r_tdate>1330483215</r_tdate><r_score></r_score><r_authresponse>107</r_authresponse><r_approved>DECLINED</r_approved><r_avs></r_avs></r_fdms_custom>");

		System.out.println("\n-------------------------------------------------");
		printResponse(response);
		logger.closeFile();
*/
	}
/*
	private static void printResponse(FDMSResponse response) throws IllegalArgumentException,
			IllegalAccessException
	{

		System.out.println("avs : "+response.avs);
		System.out.println("ordernum : "+response.ordernum);
		System.out.println("error : "+response.error);
		System.out.println("approved : "+response.approved);
		System.out.println("code : "+response.code);
		System.out.println("message : "+response.message);
		System.out.println("time : "+response.time);
		System.out.println("ref : "+response.ref);
		System.out.println("tdate : "+response.tdate);
		System.out.println("tax : "+response.tax);
		System.out.println("shipping : "+response.shipping);
		System.out.println("score : "+response.score);
		System.out.println("completeResponse : "+response.completeResponse);
		System.out.println("addrNumResponse : "+response.addrNumResponse);
		System.out.println("zipResponse : "+response.zipResponse);
		System.out.println("cvvResponse : "+response.cvvResponse);
		System.out.println("rawAVSResponse : "+response.rawAVSResponse);
		System.out.println("isValidAVSResponse : "+response.isValidAVSResponse);
		System.out.println("arcotError : "+response.arcotError);


	}
*/
}
