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
public class CpuLoadCollectorTestCase extends AbstractCollectorTestCase {
	
	@Autowired
	private CpuLoadCollector cpuLoadCollector;
	
	@Override
	protected CpuLoadCollector getCollector() {
		return cpuLoadCollector;
	}

	@Override
	protected void doAssert(List<? extends AbstractCollectorStatistics> stats) {
		Assert.assertEquals("ProcessCpuLoad", stats.get(0).getName());
		Assert.assertEquals("SystemCpuLoad", stats.get(1).getName());
		
	}

}
