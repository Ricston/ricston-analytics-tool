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

import org.springframework.stereotype.Component;

import com.ricston.mule.statistics.model.Garbadge;

@Component
public class GarbadgeCollector extends AbstractCollector{
	
	@Override
	public List<Garbadge> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting garbadge collector statistics");
		List<Garbadge> stats = new ArrayList<Garbadge>();
		
		stats.add(collectStat(mbeanServer, "PS MarkSweep"));
		stats.add(collectStat(mbeanServer, "PS Scavenge"));
		
		logger.debug("Collecting garbadge collector statistics complete");
		return stats;
	}
	
	protected Garbadge collectStat(MBeanServerConnection mbeanServer, String collectorName) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		ObjectName objectName = new ObjectName(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",name=" + collectorName);
		
		Garbadge stat = new Garbadge();
		stat.setName(collectorName);
		stat.setCollectionCount((Long) mbeanServer.getAttribute(objectName, "CollectionCount"));
		stat.setCollectionTime((Long) mbeanServer.getAttribute(objectName, "CollectionTime"));
		
		return stat;
	}

}