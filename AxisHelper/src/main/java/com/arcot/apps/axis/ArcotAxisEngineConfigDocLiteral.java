package com.arcot.apps.axis;

import javax.xml.namespace.QName;

import org.apache.axis.ConfigurationException;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.handlers.soap.SOAPService;

/**
 * Subclass of ArcotAxisEngineConfig to create custom Axis configuration using 
 * Style.DOCUMENT and Use.LITERAL
 * Request/Response in the form of Arrays.
 * 
 * @author Administrator
 *
 */
public class ArcotAxisEngineConfigDocLiteral extends ArcotAxisEngineConfig {
	
	/**
	 * Override getService() to set the Style.DOCUMENT and Use.LITERAL
	 * in-order the output is returned in the form of Array of objects 
	 */
	@Override
	public SOAPService getService(QName qname) throws ConfigurationException {
		SOAPService service =  super.getService(qname);		
		if(service != null) {
			// ser.addHandler((Handler) serviceOptions.get("ServiceHandler"));			
			service.setUse(Use.LITERAL);
			service.setStyle(Style.DOCUMENT);
		}
		return service;
	}
}