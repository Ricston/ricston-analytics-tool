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

import org.springframework.stereotype.Component;

import com.ricston.mule.statistics.model.AbstractCollectorStatistics;
import com.ricston.mule.statistics.model.CpuLoad;
import com.ricston.mule.statistics.model.OperatingSystem;
import com.ricston.mule.statistics.model.PhysicalMemory;

@Component
public class OperatingSystemCollector extends AbstractCollector{
	
	@Override
	public List<AbstractCollectorStatistics> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting operating system statistics");
		
		List<AbstractCollectorStatistics> stats = new ArrayList<AbstractCollectorStatistics>();
		
		ObjectName objectName = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);

		OperatingSystem stat = new OperatingSystem();
		stat.setName("OperatingSystem");
		stat.setOpenFileDescriptorCount((Long)mbeanServer.getAttribute(objectName, "OpenFileDescriptorCount"));
		
		stats.add(stat);
		
		stats.addAll(collectPhysicalMemoryStat(mbeanServer, objectName));
		stats.addAll(collectCpuLoadStat(mbeanServer, objectName));
		
		logger.debug("Collecting operating system statistics completed");
		return stats;
		
	}
	
	public List<CpuLoad> collectCpuLoadStat(MBeanServerConnection mbeanServer, ObjectName objectName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		List<CpuLoad> stats = new ArrayList<CpuLoad>();
		
		CpuLoad stat = new CpuLoad();
		stat.setName("ProcessCpuLoad");
		stat.setCpuLoad((Double)mbeanServer.getAttribute(objectName, "ProcessCpuLoad"));
		stats.add(stat);
		
		stat = new CpuLoad();
		stat.setName("SystemCpuLoad");
		stat.setCpuLoad((Double)mbeanServer.getAttribute(objectName, "SystemCpuLoad"));
		stats.add(stat);
		
		return stats;
		
	}
	
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
