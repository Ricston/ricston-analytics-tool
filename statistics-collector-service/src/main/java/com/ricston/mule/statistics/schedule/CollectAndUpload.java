package com.ricston.mule.statistics.schedule;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ricston.mule.statistics.main.Collector;
import com.ricston.mule.statistics.main.ElasticSearchUpload;
import com.ricston.mule.statistics.model.AbstractCollectorStatistics;
import com.ricston.mule.statistics.model.StatisticsType;

@Component
@EnableScheduling
public class CollectAndUpload {

	/**
	 * Collector instance to collect statistics
	 */
	@Autowired
	private Collector collector;
	
	/**
	 * Helper class to upload statistics to Elastic Search
	 */
	@Autowired
	private ElasticSearchUpload elasticSearchUpload;
	
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(getClass());
	
	/**
	 * Main method called by the scheduler. This method first collects all the statistics using the 
	 * autowired collector, and then uploads all the data to Elastic Search using the autowired
	 * Elastic Search upload helper.
	 * 
	 * @throws MalformedObjectNameException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
	@Scheduled(cron="${cron}")
	public void collectAndUpload() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		
		//collect data
		Map<StatisticsType, List<? extends AbstractCollectorStatistics>> stats = collector.collectAllStatisticalData();
		
		//upload in bulk for each type
		for(Map.Entry<StatisticsType, List<? extends AbstractCollectorStatistics>> statList : stats.entrySet()){
			elasticSearchUpload.uploadBulkData(statList.getKey(), statList.getValue());
		}
		
		logger.info("Stats types collected: " + stats.size());
		
		if (logger.isDebugEnabled()){
			logger.debug("Stats: " + stats.toString());
		}
	}

	/**
	 * 
	 * @return The statistics collector
	 */
	public Collector getCollector() {
		return collector;
	}

	/**
	 * 
	 * @param collector The statistics collector
	 */
	public void setCollector(Collector collector) {
		this.collector = collector;
	}

	/**
	 * 
	 * @return The Elastic Search upload helper
	 */
	public ElasticSearchUpload getElasticSearchUpload() {
		return elasticSearchUpload;
	}

	/**
	 * 
	 * @param elasticSearchUpload The Elastic Search upload helper
	 */
	public void setElasticSearchUpload(ElasticSearchUpload elasticSearchUpload) {
		this.elasticSearchUpload = elasticSearchUpload;
	}

}
