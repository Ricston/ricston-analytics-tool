/**
 * (c) 2003-2014 Ricston, Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.ricston.component;

import com.ricston.message.ApplicationRequest;
import com.ricston.message.ApplicationResponse;

import javax.jws.WebService;

@WebService
public interface IRegistration
{
    public ApplicationResponse register(ApplicationRequest request);
}


