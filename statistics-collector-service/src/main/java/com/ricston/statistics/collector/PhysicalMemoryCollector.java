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

import org.springframework.stereotype.Component;

import com.ricston.statistics.model.PhysicalMemory;

/**
 * Collect physical memory statistics. This collector is stateless.
 *
 */
@Component
public class PhysicalMemoryCollector extends AbstractCollector{
	
	/**
	 * Collect statistics for different operating system features
	 */
	@Override
	public List<PhysicalMemory> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting operating system statistics");
		
		List<PhysicalMemory> stats = new ArrayList<PhysicalMemory>();
		
		ObjectName objectName = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);

		stats.addAll(collectPhysicalMemoryStat(mbeanServer, objectName));
		
		logger.debug("Collecting physical memory statistics completed");
		return stats;
		
	}
	
	/**
	 * Collect physical memory statistics
	 * @param mbeanServer The MBean server
	 * @param objectName The operating system mbean name
	 * @return List of Physical Memory statistics
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
	public List<PhysicalMemory> collectPhysicalMemoryStat(MBeanServerConnection mbeanServer, ObjectName objectName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		List<PhysicalMemory> stats = new ArrayList<PhysicalMemory>();
		
		PhysicalMemory stat = new PhysicalMemory();
		stat.setName("CommittedVirtualMemorySize");
		stat.setMemorySize((Long)mbeanServer.getAttribute(objectName, "CommittedVirtualMemorySize"));
		stats.add(stat);
		
		stat = new PhysicalMemory();
		stat.setName("FreePhysicalMemorySize");
		stat.setMemorySize((Long)mbeanServer.getAttribute(objectName, "FreePhysicalMemorySize"));
		stats.add(stat);
		
		return stats;
		
	}
	
}
