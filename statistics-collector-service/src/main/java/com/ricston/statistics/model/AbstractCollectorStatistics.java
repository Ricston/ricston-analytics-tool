package com.ricston.statistics.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * The base model for statistical data. All statistics need to extends this class
 *
 */
/**
 * @author balinju
 *
 */
/**
 * @author balinju
 *
 */
@JsonAutoDetect
public abstract class AbstractCollectorStatistics {
	/**
	 * Statistics name
	 */
	private String name;
	
	/**
	 * Statistics type
	 */
	private StatisticsType type;
	
	/**
	 * Statistics timestamp
	 */
	private Date timestamp = new Date();
	
	/**
	 * 
	 * @param type Type of statistics
	 */
	public AbstractCollectorStatistics(StatisticsType type){
		this.type = type;
	}

	/**
	 * 
	 * @return The name of the statistic
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name The name of the statistic
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The type of the statistic
	 */
	public StatisticsType getType() {
		return type;
	}

	
	/**
	 * @param type The type of the statistic
	 */
	public void setType(StatisticsType type) {
		this.type = type;
	}

	/**
	 * @return The time of the statistic
	 */
	@JsonProperty("@timestamp")
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
