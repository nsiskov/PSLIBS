package com.arcot.apps.logger;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

import com.arcot.util.GenericValidator;

public class TransfortEmailTriggerEvaluator implements TriggeringEventEvaluator {
	
	@Override
	public boolean isTriggeringEvent(LoggingEvent loggingEvent) {
		String categoryName = loggingEvent.getLogger().getName();
		//System.out.println(Thread.currentThread().getName() + "  >>>> CATEGORY NAME " + categoryName + " SEND EMAIL " + Log4jWrapper.getInstance().getEmailTriggerMap().get(categoryName));
		if(!GenericValidator.isBlankOrNull(categoryName) && !Log4jWrapper.getInstance().getEmailTriggerMap().isEmpty() && Log4jWrapper.getInstance().getEmailTriggerMap().get(categoryName) == true)
			return true;
		return false;
	}

}
