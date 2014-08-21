/**
 * (c) 2003-2014 Ricston, Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.ricston.modules.dataanalysis;

import java.util.List;
import java.util.Map;

/**
 * Simple interface to expose some methods over JMX
 * 
 * @author Ricston, Ltd.
 *
 */
public interface DataAnalysisMBean {
	
	/**
	 * Retrieves persisted data from store and return it. 
	 * 
	 * @return Persisted data
	 */
	public List<Map<String,Object>> getData();
	
}
