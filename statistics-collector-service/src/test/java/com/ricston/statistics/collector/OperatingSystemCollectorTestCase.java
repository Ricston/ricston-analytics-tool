package com.ricston.statistics.collector;

import java.util.List;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ricston.statistics.model.AbstractCollectorStatistics;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public class OperatingSystemCollectorTestCase extends AbstractCollectorTestCase {
	
	@Autowired
	private OperatingSystemCollector openFileDescriptorCountCollector;
	
	@Override
	protected OperatingSystemCollector getCollector() {
		return openFileDescriptorCountCollector;
	}

	@Override
	protected void doAssert(List<? extends AbstractCollectorStatistics> stats) {
		Assert.assertEquals("OperatingSystem", stats.get(0).getName());
	}

}
