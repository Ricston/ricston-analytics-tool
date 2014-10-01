package com.ricston.mule.statistics.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ricston.mule.jmx.Connector;

/**
 * Main class which starts the Spring Context and adds
 * a shutdown hook to close the context
 *
 */
public class Main {
	
	static protected Log logger = LogFactory.getLog(Main.class);
	
	public static void main(String[] args) {
		
		//start the Spring context
		final ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
		Connector jmxConnector = context.getBean(Connector.class);
		logger.info("Collector started. " + jmxConnector.toString());
		
		//add shutdown hook to close the Spring context cleanly
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				logger.info("Shutting down");
				((ConfigurableApplicationContext)context).close();
				logger.info("Shut down complete");
			}
		});
	}

}
