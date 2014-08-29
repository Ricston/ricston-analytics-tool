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

	@Autowired
	private Collector collector;
	
	@Autowired
	private ElasticSearchUpload elasticSearchUpload;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Scheduled(cron="${cron}")
	public void collectAndUpload() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		
		//collect data
		Map<StatisticsType, List<? extends AbstractCollectorStatistics>> stats = collector.collectAllStatisticalData();
		
		//upload data one at a time
		//TODO: use elastic search bulk upload
		for(Map.Entry<StatisticsType, List<? extends AbstractCollectorStatistics>> statList : stats.entrySet()){
			for(AbstractCollectorStatistics stat : statList.getValue()){
				elasticSearchUpload.uploadData(stat);
			}
		}
		logger.info("Stats types: " + stats.size() + " Stats: " + stats.toString());
	}

	public Collector getCollector() {
		return collector;
	}

	public void setCollector(Collector collector) {
		this.collector = collector;
	}

	public ElasticSearchUpload getElasticSearchUpload() {
		return elasticSearchUpload;
	}

	public void setElasticSearchUpload(ElasticSearchUpload elasticSearchUpload) {
		this.elasticSearchUpload = elasticSearchUpload;
	}

}
