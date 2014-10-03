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

import com.ricston.statistics.model.ThreadCount;

/**
 * Collect thread count. This collector is stateless.
 *
 */
@Component
public class ThreadCountCollector extends AbstractCollector{
	
	/**
	 * Collect different thread statistics
	 */
	@Override
	public List<ThreadCount> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting thread statistics");
		List<ThreadCount> stats = new ArrayList<ThreadCount>();
		
		stats.addAll(collectThreadCountStat(mbeanServer));
		
		logger.debug("Collecting thread count statistics completed");
		return stats;
	}
	
	/**
	 * Collect thread count statistics 
	 * @param mbeanServer The MBean server
	 * @return List of Thread Count statistics
	 * @throws MalformedObjectNameException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
	protected List<ThreadCount> collectThreadCountStat(MBeanServerConnection mbeanServer) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		ObjectName objectName = new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME);
		
		List<ThreadCount> stats = new ArrayList<ThreadCount>();
		
		ThreadCount stat = new ThreadCount();
		stat.setName("ThreadCount");
		stat.setThreadCount((Integer)mbeanServer.getAttribute(objectName, "ThreadCount"));
		stats.add(stat);
		
		stat = new ThreadCount();
		stat.setName("DaemonThreadCount");
		stat.setThreadCount((Integer)mbeanServer.getAttribute(objectName, "DaemonThreadCount"));
		stats.add(stat);
		
		return stats;
	}
	
}
