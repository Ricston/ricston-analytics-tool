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

import com.ricston.mule.statistics.model.Garbage;

/**
 * Collect garbage collector statistics. This collector is stateless.
 *
 */
@Component
public class GarbageCollector extends AbstractCollector{
	
	/**
	 * Collect statistics for PS Mark Sweep and PS Scavenge
	 */
	@Override
	public List<Garbage> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting garbadge collector statistics");
		List<Garbage> stats = new ArrayList<Garbage>();
		
		stats.add(collectStat(mbeanServer, "PS MarkSweep"));
		stats.add(collectStat(mbeanServer, "PS Scavenge"));
		
		logger.debug("Collecting garbadge collector statistics complete");
		return stats;
	}
	
	/**
	 * Collect collection time and count for the collector (PS MarkSwep or PS Scavenge) 
	 * 
	 * @param mbeanServer The MBean server
	 * @param collectorName The collector 
	 * @return Returns Garbage collector statistics
	 * @throws MalformedObjectNameException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
	protected Garbage collectStat(MBeanServerConnection mbeanServer, String collectorName) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		ObjectName objectName = new ObjectName(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",name=" + collectorName);
		
		Garbage stat = new Garbage();
		stat.setName(collectorName);
		stat.setCollectionCount((Long) mbeanServer.getAttribute(objectName, "CollectionCount"));
		stat.setCollectionTime((Long) mbeanServer.getAttribute(objectName, "CollectionTime"));
		
		return stat;
	}

}
