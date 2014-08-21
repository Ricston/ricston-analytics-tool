package com.ricston.mule.statistics.model;


public class CpuLoad extends AbstractCollectorStatistics {

	private Double cpuLoad;
	
	public CpuLoad(){
		super(StatisticsType.CPU_LOAD);
	}

	public Double getCpuLoad() {
		return cpuLoad;
	}

	public void setCpuLoad(Double cpuLoad) {
		this.cpuLoad = cpuLoad;
	}

}
