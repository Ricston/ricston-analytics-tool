package com.ricston.mule.statistics.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ricston.mule.jmx.Connector;
import com.ricston.mule.statistics.collector.AbstractCollector;
import com.ricston.mule.statistics.model.AbstractCollectorStatistics;
import com.ricston.mule.statistics.model.StatisticsType;

/**
 * Collects the data from all the configured collectors. Each collector
 * must implement <code>com.ricston.mule.statistics.collector.AbstractCollector</code>
 * and annotated with Spring's <code>org.springframework.stereotype.Component</code>
 */
@Component
public class Collector {

	/**
	 * The JMX connector to use for statistics collection 
	 */
	@Autowired
	private Connector jmxConnector;
	
	/**
	 * List of collectors auto wired through Spring
	 */
	@Autowired
	private List<AbstractCollector> collectors;
	
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * Connects to JMX server, collects all statistical data, and closes the JMX connection
	 * 
	 * @return Map of Lists containing the statistical data, keyed by statistics type
	 * @throws MalformedObjectNameException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
	public Map<StatisticsType, List<? extends AbstractCollectorStatistics>> collectAllStatisticalData() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		//connect to JMX
		jmxConnector.connect();
		
		//collect all the data
		Map<StatisticsType, List<? extends AbstractCollectorStatistics>> statistics = 
				collectAllStatisticalData(jmxConnector.getMbeanServer());
		
		//close JMX connection
		jmxConnector.close();
		
		return statistics;
	}
	
	/**
	 * Collects the statistical data using the wired list of collectors and the MBean server connection
	 * passed as parameter
	 * 
	 * @param mbeanServer The MBean server connection
	 * @return Map of Lists containing the statistical data, keyed by statistics type
	 * @throws MalformedObjectNameException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
	protected Map<StatisticsType, List<? extends AbstractCollectorStatistics>> collectAllStatisticalData(MBeanServerConnection mbeanServer) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		//create map to be returned
		Map<StatisticsType, List<? extends AbstractCollectorStatistics>> statistics = new HashMap<StatisticsType, List<? extends AbstractCollectorStatistics>>();
		
		//loop for each collector
		for(AbstractCollector collector : collectors){
			
			//collect the data
			List<? extends AbstractCollectorStatistics> stats = collector.collect(mbeanServer);
			
			//add only if we extracted some statistics, using the type of data collected as key
			if (stats != null && stats.size() > 0)
			{
				statistics.put(stats.get(0).getType(), stats);
			}
			
		}
		
		return statistics;
	}

	/**
	 * 
	 * @return The JMX Connector
	 */
	public Connector getJmxConnector() {
		return jmxConnector;
	}

	/**
	 * 
	 * @param jmxConnector The JMX Connector
	 */
	public void setJmxConnector(Connector jmxConnector) {
		this.jmxConnector = jmxConnector;
	}


	/***
	 * 
	 * @return The list of collectors
	 */
	public List<AbstractCollector> getCollectors() {
		return collectors;
	}
	
	/**
	 * 
	 * @param collectors The list of collectors
	 */
	public void setCollectors(List<AbstractCollector> collectors) {
		this.collectors = collectors;
	}
}
