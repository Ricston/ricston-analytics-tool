package com.ricston.mule.statistics.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ricston.mule.jmx.Connector;

public class Main {
	
	static protected Log logger = LogFactory.getLog(Main.class);
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
		Connector jmxConnector = context.getBean(Connector.class);
		logger.info("Collector started. " + jmxConnector.toString());
		//((ConfigurableApplicationContext)context).close();
	}

}
