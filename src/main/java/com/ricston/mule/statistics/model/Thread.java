package com.ricston.mule.statistics.model;

public class Thread extends AbstractCollectorStatistics {

	private Integer daemonThreadCount;
	private Integer threadCount;
	private Long currentThreadCpuTime;
	private Long currentThreadUserTime;
	
	public Thread(){
		super(StatisticsType.THREAD);
	}

	public Integer getDaemonThreadCount() {
		return daemonThreadCount;
	}

	public void setDaemonThreadCount(Integer daemonThreadCount) {
		this.daemonThreadCount = daemonThreadCount;
	}

	public Integer getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(Integer threadCount) {
		this.threadCount = threadCount;
	}

	public Long getCurrentThreadCpuTime() {
		return currentThreadCpuTime;
	}

	public void setCurrentThreadCpuTime(Long currentThreadCpuTime) {
		this.currentThreadCpuTime = currentThreadCpuTime;
	}

	public Long getCurrentThreadUserTime() {
		return currentThreadUserTime;
	}

	public void setCurrentThreadUserTime(Long currentThreadUserTime) {
		this.currentThreadUserTime = currentThreadUserTime;
	}
}
