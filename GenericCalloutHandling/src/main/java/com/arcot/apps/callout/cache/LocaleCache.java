/**
 * 
 */
package com.arcot.apps.callout.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import com.arcot.dboperations.admin.AdminOperations;
import com.arcot.logger.ArcotLogger;
import com.arcot.util.ArcotException;
import com.arcot.util.LocalesCache;

/**
 * @author porja04
 *
 */
public class LocaleCache
{
	
	private static final LocaleCache instance = new LocaleCache();
	private HashMap<Integer, LocalesCache> localesById;
	private HashMap<String, LocalesCache> localesByName;
	
	private LocaleCache()
	{
		init();
	}

	private void init()
	{
		Hashtable htLocales = new Hashtable(20);
		Hashtable htLocalesName = new Hashtable(20);
		
		try
		{
			AdminOperations.populateLocales(htLocales, htLocalesName);
			localesById = new HashMap<Integer, LocalesCache>(htLocales);
			localesByName = new HashMap<String, LocalesCache>(htLocalesName);
		}
		catch (ArcotException e)
		{			
			ArcotLogger.logError("Error initializing Callout Locales Cache", e);
			e.printStackTrace();
		}		
		
	}	
	
	
	public static LocaleCache getInstance()
	{
		return instance;
	}
	
	public LocalesCache getLocaleById(Integer id)
	{		
		return localesById.get(id);
	}
	
	public LocalesCache getLocaleByName(String name)
	{
		return localesByName.get(name);
	}
	
	public LocalesCache getLocale(Locale locale)
	{	
		return getLocaleByName(locale.toString());
	}
	
	public Collection<LocalesCache> getAllLocales()
	{
		return Collections.unmodifiableCollection(localesById.values());
	}
	
	

}
