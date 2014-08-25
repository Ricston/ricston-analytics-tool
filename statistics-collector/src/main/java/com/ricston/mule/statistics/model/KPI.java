package com.ricston.mule.statistics.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;

public class KPI extends AbstractCollectorStatistics {
	
	private Map<String, Object> kpis;

	public KPI() {
		super(StatisticsType.KPI);
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

}
