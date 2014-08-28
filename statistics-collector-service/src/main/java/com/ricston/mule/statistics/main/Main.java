package com.ricston.mule.statistics.main;

import java.io.IOException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	
	public static void main(String[] args) throws InterruptedException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
		System.out.println("Started Spring context");
		//((ConfigurableApplicationContext)context).close();
	}

}
