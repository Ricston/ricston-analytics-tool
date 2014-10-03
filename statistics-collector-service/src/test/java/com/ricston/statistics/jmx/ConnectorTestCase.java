package com.ricston.statistics.jmx;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public class ConnectorTestCase {
	
	@Autowired
	private Connector connector;
	
	@Test
	public void testJmxConnection() throws IOException{
		connector.connect();
		Assert.assertNotNull(connector.getMbeanServer());
	}

}
