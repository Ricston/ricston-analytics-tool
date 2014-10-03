package com.ricston.statistics.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ricston.statistics.model.AbstractCollectorStatistics;
import com.ricston.statistics.model.StatisticsType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public class CollectorTestCase {
	
	@Autowired
	private Collector collector;
	
	@Test
	public void testStatsAreCollected() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		Map<StatisticsType, List<? extends AbstractCollectorStatistics>> stats = collector.collectAllStatisticalData();
		
		//10 collectors in total, but KPIs and Flow collectors will not return
		//any data unless paired with a connector on the Mule side
		Assert.assertEquals(8, stats.size());
	}
	
//	@Test
	public void testStatsAreOfTheSameTypeInList() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		Map<StatisticsType, List<? extends AbstractCollectorStatistics>> stats = collector.collectAllStatisticalData();
		
		for(Map.Entry<StatisticsType, List<? extends AbstractCollectorStatistics>> entry : stats.entrySet()){
			StatisticsType type = entry.getKey();
			
			for(AbstractCollectorStatistics stat : entry.getValue()){
				if (type == StatisticsType.OPERATING_SYSTEM){
					Assert.assertTrue(stat.getType() == StatisticsType.OPERATING_SYSTEM ||
							stat.getType() == StatisticsType.PHYSICAL_MEMORY ||
							stat.getType() == StatisticsType.CPU_LOAD );
				}
				else{
					Assert.assertEquals(type, stat.getType());
				}
			}
		}
	}

}
