package com.ricston.statistics.model;

/**
 * Statistics for Flow
 *
 */
public class Flow extends AbstractCollectorStatistics {

	private String application;
	private Long asyncEventsReceived;
	private Long averageProcessingTime;
	private Long executionErrors;
	private Long fatalErrors;
	private Long maxProcessingTime;
	private Long minProcessingTime;
	private Long processedEvents;
	private Long syncEventsReceived;
	private Long totalEventsReceived;
	private Long totalProcessingTime;
	
	private Long newAsyncEventsReceived;
	private Long newExecutionErrors;
	private Long newFatalErrors;
	private Long newProcessedEvents;
	private Long newSyncEventsReceived;
	private Long newTotalEventsReceived;
	private Long newTotalProcessingTime;
	
	public Flow(){
		super(StatisticsType.FLOW);
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public Long getAsyncEventsReceived() {
		return asyncEventsReceived;
	}

	public void setAsyncEventsReceived(Long asyncEventsReceived) {
		this.asyncEventsReceived = asyncEventsReceived;
	}

	public Long getAverageProcessingTime() {
		return averageProcessingTime;
	}

	public void setAverageProcessingTime(Long averageProcessingTime) {
		this.averageProcessingTime = averageProcessingTime;
	}

	public Long getExecutionErrors() {
		return executionErrors;
	}

	public void setExecutionErrors(Long executionErrors) {
		this.executionErrors = executionErrors;
	}

	public Long getFatalErrors() {
		return fatalErrors;
	}

	public void setFatalErrors(Long fatalErrors) {
		this.fatalErrors = fatalErrors;
	}

	public Long getMaxProcessingTime() {
		return maxProcessingTime;
	}

	public void setMaxProcessingTime(Long maxProcessingTime) {
		this.maxProcessingTime = maxProcessingTime;
	}

	public Long getMinProcessingTime() {
		return minProcessingTime;
	}

	public void setMinProcessingTime(Long minProcessingTime) {
		this.minProcessingTime = minProcessingTime;
	}

	public Long getProcessedEvents() {
		return processedEvents;
	}

	public void setProcessedEvents(Long processedEvents) {
		this.processedEvents = processedEvents;
	}

	public Long getSyncEventsReceived() {
		return syncEventsReceived;
	}

	public void setSyncEventsReceived(Long syncEventsReceived) {
		this.syncEventsReceived = syncEventsReceived;
	}

	public Long getTotalEventsReceived() {
		return totalEventsReceived;
	}

	public void setTotalEventsReceived(Long totalEventsReceived) {
		this.totalEventsReceived = totalEventsReceived;
	}

	public Long getTotalProcessingTime() {
		return totalProcessingTime;
	}

	public void setTotalProcessingTime(Long totalProcessingTime) {
		this.totalProcessingTime = totalProcessingTime;
	}

	public Long getNewAsyncEventsReceived() {
		return newAsyncEventsReceived;
	}

	public void setNewAsyncEventsReceived(Long newAsyncEventsReceived) {
		this.newAsyncEventsReceived = newAsyncEventsReceived;
	}

	public Long getNewExecutionErrors() {
		return newExecutionErrors;
	}

	public void setNewExecutionErrors(Long newExecutionErrors) {
		this.newExecutionErrors = newExecutionErrors;
	}

	public Long getNewFatalErrors() {
		return newFatalErrors;
	}

	public void setNewFatalErrors(Long newFatalErrors) {
		this.newFatalErrors = newFatalErrors;
	}

	public Long getNewProcessedEvents() {
		return newProcessedEvents;
	}

	public void setNewProcessedEvents(Long newProcessedEvents) {
		this.newProcessedEvents = newProcessedEvents;
	}

	public Long getNewSyncEventsReceived() {
		return newSyncEventsReceived;
	}

	public void setNewSyncEventsReceived(Long newSyncEventsReceived) {
		this.newSyncEventsReceived = newSyncEventsReceived;
	}

	public Long getNewTotalEventsReceived() {
		return newTotalEventsReceived;
	}

	public void setNewTotalEventsReceived(Long newTotalEventsReceived) {
		this.newTotalEventsReceived = newTotalEventsReceived;
	}

	public Long getNewTotalProcessingTime() {
		return newTotalProcessingTime;
	}

	public void setNewTotalProcessingTime(Long newTotalProcessingTime) {
		this.newTotalProcessingTime = newTotalProcessingTime;
	}
	
	/**
	 * Calculate delta in statistics
	 * 
	 * @param oldStatistics The previous statistics
	 */
	public void workNewStatistics(Flow oldStatistics){
		
		if (oldStatistics == null){
			newAsyncEventsReceived = asyncEventsReceived;
			newExecutionErrors = executionErrors;
			newFatalErrors = fatalErrors;
			newProcessedEvents = processedEvents;
			newSyncEventsReceived = syncEventsReceived;
			newTotalEventsReceived = totalEventsReceived;
			newTotalProcessingTime = totalProcessingTime;
		}
		else{
			//When application is restarted, statistics are lost. In this case, we default the result to zero rather than a negative value. 
			newAsyncEventsReceived = Math.max(asyncEventsReceived - oldStatistics.asyncEventsReceived, 0);
			newExecutionErrors = Math.max(executionErrors - oldStatistics.executionErrors, 0);
			newFatalErrors = Math.max(fatalErrors - oldStatistics.fatalErrors, 0);
			newProcessedEvents = Math.max(processedEvents - oldStatistics.processedEvents, 0);
			newSyncEventsReceived = Math.max(syncEventsReceived - oldStatistics.syncEventsReceived, 0);
			newTotalEventsReceived = Math.max(totalEventsReceived - oldStatistics.totalEventsReceived, 0);
			newTotalProcessingTime = Math.max(totalProcessingTime - oldStatistics.totalProcessingTime, 0);
		}
	}
	
}
