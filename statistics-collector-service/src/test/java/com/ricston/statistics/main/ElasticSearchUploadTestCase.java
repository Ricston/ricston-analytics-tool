package com.ricston.statistics.main;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.ricston.statistics.model.Memory;
import com.ricston.statistics.model.StatisticsType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public class ElasticSearchUploadTestCase {
	
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
	
	@Autowired
	private ElasticSearchUpload elasticSearchUpload;
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(9200);
	
	protected Memory createSampleStatistic(){
		Memory memoryStatistic = new Memory();
		memoryStatistic.setCommitted(1L);
		memoryStatistic.setInit(1L);
		memoryStatistic.setMax(1L);
		memoryStatistic.setName("Heap");
		memoryStatistic.setTimestamp(new Date());
		memoryStatistic.setUsed(1L);
		
		return memoryStatistic;
	}
	
	@Test
	public void testSingleUpload() throws ClientProtocolException, IOException{
		
		//set elastic search upload to point to localhost 9200 (for our mock service)
		elasticSearchUpload.setHost("localhost");
		elasticSearchUpload.setPort(9200);
		
		//create stub
		stubFor(post(urlMatching("/mule-[0-9.]+/MEMORY"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("{ \"failed\" : \"false\" }"))
				);
		
		//create sample statistic
		Memory memoryStatistic = createSampleStatistic();
		
		//use elastic search upload class to upload the data
		elasticSearchUpload.uploadData(memoryStatistic);
		
		//verify the request is correct
		verify(postRequestedFor(urlMatching("/mule-[0-9.]+/MEMORY"))
				.withRequestBody(matching("[{]\"name\":\"Heap\",\"type\":\"MEMORY\",\"committed\":1,\"init\":1,\"max\":1,\"used\":1,\"@timestamp\":\".*\"[}]"))
				);
	}

}
