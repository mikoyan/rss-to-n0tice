package com.n0tice.rsston0tice.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomepageController {
	
	private LoggedInUserFilter loggedInUserFilter;
	
	@Autowired
	public HomepageController(LoggedInUserFilter loggedInUserFilter) {
		this.loggedInUserFilter = loggedInUserFilter;
	}
	
	@RequestMapping("/")
	public ModelAndView homepage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView("homepage");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());
        return mv;
    }
	
}
