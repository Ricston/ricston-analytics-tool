package com.ricston.statistics.main;

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

import com.ricston.statistics.json.StatisticsToJson;
import com.ricston.statistics.model.AbstractCollectorStatistics;
import com.ricston.statistics.model.StatisticsType;

/**
 * Upload data to Elastic Search. Both single and bulk upload methods 
 * are available
 *
 */
@Component
public class ElasticSearchUpload {
	
	/**
	 * The Elastic Search host
	 */
	@Value("${elasticsearch.host}")
	private String host;
	
	/**
	 * The Elastic Search port
	 */
	@Value("${elasticsearch.port}")
	private Integer port;
	
	/**
	 * The transformer to convert statistics into JSON notation (auto wired)
	 */
	@Autowired
	private StatisticsToJson statisitcsToJson;
	
	/**
	 * Date format to use for the index name
	 */
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
	
	/**
	 * Elastic Search API URL template with place holders for host, port, date and type
	 */
	private static final String ELASTIC_SEARCH_API_URI = "http://%s:%d/mule-%s/%s";
	
	/**
	 * Elastic Search BULK API URL template with place holders for host, port, date and type
	 */
	private static final String ELASTIC_SEARCH_BULK_API_URI = ELASTIC_SEARCH_API_URI + "/_bulk";
	
	/**
	 * Elastic Search BULK API create command
	 */
	private static final String ELASTIC_SEARCH_CREATE_BULK = "{ \"create\" : { } }\n";
	
	/**
	 * New Line
	 */
	private static final String NEW_LINE = "\n";
	
	/**
	 * Logger
	 */
	private Log logger = LogFactory.getLog(getClass());
	
	/**
	 * HTTP Client
	 */
	private CloseableHttpClient httpClient = HttpClients.createDefault();
	
	/**
	 * Upload single statistic to Elastic Search
	 * 
	 * @param stat The statistic to be uploaded
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void uploadData(AbstractCollectorStatistics stat) throws ClientProtocolException, IOException{
		
		logger.debug("Uploading single data item to Elastic Search");
		
		CloseableHttpResponse response = null;
		
		try
		{
			//convert to JSON
			String json = statisitcsToJson.toJson(stat);
			
			//create and execute HTTP POST
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
	
	/**
	 * Upload a list of statistics to Elastic Search using the bulk upload API
	 */
	public void uploadBulkData(StatisticsType type, List<? extends AbstractCollectorStatistics> stats) throws ClientProtocolException, IOException{
		
		logger.debug("Uploading bulk items to Elastic Search");
		
		CloseableHttpResponse response = null;
		
		try
		{
			//convert the list of statistics into a bulk request
			StringBuilder builder = new StringBuilder();
			
			//for each statistic, create a line with the create command
			//followed by the statistic itself
			for (AbstractCollectorStatistics stat : stats){
				
				//convert to JSON
				String json = statisitcsToJson.toJson(stat);
				
				//append create command
				builder.append(ELASTIC_SEARCH_CREATE_BULK);
				//append json representation of statistic
				builder.append(json + NEW_LINE);
			}
			
			//create and execute HTTP POST
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

	/**
	 * 
	 * @return The Elastic Search host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 
	 * @param host The Elastic Search host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 
	 * @return The Elastic Search port
	 */
	public Integer getPort() {
		return port;
	}
	
	/**
	 * 
	 * @param port The Elastic Search port
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * 
	 * @return The Statistics to JSON transformer
	 */
	public StatisticsToJson getStatisitcsToJson() {
		return statisitcsToJson;
	}

	/**
	 * 
	 * @param statisitcsToJson The Statistics to JSON transformer
	 */
	public void setStatisitcsToJson(StatisticsToJson statisitcsToJson) {
		this.statisitcsToJson = statisitcsToJson;
	}

}
