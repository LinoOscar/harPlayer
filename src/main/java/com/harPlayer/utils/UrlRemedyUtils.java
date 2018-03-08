package com.harPlayer.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;

import com.harPlayer.utils.exceptions.UtilsException;

public class UrlRemedyUtils {
	
	private static final Logger logger = Logger.getLogger(UrlRemedyUtils.class);

	
	public static Date transformStringToDate(String date) throws UtilsException
	{
		try
		{
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		if (date.indexOf(".") != -1)	
		{
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = date.substring(0,date.indexOf("."));
		}
		
		if (date.indexOf("-") == 4)
		{
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}		
		
		return format.parse(date);
		}
		catch (ParseException e)
		{
			logger.error ("transformStringToDate parse exception for [" + date + "]");
			throw new UtilsException(e.getMessage());
		}
	}
	
	public static Date transformJiraStringToDate(String date) throws UtilsException
	{
		try
		{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");   // 2017-04-03T18:25:20.000+0200
		
		return format.parse(date);
		}
		catch (Throwable e)
		{
			logger.error ("transformJiraStringToDate parse exception for [" + date + "]",e);
			throw new UtilsException(e.getMessage());
		}
	}
	
	public static Date transformChromeStringToDate(String date) throws UtilsException
	{
		try
		{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");   // 2017-04-03T18:25:20.000+0200
		
		return format.parse(date);
		}
		catch (Throwable e)
		{
			try
			{
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");   // 2017-04-03T18:25:20.000+0200
				
				return format.parse(date);
			}
			catch (Throwable e2)
			{
				logger.error ("transformJiraStringToDate parse exception for [" + date + "]",e2);
				throw new UtilsException(e.getMessage());
			}

		}
	}	
	
	public static Date transformIEHarStringToDate(String date) throws UtilsException
	{
		try
		{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");   // 2017-04-03T18:25:20.000+0200
		
		return format.parse(date);
		}
		catch (Throwable e)
		{
			try
			{
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");   // 2017-04-03T18:25:20.000+0200
				
				return format.parse(date);
			}
			catch (Throwable e2)
			{
				try
				{
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");   // 2017-04-03T18:25:20.000+0200
					
					return format.parse(date);
				}
				catch (Throwable e3)
				{
					logger.error ("transformIEHarStringToDate parse exception for [" + date + "]",e3);
					throw new UtilsException(e.getMessage());
				}
			}
		}
	}
	
	public static String transformDateToString(Date date)
	{
		if (date != null)
		{
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return format.format(date);
		}
		else
			return "";
	}
	
	public static String replaceBackSlashHtml(String text)
	{
		text = text.replace("\r\n", "<BR/>");
		text = text.replace("\n", "<BR/>");
		
		return text;
	}
	
	public static String replaceBackSlashHtml2(String text)
	{
		text = HtmlUtils.htmlEscape(text);
		text = text.replace("\r\n", "<BR/>");
		text = text.replace("\n", "<BR/>");
		text = text.replaceAll("\\\\&quot;", "&quot;");
		
		return text;
	}	
	
	
	
	
	public static String insertBackSlashs(String text)
	{	
		String myText = "";
		String[] textAux = text.split("<BR/>");
		
		for (int i=0;i<textAux.length;i++)
		{
			if (textAux[i].length() > 150)
				textAux[i] = textAux[i].substring(0,150) + " <BR/> "  + textAux[i].substring(150);
		}
		
		for (int i=0;i<textAux.length;i++)
		{
			myText = myText + textAux[i] + "<BR/>";
		}
		
		return myText;

	}	
	
	public static String changeWhiteSpaces(String text)
	{
		return text.replace(" ", "&nbsp;");
	}
	

	
	
	public static String getRealAttachName(String fileName)
	{
		if (fileName.indexOf("\\\\") == -1)
			return fileName;
		else
		{
			String part1 = fileName.substring(0,fileName.lastIndexOf("/"));
			String part2 = fileName.substring(fileName.lastIndexOf("/") + 1);
			
			if (part2.indexOf("\\\\") != -1)
			{
				part2 = part2.substring(part2.lastIndexOf("\\\\") + 2);
			}
			
			return part1 + part2;
		}
	}


	public static String getDiffTime(Date myTime)
	{
		Date now = new Date();
		
		long timeTo = (now.getTime() - myTime.getTime()) / 1000;
		String unity = " segs.";

		if (timeTo > 60)
		{
			timeTo = timeTo / 60;
			unity = " mins.";
		}
		else
			return  getIntegerPart(timeTo) + unity;
		if (timeTo > 60)
		{
			timeTo = timeTo / 60;
			unity = " horas";
		}
		else
			return  getIntegerPart(timeTo) + unity;		
		
		if (timeTo > 24)
		{
			timeTo = timeTo / 24;
			unity = " días";
		}
		else
			return  getIntegerPart(timeTo) + unity;
		
		if (timeTo > 30)
		{
			timeTo = timeTo / 30;
			unity = " meses";
		}
		else
			return  getIntegerPart(timeTo) + unity;
				
		return getIntegerPart(timeTo) + unity;
	}
	
	private static String getIntegerPart(long myLong)
	{
		String result = String.valueOf(myLong);
		
		if (result.indexOf(".")!=-1)
			return result.substring(0,result.indexOf("."));
		else if (result.indexOf(".¡,")!=-1)
			return result.substring(0,result.indexOf(","));
		else
			return result;
		
	}
	

}
