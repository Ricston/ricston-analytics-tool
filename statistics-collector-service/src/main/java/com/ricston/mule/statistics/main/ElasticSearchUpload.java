package com.ricston.mule.statistics.main;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ricston.mule.statistics.model.AbstractCollectorStatistics;

@Component
public class ElasticSearchUpload {
	
	@Value("${elasticsearch.host}")
	private String host;
	
	@Value("${elasticsearch.port}")
	private Integer port;
	
	@Autowired
	private StatisticsToJson statisitcsToJson;
	
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
	
	public void uploadData(AbstractCollectorStatistics stat) throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		try
		{
			String json = statisitcsToJson.toJson(stat);
			
			HttpPost post = new HttpPost(String.format("http://%s:%d/mule-%s/%s", host, port, dateFormat.format(new Date()), stat.getType().toString()));
			StringEntity entity = new StringEntity(json);
			post.setEntity(entity);
			httpClient.execute(post);
		}
		finally{
			httpClient.close();
		}
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
