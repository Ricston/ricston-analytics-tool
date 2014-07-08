package com.ricston.mule.statistics.model;

public class ThreadCpuTime extends AbstractCollectorStatistics {

	private Long threadCpuTime;
	
	public ThreadCpuTime(){
		super(StatisticsType.THREAD_CPU_TIME);
	}

	public Long getThreadCpuTime() {
		return threadCpuTime;
	}

	public void setThreadCpuTime(Long threadCpuTime) {
		this.threadCpuTime = threadCpuTime;
	}

}
