package com.n0tice.rsston0tice.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.n0tice.rsston0tice.daos.FeedDAO;

@Controller
public class HomepageController {
	
	private LoggedInUserFilter loggedInUserFilter;
	private FeedDAO feedDAO;
	
	@Autowired
	public HomepageController(LoggedInUserFilter loggedInUserFilter, FeedDAO feedDAO) {
		this.loggedInUserFilter = loggedInUserFilter;
		this.feedDAO = feedDAO;
	}
	
	@RequestMapping("/")
	public ModelAndView homepage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView("homepage");

        final String loggedInUser = loggedInUserFilter.getLoggedInUser();
		mv.addObject("loggedInUsername", loggedInUser);
        
        if (loggedInUser != null) {
        	mv.addObject("feeds", feedDAO.getFeedsForUser(loggedInUser));
        }
        
        return mv;
    }
	
}
