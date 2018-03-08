package com.harPlayer.core.utils.har;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;


public class Cookie {
	
	private static final Logger logger = Logger.getLogger(Cookie.class);	
	
	String name;
	String value;
	String valueDecode;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
		try {
			setValueDecode( URLDecoder.decode(value,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("UnsupportedEncodingException",e);
		}
	}
	
	
	public String getValueDecode() {
		return valueDecode;
	}
	public void setValueDecode(String valueDecode) {
		this.valueDecode = valueDecode;
	}
	@Override
	public String toString() {
		return "Cookie [name=" + name + ", value=" + value + "]";
	}
	
	
	

}
