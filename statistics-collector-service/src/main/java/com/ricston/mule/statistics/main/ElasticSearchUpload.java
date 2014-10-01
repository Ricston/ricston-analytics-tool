package com.ricston.mule.statistics.main;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ricston.mule.statistics.json.StatisticsToJson;
import com.ricston.mule.statistics.model.AbstractCollectorStatistics;
import com.ricston.mule.statistics.model.StatisticsType;

@Component
public class ElasticSearchUpload {
	
	@Value("${elasticsearch.host}")
	private String host;
	
	@Value("${elasticsearch.port}")
	private Integer port;
	
	@Autowired
	private StatisticsToJson statisitcsToJson;
	
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
	
	private static final String ELASTIC_SEARCH_API_URI = "http://%s:%d/mule-%s/%s";
	private static final String ELASTIC_SEARCH_BULK_API_URI = ELASTIC_SEARCH_API_URI + "/_bulk";
	
	private static final String ELASTIC_SEARCH_CREATE_BULK = "{ \"create\" : { } }\n";
	private static final String NEW_LINE = "\n";
	
	private Log logger = LogFactory.getLog(getClass());
	
	private CloseableHttpClient httpClient = HttpClients.createDefault();
	
	public void uploadData(AbstractCollectorStatistics stat) throws ClientProtocolException, IOException{
		
		logger.debug("Uploading single data item to Elastic Search");
		
		CloseableHttpResponse response = null;
		
		try
		{
			String json = statisitcsToJson.toJson(stat);
			
			HttpPost post = new HttpPost(String.format(ELASTIC_SEARCH_API_URI, host, port, dateFormat.format(new Date()), stat.getType().toString()));
			StringEntity entity = new StringEntity(json);
			post.setEntity(entity);
			response = httpClient.execute(post);
		}
		catch(HttpHostConnectException e){
			logger.error("Elastic Search is unreacheable");
		}
		finally{
			if (response != null){
				response.close();
			}
		}
		
		logger.debug("Upload complete");
	}
	
	public void uploadBulkData(StatisticsType type, List<? extends AbstractCollectorStatistics> stats) throws ClientProtocolException, IOException{
		
		logger.debug("Uploading bulk items to Elastic Search");
		
		CloseableHttpResponse response = null;
		
		try
		{
			StringBuilder builder = new StringBuilder();
			
			for (AbstractCollectorStatistics stat : stats){
				
				String json = statisitcsToJson.toJson(stat);
				
				builder.append(ELASTIC_SEARCH_CREATE_BULK);
				builder.append(json + NEW_LINE);
			}
			
			HttpPost post = new HttpPost(String.format(ELASTIC_SEARCH_BULK_API_URI, host, port, dateFormat.format(new Date()), type.toString()));
			StringEntity entity = new StringEntity(builder.toString());
			post.setEntity(entity);
			response = httpClient.execute(post);
		}
		catch(HttpHostConnectException e){
			logger.error("Elastic Search is unreacheable");
		}
		finally{
			if (response != null){
				response.close();
			}
		}
		
		logger.debug("Upload complete");
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public StatisticsToJson getStatisitcsToJson() {
		return statisitcsToJson;
	}

	public void setStatisitcsToJson(StatisticsToJson statisitcsToJson) {
		this.statisitcsToJson = statisitcsToJson;
	}

}
