package com.ricston.mule.statistics.collector;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;

import org.springframework.stereotype.Component;

import com.ricston.mule.statistics.model.MemoryPool;

@Component
public class MemoryPoolCollector extends AbstractCollector{
	
	@Override
	public List<MemoryPool> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting memory pool statistics");
		
		List<MemoryPool> stats = new ArrayList<MemoryPool>();
		
		stats.add(collectStat(mbeanServer, "PS Eden Space"));
		stats.add(collectStat(mbeanServer, "Code Cache"));
		stats.add(collectStat(mbeanServer, "PS Old Gen"));
		stats.add(collectStat(mbeanServer, "PS Perm Gen"));
		stats.add(collectStat(mbeanServer, "PS Survivor Space"));
		
		logger.debug("Collecting memory pool statistics completed");
		return stats;
		
	}
	
	protected MemoryPool collectStat(MBeanServerConnection mbeanServer, String collectorName) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		ObjectName objectName = new ObjectName(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + ",name=" + collectorName);
		
		CompositeDataSupport compositeData = (CompositeDataSupport) mbeanServer.getAttribute(objectName, "Usage");
		
		MemoryPool stat = new MemoryPool();
		stat.setName(collectorName);
		stat.setCommitted((Long)compositeData.get("committed"));
		stat.setInit((Long)compositeData.get("init"));
		stat.setMax((Long)compositeData.get("max"));
		stat.setUsed((Long)compositeData.get("used"));
		
		return stat;
	}

}
