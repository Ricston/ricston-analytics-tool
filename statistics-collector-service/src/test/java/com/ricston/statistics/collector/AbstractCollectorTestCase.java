package com.ricston.statistics.collector;

import java.io.IOException;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ricston.statistics.jmx.Connector;
import com.ricston.statistics.model.AbstractCollectorStatistics;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public abstract class AbstractCollectorTestCase {
	
	@Autowired
	private Connector connector;
	
	@Test
	public void testCollector() throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		
		connector.connect();
		List<? extends AbstractCollectorStatistics> stats = getCollector().collect(connector.getMbeanServer());
		doAssert(stats);
	}
	
	protected abstract AbstractCollector getCollector();
	
	protected abstract void doAssert(List<? extends AbstractCollectorStatistics> stats);

}
