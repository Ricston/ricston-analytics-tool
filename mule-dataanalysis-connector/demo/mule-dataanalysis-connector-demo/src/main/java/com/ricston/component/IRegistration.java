package com.ricston.component;

import com.ricston.message.ApplicationRequest;
import com.ricston.message.ApplicationResponse;

import javax.jws.WebService;

@WebService
public interface IRegistration
{
    public ApplicationResponse register(ApplicationRequest request);
}


