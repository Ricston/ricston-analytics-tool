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
public class ThreadCpuTimeCollectorTestCase extends AbstractCollectorTestCase{
	
	@Autowired
	private ThreadCpuTimeCollector threadCpuTimeCollector;
	

	@Override
	protected ThreadCpuTimeCollector getCollector() {
		return threadCpuTimeCollector;
	}

	@Override
	protected void doAssert(List<? extends AbstractCollectorStatistics> stats) {
		Assert.assertEquals("CurrentThreadCpuTime", stats.get(0).getName());
		Assert.assertEquals("CurrentThreadUserTime", stats.get(1).getName());
	}

}
