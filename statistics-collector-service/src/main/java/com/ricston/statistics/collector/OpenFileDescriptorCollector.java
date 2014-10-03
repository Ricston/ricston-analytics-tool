package com.ricston.statistics.collector;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.springframework.stereotype.Component;

import com.ricston.statistics.model.OperatingSystem;

/**
 * Collect open file descriptor count statistics. This collector is stateless.
 *
 */
@Component
public class OpenFileDescriptorCollector extends AbstractCollector{
	
	/**
	 * Collect statistics for open file descriptor count
	 */
	@Override
	public List<OperatingSystem> collect(MBeanServerConnection mbeanServer) throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		logger.debug("Collecting operating system statistics");
		
		List<OperatingSystem> stats = new ArrayList<OperatingSystem>();
		
		ObjectName objectName = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);

		OperatingSystem stat = new OperatingSystem();
		stat.setName("OperatingSystem");
		stat.setOpenFileDescriptorCount((Long)mbeanServer.getAttribute(objectName, "OpenFileDescriptorCount"));
		stats.add(stat);
		
		logger.debug("Collecting open file descriptor count statistics completed");
		return stats;
	}
	
}
