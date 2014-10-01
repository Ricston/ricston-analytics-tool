package com.ricston.mule.statistics.model;


/**
 * Statistics for memory
 *
 */
public class Memory extends AbstractCollectorStatistics {

	private Long committed;
	private Long init;
	private Long max;
	private Long used;
	
	public Memory(){
		super(StatisticsType.MEMORY);
	}
	
	protected Memory(StatisticsType type){
		super(type);
	}
	
	public Long getCommitted() {
		return committed;
	}

	public void setCommitted(Long committed) {
		this.committed = committed;
	}

	public Long getInit() {
		return init;
	}

	public void setInit(Long init) {
		this.init = init;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}

	public Long getUsed() {
		return used;
	}

	public void setUsed(Long used) {
		this.used = used;
	}

}
