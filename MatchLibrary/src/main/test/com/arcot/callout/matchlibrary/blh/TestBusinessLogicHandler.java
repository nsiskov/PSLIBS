package test.com.arcot.callout.matchlibrary.blh;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.arcot.apps.callout.acs.ACS;
import com.arcot.apps.callout.acs.ACSRequest;
import com.arcot.apps.callout.acs.ACSResponse;
import com.arcot.apps.callout.common.BusinessLogicHandler;
import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.COLogger;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.common.COUtil;
import com.arcot.apps.callout.es.ES;
import com.arcot.apps.callout.es.ESRequest;
import com.arcot.apps.callout.es.ESResponse;
import com.arcot.apps.callout.matchlibrary.MatchLibrary;
import com.arcot.apps.callout.matchlibrary.field.DateField;
import com.arcot.apps.callout.matchlibrary.field.Field;
import com.arcot.apps.callout.matchlibrary.field.NameField;
import com.arcot.apps.callout.matchlibrary.field.NumericField;
import com.arcot.apps.callout.matchlibrary.field.RegExField;
import com.arcot.apps.callout.matchlibrary.result.MatchResult;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.database.DatabaseConnection;
import com.arcot.dboperations.DBHandler;
import com.arcot.vpas.enroll.EnrollmentCrypto;

public class TestBusinessLogicHandler extends DBHandler implements BusinessLogicHandler,ES, ACS {

	public ACSResponse process(ACSRequest request, COConfig config) {
		return null;
	}
	
	public ESResponse process(ESRequest request, COConfig config) {
		COLogger logger = config.getLogger();
		logger.log(request, "Callout event is: " + request.getCalloutEvent());
		if(ES_POST_ISSUER_QA == request.getCalloutEvent()) {
			logger.log(request, "Calloing PostIssuerQA");
			return doPostIssuerQA(request, config, logger);
		} else if(AA_LOGIN == request.getCalloutEvent()) {
		} else if(ES_BEFORE_FINISH == request.getCalloutEvent() || ES_AFTER_FINISH == request.getCalloutEvent()) {
		} else if(AA_POST_UPDATE_PROFILE == request.getCalloutEvent()) {
		}
		logger.log(request, "INVALID_MSG");
		return COUtil.prepareESResponse(request, COUtil.createCOResponse(false, 0, "INVALID_CAP1_MSG", "Invalid message type for Cap1", ACS_CALLOUT_UNSURE));
	}
	
	@SuppressWarnings("unchecked")
	private ESResponse doPostIssuerQA(ESRequest request, COConfig config, COLogger logger) {
		ArrayList<String> answers = request.getAnswers();
		String dbAns[] = getDBAnswers(request, config, "", -1);
		
		int result = ACS_CALLOUT_UNSURE;
		String fieldName[] = new String[7];
		String storedData[] = new String[7];
		String userData[] = new String[7];
		
		fieldName[0] = Field.FIELD_DOB;
		fieldName[1] = Field.FIELD_EXPIRY_DATE;
		fieldName[2] = Field.FIELD_SSN;
		fieldName[3] = Field.FIELD_POSTALCODE;
		fieldName[4] = Field.FIELD_CARDHOLDERNAME;
		fieldName[5] = Field.FIELD_MOBILE;
		fieldName[6] = Field.FIELD_LANDLINE;
		
		storedData[0] = dbAns[0];
		storedData[1] = dbAns[1];
		storedData[2] = dbAns[2];
		storedData[3] = dbAns[3];
		storedData[4] = dbAns[4];
		storedData[5] = dbAns[5];
		storedData[6] = dbAns[6];
		
		userData[0] = answers.get(0);
		userData[1] = answers.get(1);
		userData[2] = answers.get(2);
		userData[3] = answers.get(3);
		userData[4] = answers.get(4);
		userData[5] = answers.get(5);
		userData[6] = answers.get(6);
		
		Field fields[] = new Field[7];
		
		MatchResult matchResult = null;
		try {
			fields[0] = new DateField(request, config, fieldName[0], userData[0], storedData[0]);
			fields[1] = new DateField(request, config, fieldName[1], userData[1], storedData[1]);
			fields[2] = new NumericField(request, config, fieldName[2], userData[2], storedData[2]);
			fields[3] = new RegExField(request, config, fieldName[3], userData[3], storedData[3]);
			fields[4] = new NameField(request, config, fieldName[4], userData[4], storedData[4]);
			fields[5] = new NumericField(request, config, fieldName[5], userData[5], storedData[5]);
			fields[6] = new NumericField(request, config, fieldName[6], userData[6], storedData[6]);
			
			matchResult = MatchLibrary.match(fields);
		} catch (MatchException e) {
			e.printStackTrace();
			logger.log(request, "Error while calling match library :"+ e.getMessage());
		}
		
		if(matchResult.getMatchCount() == 7)
			result = ACS_CALLOUT_SUCCESS;
		else
			result = ACS_CALLOUT_FAIL;
		
		return COUtil.prepareESResponse(request, COUtil.createCOResponse((matchResult.getMatchCount() == 7), 0, matchResult.getCalloutStatus(), matchResult.getCalloutStatus(), result, null));
	}
	
	private static String getDBAnswers = "select questionid, answer from arissueranswers where bankid = ? and pan = ? order by questionid";		
	public static String[] getDBAnswers(CORequest request, COConfig config, String chName, int maxQID) {
		COLogger logger = config.getLogger();

		DatabaseConnection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String[] answers = null;
		if (maxQID < 0) {
			maxQID = 5;
		}
		String encCardNumber = EnrollmentCrypto.encrypt(request.getCardNumber(), request.getBankId());
		
		answers = new String[maxQID];
		while (true) {
			try {
				conn = dbMan.getConnection();
				if (conn != null && !conn.isClosed()) {
					if (chName == null) {
						chName = "";
					}

					stmt = conn.prepareStatement(getDBAnswers);
					stmt.setInt(1, request.getBankId());
					stmt.setString(2, encCardNumber);

					rs = stmt.executeQuery();

					int qid = -1;
					String answer = null;
					while (rs.next()) {
						qid = rs.getInt(1);
						answer = rs.getString(2);
						if (answer != null && !"".equals(answer)) {
							answer = EnrollmentCrypto.decrypt(answer, request
									.getBankId());
							answer = answer.trim();
						}

						if (qid <= maxQID) {
							answers[qid - 1] = answer;
						} else {
							// refactor the array to atleast the size of qid
							maxQID = qid;
							String[] moreAnswers = new String[maxQID];
							for (int i = 0; i < answers.length; i++) {
								moreAnswers[i] = answers[i];
							}
							moreAnswers[qid - 1] = answer;
							answers = moreAnswers;
						}
					}
				} else {
					logger.log(request, "Could not get DB connection");
					return null;
				}
				break;
			} catch (SQLException sqle) {
				try {
					if (dbMan.failedDatabaseConnection(conn, sqle
							.getErrorCode()))
						continue;
				} catch (SQLException sqle1) {
				}

				// DB down
				logger.log(request, "SQL Error in getting AHA details", sqle);
				return null;
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException sqle) {
				}
				if (conn != null)
					dbMan.release(conn);
			}
		}
		return answers;
	}

}
