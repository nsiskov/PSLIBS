package com.arcot.apps.callout.es;

import com.arcot.apps.callout.common.COResponse;

public class ESResponse extends COResponse{
	
	public String toString(){
		StringBuffer myString = new StringBuffer("[result, lockStatus]=[");
		myString.append(isResult());
		myString.append(", ");
		myString.append(getLockStatus());
		myString.append("]");
		return myString.toString();
	}
}
