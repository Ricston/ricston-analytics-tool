package com.ricston.mule.statistics.model;


public class PhysicalMemory extends AbstractCollectorStatistics {

	private Long memorySize;
	
	public PhysicalMemory(){
		super(StatisticsType.PHYSICAL_MEMORY);
	}

	public Long getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(Long memorySize) {
		this.memorySize = memorySize;
	}

}
