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

import com.ricston.mule.statistics.model.AbstractCollectorStatistics;
import com.ricston.mule.statistics.model.ThreadCount;
import com.ricston.mule.statistics.model.ThreadCpuTime;

public class ThreadCollector extends AbstractCollector{
	
	@Override
	public List<AbstractCollectorStatistics> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting thread statistics");
		List<AbstractCollectorStatistics> stats = new ArrayList<AbstractCollectorStatistics>();
		
		stats.addAll(collectThreadCountStat(mbeanServer));
		stats.addAll(collectThreadCpuTimeStat(mbeanServer));
		
		logger.debug("Collecting thread statistics completed");
		return stats;
	}
	
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
