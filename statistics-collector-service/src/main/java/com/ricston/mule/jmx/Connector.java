package com.ricston.mule.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JMX Connector class used to create connections to either
 * a local JVM or to a remote JVM 
 *
 */
public class Connector {
	
	/**
	 * Represents the connection to the MBean server
	 */
	protected MBeanServerConnection mbeanServer;
	
	/**
	 * The JMX Connector used to create the connection
	 */
	protected JMXConnector jmxConnector;
	
	/**
	 * JMX URL template with place holders for host, port, and path
	 */
	protected final String JMX_URL = "service:jmx:rmi:///jndi/rmi://%s:%d/%s";
	
	/**
	 * JMX host to connect to
	 */
	private String host;
	
	/**
	 * JMX port to connect to
	 */
	private Integer port;
	
	/**
	 * JMX path to connect to
	 */
	private String path;
	
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * Connect to JMX server. Try a local connection is host is empty,
	 * otherwise try a remote connection
	 * 
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
	 * Close the current JMX connection
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException{
		logger.debug("Closing connection");
		if (jmxConnector != null){
			jmxConnector.close();
			jmxConnector = null;
		}
		
		mbeanServer = null;
	}
	
	/**
	 * Connects to the local JVM's MBean server
	 * 
	 * @return Connection to MBean server
	 */
	protected MBeanServerConnection connectToLocalMBeanServer(){
		logger.debug("Connecting to local MBean server");
		return ManagementFactory.getPlatformMBeanServer();
	}
	
	/**
	 * Connects to the remote JVM's MBean server
	 * 
	 * @return Connection to MBean server
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
	 * 
	 * @return Connection to MBean server
	 */
	public MBeanServerConnection getMbeanServer() {
		return mbeanServer;
	}

	/**
	 * @return The host of the JMX connection
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host The host of the JMX connection
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return The port of the JMX connection
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port The port of the JMX connection
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return The path of the JMX connection
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path The path of the JMX connection
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Writes a nicely formatted string explaining to where the JMX 
	 * connection was made
	 */
	@Override
	public String toString() {
		String jmxUrl;
		if (StringUtils.isBlank(host)){
			jmxUrl = "LOCALHOST";
		}
		else{
			jmxUrl = String.format(JMX_URL, host, port, path);
		}
		
		return "Jmx Connector configured for: " + jmxUrl;
	}

}
