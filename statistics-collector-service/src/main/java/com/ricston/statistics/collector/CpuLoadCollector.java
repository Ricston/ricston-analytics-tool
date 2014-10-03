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

import com.ricston.statistics.model.CpuLoad;

/**
 * Collect CPU load statistics. This collector is stateless.
 *
 */
@Component
public class CpuLoadCollector extends AbstractCollector{
	
	/**
	 * Collect statistics for different operating system features
	 */
	@Override
	public List<CpuLoad> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting operating system statistics");
		
		List<CpuLoad> stats = new ArrayList<CpuLoad>();
		
		ObjectName objectName = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);

		stats.addAll(collectCpuLoadStat(mbeanServer, objectName));
		
		logger.debug("Collecting CPU load statistics completed");
		return stats;
		
	}
	
	/**
	 * Collect CPU load statistics
	 * 
	 * @param mbeanServer The MBean Server
	 * @param objectName The operating system mbean name
	 * @return List of CPU load statistics
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
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
}
