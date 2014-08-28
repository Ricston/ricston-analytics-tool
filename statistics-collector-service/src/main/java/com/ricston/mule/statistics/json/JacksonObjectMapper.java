package com.ricston.mule.statistics.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JacksonObjectMapper extends ObjectMapper{

    private static DateFormat getIso8601DateFormat(){
    	TimeZone utcTimezone = TimeZone.getTimeZone("UTC");
        DateFormat iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        iso8601DateFormat.setTimeZone(utcTimezone);
        
        return iso8601DateFormat;
    }
	
    public JacksonObjectMapper()
    {
        super();
        this.setDateFormat(getIso8601DateFormat());
    }

}
