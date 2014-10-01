package com.ricston.mule.statistics.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ricston.mule.statistics.model.AbstractCollectorStatistics;

/**
 * Use Jackson's ObjectMapper to convert any statistics class into a JSON string 
 *
 */
@Component
public class StatisticsToJson {
	
	/**
	 * The Jackson's ObjectMapper to use for the converstion
	 */
	@Autowired
	private ObjectMapper objectMapper;
	
	/**
	 * Convert statistics into JSON format
	 * 
	 * @param statistic The statistics to convert
	 * @return JSON format of the statistics passed as parameter
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String toJson(AbstractCollectorStatistics statistic) throws JsonGenerationException, JsonMappingException, IOException{
		return objectMapper.writeValueAsString(statistic);
	}

}
