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
public class MemoryPoolCollectorTestCase extends AbstractCollectorTestCase {
	
	@Autowired
	private MemoryPoolCollector memoryPoolCollector;
	
	@Override
	protected MemoryPoolCollector getCollector() {
		return memoryPoolCollector;
	}

	@Override
	protected void doAssert(List<? extends AbstractCollectorStatistics> stats) {
		Assert.assertEquals("PS Eden Space", stats.get(0).getName());
		Assert.assertEquals("Code Cache", stats.get(1).getName());
		Assert.assertEquals("PS Old Gen", stats.get(2).getName());
		Assert.assertEquals("PS Perm Gen", stats.get(3).getName());
		Assert.assertEquals("PS Survivor Space", stats.get(4).getName());
	}

}
