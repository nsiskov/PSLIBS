package com.arcot.apps.callout.receiptauthconfig;

/**
 * @author venan07
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.database.DatabaseConnection;
import com.arcot.dboperations.DBHandler;

public class ReceiptAuthConfig extends DBHandler {
	
	private static HashMap<String, String> mapAuthMethodToAuthChallenge = new HashMap<String, String> ();
	
	/**
	 * 
	 * @return mapAuthApproachToAuthChallenge of type HashMap, which has AuthMethod mapped to AuthChallenge code
	 */
	public static HashMap<String, String> getAuthMethodToAuthChallengeMap() {
		return mapAuthMethodToAuthChallenge;
	}

	/**
	 * Called from ACSServlet.
	 * Will create a map of CONFIGPARAMNAME, CONFIGVALUEINT of ARRECEIPTCONFIG table, which has CONFIGSTATUS=1. 
	 * i.e Authentication Method to Authentication Challenge Code map is created
	 * 
	 * @param logger
	 *  
	 */
	public static void populateAuthMethodToAuthChallengeMap(COLogger logger){
		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		while (true){
			try{
				conn = dbMan.getConnection();
				
				if (conn != null && !conn.isClosed()){
					
					stmt = conn.prepareStatement("SELECT CONFIGPARAMNAME, CONFIGVALUEINT from ARRECEIPTCONFIG where CONFIGSTATUS=1");
					
					rs = stmt.executeQuery();
					
					while(rs.next()){
						mapAuthMethodToAuthChallenge.put(rs.getString("CONFIGPARAMNAME"), Integer.toString(rs.getInt("CONFIGVALUEINT")));
					}
					logger.debug("Hashmap mapReceiptConfig successfuly populated");
				}else{
					logger.warn("ReceiptAuthConfig::populateReceiptConfigMap():Could not get DB connection");
					return;
				}
				break;
			}catch ( SQLException sqle ){
				try{
					if ( dbMan.failedDatabaseConnection(conn, sqle.getErrorCode()))
						continue;
				}catch ( SQLException sqle1 )
				{
					logger.error("ReceiptAuthConfig::populateReceiptConfigMap()", sqle1);
				}
				logger.error("ReceiptAuthConfig::populateReceiptConfigMap()", sqle);
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
					logger.error("ReceiptAuthConfig::populateReceiptConfigMap()", sqle);
				}
				if(conn != null)
					dbMan.release(conn);
			}
		}		
		return;
	}
	
}
