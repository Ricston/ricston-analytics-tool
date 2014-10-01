package com.ricston.statistics.model;

/**
 * Statistics for thread count
 *
 */
public class ThreadCount extends AbstractCollectorStatistics {

	private Integer threadCount;
	
	public ThreadCount(){
		super(StatisticsType.THREAD_COUNT);
	}
	
	public Integer getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(Integer threadCount) {
		this.threadCount = threadCount;
	}
}
