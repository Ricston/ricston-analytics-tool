/**
 * (c) 2003-2014 Ricston, Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.ricston.modules.dataanalysis;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import javax.inject.Inject;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.util.FileUtils;
import org.mule.util.UUID;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mule.api.MuleContext;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.expressions.Expr;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.lifecycle.Stop;

/**
 * Data Analysis Module. 
 * 
 * Collect KPIs from your flows and persist them temporarily until
 * the statistics collector reads all the data and publishes for analysis.
 *
 * @author Ricston Ltd.
 */
@Module(name="dataanalysis", schemaVersion="1.0", friendlyName="DataAnalysis")
public class DataAnalysisModule implements DataAnalysisMBean
{
	/**
	 * MapDb database
	 */
    private DB db;
    
    /**
     * Logger
     */
    protected Log logger = LogFactory.getLog(getClass());
    
    /**
     * MBean name to use to expose the data
     */
    private String MBEAN_NAME = "Mule.%s:type=com.ricston.dataanalysis,name=kpi";
    
    /**
     * Mule application name 
     */
    private String application;
    
    /**
     * The Mule Context
     */
    @Inject
    private MuleContext muleContext;
    
    /**
     * Start the module by registering to the platform Mbean server, and starting the Map DB
     * 
     * @throws MalformedObjectNameException
     * @throws NotCompliantMBeanException
     * @throws InstanceAlreadyExistsException
     * @throws MBeanRegistrationException
     * @throws IOException 
     */
    @Start
    public void startModule() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, IOException{
    	application = muleContext.getConfiguration().getId();
    	
    	logger.info("**********************************");
    	logger.info("*Starting Data Analysis Connector*");
    	logger.info("**********************************");
    	
    	MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		ObjectName id = new ObjectName(String.format(MBEAN_NAME, application));
		StandardMBean mbean = new StandardMBean(this, DataAnalysisMBean.class);
		server.registerMBean(mbean, id);
		
		logger.info("Registered with mbean using name: " + String.format(MBEAN_NAME, application));
		
		startMapDb();
    }
    
    /**
     * Stop the module by closing the Map DB and unregister from the Mbean server
     * 
     * @throws MalformedObjectNameException
     * @throws MBeanRegistrationException
     * @throws InstanceNotFoundException
     */
    @Stop
    public void stopModule() throws MalformedObjectNameException, MBeanRegistrationException, InstanceNotFoundException{
    	logger.info("**********************************");
    	logger.info("*Stopping Data Analysis Connector*");
    	logger.info("**********************************");
    	
    	stopMapDb();
    	
    	MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		ObjectName id = new ObjectName(String.format(MBEAN_NAME, application));
		server.unregisterMBean(id);
		
		logger.info("Unregistered mbean with name: " + String.format(MBEAN_NAME, application));
    }
    
    /**
     * Create a database with all the necessary settings
     * 
     * @param dbFile File backing up the MapDb
     * @return The MapDb
     */
    protected DB makeDb(File dbFile){
    	return DBMaker.newFileDB(dbFile)
						.transactionDisable()
						.asyncWriteEnable()
						.commitFileSyncDisable()
						.make();
    }
    
    /**
     * Start the MapDb
     * @throws IOException 
     */
    private void startMapDb() throws IOException{
    	String workingDir = muleContext.getConfiguration().getWorkingDirectory();
    	String dbFolderPath = workingDir + "/dataanalysis/" + application;
    	String dbFilePath = dbFolderPath + "/mapdb.dat";
    	FileUtils.forceMkdir(new File(dbFolderPath));
    	File dbFile = new File(dbFilePath);
    	
    	logger.info("Using " + dbFilePath + " as directory for analysis data persistency");
    	
    	//try to create the database
    	try
    	{
	    	db = makeDb(dbFile);
    	}
    	catch (IOError exc){
    		//if store was corrupted, back it up and create a new one
    		if (exc.getMessage().contains("Wrong index checksum")){
    			logger.error("Store is corrupted due to an incorrect shutdown. A backup of the current store will be taken, and a new store will be created.");
    			FileUtils.renameFile(dbFile, new File(dbFolderPath + "/" + UUID.getUUID()));  
    			db = makeDb(dbFile);
    		}
    	}
    }
    
    /**
     * Stop the MapDb
     */
    private void stopMapDb(){
    	if (db != null){
    		db.close();
    		db = null;
    	}
    }

    /**
     * Collect for Analysis processor. Accepts a map of key performance indicators (KPIs) and stores
     * the data in a MapDb backed persisted queue. Data is enhanced with current time, and message id.
     *
     * {@sample.xml ../../../doc/dataanalysis-connector.xml.sample dataanalysis:collect-for-analysis}
     *
     * @param kpiName name for the key performance indicator
     * @param data Key value pairs of data to collect
     * @param the message id
     */
    @Processor
    public void collectForAnalysis(String kpiName, Map<String, Object> data, @Expr("#[message.id]")String messageId)
    {
    	data.put("kpiName", kpiName);
    	data.put("type", "KPI");
    	data.put("messageId", messageId);
    	data.put("application", application);
    	data.put("@timestamp", new Date());
    	
    	BlockingQueue<Map<String, Object>> queue = db.getStack(kpiName);
    	queue.add(data);
    	
//    	db.commit();
    }

    /**
     * Get the Mule Context
     * 
     * @return the Mule Context
     */
	public MuleContext getMuleContext() {
		return muleContext;
	}
	
	/**
	 * Set the Mule Context
	 * 
	 * @param muleContext
	 */
	public void setMuleContext(MuleContext muleContext) {
		this.muleContext = muleContext;
	}

	/**
	 * Take the data from the MapDb backed persisted queue and return it as a List of Maps.
	 * The idea behind this method is that the statistics collector will poll this method
	 * for data every couple of seconds.
	 * 
	 */
	@Override
	public List<Map<String,Object>> getData() {
		logger.info("Retreiving analysis data");
		
		Map<String, Object > all = db.getAll();
		
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		
		for(Iterator<Map.Entry<String, Object>> i = all.entrySet().iterator(); i.hasNext(); ){
			@SuppressWarnings("unchecked")
			BlockingQueue<Map<String, Object>> q = (BlockingQueue<Map<String, Object>>) i.next().getValue();
			q.drainTo(items);
		}
		
//		db.commit();
		
		return items;
	}

}