package com.harPlayer.core.controllers;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.harPlayer.core.utils.HarStaticList;
import com.harPlayer.core.utils.har.HarEntry;

@Controller
public class HarController implements ErrorController{
	
	private static final Logger logger = Logger.getLogger(HarController.class);
	
	
	
	@RequestMapping(value={"/error"})
    public void har(@CookieValue(value = "JSESSIONID", defaultValue = "") String cookieJSessionid, 
			  				@CookieValue(value = "IP-Restriction-GUID", defaultValue = "") String cookieIpRestrictionGUID,
			  				@CookieValue(value = "sn3User", defaultValue = "")String cookieSn3User,
			  				@CookieValue(value = "sn3Password", defaultValue = "")String cookieSn3Password,
			  				@CookieValue(value = "sn3Nebula", defaultValue = "")String sn3Nebula,
			  				 ModelMap model,
			  				 HttpServletRequest request,
			  				 HttpServletResponse response,
			  				RedirectAttributes rattrs)
	{
		
    	
    try
    {	
    	String urlPath = (String)request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
    	//logger.debug("urlPath [" + urlPath + "]");
    	
    	// urlPath = (String) request.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE );
    	
    	if (request.getQueryString()!=null)
    		urlPath = urlPath + "?" + request.getQueryString();
    	
    	urlPath = urlPath.substring(1);
    	
    	logger.debug("urlPath [" + urlPath + "]");
    	
    	ArrayList<HarEntry> myNavegationHar = HarStaticList.getMyNavegationHar();
    	
    	if ((myNavegationHar==null) || (myNavegationHar.size()==0))
    	{
    		response.sendRedirect("/404?message=" + URLEncoder.encode("Now we use the error mappinf for reproduce har files... there aren't har charged so... sorry but ... 404 friend..."));
    		return ;
    	}
    	
    	String myResponseText="";
    	boolean founded=false;
    	
    		for (int i=0;i<myNavegationHar.size();i++)
    		{
    			String urlAux = myNavegationHar.get(i).getRequesUrl();

    			urlAux = urlAux.substring(urlAux.indexOf("//")+2);
    			urlAux = urlAux.substring(urlAux.indexOf("/")+1);
    			
    			if (urlAux.indexOf("#")!=-1)
    				urlAux = urlAux.substring(0,urlAux.indexOf("#"));

    			logger.debug(urlAux);
    			
    			if (urlAux.equals(urlPath))
    			{
    				founded=true;
    				response.setHeader("Content-Type",  myNavegationHar.get(i).getResponseContentMimeType());
    				response.setHeader("Cache-Control",  "no-cache, no-store, max-age=0, must-revalidate");
    				response.setStatus(200);
    				myResponseText = myNavegationHar.get(i).getResponseContentText();
    				
//    				for (int j=0;j<myNavegationHar.get(i).getResponseHeaders().size();j++)
//    				{
//    					String headerName = myNavegationHar.get(i).getResponseHeaders().get(j).getName();
//    					if ((!headerName.equals("Content-Encoding")) && (!headerName.equals("Date")) && (!headerName.equals("Transfer-Encoding")) && (!headerName.equals("Keep-Alive"))  && (!headerName.equals("Connection")) && (!headerName.equals("Content-Type")))
//    						response.setHeader(myNavegationHar.get(i).getResponseHeaders().get(j).getName(),  myNavegationHar.get(i).getResponseHeaders().get(j).getValue());
//    				}
    				
    				break;
    			}
    			
    		}

    		
    		if (myResponseText.equals(""))
    		{
    			if (founded)
    				response.sendRedirect("/404?message=" + URLEncoder.encode("Se ha encontrado en el har la request solicitada, pero su response en el har es vacía... no hay response text así que 404!"));
    			else
    				response.sendRedirect("/404?message=" + URLEncoder.encode("Hay har cargado pero no se ha encontrado al request [" + urlPath + "]... no queda otra que 404!"));
    			
    				return ;
    		}

    		 OutputStream os = response.getOutputStream();
     		 
    		 //logger.debug(myResponseText);
    		 byte[] responseByte = myResponseText.getBytes("UTF-8");
    		 
    		 os.write(responseByte);
    		 os.close();
    }

    catch (Exception e)
    {
    	logger.error("Exception in har",e);
    }   
    

    }



	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return null;
	}	

}
