package com.ricston.statistics.model;

/**
 * Statistics for physical memory
 *
 */
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
