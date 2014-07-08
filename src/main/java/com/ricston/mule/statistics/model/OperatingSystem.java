package com.ricston.mule.statistics.model;


public class OperatingSystem extends AbstractCollectorStatistics {

	private Long openFileDescriptorCount;
	
	public OperatingSystem(){
		super(StatisticsType.OPERATING_SYSTEM);
	}

	public Long getOpenFileDescriptorCount() {
		return openFileDescriptorCount;
	}

	public void setOpenFileDescriptorCount(Long openFileDescriptorCount) {
		this.openFileDescriptorCount = openFileDescriptorCount;
	}
	
}
