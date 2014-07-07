package com.ricston.mule.statistics;

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

import org.mule.util.StringUtils;

import com.google.common.collect.Sets;
import com.ricston.mule.statistics.model.Flow;

public class FlowCollector extends AbstractCollector{
	
	protected Set<String> exclude = Sets.newHashSet("Mule..agent", "Mule.default");
	protected Map<String, Flow> oldStats = new HashMap<String, Flow>();
	
	protected List<String> applicationsToMonitor(MBeanServerConnection mbeanServer) throws IOException{
		String[] domains = mbeanServer.getDomains();
		List<String> applications = new ArrayList<String>();
		
		for (String domain : domains){
			if (StringUtils.startsWith(domain, "Mule.") && !exclude.contains(domain)){
				applications.add(domain);
			}
		}
		
		return applications;
	}
	
	@Override
	public List<Flow> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		
		logger.debug("Collecting flow statistics");
		List<Flow> stats = new ArrayList<Flow>();
		List<String> applications = applicationsToMonitor(mbeanServer);
		
		for (String application : applications){
			stats.addAll(collectFlowsStats(mbeanServer, application));
		}
		
		logger.debug("Collecting flow statistics complete");
		return stats;
	}
	
	protected List<Flow> collectFlowsStats(MBeanServerConnection mbeanServer, String application) throws MalformedObjectNameException, IOException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		List<Flow> stats = new ArrayList<Flow>();
		
		ObjectName objectName = new ObjectName(application + ":type=Flow,name=*");
		
		@SuppressWarnings("unchecked")
		Set<ObjectInstance> flowObjectInstances = mbeanServer.queryMBeans(objectName, null);
		
		for (ObjectInstance flowObjectInstance : flowObjectInstances){
			stats.add(collectStat(mbeanServer, flowObjectInstance));
		}
		
		return stats;
		
	}
	
	protected Flow collectStat(MBeanServerConnection mbeanServer, ObjectInstance o) throws MalformedObjectNameException, IOException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException  {
		Flow flow = new Flow();
		
		String flowName = (String)mbeanServer.getAttribute(o.getObjectName(), "Name");
		
		flow.setName(flowName);
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
		
		flow.workNewStatistics(oldStats.get(flowName));
		oldStats.put(flowName, flow);
		
		return flow;
	}

}
