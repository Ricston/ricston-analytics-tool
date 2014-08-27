package com.ricston.mule.statistics.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ricston.mule.statistics.AbstractCollector;
import com.ricston.mule.statistics.model.AbstractCollectorStatistics;

@Component
public class Collector {

	@Autowired
	private MBeanServerConnection mbeanServer;
	
	@Resource(name="collectors")
	private List<AbstractCollector> collectors;

	public List<AbstractCollector> getCollectors() {
		return collectors;
	}

	public void setCollectors(List<AbstractCollector> collectors) {
		this.collectors = collectors;
	}

	public MBeanServerConnection getMbeanServer() {
		return mbeanServer;
	}

	public void setMbeanServer(MBeanServerConnection mbeanServer) {
		this.mbeanServer = mbeanServer;
	}

	public List<List<? extends AbstractCollectorStatistics>> collecData() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		List<List<? extends AbstractCollectorStatistics>> statistics = new ArrayList<List<? extends AbstractCollectorStatistics>>();
		
		for(AbstractCollector collector : collectors){
			statistics.add(collector.collect(mbeanServer));
		}
		
		return statistics;
	}
}
