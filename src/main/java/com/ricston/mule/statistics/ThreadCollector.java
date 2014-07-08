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

import com.ricston.mule.statistics.model.Thread;

public class ThreadCollector extends AbstractCollector{
	
	@Override
	public List<Thread> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting memory statistics");
		List<Thread> stats = new ArrayList<Thread>();
		
		stats.add(collectStat(mbeanServer));
		
		logger.debug("Collecting memory statistics completed");
		return stats;
	}
	
	protected Thread collectStat(MBeanServerConnection mbeanServer) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		ObjectName objectName = new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME);
		
		Thread stat = new Thread();
		stat.setName("Threading");
		stat.setCurrentThreadCpuTime((Long)mbeanServer.getAttribute(objectName, "CurrentThreadCpuTime"));
		stat.setCurrentThreadUserTime((Long)mbeanServer.getAttribute(objectName, "CurrentThreadUserTime"));
		stat.setDaemonThreadCount((Integer)mbeanServer.getAttribute(objectName, "DaemonThreadCount"));
		stat.setThreadCount((Integer)mbeanServer.getAttribute(objectName, "ThreadCount"));
		
		return stat;
	}

}
