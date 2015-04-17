/**
 * (c) 2003-2014 Ricston, Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.ricston.component;

import com.ricston.message.ApplicationRequest;
import com.ricston.message.ApplicationResponse;

import java.util.Date;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@WebService(endpointInterface = "com.mulesoft.training.component.IRegistration", serviceName = "Registration")
public class Registration implements IRegistration
{
    protected Log logger = LogFactory.getLog(getClass());
    protected static final String MESSAGE = "Application from %s %s was %s.";
    
    public ApplicationResponse register(ApplicationRequest request)
    {
        ApplicationResponse response = new ApplicationResponse();
        response.setTimestamp(new Date());
        response.setAccepted(request.getAge() >= 25);
        
        logger.info(String.format(MESSAGE, request.getName(), request.getSurname(), response.isAccepted() ? "accepted" : "rejected"));
        
        return response;
    }
}


