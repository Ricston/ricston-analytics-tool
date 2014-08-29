package com.ricston.mule.statistics.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ricston.mule.statistics.model.AbstractCollectorStatistics;

@Component
public class StatisticsToJson {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public String toJson(AbstractCollectorStatistics statistic) throws JsonGenerationException, JsonMappingException, IOException{
		return objectMapper.writeValueAsString(statistic);
	}

}
