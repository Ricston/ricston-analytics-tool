package com.ricston.statistics.collector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


import com.ricston.statistics.model.Flow;

/**
 * Collects flow data. This collector is stateful. It keeps the old statistics so that it returns 
 * the delta which is more useful than just the totals.
 *
 */
@Component
public class FlowCollector extends AbstractCollector{
	
	/**
	 * Store for the old statistics
	 */
	protected Map<String, Flow> oldStats = new HashMap<String, Flow>();
	
	/**
	 * Collects the flow data from JMX by querying all Mule applications
	 */
	@Override
	public List<Flow> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		
		logger.debug("Collecting flow statistics");
		List<Flow> stats = new ArrayList<Flow>();
		
		//get applications to monitor
		List<String> applications = applicationsToMonitor(mbeanServer);
		
		//collect stats for all applications
		for (String application : applications){
			stats.addAll(collectFlowsStats(mbeanServer, application));
		}
		
		logger.debug("Collecting flow statistics complete");
		return stats;
	}
	
	/**
	 * Collect the flow statistics for an individual Mule application
	 * 
	 * @param mbeanServer The MBean server
	 * @param application The application to monitor
	 * @return List of Flow statistics
	 * @throws MalformedObjectNameException
	 * @throws IOException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 */
	protected List<Flow> collectFlowsStats(MBeanServerConnection mbeanServer, String application) throws MalformedObjectNameException, IOException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		List<Flow> stats = new ArrayList<Flow>();
		
		//create object name to query
		ObjectName objectName = new ObjectName(application + ":type=Flow,name=*");
		
		//execute the JMX query to get flow names
		Set<ObjectInstance> flowObjectInstances = mbeanServer.queryMBeans(objectName, null);
		
		//collect the statics for each flow and add to final result
		for (ObjectInstance flowObjectInstance : flowObjectInstances){
			stats.add(collectStat(mbeanServer, flowObjectInstance, application));
		}
		
		return stats;
		
	}
	
	/**
	 * Collect the statistics for an individual Mule flow within an application
	 * and calculate the delta using the old statistics
	 * 
	 * @param mbeanServer The MBean server
	 * @param o The object instance to collect statistics from
	 * @param application The application to monitor
	 * @return Flow statistics
	 * @throws MalformedObjectNameException
	 * @throws IOException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 */
	protected Flow collectStat(MBeanServerConnection mbeanServer, ObjectInstance o, String application) throws MalformedObjectNameException, IOException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException  {
		Flow flow = new Flow();
		
		//get statistics of an individual flow
		String flowName = (String)mbeanServer.getAttribute(o.getObjectName(), "Name");
		
		//fill the Flow statistics object
		flow.setName(flowName);
		flow.setApplication(StringUtils.remove(application, APPLICATION_PREFIX));
		flow.setAsyncEventsReceived((Long)mbeanServer.getAttribute(o.getObjectName(), "AsyncEventsReceived"));
		flow.setAverageProcessingTime((Long)mbeanServer.getAttribute(o.getObjectName(), "AverageProcessingTime"));
		flow.setExecutionErrors((Long)mbeanServer.getAttribute(o.getObjectName(), "ExecutionErrors"));
		flow.setFatalErrors((Long)mbeanServer.getAttribute(o.getObjectName(), "FatalErrors"));
		flow.setMaxProcessingTime((Long)mbeanServer.getAttribute(o.getObjectName(), "MaxProcessingTime"));
		flow.setMinProcessingTime((Long)mbeanServer.getAttribute(o.getObjectName(), "MinProcessingTime"));
		flow.setProcessedEvents((Long)mbeanServer.getAttribute(o.getObjectName(), "ProcessedEvents"));
		flow.setSyncEventsReceived((Long)mbeanServer.getAttribute(o.getObjectName(), "SyncEventsReceived"));
		flow.setTotalEventsReceived((Long)mbeanServer.getAttribute(o.getObjectName(), "TotalEventsReceived"));
		flow.setTotalProcessingTime((Long)mbeanServer.getAttribute(o.getObjectName(), "TotalProcessingTime"));
		
		//calculate the deltas
		flow.workNewStatistics(oldStats.get(flowName));
		
		//save the last collected statistic
		oldStats.put(flowName, flow);
		
		return flow;
	}

}
