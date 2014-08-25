package com.ricston.mule.statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import com.ricston.mule.statistics.model.KPI;

public class KPICollector extends AbstractCollector{
	
	private String MBEAN_NAME = "%s:type=com.ricston.dataanalysis,name=kpi";

	@Override
	public List<KPI> collect(
			MBeanServerConnection mbeanServer) throws IOException,
			MalformedObjectNameException, AttributeNotFoundException,
			InstanceNotFoundException, MBeanException, ReflectionException {
		
		List<String> applications = applicationsToMonitor(mbeanServer);
		List<KPI> kpis = new ArrayList<KPI>();
		
		for (String application : applications){
			ObjectName object = new ObjectName(String.format(MBEAN_NAME, application));
			
			if (mbeanServer.isRegistered(object)){
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> appKpis = (List<Map<String, Object>>) mbeanServer.getAttribute(object, "Data");
				
				for (Map<String, Object> appKpi : appKpis){
					kpis.add(new KPI(appKpi));
				}
			}
		}
		
		return kpis;
	}

}
