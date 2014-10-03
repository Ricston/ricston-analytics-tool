package com.ricston.statistics.main;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.ricston.statistics.model.Memory;
import com.ricston.statistics.model.StatisticsType;
import com.ricston.statistics.utils.Helper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public class ElasticSearchUploadTestCase {
	
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
	
	@Before
	public void setupElasticSearchUpload(){
		//set elastic search upload to point to localhost 9200 (for our mock service)
		elasticSearchUpload.setHost("localhost");
		elasticSearchUpload.setPort(9200);
	}
	
	@Test
	public void testSingleUpload() throws ClientProtocolException, IOException{
		
		//create stub
		stubFor(post(urlMatching("/mule-[0-9.]+/MEMORY"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("{ \"error\" : \"false\" }"))
				);
		
		//create sample statistic
		Memory memoryStatistic = createSampleStatistic();
		
		//use elastic search upload class to upload the data
		elasticSearchUpload.uploadData(memoryStatistic);
		
		//verify the request is correct
		verify(postRequestedFor(urlMatching("/mule-[0-9.]+/MEMORY"))
				.withRequestBody(matching(Helper.getResourceAsString("elastic-search-single-upload.json")))
				);
	}
	
	@Test
	public void testBulkUpload() throws ClientProtocolException, IOException{
		
		//create stub
		stubFor(post(urlMatching("/mule-[0-9.]+/MEMORY/_bulk"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("{ \"error\" : \"false\" }"))
				);
		
		//create sample statistic
		Memory memoryStatistic = createSampleStatistic();
		
		//create a list of statistics
		List<Memory> stats = new ArrayList<Memory>();
		stats.add(memoryStatistic);
		stats.add(memoryStatistic);
		stats.add(memoryStatistic);
		
		//use elastic search upload class to upload the data
		elasticSearchUpload.uploadBulkData(StatisticsType.MEMORY, stats);
		
		//verify the request is correct
		verify(postRequestedFor(urlMatching("/mule-[0-9.]+/MEMORY/_bulk"))
				.withRequestBody(matching(Helper.getResourceAsString("elastic-search-bulk-upload.json")))
				);
	}

}
