package com.ricston.statistics.model;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

/**
 * Statistics for KPI
 *
 */
public class KPI extends AbstractCollectorStatistics {
	
	private Map<String, Object> kpis;

	public KPI() {
		super(StatisticsType.KPI);
		kpis = new HashMap<String, Object>();
	}
	
	public KPI(Map<String, Object> kpis) {
		this();
		this.kpis = kpis;
	}

	@JsonAnyGetter
	public Map<String, Object> getKpis() {
		return kpis;
	}

	public void setKpis(Map<String, Object> kpis) {
		this.kpis = kpis;
	}
	
	@JsonAnySetter
	public void setAnyField(String key, Object value){
		this.kpis.put(key, value);
	}

}
