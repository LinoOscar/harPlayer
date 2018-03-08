package com.harPlayer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class PropertyCharge {
	
	private static final Logger logger = Logger.getLogger(PropertyCharge.class);		
	
	private static String harDNS;

	static
	 {
		 try
		 {
			 //BufferedReader br = new BufferedReader(new FileReader(PropertyCharge.getResourcesPath() + "\\application.properties"));
			 InputStream in = PropertyCharge.class.getResourceAsStream("/application.properties");
			 Properties result = new Properties();
			 result.load(in);
			 in.close();

			 setHarDNS(result.getProperty("harDNS"));
		 }
		 catch (IOException e)
		 {
			 logger.error("IO Exception charging application.properties for proxy load... use default values");
		 }
	 }



	public static String getHarDNS() {
		return harDNS;
	}

	public static void setHarDNS(String harDNS) {
		PropertyCharge.harDNS = harDNS;
	}


	
}
