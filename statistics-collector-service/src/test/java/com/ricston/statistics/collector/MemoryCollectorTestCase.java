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
public class MemoryCollectorTestCase extends AbstractCollectorTestCase{
	
	@Autowired
	private MemoryCollector memoryCollector;
	

	@Override
	protected MemoryCollector getCollector() {
		return memoryCollector;
	}

	@Override
	protected void doAssert(List<? extends AbstractCollectorStatistics> stats) {
		Assert.assertEquals("HeapMemoryUsage", stats.get(0).getName());
		Assert.assertEquals("NonHeapMemoryUsage", stats.get(1).getName());
		
	}

}
