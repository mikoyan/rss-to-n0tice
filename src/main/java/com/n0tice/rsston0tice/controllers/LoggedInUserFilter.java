package com.n0tice.rsston0tice.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoggedInUserFilter {

	private static final String LOGGED_IN_USER = "loggedInUser";

	private HttpServletRequest request;
	
	@Autowired
	public LoggedInUserFilter(HttpServletRequest request) {
		this.request = request;
	}
	
	public void setLoggedInUser(String username) {
		request.getSession().setAttribute(LOGGED_IN_USER, username);		
	}
	
	public String getLoggedInUser() {
		return (String) request.getSession().getAttribute(LOGGED_IN_USER);	
	}

	public void logoutUser() {
		request.getSession().removeAttribute(LOGGED_IN_USER);		
	}
	
}
