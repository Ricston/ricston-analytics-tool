package com.ricston.mule.statistics.model;

public class Garbadge extends AbstractCollectorStatistics {

	private Long collectionCount;
	private Long collectionTime;
	
	public Garbadge(){
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
