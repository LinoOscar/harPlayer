package com.harPlayer.core.controllers;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class Redirect404Controller {
	
	private static final Logger logger = Logger.getLogger(Redirect404Controller.class);
	
	
	@RequestMapping(value={"/404"})
    public String return404(@CookieValue(value = "JSESSIONID", defaultValue = "") String cookieJSessionid, 
			  				@CookieValue(value = "IP-Restriction-GUID", defaultValue = "") String cookieIpRestrictionGUID,
			  				@CookieValue(value = "sn3User", defaultValue = "")String cookieSn3User,
			  				@CookieValue(value = "sn3Password", defaultValue = "")String cookieSn3Password,
			  				@CookieValue(value = "sn3Nebula", defaultValue = "")String sn3Nebula,
			  				@RequestParam("message") String message,
			  				HttpServletResponse response,ModelMap model)
	{
		logger.debug("Solicitado 404 con mensaje [" + message + "]");
		model.addAttribute("message",message);
		response.setStatus(404);
		return "404::404";
	}		
	

}
