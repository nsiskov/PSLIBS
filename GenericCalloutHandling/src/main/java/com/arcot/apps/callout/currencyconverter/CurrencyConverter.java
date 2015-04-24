package com.arcot.apps.callout.currencyconverter;

/**
 * @author bossa02
 * 
 * Currency Conversion
 * 
 * Class used to create an object of table ARRFCURRCONVRATES
 * Called from <pre> ACSServlet </pre> for initialization of this table into a Map
 * Callouts will interact with this map to get a row of ARRFCURRCONVRATES; for a given Currency.
 *  
 */
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.arcot.apps.callout.common.COLogger;
import com.arcot.database.DatabaseConnection;
import com.arcot.dboperations.DBHandler;

public class CurrencyConverter extends DBHandler {
	
	private static HashMap<Integer, ArRFCurrConvRates> mapCurrency = new HashMap<Integer, ArRFCurrConvRates> ();	
	
	/**
	 * Called from ACSServlet 
	 * Will create a map of rows in ARRFCURRCONVRATES table
	 * 
	 * 
	 * @param logger
	 * @param version - this is by default set to '1'; 
	 * in case there is no CurrencyConversionVersion set in ARESCONFIG table
	 */
	public static void fetchCurrencyConvRate(COLogger logger, int version){
		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		while (true){
			try{
				conn = dbMan.getConnection();
				
				if (conn != null && !conn.isClosed()){
					
					if (version > 1)
						stmt = conn.prepareStatement("SELECT * from ARRFCURRCONVRATES where version="+version);
					else
						stmt = conn.prepareStatement("SELECT * from ARRFCURRCONVRATES where version=1");
					
					rs = stmt.executeQuery();
					
					while(rs.next()){
						ArRFCurrConvRates currObj = new ArRFCurrConvRates();
						currObj.setVersion(rs.getInt("VERSION"));
						int intCurrFrom = rs.getInt("CURR_FROM");
						currObj.setCurr_from(intCurrFrom);
						currObj.setCurr_to(rs.getInt("CURR_TO"));
						currObj.setCurr_from_str(rs.getString("CURR_FROM_STR"));
						currObj.setCurr_to_str(rs.getString("CURR_TO_STR"));
						currObj.setConv_rate(rs.getBigDecimal("CONV_RATE"));
						currObj.setCurr_name_and_notes(rs.getString("CURR_NAME_AND_NOTES"));
						mapCurrency.put(intCurrFrom, currObj);
						logger.debug(currObj.toString());
					}
				}else{
					logger.warn("CurrencyConverter::fetchCurrencyConvRate():Could not get DB connection");
					return;
				}
				break;
			}catch ( SQLException sqle ){
				try{
					if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
						continue;
				}catch ( SQLException sqle1 )
				{
					logger.error("CurrencyConverter::fetchCurrencyConvRate()", sqle1);
				}
				logger.error("CurrencyConverter::fetchCurrencyConvRate()", sqle);
				return;
			}finally{
				try{
					if ( rs != null ){
						rs.close();
					}
					if ( stmt != null ){
						stmt.close();
					}
				}catch ( SQLException sqle ){
					logger.error("CurrencyConverter::fetchCurrencyConvRate()", sqle);
				}
				if(conn != null)
					dbMan.release(conn);
			}
		}		
		return;
	}
	
	/**
	 * 
	 * Called from Module which needs to know the Conversion rate
	 * This method provides the ArRFCurrConvRates Object containing (VERSION, CURR_FROM, CURR_FROM_STR, CURR_TO, CURR_TO_STR, CONV_RATE, CURR_NAME_AND_NOTES) for a given Currency Code.
	 * 
	 * <pre>
	 * (ArRFCurrConvRates)CurrencyConverter.getCurrencyMapObj(978, logger)
	 * </pre>
	 * 
	 * @param trxCurrency - Currency Code for which the ARRFCURRCONVRATES row needs to be extracted.
	 * @return
	 */
	public static ArRFCurrConvRates getCurrencyMapObj(int trxCurrency, COLogger logger){
		if (mapCurrency.containsKey(trxCurrency)){
			return (ArRFCurrConvRates)mapCurrency.get(trxCurrency);
		}else{
			logger.warn("No Data present for Currency Code [ "+trxCurrency+" ] in ARRFCURRCONVRATES table");
			return null;
		}
	}
	
	/**
	 * 
	 * Called from Module which needs to know the Conversion rate
	 * Presently converts the amount into USD; as the base Currency is in USD
	 * 
	 * EUR --> USD
	 * 
	 * <pre>
	 * CurrencyConverter.getConvertedAmountToUSD(978, new BigDecimal(10), logger)
	 * </pre>
	 * 
	 * @param trxCurrency - Currency Code FROM which the amount needs to be converted TO USD.
	 * @param trxAmount
	 * @return
	 */
	public static BigDecimal getConvertedAmountToUSD(int trxCurrency, BigDecimal trxAmount, COLogger logger){
		BigDecimal convertedAmt;
		ArRFCurrConvRates currConvObj = null;
		currConvObj = getCurrencyMapObj(trxCurrency, logger);
		if (currConvObj == null) return new BigDecimal(1);
		convertedAmt = trxAmount.multiply(currConvObj.getConv_rate(), MathContext.DECIMAL128);
		return convertedAmt;
	}
	
	/**
	 * 
	 * Called from Module which needs to know the Conversion rate
	 * Presently converts the amount from USD, as the base Currency is in USD, to the Currency Specified
	 * 
	 * USD --> EUR
	 * 
	 * <pre>
	 * CurrencyConverter.getConvertedAmountFromUSD(978, new BigDecimal(10), logger)
	 * </pre>
	 * 
	 * @param trxCurrency - Currency Code TO which the amount needs to be converted FROM USD.
	 * @param trxAmount
	 * @return
	 */
	public static BigDecimal getConvertedAmountFromUSD(int trxCurrency, BigDecimal trxAmount, COLogger logger){
		BigDecimal convertedAmt;
		ArRFCurrConvRates currConvObj = null;
		currConvObj = getCurrencyMapObj(trxCurrency, logger);
		if (currConvObj == null) return new BigDecimal(1);
		convertedAmt = trxAmount.divide(currConvObj.getConv_rate(), MathContext.DECIMAL128);
		return convertedAmt;
	}
	
	/**
	 * 
	 * Called from Module which needs to know the Conversion rate
	 * Converts from CURR1 to CURR2; assuming base currency as USD
	 * 
	 * EUR --> INR
	 * 
	 * <pre>
	 * CurrencyConverter.getConvertedAmountFromCurr1ToCurr2(978, 356, new BigDecimal(10), logger)
	 * </pre>
	 * 
	 * @param frmTrxCurrency - Currency Code in which the trx is done
	 * @param toTrxCurrency - Currency Code to which the trx amount needs to be converted.
	 * @param trxAmount
	 * @param logger
	 * @return
	 */
	public static BigDecimal getConvertedAmountFromCurr1ToCurr2(int frmTrxCurrency, int toTrxCurrency, BigDecimal trxAmount, COLogger logger){
		BigDecimal convertedAmt;
		BigDecimal convertedAmtInUSD = getConvertedAmountToUSD(frmTrxCurrency, trxAmount, logger);
		convertedAmt = getConvertedAmountFromUSD(toTrxCurrency, convertedAmtInUSD, logger);
		return convertedAmt;
	}
	
}
