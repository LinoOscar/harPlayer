package com.harPlayer.core.controllers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.harPlayer.core.utils.HarStaticList;
import com.harPlayer.core.utils.har.HarEntry;
import com.harPlayer.core.utils.har.Page;
import com.harPlayer.utils.PropertyCharge;
import com.harPlayer.utils.UrlRemedyUtils;


@Controller
public class ToolsController {
	
	private static final Logger logger = Logger.getLogger(ToolsController.class);


	@RequestMapping(value={"/","/tools"})
    public String tools( 
    						@RequestParam(value = "fileName", defaultValue = "") String myvalue,
			  				@RequestParam(value = "datacontent",  defaultValue = "") String dataContent,
			  				 ModelMap model,
			  				 HttpServletResponse response,
			  				RedirectAttributes rattrs)
	{
		
    	String messsage = "";
    	
    try
    {
    	if (!dataContent.equals("")) {
			String subData = dataContent.substring(dataContent.indexOf("base64,")+7);
			
			subData = subData.replaceAll(" ","\\+");
    	}

    		if (myvalue.toLowerCase().indexOf(".har")!=-1)
    		{
    			//fill
    			model.addAttribute("har", toolsIEHar(dataContent));
    			
        		ArrayList<String> myExit = new ArrayList<String>();
        		myExit.add("");
    			model.addAttribute("valueResult", myExit);
    		}			    		

    	
    	model.addAttribute("message",messsage);
    	model.addAttribute("valueId", myvalue);
    	return "tools :: tools";
    	
    }

    catch (Exception e)
    {
    	logger.error("Exception in ToolsController har charging...",e);
    	model.addAttribute("valueId", myvalue);    	
    	model.addAttribute("message", "Error [" + e.toString().replace(":", ":\n") + "]");
    	return "tools :: tools";
    }   
    

    }	

	
	public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }

        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String       t;

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
            case '\\':
            case '"':
                sb.append('\\');
                sb.append(c);
                break;
            case '/':
//                if (b == '<') {
                    sb.append('\\');
//                }
                sb.append(c);
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
               sb.append("\\r");
               break;
            default:
                if (c < ' ') {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
        sb.append('"');
        return sb.toString();
    }	
		
		
		private ArrayList<HarEntry> toolsIEHar(String dataContent) 	throws Exception			  				
		{ 
			

	    try
	    {
	    	logger.debug("start toolsIEHar");
			String subData = dataContent.substring(dataContent.indexOf("base64,")+7);
			subData = subData.replaceAll(" ","\\+");	    	
	    	String valueString = new String(Base64.getDecoder().decode(subData),"UTF-8");
	    	
	    	if ((int)valueString.charAt(0) == 65279)
	    	{
	    		valueString = valueString.substring(1);
	    	}
	    
	    	if (valueString.startsWith("{"))
	    		return toolsCHHar(dataContent);
	    	
	    	ArrayList<HarEntry> myHarEntryList = new ArrayList<HarEntry>();
	    	
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder =  dbFactory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(valueString));
	        
	        logger.debug("going to parse...");
	        Document doc = dBuilder.parse(is);
	        logger.debug("Parsed!");
	        
	        NodeList nListPage = (NodeList) doc.getElementsByTagName("page");
	        ArrayList<Page> myPages = new ArrayList<Page>();
	        
	        for (int i = 0; i < nListPage.getLength(); i++) 
	        {
	        	Element eElement = (Element) nListPage.item(i);
	        	
	        	Page myPage = new Page();
	        	
	        	myPage.setStartedDateTime(UrlRemedyUtils.transformIEHarStringToDate(eElement.getElementsByTagName("startedDateTime").item(0).getFirstChild().getNodeValue()));
	        	
	        	try
	        	{
	        		myPage.setTitle(eElement.getElementsByTagName("title").item(0).getFirstChild().getNodeValue());
   			 	}
   			 	catch (NullPointerException np1)
   			 	{
   			 		myPage.setTitle("");
   			 	}
	        	
	        	Element pageTimings =  (Element) eElement.getElementsByTagName("pageTimings").item(0);
	        	
	        	myPage.setDOMLoaded(Integer.parseInt(pageTimings.getElementsByTagName("onContentLoad").item(0).getFirstChild().getNodeValue()));
	        	myPage.setPageLoaded(Integer.parseInt(pageTimings.getElementsByTagName("onLoad").item(0).getFirstChild().getNodeValue()));
	        	
	        	myPage.setMaxMs(0);
	        	
	        	myPages.add(myPage);
	        }
	        
	        
	        NodeList nList = (NodeList) doc.getElementsByTagName("entry");
	        
	        logger.debug("xml requests result ->"  +nList.getLength() );
	        int accumMs=0;
	        int previousReq=-1;
	        
	        for (int i = 0; i < nList.getLength(); i++) 
	        {
	        	 	Element eElement = (Element) nList.item(i);

	        		 HarEntry auxHarEntry = new HarEntry();
	        		 
	        		 auxHarEntry.setPageRef(Integer.parseInt(eElement.getElementsByTagName("pageref").item(0).getFirstChild().getNodeValue()));
	        		 
	        		 if (auxHarEntry.getPageRef()!=previousReq)
	        		 {
	        			 previousReq = auxHarEntry.getPageRef();
	        			 auxHarEntry.setNewRequestColor("#E3E2E2");
	        		 }
	        		 else
	        		 {
	        			 auxHarEntry.setNewRequestColor("#f0f0f0");
	        		 }
	        		 
	        		 auxHarEntry.setPageStartedDateTime(myPages.get(auxHarEntry.getPageRef()).getStartedDateTime());
	        		 auxHarEntry.setPageTitle(myPages.get(auxHarEntry.getPageRef()).getTitle());
	        		 auxHarEntry.setPageDOMLoaded(myPages.get(auxHarEntry.getPageRef()).getDOMLoaded());
	        		 auxHarEntry.setPageContentLoaded(myPages.get(auxHarEntry.getPageRef()).getPageLoaded());
	        		 
	        		 auxHarEntry.setStartedDateTime(UrlRemedyUtils.transformIEHarStringToDate(eElement.getElementsByTagName("startedDateTime").item(0).getFirstChild().getNodeValue()));
	        		 auxHarEntry.setTime(Integer.parseInt(eElement.getElementsByTagName("time").item(0).getFirstChild().getNodeValue()));
	        		 auxHarEntry.setTimingsStart(new Long(auxHarEntry.getStartedDateTime().getTime() - myPages.get(auxHarEntry.getPageRef()).getStartedDateTime().getTime()).intValue());
	        		 
	        		 if ((auxHarEntry.getTimingsStart() + auxHarEntry.getTime()) > myPages.get(auxHarEntry.getPageRef()).getMaxMs())
	        			 myPages.get(auxHarEntry.getPageRef()).setMaxMs(auxHarEntry.getTimingsStart() + auxHarEntry.getTime());
	        		 
	        		 Element requestElement =  (Element) eElement.getElementsByTagName("request").item(0);
	        		
	        		 try
	        		 {
	        			 auxHarEntry.setRequestMethod(requestElement.getElementsByTagName("method").item(0).getFirstChild().getNodeValue());
	        		 }
	        		 catch (NullPointerException np)
	        		 {
	        			 auxHarEntry.setRequestMethod("???");
	        		 }
	        		 
	        		 auxHarEntry.setRequesUrl(requestElement.getElementsByTagName("url").item(0).getFirstChild().getNodeValue());
	        		 
	        		 String auxReqURL = auxHarEntry.getRequesUrl();
	        		 
	        		 if (auxHarEntry.getRequesUrl().charAt(auxHarEntry.getRequesUrl().length() - 1) == '/')
	        			 auxReqURL =  auxHarEntry.getRequesUrl().substring(0,auxHarEntry.getRequesUrl().length()-1);

	        		 auxReqURL = auxReqURL.substring(auxReqURL.lastIndexOf("/") + 1);
	        		 if (auxReqURL.indexOf("?")!=-1)
	        			 auxReqURL = auxReqURL.substring(0,auxReqURL.indexOf("?"));
	        		 
	        		 if (auxHarEntry.getRequesUrl().indexOf("/json")!=-1)
	        		 {
		        		 auxReqURL = auxHarEntry.getRequesUrl().substring(auxHarEntry.getRequesUrl().indexOf("/json"));
		        		 if (auxReqURL.indexOf("?")!=-1)
		        			 auxReqURL = auxReqURL.substring(0,auxReqURL.indexOf("?"));
	        		 }
	        		 
	        		 auxHarEntry.setRequesUrlShort(auxReqURL);
	        		 
	        		 auxReqURL = auxHarEntry.getRequesUrl();
	        		 if (auxReqURL.length() > 100)
	        			 auxReqURL = auxReqURL.substring(0, 100) + "...";
	        		 
	        		 auxHarEntry.setRequesUrlMiddle(auxReqURL);
	        		 
	        		 auxReqURL = auxHarEntry.getRequesUrl();
	        		 if (auxReqURL.indexOf("?") != -1)
	        			 auxHarEntry.setRequesUrlGetPart(auxReqURL.substring(auxReqURL.indexOf("?")));
	        		 else
	        			 auxHarEntry.setRequesUrlGetPart("");
	        		 
	        		 
	        		 auxReqURL = auxHarEntry.getRequesUrl();
	        		 if ((auxReqURL.toLowerCase().indexOf("ens") != -1) && ((auxReqURL.toLowerCase().indexOf("bto") != -1) || (auxReqURL.toLowerCase().indexOf("/json") != -1) || (auxReqURL.toLowerCase().indexOf("/ws") != -1)))
	        		 {
	        			 String[] auxENS= auxReqURL.split("/");
	        			 for (int jj1=0;jj1<auxENS.length;jj1++)
	        			 {
	        				 if (auxENS[jj1].toLowerCase().indexOf("ens")!=-1)
	        					 auxHarEntry.setRequestUrlAssembly("[" + auxENS[jj1] + "] ");
	        			 }
	        		 }
	        		 else
	        		 {
	        			 auxHarEntry.setRequestUrlAssembly("");
	        		 }
	        		 
	        		 auxReqURL = auxHarEntry.getRequesUrl();
	        		 auxReqURL=auxReqURL.substring(auxReqURL.indexOf("//") + 2);
	        		 auxReqURL=auxReqURL.substring(auxReqURL.indexOf("/") + 1);
	        		 
	        		 auxHarEntry.setRequestURLLocalHost(PropertyCharge.getHarDNS() + auxReqURL);
	        		 
	        		 try
	        		 {
	        			 auxHarEntry.setRequesHttpVersion(requestElement.getElementsByTagName("httpVersion").item(0).getFirstChild().getNodeValue());
	        		 }
	        		 catch (NullPointerException npr)
	        		 {
	        			 auxHarEntry.setRequesHttpVersion("");
	        		 }
	        		 
	        		 NodeList nListCookies = (NodeList) requestElement.getElementsByTagName("cookie");
	        		 ArrayList<com.harPlayer.core.utils.har.Cookie> myHarEntryCookies = new ArrayList<com.harPlayer.core.utils.har.Cookie>();
	        		 
	        		 for (int i2 = 0; i2 < nListCookies.getLength(); i2++) 
	        		 {
	        			 Element eElementCookie = (Element) nListCookies.item(i2);
	        			 
	        			 com.harPlayer.core.utils.har.Cookie auxCookie = new com.harPlayer.core.utils.har.Cookie();
	        			 auxCookie.setName(eElementCookie.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
	        			 auxCookie.setValue(eElementCookie.getElementsByTagName("value").item(0).getFirstChild().getNodeValue());
	        			
	        			 myHarEntryCookies.add(auxCookie);
	        		 }
	        		 auxHarEntry.setRequestCookies(myHarEntryCookies);
	        		 
	        		 NodeList nListHeaders = (NodeList) requestElement.getElementsByTagName("header");
	        		 ArrayList<com.harPlayer.core.utils.har.Header> myHarEntryHeaders = new ArrayList<com.harPlayer.core.utils.har.Header>();
	        		 
	        		 for (int i3 = 0; i3 < nListHeaders.getLength(); i3++) 
	        		 {
	        			 Element eElementHeader = (Element) nListHeaders.item(i3);
	        			 
	        			 com.harPlayer.core.utils.har.Header auxHeader = new com.harPlayer.core.utils.har.Header();
	        			 auxHeader.setName(eElementHeader.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
	        			 auxHeader.setValue(eElementHeader.getElementsByTagName("value").item(0).getFirstChild().getNodeValue());
	        			
	        			 myHarEntryHeaders.add(auxHeader);
	        		 }
	        		 auxHarEntry.setRequestHeaders(myHarEntryHeaders);	        		 
	        		 
	        		 NodeList nListParams = (NodeList) requestElement.getElementsByTagName("param");
	        		 ArrayList<com.harPlayer.core.utils.har.Param> myHarEntryParams = new ArrayList<com.harPlayer.core.utils.har.Param>();
	        		 
	        		 for (int i4 = 0; i4 < nListParams.getLength(); i4++) 
	        		 {
	        			 Element eElementParam = (Element) nListParams.item(i4);
	        			 
	        			 com.harPlayer.core.utils.har.Param auxParam = new com.harPlayer.core.utils.har.Param();
	        			 
	        			 try
	        			 {
	        			 auxParam.setName(eElementParam.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
	        			 }
	        			 catch (NullPointerException np11)
	        			 {
	        				 auxParam.setName("");
	        			 }	        			
	        			
	        			 try
	        			 {
	        				 auxParam.setValue(eElementParam.getElementsByTagName("value").item(0).getFirstChild().getNodeValue());
	        			 }
	        			 catch (NullPointerException np1)
	        			 {
	        				 auxParam.setValue("");
	        			 }
	        			
	        			 myHarEntryParams.add(auxParam);
	        		 }
	        		 auxHarEntry.setRequestQueryString(myHarEntryParams);	    
	        		 
	        		 
	        		 ArrayList<com.harPlayer.core.utils.har.Param> myHarPostDataItems = new ArrayList<com.harPlayer.core.utils.har.Param>();
	        		 try
	        		 {
	        			 Element postDataElement =  (Element) eElement.getElementsByTagName("postData").item(0);
	        			 
	        			 String postDataText = postDataElement.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
	        			 
	        			 postDataText = postDataText.replaceAll("&amp;","&");
	        			 
	        			 String[] myPostValues = postDataText.split("&");
	        			 
	        			 for (int j1=0;j1<myPostValues.length;j1++)
	        			 {
	        				 com.harPlayer.core.utils.har.Param auxParam = new com.harPlayer.core.utils.har.Param();
	        				 try
	        				 {
	        					 auxParam.setName(myPostValues[j1].substring(0,myPostValues[j1].indexOf("=")));
	        				 } catch (Exception e)
	        				 {
	        					 auxParam.setName("");
	        				 }
	        				 
	        				 try
	        				 {
	        					 auxParam.setValue(myPostValues[j1].substring(myPostValues[j1].indexOf("=") + 1));
	        				 } catch (Exception e)
	        				 {
	        					 auxParam.setValue("");
	        				 }	        				 
	        				 //auxParam.setValue("casita");
	        				 
	        				 myHarPostDataItems.add(auxParam);
	        			 }
	        			 
	        			 auxHarEntry.setRequestPostDataMymeType(postDataElement.getElementsByTagName("mimeType").item(0).getFirstChild().getNodeValue());
	        		 }
        			 catch (NullPointerException np1)
        			 {
        				 auxHarEntry.setRequestPostDataMymeType("");
        			 }
    				 auxHarEntry.setRequestPostData(myHarPostDataItems);
        			
	        		 
	        		 
	        		 
	        		 auxHarEntry.setRequestHeaderSize(Integer.parseInt(requestElement.getElementsByTagName("headersSize").item(0).getFirstChild().getNodeValue()));
	        		 auxHarEntry.setRequestBodySize(Integer.parseInt(requestElement.getElementsByTagName("bodySize").item(0).getFirstChild().getNodeValue()));
	        		 
	        		 Element responseElement =  (Element) eElement.getElementsByTagName("response").item(0);
	        		 try
	        		 {
	        			 auxHarEntry.setResponseStatus(Integer.parseInt(responseElement.getElementsByTagName("status").item(0).getFirstChild().getNodeValue()));
	        		 }
	        		 catch (NullPointerException npres)
	        		 {
	        			 auxHarEntry.setResponseStatus(-1);
	        		 }
	        		 
	        		 try
	        		 {
	        			 auxHarEntry.setResponseStatusText(responseElement.getElementsByTagName("statusText").item(0).getFirstChild().getNodeValue());
	        		 }
	        		 catch (NullPointerException npres)
	        		 {
	        			 auxHarEntry.setResponseStatusText("");
	        		 }	        		 
	        		 
	        		 NodeList nListCookiesResponse = (NodeList) responseElement.getElementsByTagName("cookie");
	        		 ArrayList<com.harPlayer.core.utils.har.Cookie> myHarEntryCookiesResponse = new ArrayList<com.harPlayer.core.utils.har.Cookie>();
	        		 
	        		 for (int i5 = 0; i5 < nListCookiesResponse.getLength(); i5++) 
	        		 {
	        			 Element eElementCookie = (Element) nListCookiesResponse.item(i5);
	        			 
	        			 com.harPlayer.core.utils.har.Cookie auxCookie = new com.harPlayer.core.utils.har.Cookie();
	        			 auxCookie.setName(eElementCookie.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
	        			 auxCookie.setValue(eElementCookie.getElementsByTagName("value").item(0).getFirstChild().getNodeValue());
	        			
	        			 myHarEntryCookiesResponse.add(auxCookie);
	        		 }
	        		 auxHarEntry.setResponseCookies(myHarEntryCookiesResponse);	    
	        		 
	        		 NodeList nListHeadersResponse = (NodeList) responseElement.getElementsByTagName("header");
	        		 ArrayList<com.harPlayer.core.utils.har.Header> myHarEntryHeadersResponse = new ArrayList<com.harPlayer.core.utils.har.Header>();
	        		 
	        		 for (int i6 = 0; i6 < nListHeadersResponse.getLength(); i6++) 
	        		 {
	        			 Element eElementHeader = (Element) nListHeadersResponse.item(i6);
	        			 
	        			 com.harPlayer.core.utils.har.Header auxHeader = new com.harPlayer.core.utils.har.Header();
	        			 auxHeader.setName(eElementHeader.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
	        			 
	        			 try
	        			 {
	        			 auxHeader.setValue(eElementHeader.getElementsByTagName("value").item(0).getFirstChild().getNodeValue());
	        			 }
	        			 catch (Throwable t1)
	        			 {
	        				 auxHeader.setValue("");
	        			 }
	        			
	        			 myHarEntryHeadersResponse.add(auxHeader);
	        		 }
	        		 auxHarEntry.setResponseHeaders(myHarEntryHeadersResponse);		
	        		 
	        		 Element responseContentElement =  (Element) responseElement.getElementsByTagName("content").item(0);
	        		 
	        		 auxHarEntry.setResponseContentSize(Integer.parseInt(responseContentElement.getElementsByTagName("size").item(0).getFirstChild().getNodeValue()));
	        		 
	        		 try
	        		 {
	        			 auxHarEntry.setResponseContentMimeType(responseContentElement.getElementsByTagName("mimeType").item(0).getFirstChild().getNodeValue());
	        		 
	        		 
	        		 String auxMimeType = auxHarEntry.getResponseContentMimeType();
	        		 if (auxMimeType.indexOf("/")!=-1)
	        			 auxMimeType = auxMimeType.substring(auxMimeType.indexOf("/")+1);
	        		 if (auxMimeType.indexOf(";")!=-1)
	        			 auxMimeType = auxMimeType.substring(0,auxMimeType.indexOf(";"));
	        		 
	        		 auxHarEntry.setResponseContentMimeTypeShort(auxMimeType);
	        		 }
	        		 catch (NullPointerException npAuxMime)
	        		 {
	        			 auxHarEntry.setResponseContentMimeType("");
	        			 auxHarEntry.setResponseContentMimeTypeShort("");
	        		 }
	        		 
	        		 try
	        		 {
	        		 auxHarEntry.setResponseContentText(responseContentElement.getElementsByTagName("text").item(0).getFirstChild().getNodeValue());
	        		 }
	        		 catch (NullPointerException np2)
	        		 {
	        			 auxHarEntry.setResponseContentText("");
	        		 }	        		 
	        		 
	        		 try
	        		 {
	        			 auxHarEntry.setResponseRedirectionUrl(responseElement.getElementsByTagName("redirectionURL").item(0).getFirstChild().getNodeValue());
	        		 }
	        		 catch (NullPointerException np2)
	        		 {
	        			 auxHarEntry.setResponseRedirectionUrl("");
	        		 }
	        		 
	        		 auxHarEntry.setResponseHeadersSize(Integer.parseInt(responseElement.getElementsByTagName("headersSize").item(0).getFirstChild().getNodeValue()));
	        		 auxHarEntry.setResponseBodySize(Integer.parseInt(responseElement.getElementsByTagName("bodySize").item(0).getFirstChild().getNodeValue()));
	        		 
	        		 try
	        		 {
	        			 auxHarEntry.setCache(eElement.getElementsByTagName("cache").item(0).getFirstChild().getNodeValue());
	        		 }
	        		 catch (NullPointerException np3)
	        		 {
	        			 auxHarEntry.setCache("");
	        		 }	        		 
	        		 
	        		 Element timingsElement =  (Element) eElement.getElementsByTagName("timings").item(0);
	        		 
	        		 auxHarEntry.setTimingsSend(Integer.parseInt(timingsElement.getElementsByTagName("send").item(0).getFirstChild().getNodeValue()));
	        		 auxHarEntry.setTimingsWait(Integer.parseInt(timingsElement.getElementsByTagName("wait").item(0).getFirstChild().getNodeValue()));
	        		 auxHarEntry.setTimingsReceive(Integer.parseInt(timingsElement.getElementsByTagName("receive").item(0).getFirstChild().getNodeValue()));
	        		 
	        		 myHarEntryList.add(auxHarEntry);

	        }
	        
	        for (int i=0;i<myHarEntryList.size();i++)
	        {
	        	myHarEntryList.get(i).setPageMaxMs(myPages.get(myHarEntryList.get(i).getPageRef()).getMaxMs());	        	
	        	logger.debug(myHarEntryList.get(i).getRequesUrlShort() + " " + myHarEntryList.get(i).getTimingsStart() + " " + myHarEntryList.get(i).getTime() + " "  +  myHarEntryList.get(i).getPageDOMLoaded() + " " + myHarEntryList.get(i).getPageContentLoaded()  + " " + myHarEntryList.get(i).getPageMaxMs());
	        	//logger.debug((new Double(myHarEntryList.get(i).getPageDOMLoaded()).doubleValue() / new Double(myHarEntryList.get(i).getPageMaxMs()).doubleValue()) * 100.00);
	        }
	        
	        HarStaticList.setMyNavegationHar(myHarEntryList);
	    	
	    	return myHarEntryList;
	    }
	    catch (Exception e)
	    {
	    	logger.error("Exception in toolsHar",e);
	    	throw e;
	    }   
	    

	    }	
		
		private ArrayList<HarEntry> toolsCHHar(String dataContent) 				  				
		{ 
			

	    try
	    {
	    	logger.debug("start toolsCHHar");
			String subData = dataContent.substring(dataContent.indexOf("base64,")+7);
			subData = subData.replaceAll(" ","\\+");	    	
	    	String valueString = new String(Base64.getDecoder().decode(subData),"UTF-8");
	    	
	    	if ((int)valueString.charAt(0) == 65279)
	    	{
	    		valueString = valueString.substring(1);
	    	}	    	
	    	
	    	ArrayList<HarEntry> myHarEntryList = new ArrayList<HarEntry>();
	    	
	    	JSONObject obj = new JSONObject(valueString);
	    	
	    	obj = obj.getJSONObject("log");

	    	ArrayList<Page> myPages = new ArrayList<Page>();
	    	boolean noPages=false;
	    	int firstPage = 0;
	    	
	    	try
	    	{
	    		
	    	JSONArray myJSONPages = obj.getJSONArray("pages");
	    	
	    	if (myJSONPages.length() == 0)
	    	{
	    		noPages=true;
	    		Page myPage = new Page();
	    		myPage.setTitle("Untitled (no Page)");
				myPage.setMaxMs(0);	    	
				myPage.setDOMLoaded(0);
				myPage.setPageLoaded(0);
				myPages.add(myPage);
	    	}
	    	else
	    	{
			for (int i=0;i<myJSONPages.length();i++)
			{
				if (i==0)
				{
					//starts with 0 page_ref??? see...
					firstPage = Integer.parseInt(myJSONPages.getJSONObject(i).getString("id").substring(5)) - 1;
					if (firstPage < 0) firstPage=0;
				}
				
				
				Page myPage = new Page();
				
				myPage.setStartedDateTime(UrlRemedyUtils.transformChromeStringToDate(myJSONPages.getJSONObject(i).getString("startedDateTime")));
				myPage.setTitle(myJSONPages.getJSONObject(i).getString("title"));
				
				myPage.setDOMLoaded(new Double(myJSONPages.getJSONObject(i).getJSONObject("pageTimings").getDouble("onContentLoad")).intValue());
				myPage.setPageLoaded(new Double(myJSONPages.getJSONObject(i).getJSONObject("pageTimings").getDouble("onLoad")).intValue());
				myPage.setMaxMs(0);
	        	
	        	myPages.add(myPage);				
			}
	    	}
	    	}
	    	catch  (JSONException j1)
	    	{
	    		noPages=true;
	    		Page myPage = new Page();
	    		myPage.setTitle("Untitled (no Page)");
				myPage.setMaxMs(0);	    	
				myPage.setDOMLoaded(0);
				myPage.setPageLoaded(0);
				myPages.add(myPage);
	    	}
	    	
	    	
			JSONArray myJSONEntries = obj.getJSONArray("entries");
			
	        logger.debug("json requests result ->"  +myJSONEntries.length());
	        int accumMs=0;
	        int previousReq=-1;
	        boolean existsPage0=false;
	        
	        for (int i = 0; i < myJSONEntries.length(); i++) 
	        {
	        	logger.debug("Pasada numero [" + i + "]");
	        	 HarEntry auxHarEntry = new HarEntry();
	        	 
	        	 String pageRef = "page_0";
	        	 
	        	 try
	        	 {
	        	 if (noPages)
	        		 pageRef = "page_0";
	        	 else
	        		 pageRef = myJSONEntries.getJSONObject(i).getString("pageref");
	        	 }
	        	 catch (JSONException j1)
	        	 {
	        		 noPages=true;
	        		 pageRef = "page_0";
	        	 }
	        	 
	        	 pageRef = pageRef.substring(pageRef.indexOf("_") + 1);
	        	 
	        	 if (Integer.parseInt(pageRef) == 0)
	        	 {
	        		 existsPage0=true;
	        	 }
	        	 
	        	 
	        	 if (existsPage0)
	        		 auxHarEntry.setPageRef(Integer.parseInt(pageRef));
	        	 else
	        		 auxHarEntry.setPageRef(Integer.parseInt(pageRef) - 1);
	        	 
	        	 
        		 if (auxHarEntry.getPageRef()!=previousReq)
        		 {
        			 previousReq = auxHarEntry.getPageRef();
        			 auxHarEntry.setNewRequestColor("#E3E2E2");
        		 }
        		 else
        		 {
        			 auxHarEntry.setNewRequestColor("#f0f0f0");
        		 }
        		 
        		 auxHarEntry.setStartedDateTime(UrlRemedyUtils.transformChromeStringToDate(myJSONEntries.getJSONObject(i).getString("startedDateTime")));

        		 //logger.info("pages number [" + myPages.size() + "] current i [" + i + "] auxHarEntry.getPageRef() [" + auxHarEntry.getPageRef() + "]");
        		 if ((noPages) && (i==0))
        		 {
        			 auxHarEntry.setPageStartedDateTime(auxHarEntry.getStartedDateTime());
        			 myPages.get(0).setStartedDateTime(auxHarEntry.getStartedDateTime());
        		 }
        		 else
        			 auxHarEntry.setPageStartedDateTime(myPages.get(auxHarEntry.getPageRef() - firstPage).getStartedDateTime());
        		 
        		 auxHarEntry.setPageTitle(myPages.get(auxHarEntry.getPageRef() - firstPage).getTitle());
        		 auxHarEntry.setPageDOMLoaded(myPages.get(auxHarEntry.getPageRef() - firstPage).getDOMLoaded());
        		 auxHarEntry.setPageContentLoaded(myPages.get(auxHarEntry.getPageRef() - firstPage).getPageLoaded());
        		 
        		
        		 
        		 auxHarEntry.setTime(new Double(myJSONEntries.getJSONObject(i).getDouble("time")).intValue());
        		 auxHarEntry.setTimingsStart(new Long(auxHarEntry.getStartedDateTime().getTime() - myPages.get(auxHarEntry.getPageRef()- firstPage).getStartedDateTime().getTime()).intValue());        		 

        		 if ((auxHarEntry.getTimingsStart() + auxHarEntry.getTime()) > myPages.get(auxHarEntry.getPageRef() - firstPage).getMaxMs())
        			 myPages.get(auxHarEntry.getPageRef() - firstPage).setMaxMs(auxHarEntry.getTimingsStart() + auxHarEntry.getTime());

        		 //request
        		 JSONObject myRequestJson = myJSONEntries.getJSONObject(i).getJSONObject("request");
        		 
        		 auxHarEntry.setRequestMethod(myRequestJson.getString("method"));
        		 auxHarEntry.setRequesUrl(myRequestJson.getString("url"));
        		 
        		 String auxReqURL = auxHarEntry.getRequesUrl();
        		 
        		 if (auxHarEntry.getRequesUrl().charAt(auxHarEntry.getRequesUrl().length() - 1) == '/')
        			 auxReqURL =  auxHarEntry.getRequesUrl().substring(0,auxHarEntry.getRequesUrl().length()-1);

        		 auxReqURL = auxReqURL.substring(auxReqURL.lastIndexOf("/") + 1);
        		 if (auxReqURL.indexOf("?")!=-1)
        			 auxReqURL = auxReqURL.substring(0,auxReqURL.indexOf("?"));
        		 
        		 if (auxHarEntry.getRequesUrl().indexOf("/json")!=-1)
        		 {
	        		 auxReqURL = auxHarEntry.getRequesUrl().substring(auxHarEntry.getRequesUrl().indexOf("/json"));
	        		 if (auxReqURL.indexOf("?")!=-1)
	        			 auxReqURL = auxReqURL.substring(0,auxReqURL.indexOf("?"));
        		 }
        		 
        		 auxHarEntry.setRequesUrlShort(auxReqURL);
        		 
        		 auxReqURL = auxHarEntry.getRequesUrl();
        		 if (auxReqURL.length() > 100)
        			 auxReqURL = auxReqURL.substring(0, 100) + "...";
        		 
        		 auxHarEntry.setRequesUrlMiddle(auxReqURL);
        		 
        		 auxReqURL = auxHarEntry.getRequesUrl();
        		 if (auxReqURL.indexOf("?") != -1)
        			 auxHarEntry.setRequesUrlGetPart(auxReqURL.substring(auxReqURL.indexOf("?")));
        		 else
        			 auxHarEntry.setRequesUrlGetPart("");
        		 
        		 
        		 auxReqURL = auxHarEntry.getRequesUrl();
        		 if ((auxReqURL.toLowerCase().indexOf("ens") != -1) && ((auxReqURL.toLowerCase().indexOf("bto") != -1) || (auxReqURL.toLowerCase().indexOf("/json") != -1) || (auxReqURL.toLowerCase().indexOf("/ws") != -1)))
        		 {
        			 String[] auxENS= auxReqURL.split("/");
        			 for (int jj1=0;jj1<auxENS.length;jj1++)
        			 {
        				 if (auxENS[jj1].toLowerCase().indexOf("ens")!=-1)
        					 auxHarEntry.setRequestUrlAssembly("[" + auxENS[jj1] + "] ");
        			 }
        		 }
        		 else
        		 {
        			 auxHarEntry.setRequestUrlAssembly("");
        		 }
        		 
        		 auxReqURL = auxHarEntry.getRequesUrl();
        		 auxReqURL=auxReqURL.substring(auxReqURL.indexOf("//") + 2);
        		 auxReqURL=auxReqURL.substring(auxReqURL.indexOf("/") + 1);
        		 
        		 auxHarEntry.setRequestURLLocalHost(PropertyCharge.getHarDNS() + auxReqURL);
        		 
        		 auxHarEntry.setRequesHttpVersion(myRequestJson.getString("httpVersion"));
        		 
        		 JSONArray myEntryCookies = myRequestJson.getJSONArray("cookies");
        		 ArrayList<com.harPlayer.core.utils.har.Cookie> myHarEntryCookies = new ArrayList<com.harPlayer.core.utils.har.Cookie>();
        		 
        		 for (int i2=0;i2 < myEntryCookies.length();i2++)
        		 {
        			 com.harPlayer.core.utils.har.Cookie auxCookie = new com.harPlayer.core.utils.har.Cookie();
        			 
        			 auxCookie.setName(myEntryCookies.getJSONObject(i2).getString("name"));
        			 auxCookie.setValue(myEntryCookies.getJSONObject(i2).getString("value"));
        			 myHarEntryCookies.add(auxCookie);
        		 }
        		 auxHarEntry.setRequestCookies(myHarEntryCookies);
        		 
        		 JSONArray myEntryHeaders = myRequestJson.getJSONArray("headers");
        		 ArrayList<com.harPlayer.core.utils.har.Header> myHarEntryHeaders = new ArrayList<com.harPlayer.core.utils.har.Header>();
        		 
        		 for (int i3=0;i3 < myEntryHeaders.length();i3++)
        		 {
        			 com.harPlayer.core.utils.har.Header auxHeader = new com.harPlayer.core.utils.har.Header();
        			 
        			 auxHeader.setName(myEntryHeaders.getJSONObject(i3).getString("name"));
        			 auxHeader.setValue(myEntryHeaders.getJSONObject(i3).getString("value"));
        			 
        			 myHarEntryHeaders.add(auxHeader);

        			 if (auxHeader.getName().toLowerCase().equals("content-type"))  
        				 auxHarEntry.setRequestPostDataMymeType(auxHeader.getValue());        			 
        		 }
        		 auxHarEntry.setRequestHeaders(myHarEntryHeaders);
        		 
        		 JSONArray myEntryQueryString = myRequestJson.getJSONArray("queryString");
        		 ArrayList<com.harPlayer.core.utils.har.Param> myHarEntryParams = new ArrayList<com.harPlayer.core.utils.har.Param>();
        		 
        		 for (int i4=0;i4 < myEntryQueryString.length();i4++)
        		 {
        			 com.harPlayer.core.utils.har.Param auxParam = new com.harPlayer.core.utils.har.Param();
        			 
        			 auxParam.setName(myEntryQueryString.getJSONObject(i4).getString("name"));
        			 auxParam.setValue(myEntryQueryString.getJSONObject(i4).getString("value"));
        			 
        			 myHarEntryParams.add(auxParam);
        		 }
        		 auxHarEntry.setRequestQueryString(myHarEntryParams);	
        		 
        		 ArrayList<com.harPlayer.core.utils.har.Param> myHarPostDataItems = new ArrayList<com.harPlayer.core.utils.har.Param>();
        		 
        		 try
        		 {
        		
        		 JSONArray myEntryPostData = myRequestJson.getJSONObject("postData").getJSONArray("params");
        		 
        		 for (int i5=0;i5 < myEntryPostData.length();i5++)
        		 {
        			 com.harPlayer.core.utils.har.Param auxParam = new com.harPlayer.core.utils.har.Param();
        			 
        			 auxParam.setName(myEntryPostData.getJSONObject(i5).getString("name"));
        			 auxParam.setValue(myEntryPostData.getJSONObject(i5).getString("value"));
        			 
        			 myHarPostDataItems.add(auxParam);
        			 
        			 logger.debug("postData es [" + auxParam.getName() + "]["  + auxParam.getValue() + "]");
        		 }
        		 } catch (JSONException j1)
        		 {
        		 }
        		 
        		 auxHarEntry.setRequestPostData(myHarPostDataItems);
        		 
        		 auxHarEntry.setRequestHeaderSize(myRequestJson.getInt("headersSize"));
        		 auxHarEntry.setRequestBodySize(myRequestJson.getInt("bodySize"));
        		 
        		//response
        		 JSONObject myResponseJson = myJSONEntries.getJSONObject(i).getJSONObject("response");
        		 
        		 auxHarEntry.setResponseStatus(myResponseJson.getInt("status"));
        		 auxHarEntry.setResponseStatusText(myResponseJson.getString("statusText"));
        		 
        		 JSONArray myEntryResponseCookies = myResponseJson.getJSONArray("cookies");
        		 ArrayList<com.harPlayer.core.utils.har.Cookie> myHarEntryCookiesResponse = new ArrayList<com.harPlayer.core.utils.har.Cookie>();        		 
        		 
        		 for (int i5=0;i5 < myEntryResponseCookies.length();i5++)
        		 {
        			 com.harPlayer.core.utils.har.Cookie auxCookie = new com.harPlayer.core.utils.har.Cookie();
        			 
        			 auxCookie.setName(myEntryResponseCookies.getJSONObject(i5).getString("name"));
        			 auxCookie.setValue(myEntryResponseCookies.getJSONObject(i5).getString("value"));
        			 myHarEntryCookiesResponse.add(auxCookie);
        		 }
        		 auxHarEntry.setResponseCookies(myHarEntryCookiesResponse);	   
        		 
        		 
        		 JSONArray myEntryHeadersResponse = myResponseJson.getJSONArray("headers");
        		 ArrayList<com.harPlayer.core.utils.har.Header> myHarEntryHeadersResponse = new ArrayList<com.harPlayer.core.utils.har.Header>();
        		 
        		 for (int i6=0;i6 < myEntryHeadersResponse.length();i6++)
        		 {
        			 com.harPlayer.core.utils.har.Header auxHeader = new com.harPlayer.core.utils.har.Header();
        			 
        			 auxHeader.setName(myEntryHeadersResponse.getJSONObject(i6).getString("name"));
        			 auxHeader.setValue(myEntryHeadersResponse.getJSONObject(i6).getString("value"));
        			 
        			 myHarEntryHeadersResponse.add(auxHeader);
        			 
        			 if (auxHeader.getName().toLowerCase().equals("content-type"))
        				 auxHarEntry.setResponseContentMimeType(auxHeader.getValue());           			 
        		 }
        		 auxHarEntry.setResponseHeaders(myHarEntryHeadersResponse);  
        		 
        		 auxHarEntry.setResponseContentSize(myResponseJson.getInt("bodySize"));
        		 
        		 String auxMimeType = auxHarEntry.getResponseContentMimeType();
        		 
        		 if ((auxMimeType!=null) && (!auxMimeType.equals("")))
        		 {
        			 if (auxMimeType.indexOf("/")!=-1)
        				 auxMimeType = auxMimeType.substring(auxMimeType.indexOf("/")+1);
        			 if (auxMimeType.indexOf(";")!=-1)
        				 auxMimeType = auxMimeType.substring(0,auxMimeType.indexOf(";"));
        		 
        			 auxHarEntry.setResponseContentMimeTypeShort(auxMimeType);   
        		 }
        		 else
        		 {
        			 auxHarEntry.setResponseContentMimeTypeShort("");
        		 }
        		 
        		 JSONObject MyResponseContent = myResponseJson.getJSONObject("content");
        		 
        		 try
        		 {
        			 auxHarEntry.setResponseContentText(MyResponseContent.getString("text"));
        		 } catch (JSONException j2)
        		 {
        			 auxHarEntry.setResponseContentText("");
        		 }        		 
        		 
        		 try
        		 {
        			 auxHarEntry.setResponseRedirectionUrl(myResponseJson.getString("redirectURL"));
           		 } catch (JSONException j3)
        		 {
        			 auxHarEntry.setResponseRedirectionUrl("");
        		 }         			 
        		 
        		 auxHarEntry.setResponseHeadersSize(myResponseJson.getInt("headersSize"));
        		 auxHarEntry.setResponseBodySize(myResponseJson.getInt("bodySize"));
        		 auxHarEntry.setCache("");
        		 
        		 JSONObject myTimingsResponse = myJSONEntries.getJSONObject(i).getJSONObject("timings");
        		 
        		 Double auxTimins = new Double(myTimingsResponse.getDouble("blocked") +  myTimingsResponse.getDouble("dns") +  myTimingsResponse.getDouble("connect") +  myTimingsResponse.getDouble("send"));
        		 auxHarEntry.setTimingsSend(auxTimins.intValue());
        		 auxTimins = new Double(myTimingsResponse.getDouble("wait"));
        		 auxHarEntry.setTimingsWait(auxTimins.intValue());
        		 auxTimins = new Double(myTimingsResponse.getDouble("receive"));
        		 auxHarEntry.setTimingsReceive(auxTimins.intValue());        		 
        		 
        		 myHarEntryList.add(auxHarEntry);
	        }
	        
	        for (int i=0;i<myHarEntryList.size();i++)
	        {
	        	myHarEntryList.get(i).setPageMaxMs(myPages.get(myHarEntryList.get(i).getPageRef() - firstPage).getMaxMs());	        	
	        	logger.debug(myHarEntryList.get(i).getRequesUrlShort() + " " + myHarEntryList.get(i).getTimingsStart() + " " + myHarEntryList.get(i).getTime() + " "  +  myHarEntryList.get(i).getPageDOMLoaded() + " " + myHarEntryList.get(i).getPageContentLoaded()  + " " + myHarEntryList.get(i).getPageMaxMs());
	        	//logger.debug((new Double(myHarEntryList.get(i).getPageDOMLoaded()).doubleValue() / new Double(myHarEntryList.get(i).getPageMaxMs()).doubleValue()) * 100.00);
	        }	        
			

	        HarStaticList.setMyNavegationHar(myHarEntryList);
	    	
	    	return myHarEntryList;
	    }
	    catch (Exception e)
	    {
	    	logger.error("Exception in toolsCHHar",e);
	    	e.printStackTrace();
	    	return null;
	    }   
	    

	    }		

}
