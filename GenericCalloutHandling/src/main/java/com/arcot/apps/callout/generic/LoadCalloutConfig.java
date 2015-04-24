package com.arcot.apps.callout.generic;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import com.arcot.callout.CallOutsConfig;
import com.arcot.dboperations.admin.AdminSelectOperations;
import com.arcot.logger.ArcotLogger;
import com.arcot.util.ArcotException;
import com.arcot.vpas.enroll.cache.ESCache;

public class LoadCalloutConfig {

	private static LoadCalloutConfig calloutConfigs;
	private static Object lock = new Object();
	private HashMap<Integer, CallOutsConfig> calloutId2ObjectMap = new HashMap<Integer, CallOutsConfig>();
	private HashMap<String, CallOutsConfig> calloutName2ObjectMap = new HashMap<String, CallOutsConfig>();

	private LoadCalloutConfig() {

	}

	public static LoadCalloutConfig getInstance(Vector<CallOutsConfig> output) {

		if (calloutConfigs == null) {
			synchronized (lock) {
				if (calloutConfigs == null) {
					calloutConfigs = new LoadCalloutConfig();
					try {
						String masterKey = ESCache.bic.getBank(0).BANKKEY;
						AdminSelectOperations.getAllCallOutConfig(output,
								masterKey);
						calloutConfigs.mapCalloutConfigObjects(output);
					} catch (ArcotException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return calloutConfigs;
	}

	private void mapCalloutConfigObjects(Vector<CallOutsConfig> output) {
		for (CallOutsConfig callConfig : output) {
			calloutId2ObjectMap.put(callConfig.getConfID(), callConfig);
			calloutName2ObjectMap.put(callConfig.getName(), callConfig);
		}
		ArcotLogger.logInfo("Total callout configs by ID[" + calloutId2ObjectMap.size() +"]");
		ArcotLogger.logInfo("Total callout configs by Name[" + calloutName2ObjectMap.size() +"]");
	}

	public CallOutsConfig getCalloutConfigFromId(int calloutid) {
		return calloutId2ObjectMap.get(calloutid);
	}
	
	public CallOutsConfig getCalloutConfigFromName(String callOutName) {
		return calloutName2ObjectMap.get(callOutName);
	}
	
	public Collection<CallOutsConfig> getAllCalloutConfigs()
	{
		return Collections.unmodifiableCollection(calloutId2ObjectMap.values());
	}
}
