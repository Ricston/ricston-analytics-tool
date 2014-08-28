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

@Component
public class Collector {

	@Autowired
	private Connector jmxConnector;
	
	@Autowired
	private List<AbstractCollector> collectors;
	
	protected Log logger = LogFactory.getLog(getClass());

	public Map<StatisticsType, List<? extends AbstractCollectorStatistics>> collectAllStatisticalData() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		jmxConnector.connect();
		
		Map<StatisticsType, List<? extends AbstractCollectorStatistics>> statistics = 
				collectAllStatisticalData(jmxConnector.getMbeanServer());
		
		jmxConnector.close();
		
		return statistics;
	}
	
	protected Map<StatisticsType, List<? extends AbstractCollectorStatistics>> collectAllStatisticalData(MBeanServerConnection mbeanServer) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		Map<StatisticsType, List<? extends AbstractCollectorStatistics>> statistics = new HashMap<StatisticsType, List<? extends AbstractCollectorStatistics>>();
		
		for(AbstractCollector collector : collectors){
			List<? extends AbstractCollectorStatistics> stats = collector.collect(mbeanServer);
			
			if (stats != null && stats.size() > 0)
			{
				statistics.put(stats.get(0).getType(), stats);
			}
			
		}
		
		return statistics;
	}

	public Connector getJmxConnector() {
		return jmxConnector;
	}


	public void setJmxConnector(Connector jmxConnector) {
		this.jmxConnector = jmxConnector;
	}


	public List<AbstractCollector> getCollectors() {
		return collectors;
	}

	public void setCollectors(List<AbstractCollector> collectors) {
		this.collectors = collectors;
	}
}
