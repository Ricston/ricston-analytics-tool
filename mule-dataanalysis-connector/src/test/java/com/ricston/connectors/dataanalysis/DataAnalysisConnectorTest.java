/**
 * (c) 2003-2014 Ricston, Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.ricston.connectors.dataanalysis;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Assert;
import org.junit.Test;
import org.mule.api.registry.RegistrationException;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.util.FileUtils;
import org.mule.util.IOUtils;

import com.ricston.connectors.dataanalysis.DataAnalysisConnector;

public class DataAnalysisConnectorTest extends FunctionalTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "dataanalysis-config.xml";
    }

    @Test
    public void testSameKpi() throws Exception
    {
    	runFlowWithPayloadAndExpect("testFlow", "Another string", "Another string");
    	runFlowWithPayloadAndExpect("testFlow", "Another string", "Another string");
    	runFlowWithPayloadAndExpect("testFlow", "Another string", "Another string");
    	
    	DataAnalysisConnector connector = muleContext.getRegistry().lookupObject(DataAnalysisConnector.class);
    	List<Map<String, Object>> data = connector.getData();
    	Assert.assertNotNull(data);
    	Assert.assertEquals(3, data.size());
    }
    
    @Test
    public void testMultipleKpis() throws Exception
    {
    	runFlowWithPayloadAndExpect("testFlow", "Another string", "Another string");
    	runFlowWithPayloadAndExpect("testFlow2", "Another string", "Another string");
    	
    	DataAnalysisConnector connector = muleContext.getRegistry().lookupObject(DataAnalysisConnector.class);
    	List<Map<String, Object>> data = connector.getData();
    	Assert.assertNotNull(data);
    	Assert.assertEquals(2, data.size());
    }
    
    @Test
    public void corruptedDataFile() throws IOException, RegistrationException, MalformedObjectNameException, MBeanRegistrationException, InstanceNotFoundException, NotCompliantMBeanException, InstanceAlreadyExistsException{
    	
    	DataAnalysisConnector connector = muleContext.getRegistry().lookupObject(DataAnalysisConnector.class);
    	
    	//stop connector
    	connector.stopModule();
    	
    	//create corrupted file
    	String workingDir = muleContext.getConfiguration().getWorkingDirectory();
    	String dbFolderPath = workingDir + "/dataanalysis/" + muleContext.getConfiguration().getId();
    	String dbFilePath = dbFolderPath + "/mapdb.dat";
    	
    	logger.info("Corrupting file: " + dbFilePath);
    	
    	FileUtils.copyStreamToFile(IOUtils.getResourceAsStream("errorfile.dat", this.getClass()), new File(dbFilePath));
    	
    	//start connector, this should trigger the code to backup the corrupted file
    	connector.startModule();
    	
    	//check there are 3 files, 2 files for MapDb and a backup file
    	Collection<?> files = FileUtils.listFiles(new File(dbFolderPath), TrueFileFilter.TRUE, TrueFileFilter.TRUE);
    	Assert.assertEquals(3, files.size());
    }
    
    @Override
    protected <T, U> void runFlowWithPayloadAndExpect(String flowName, T expect, U payload) throws Exception
    {
        Assert.assertEquals(expect, this.runFlow(flowName, payload).getMessage().getPayload());
    }
}
