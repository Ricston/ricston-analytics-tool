package com.ricston.mule.statistics.model;


public class OperatingSystem extends AbstractCollectorStatistics {

	private Double processCpuLoad;
	private Double systemCpuLoad;
	private Long freePhysicalMemorySize;
	private Long committedVirtualMemorySize;
	private Long openFileDescriptorCount;
	
	public OperatingSystem(){
		super(StatisticsType.OPERATING_SYSTEM);
	}

	public Double getProcessCpuLoad() {
		return processCpuLoad;
	}

	public void setProcessCpuLoad(Double processCpuLoad) {
		this.processCpuLoad = processCpuLoad;
	}

	public Double getSystemCpuLoad() {
		return systemCpuLoad;
	}

	public void setSystemCpuLoad(Double systemCpuLoad) {
		this.systemCpuLoad = systemCpuLoad;
	}

	public Long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}

	public void setFreePhysicalMemorySize(Long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}

	public Long getCommittedVirtualMemorySize() {
		return committedVirtualMemorySize;
	}

	public void setCommittedVirtualMemorySize(Long committedVirtualMemorySize) {
		this.committedVirtualMemorySize = committedVirtualMemorySize;
	}

	public Long getOpenFileDescriptorCount() {
		return openFileDescriptorCount;
	}

	public void setOpenFileDescriptorCount(Long openFileDescriptorCount) {
		this.openFileDescriptorCount = openFileDescriptorCount;
	}
	
}
