package com.ricston.statistics.model;

/**
 * Statistics for gabage collector
 *
 */
public class Garbage extends AbstractCollectorStatistics {

	private Long collectionCount;
	private Long collectionTime;
	
	public Garbage(){
		super(StatisticsType.GARBADGE_COLLECTOR);
	}
	
	public Long getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Long collectionCount) {
		this.collectionCount = collectionCount;
	}

	public Long getCollectionTime() {
		return collectionTime;
	}

	public void setCollectionTime(Long collectionTime) {
		this.collectionTime = collectionTime;
	}

}
