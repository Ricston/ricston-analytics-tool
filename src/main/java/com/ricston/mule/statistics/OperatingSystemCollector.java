package com.ricston.mule.statistics;

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

import com.ricston.mule.statistics.model.OperatingSystem;

public class OperatingSystemCollector extends AbstractCollector{
	
	@Override
	public List<OperatingSystem> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting operating system statistics");
		
		List<OperatingSystem> stats = new ArrayList<OperatingSystem>();
		
		ObjectName objectName = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);

		OperatingSystem stat = new OperatingSystem();
		stat.setName("OperatingSystem");
		stat.setCommittedVirtualMemorySize((Long)mbeanServer.getAttribute(objectName, "CommittedVirtualMemorySize"));
		stat.setFreePhysicalMemorySize((Long)mbeanServer.getAttribute(objectName, "FreePhysicalMemorySize"));
		stat.setOpenFileDescriptorCount((Long)mbeanServer.getAttribute(objectName, "OpenFileDescriptorCount"));
		stat.setProcessCpuLoad((Double)mbeanServer.getAttribute(objectName, "ProcessCpuLoad"));
		stat.setSystemCpuLoad((Double)mbeanServer.getAttribute(objectName, "SystemCpuLoad"));
		
		stats.add(stat);
		
		logger.debug("Collecting operating system statistics completed");
		return stats;
		
	}
	
}
