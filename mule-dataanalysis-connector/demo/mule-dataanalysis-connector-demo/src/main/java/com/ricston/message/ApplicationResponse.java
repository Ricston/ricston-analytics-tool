/**
 * (c) 2003-2014 Ricston, Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.ricston.message;

import java.util.Date;

public class ApplicationResponse
{
    private boolean accepted;
    private Date timestamp;

    public ApplicationResponse()
    {
        super();
    }

    public ApplicationResponse(boolean accepted, Date timestamp)
    {
        super();
        this.accepted = accepted;
        this.timestamp = timestamp;
    }

    public boolean isAccepted()
    {
        return accepted;
    }

    public void setAccepted(boolean accepted)
    {
        this.accepted = accepted;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

}
