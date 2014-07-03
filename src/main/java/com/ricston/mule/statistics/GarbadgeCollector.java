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

import com.ricston.mule.statistics.model.Garbadge;

public class GarbadgeCollector extends AbstractCollector{
	
	@Override
	public List<Garbadge> collect() throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		List<Garbadge> stats = new ArrayList<Garbadge>();
		
		stats.add(collectStat("PS MarkSweep"));
		stats.add(collectStat("PS Scavenge"));
		
		return stats;
	}
	
	protected Garbadge collectStat(String collectorName) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		ObjectName objectName = new ObjectName(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",name=" + collectorName);
		
		Garbadge stat = new Garbadge();
		stat.setName(collectorName);
		stat.setCollectionCount((Long) mbeanServer.getAttribute(objectName, "CollectionCount"));
		stat.setCollectionTime((Long) mbeanServer.getAttribute(objectName, "CollectionTime"));
		
		return stat;
	}

}
