/**
 * (c) 2003-2014 Ricston, Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.ricston.modules.dataanalysis;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;

public class DataAnalysisModuleTest extends FunctionalTestCase
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
    	
    	DataAnalysisModule connector = muleContext.getRegistry().lookupObject(DataAnalysisModule.class);
    	List<Map<String, Object>> data = connector.getData();
    	Assert.assertNotNull(data);
    	Assert.assertEquals(3, data.size());
    }
    
    @Test
    public void testMultipleKpis() throws Exception
    {
    	runFlowWithPayloadAndExpect("testFlow", "Another string", "Another string");
    	runFlowWithPayloadAndExpect("testFlow2", "Another string", "Another string");
    	
    	DataAnalysisModule connector = muleContext.getRegistry().lookupObject(DataAnalysisModule.class);
    	List<Map<String, Object>> data = connector.getData();
    	Assert.assertNotNull(data);
    	Assert.assertEquals(2, data.size());
    }
    
    @Override
    protected <T, U> void runFlowWithPayloadAndExpect(String flowName, T expect, U payload) throws Exception
    {
        Assert.assertEquals(expect, this.runFlow(flowName, payload).getMessage().getPayload());
    }
}
