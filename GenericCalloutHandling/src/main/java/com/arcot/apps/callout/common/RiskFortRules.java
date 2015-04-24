/**
 * 
 */
package com.arcot.apps.callout.common;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.logger.ArcotLogger;
import com.arcot.util.GenericValidator;

/**
 * @author porja04
 * 
 */
public class RiskFortRules
{

	private RiskFortRules()
	{

	}

	/**
	 * @param riskAdvice
	 *            RiskAdvice retrieved from given ACSRequest
	 *            {@link ACSRequest#getRiskAdvice()}
	 * @param config
	 *            COConfig object
	 * @return appropriate RA_ACTION, as configured in Callout configuration
	 * @throws IllegalArgumentException
	 *             if riskAdvice is invalid or the Action configured in callout
	 *             does not belong to {@link RA_ACTION} enum.
	 */
	public static RA_ACTION getActionForRiskAdvice(int riskAdvice, COConfig config)
	{

		String strAdvice = getRAConfigName(riskAdvice);

		if (strAdvice == null)
		{
			throw new IllegalArgumentException("Invalid Risk Advice : " + riskAdvice);
		}

		String configuredAction = config.getValue(strAdvice);

		if (!GenericValidator.isBlankOrNull(configuredAction))
		{
			return RA_ACTION.valueOf(configuredAction);
		}
		else
		{
			return RA_ACTION.PERFORM_AUTH;
		}

	}
	
	
	public static RA_ACTION getActionForRiskAdvice(ACSRequest request, COConfig config)
	{

		int riskAdvice = request.getRiskAdvice();
		String mnemonic = request.getMatchedRuleMnemonic();
		
		String strAdvice = getRAConfigName(riskAdvice);
		
		if (!GenericValidator.isBlankOrNull(strAdvice)){
			String configuredAction = config.getValue(strAdvice);
			ArcotLogger.logInfo("configuredAction:"+configuredAction+",riskAdvice:"+riskAdvice+",strAdvice:"+strAdvice+",mnemonic:"+mnemonic);
			if (!GenericValidator.isBlankOrNull(configuredAction)){
				String[] configArray = configuredAction.split("~");
				if(configArray!=null && configArray.length>1)
				{	
					ArcotLogger.logInfo("configArray[0]:"+configArray[0]+",configArray[1]"+configArray[1]);
					if (!GenericValidator.isBlankOrNull(configArray[0]))
					{
						if("NONE".equals(configArray[0]))
							return RA_ACTION.valueOf(configArray[1]);
						else if(configArray[0].equals(mnemonic) && request.getIsAbridged()<100)
							return RA_ACTION.valueOf(configArray[1]);
					}
				}	
			}
		}else{
			ArcotLogger.logInfo("No riskAdvice....");
		}
		return null;
	}

	private static String getRAConfigName(int riskAdvice)
	{
		switch (riskAdvice)
		{

		case ACS.RA_ALLOW:
			return "RA_ALLOW";

		case ACS.RA_ALERT:
			return "RA_ALERT";

		case ACS.RA_DENY:
			return "RA_DENY";

		case ACS.RA_INCREASE_AUTH:
			return "RA_INCREASE_AUTH";

		}

		return null;
	}

	/**
	 * @author porja04
	 * 
	 *         Enumeration defining possible Flow Actions for given RiskAdvice
	 *         {@link ACSRequest#getRiskAdvice()}
	 * 
	 */
	public enum RA_ACTION
	{
		ATTEMPTS(ACS.ACS_CALLOUT_ATTEMPTS, "ATTEMPTS", true, null), SILENT_ATTEMPTS(ACS.ACS_CALLOUT_PARES_A,
				"ATTEMPTS", true, null), PERFORM_AUTH(ACS.ACS_CALLOUT_SUCCESS, "AUTH", true, null), DENY(
				ACS.ACS_CALLOUT_PARES_N, "DENY", false, null), SILENT_DENY(ACS.ACS_CALLOUT_SEND_PARES_N, "DENY",
				false, null), SEND_Y(ACS.ACS_CALLOUT_PARES_Y, "ALLOW", true, null),
				PERFORM_AUTH_OTP(ACS.ACS_CALLOUT_SUCCESS, "AUTH", true, "OTP"),
				PERFORM_AUTH_PASSWORD(ACS.ACS_CALLOUT_SUCCESS, "AUTH", true, "PASSWORD"),
				PERFORM_AUTH_OTP_PASSWORD(ACS.ACS_CALLOUT_SUCCESS, "AUTH", true, "OTP_PASSWORD");

		int acsRetStatus = 0;
		String statusLog = null;
		boolean status = false;
		String flow = null;
		// null; OTP - Call Pareq; PASSWORD - Dont need to call pareq, set flow param, OTP_PASSWORD - Need to call pareq
		

		public String getFlow() {
			return flow;
		}

		public void setFlow(String flow) {
			this.flow = flow;
		}

		private RA_ACTION(int acsRetStatus, String statusLog, boolean status, String flow)
		{
			this.acsRetStatus = acsRetStatus;
			this.statusLog = statusLog;
			this.status = status;
			this.flow = flow;
		}

		public int getAcsRetStatus()
		{
			return acsRetStatus;
		}

		public String getCOStatusLog()
		{
			return statusLog;
		}

		public boolean getResult()
		{
			return status;
		}

	}

}
