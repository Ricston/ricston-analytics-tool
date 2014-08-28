package com.ricston.mule.statistics.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ricston.mule.statistics.model.AbstractCollectorStatistics;
import com.ricston.mule.statistics.model.StatisticsType;

@Component
@EnableScheduling
public class ScheduledTasks {

	@Autowired
	private Collector collector;
	
	@Autowired
	private ApplicationContext context;

	@Scheduled(initialDelay=1000, fixedRate = 5000)
	public void reportCurrentTime() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		
		Map<StatisticsType, List<? extends AbstractCollectorStatistics>> stats = collector.collectAllStatisticalData();
		System.out.println("Stats size: " + stats.size() + " Stats: " + stats.toString());
	}

	public Collector getCollector() {
		return collector;
	}

	public void setCollector(Collector collector) {
		this.collector = collector;
	}

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

}
