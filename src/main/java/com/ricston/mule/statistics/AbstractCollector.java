package com.ricston.mule.statistics;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.util.StringUtils;

import com.ricston.mule.statistics.model.AbstractCollectorStatistics;

public abstract class AbstractCollector {
	
	protected MBeanServerConnection mbeanServer;
	protected JMXConnector jmxConnector;
	
	protected final String JMX_URL = "service:jmx:rmi:///jndi/rmi://%s:%d/%s";
	
	private String host;
	private Integer port;
	private String path;
	
	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * @throws IOException
	 */
	public void connect() throws IOException{
		if (StringUtils.isBlank(host)){
			mbeanServer = connectToLocalMBeanServer();
		}
		else{
			mbeanServer = connectToRemoteMBeanServer();
		}
	}
	
	/**
	 * @throws IOException
	 */
	public void close() throws IOException{
		logger.debug("Closing connection");
		if (jmxConnector != null){
			jmxConnector.close();
		}
	}
	
	public abstract List<? extends AbstractCollectorStatistics> collect() throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException;
	
	public final List<? extends AbstractCollectorStatistics> collectStatistics() throws IOException, MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		connect();
		List<? extends AbstractCollectorStatistics> stats = collect();
		close();
		
		return stats;
	}
	
	/**
	 * @return
	 */
	protected MBeanServerConnection connectToLocalMBeanServer(){
		logger.debug("Connecting to local MBean server");
		return ManagementFactory.getPlatformMBeanServer();
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	protected MBeanServerConnection connectToRemoteMBeanServer() throws IOException{
		String jmxUrl = String.format(JMX_URL, host, port, path);
		logger.debug("Connecting to " + jmxUrl);
		
		JMXServiceURL url = new JMXServiceURL(String.format(JMX_URL, host, port, path));
		jmxConnector = JMXConnectorFactory.connect(url, null);
		
		MBeanServerConnection server = jmxConnector.getMBeanServerConnection();
		return server;
	}

	/**
	 * @return
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
