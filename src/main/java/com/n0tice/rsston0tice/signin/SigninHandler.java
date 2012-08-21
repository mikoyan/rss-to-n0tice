package com.n0tice.rsston0tice.signin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public interface SigninHandler {

	public ModelAndView getLoginView(HttpServletRequest request, HttpServletResponse response) throws Exception;	
	public String getExternalUserIdentifierFromCallbackRequest(HttpServletRequest request);
		
}
