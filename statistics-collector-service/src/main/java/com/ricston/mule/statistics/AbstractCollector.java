package com.ricston.mule.statistics;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Sets;
import com.ricston.mule.statistics.model.AbstractCollectorStatistics;

public abstract class AbstractCollector {
	
	protected final String APPLICATION_PREFIX = "Mule.";
	protected Set<String> exclude = Sets.newHashSet(APPLICATION_PREFIX + ".agent", APPLICATION_PREFIX + "default");
	
	protected Log logger = LogFactory.getLog(getClass());

	public abstract List<? extends AbstractCollectorStatistics> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException;
	
	protected List<String> applicationsToMonitor(MBeanServerConnection mbeanServer) throws IOException{
		String[] domains = mbeanServer.getDomains();
		List<String> applications = new ArrayList<String>();
		
		for (String domain : domains){
			if (StringUtils.startsWith(domain, APPLICATION_PREFIX) && !exclude.contains(domain)){
				applications.add(domain);
			}
		}
		
		return applications;
	}
	

}
