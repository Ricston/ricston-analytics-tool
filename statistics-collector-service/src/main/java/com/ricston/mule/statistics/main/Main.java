package com.ricston.mule.statistics.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ricston.mule.jmx.Connector;
import com.ricston.mule.statistics.AbstractCollector;
import com.ricston.mule.statistics.FlowCollector;
import com.ricston.mule.statistics.GarbadgeCollector;
import com.ricston.mule.statistics.KPICollector;
import com.ricston.mule.statistics.MemoryCollector;
import com.ricston.mule.statistics.MemoryPoolCollector;
import com.ricston.mule.statistics.OperatingSystemCollector;

@Configuration
@ComponentScan
public class Main {
	
	@Bean
	MBeanServerConnection server() throws IOException{
		Connector c = new Connector();
		c.setHost("localhost");
		c.setPort(1099);
		c.setPath("server");
		c.connect();
		return c.getMbeanServer();
	}
	
	@Bean(name="collectors")
	List<AbstractCollector> collectors(){
		
		List<AbstractCollector> collectors = new ArrayList<AbstractCollector>();
		collectors.add(new FlowCollector());
		collectors.add(new GarbadgeCollector());
		collectors.add(new KPICollector());
		collectors.add(new MemoryCollector());
		collectors.add(new MemoryPoolCollector());
		collectors.add(new OperatingSystemCollector());
//		collectors.add(new ThreadCollector());
		
		return collectors;
	}
	
	public static void discoverCollectors(ApplicationContext context){
//		@SuppressWarnings("unchecked")
//		List<AbstractCollector> collectors = (List<AbstractCollector>) context.getBean("collectors");
//		collectors.add(new ThreadCollector());
	}

	public static void main(String[] args) throws InterruptedException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
		discoverCollectors(context);
	}

}
