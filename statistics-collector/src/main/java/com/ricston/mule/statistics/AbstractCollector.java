package com.ricston.mule.statistics;

import java.io.IOException;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ricston.mule.jmx.Connector;
import com.ricston.mule.statistics.model.AbstractCollectorStatistics;

public abstract class AbstractCollector {
	
	protected Log logger = LogFactory.getLog(getClass());

	public abstract List<? extends AbstractCollectorStatistics> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException;
	
	public final List<? extends AbstractCollectorStatistics> connectCollectClose(Connector connector) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		connector.connect();
		List<? extends AbstractCollectorStatistics> stats = collect(connector.getMbeanServer());
		connector.close();
		
		return stats;
	}
	

}
