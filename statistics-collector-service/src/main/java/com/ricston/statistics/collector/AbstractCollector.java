package com.ricston.statistics.collector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Sets;
import com.ricston.statistics.model.AbstractCollectorStatistics;

/**
 * All collectors have to extend this class and implement the <code>collect</code> method
 * 
 */
public abstract class AbstractCollector {

	/**
	 * Prefix of the Mule applications within JMX
	 */
	protected final String APPLICATION_PREFIX = "Mule.";
	
	/**
	 * Set of Mule applications to ignore
	 */
	protected Set<String> exclude = Sets.newHashSet(APPLICATION_PREFIX + ".agent", APPLICATION_PREFIX + "default");
	
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * Method to be implemented by collectors. Here the data should be collected from the MBean server connection passed as parameter
	 * 
	 * @param mbeanServer The Mbean server connection
	 * @return List of statistics collected
	 * @throws IOException
	 * @throws MalformedObjectNameException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 */
	public abstract List<? extends AbstractCollectorStatistics> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException;
	
	/**
	 * Helper method to list the applications to be monitored. These are all the Mule applications
	 * that start with the prefix, excluding the ones in the exclude set
	 * 
	 * @param mbeanServer
	 * @return List of monitorable Mule application
	 * @throws IOException
	 */
	protected List<String> applicationsToMonitor(MBeanServerConnection mbeanServer) throws IOException{
		
		//get all domains from JMX
		String[] domains = mbeanServer.getDomains();
		List<String> applications = new ArrayList<String>();
		
		//loop through the domains, and add the ones that start 
		//with the prefix and are not in the exclude set
		for (String domain : domains){
			if (StringUtils.startsWith(domain, APPLICATION_PREFIX) && !exclude.contains(domain)){
				applications.add(domain);
			}
		}
		
		return applications;
	}
	
	/**
	 * Using Reflection to String Builder from commons lang to display
	 * a nice stringified version of the class
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	

}
