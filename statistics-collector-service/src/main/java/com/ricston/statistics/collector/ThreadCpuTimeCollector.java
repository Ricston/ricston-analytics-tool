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

import com.ricston.statistics.model.ThreadCpuTime;

/**
 * Collect thread CPU time. This collector is stateless.
 *
 */
@Component
public class ThreadCpuTimeCollector extends AbstractCollector{
	
	/**
	 * Collect different thread statistics
	 */
	@Override
	public List<ThreadCpuTime> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting thread statistics");
		List<ThreadCpuTime> stats = new ArrayList<ThreadCpuTime>();
		
		stats.addAll(collectThreadCpuTimeStat(mbeanServer));
		
		logger.debug("Collecting thread CPU time statistics completed");
		return stats;
	}
	
	/**
	 * Collect thread CPU time statistics
	 * @param mbeanServer The MBean server
	 * @return List of Thread CPU Time statistics
	 * @throws MalformedObjectNameException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
	protected List<ThreadCpuTime> collectThreadCpuTimeStat(MBeanServerConnection mbeanServer) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		ObjectName objectName = new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME);
		
		List<ThreadCpuTime> stats = new ArrayList<ThreadCpuTime>();
		
		ThreadCpuTime stat = new ThreadCpuTime();
		stat.setName("CurrentThreadCpuTime");
		stat.setThreadCpuTime((Long)mbeanServer.getAttribute(objectName, "CurrentThreadCpuTime"));
		stats.add(stat);
		
		stat = new ThreadCpuTime();
		stat.setName("CurrentThreadUserTime");
		stat.setThreadCpuTime((Long)mbeanServer.getAttribute(objectName, "CurrentThreadUserTime"));
		stats.add(stat);
		
		return stats;
	}

}
