package com.ricston.statistics.collector;

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

import com.ricston.statistics.model.Memory;

/**
 * Collect memory statistics. This collector is stateless.
 *
 */
@Component
public class MemoryCollector extends AbstractCollector{
	
	/**
	 * Collect statistics for HeapMemoryUsage and NonHeapMemoryUsage
	 */
	@Override
	public List<Memory> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting memory statistics");
		List<Memory> stats = new ArrayList<Memory>();
		
		stats.add(collectStat(mbeanServer, "HeapMemoryUsage"));
		stats.add(collectStat(mbeanServer, "NonHeapMemoryUsage"));
		
		logger.debug("Collecting memory statistics completed");
		return stats;
	}
	
	/**
	 * Collect memory statistics for an individual type of memory
	 * 
	 * @param mbeanServer The MBean server
	 * @param collectorName The type of memory
	 * @return Memory statistic
	 * @throws MalformedObjectNameException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
	protected Memory collectStat(MBeanServerConnection mbeanServer, String collectorName) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		ObjectName objectName = new ObjectName(ManagementFactory.MEMORY_MXBEAN_NAME);
		
		CompositeDataSupport compositeData = (CompositeDataSupport) mbeanServer.getAttribute(objectName, collectorName);

		Memory stat = new Memory();
		stat.setName(collectorName);
		stat.setCommitted((Long)compositeData.get("committed"));
		stat.setInit((Long)compositeData.get("init"));
		stat.setMax((Long)compositeData.get("max"));
		stat.setUsed((Long)compositeData.get("used"));
		
		return stat;
	}

}
