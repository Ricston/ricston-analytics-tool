package com.ricston.mule.statistics.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.ricston.mule.statistics.AbstractCollector;
import com.ricston.mule.statistics.model.AbstractCollectorStatistics;

@Component
@EnableScheduling
public class ScheduledTasks {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Autowired
	private Collector collector;
	
	@Autowired
	private ApplicationContext context;

	@Scheduled(initialDelay=1000, fixedRate = 5000)
	public void reportCurrentTime() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		System.out.println("The time is now " + dateFormat.format(new Date()));
		List<List<? extends AbstractCollectorStatistics>> stats = collector.collecData();
		
		System.out.println(stats.size() + " " + stats.toString());
		System.out.println(context);
		
		Map<String, AbstractCollector> collectors = context.getBeansOfType(AbstractCollector.class);
		System.out.println(collectors.size());
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
