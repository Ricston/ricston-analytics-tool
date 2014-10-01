package com.ricston.statistics.collector;

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

import org.springframework.stereotype.Component;

import com.ricston.statistics.model.KPI;

/**
 * Collect the Mule KPIs exposed by the data analysis connector
 *
 */
@Component
public class KPICollector extends AbstractCollector{

	/**
	 * The MBean name to query for KPIs
	 */
	private String MBEAN_NAME = "%s:type=com.ricston.dataanalysis,name=kpi";

	/**
	 * Collect the Mule KPIs
	 */
	@Override
	public List<KPI> collect(
			MBeanServerConnection mbeanServer) throws IOException,
			MalformedObjectNameException, AttributeNotFoundException,
			InstanceNotFoundException, MBeanException, ReflectionException {
		
		//get all applications we need to monitor
		List<String> applications = applicationsToMonitor(mbeanServer);
		List<KPI> kpis = new ArrayList<KPI>();
		
		//collect KPIs for each application
		for (String application : applications){
			ObjectName object = new ObjectName(String.format(MBEAN_NAME, application));
			
			//applications not using the data analysis connector should be skipped
			if (mbeanServer.isRegistered(object)){

				//get the KPIs as List of Maps
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> appKpis = (List<Map<String, Object>>) mbeanServer.getAttribute(object, "Data");
				
				//convert into KPI statistics class and add to result list
				for (Map<String, Object> appKpi : appKpis){
					kpis.add(new KPI(appKpi));
				}
			}
		}
		
		return kpis;
	}

}
