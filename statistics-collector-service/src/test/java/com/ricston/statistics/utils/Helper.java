package com.ricston.statistics.utils;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class Helper {
	
	public static String getResourceAsString(String path) throws IOException{
		return IOUtils.toString(Helper.class.getClassLoader().getResourceAsStream(path));
	}
}
