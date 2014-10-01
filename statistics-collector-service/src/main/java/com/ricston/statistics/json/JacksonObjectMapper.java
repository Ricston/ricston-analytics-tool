package com.ricston.statistics.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * Class that extends Jackson's ObjectMapper to set the
 * default date to ISO8601 format and UTC time zone
 *
 */
@Component
public class JacksonObjectMapper extends ObjectMapper{

	/**
	 * Create an ISO8601 date format with UTC time zone
	 * 
	 * @return ISO8601 date format with UTC time zone
	 */
    private static DateFormat getIso8601DateFormat(){
    	TimeZone utcTimezone = TimeZone.getTimeZone("UTC");
        DateFormat iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        iso8601DateFormat.setTimeZone(utcTimezone);
        
        return iso8601DateFormat;
    }
	
    /**
     * Initialise the Jackson ObjectMapper and set the 
     * ISO8601 date format with UTC time zone
     */
    public JacksonObjectMapper()
    {
        super();
        this.setDateFormat(getIso8601DateFormat());
    }

}
