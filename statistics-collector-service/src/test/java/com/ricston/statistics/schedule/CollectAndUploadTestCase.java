package com.ricston.statistics.schedule;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import java.io.IOException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.ricston.statistics.main.ElasticSearchUpload;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public class CollectAndUploadTestCase {
	
	@Autowired
	private ElasticSearchUpload elasticSearchUpload;
	
	@Autowired
	private CollectAndUpload collectAndUpload;
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(9201);
	
	@Before
	public void setupElasticSearchUpload(){
		//set elastic search upload to point to localhost 9200 (for our mock service)
		elasticSearchUpload.setHost("localhost");
		elasticSearchUpload.setPort(9201);
	}
	
	@Test
	public void testCollectAndUpload() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		
		//create stub
		stubFor(post(urlMatching("/mule-[0-9.]+/.*/_bulk"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("{ \"error\" : \"false\" }"))
				);
		
		//collect and upload data
		collectAndUpload.collectAndUpload();
		
		
		//verify that 8 requests to Elastic Search bulk create were made
		verify(8, postRequestedFor(urlMatching("/mule-[0-9.]+/.*/_bulk")));
		
	}

}
