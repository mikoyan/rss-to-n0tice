package com.n0tice.rsston0tice.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.n0tice.rsston0tice.signin.N0ticeOauthSigninHandler;

@Controller
public class SigninController {

	private static Logger log = Logger.getLogger(SigninController.class);
	
	private N0ticeOauthSigninHandler signinHandler;
	private LoggedInUserFilter loggedInUserFilter;	
	private UrlBuilder urlBuilder;
	
	public SigninController() {
	}
	
	@Autowired
	public SigninController(N0ticeOauthSigninHandler signinHandler, LoggedInUserFilter loggedInUserFilter, UrlBuilder urlBuilder) {
		this.signinHandler = signinHandler;
		this.loggedInUserFilter = loggedInUserFilter;
		this.urlBuilder = urlBuilder;
	}
	
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return signinHandler.getLoginView(request, response);		
	}
	
	@RequestMapping("/callback")
	public ModelAndView callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String externalUserIdentifier = signinHandler.getExternalUserIdentifierFromCallbackRequest(request);
		if (externalUserIdentifier != null) {
			log.info("External user identifier is: " + externalUserIdentifier.toString());
			
			loggedInUserFilter.setLoggedInUser(externalUserIdentifier);
		}
		
		return new ModelAndView(new RedirectView(urlBuilder.getHomepageUrl()));							
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		loggedInUserFilter.logoutUser();
		return new ModelAndView(new RedirectView(urlBuilder.getHomepageUrl()));							
	}
	
}
