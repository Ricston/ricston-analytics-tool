package com.ricston.mule.statistics;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;

import com.ricston.mule.statistics.model.MemoryPool;

public class MemoryPoolCollector extends AbstractCollector{
	
	@Override
	public List<MemoryPool> collect() throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		List<MemoryPool> stats = new ArrayList<MemoryPool>();
		
		stats.add(collectStat("PS Eden Space"));
		stats.add(collectStat("Code Cache"));
		stats.add(collectStat("PS Old Gen"));
		stats.add(collectStat("PS Perm Gen"));
		stats.add(collectStat("PS Survivor Space"));
		
		return stats;
		
	}
	
	protected MemoryPool collectStat(String collectorName) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
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
