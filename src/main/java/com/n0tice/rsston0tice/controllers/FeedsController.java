package com.n0tice.rsston0tice.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.n0tice.rsston0tice.api.ReportPostService;
import com.n0tice.rsston0tice.daos.FeedDAO;
import com.n0tice.rsston0tice.feeds.FeedFetcher;
import com.n0tice.rsston0tice.model.Feed;
import com.n0tice.rsston0tice.model.FeedItem;
import com.n0tice.rsston0tice.model.forms.NewFeedForm;

@Controller
public class FeedsController {
		
	private LoggedInUserFilter loggedInUserFilter;
	private FeedDAO feedDAO;
	private UrlBuilder urlBuilder;
	private FeedFetcher feedFetcher;
	private ReportPostService reportPostService;
	
	@Autowired
	public FeedsController(LoggedInUserFilter loggedInUserFilter, FeedDAO feedDAO, UrlBuilder urlBuilder, FeedFetcher feedFetcher, ReportPostService reportPostService) {
		this.loggedInUserFilter = loggedInUserFilter;
		this.feedDAO = feedDAO;
		this.urlBuilder = urlBuilder;
		this.feedFetcher = feedFetcher;
		this.reportPostService = reportPostService;
	}
	
	@RequestMapping("/feeds/new")
	public ModelAndView newFeed(@ModelAttribute("feed") NewFeedForm feedForm) {
        ModelAndView mv = new ModelAndView("newfeed");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());
        return mv;
    }
	
	@RequestMapping(value="/feeds/new", method=RequestMethod.POST)
	public ModelAndView newconsumerSubmit(@Valid @ModelAttribute("feed") NewFeedForm feedForm, BindingResult result) throws Exception {
		
		// TODO duplicate check

		if (!result.hasErrors()) {
			feedDAO.addNewFeedForUser(loggedInUserFilter.getLoggedInUser(), new Feed(feedForm.getUrl()));			
			return new ModelAndView(new RedirectView(urlBuilder.getHomepageUrl()));
		}
		
		final ModelAndView mv = new ModelAndView("newfeed");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());
        return mv;
	}
	
	@RequestMapping(value="/feeds/{feedNumber}",  method=RequestMethod.GET)
	public ModelAndView newFeed(@PathVariable("feedNumber") int feedNumber) {
        ModelAndView mv = new ModelAndView("feed");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());        
        
        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(feedNumber - 1);
		mv.addObject("feed", feed);
		mv.addObject("feedNumber", feedNumber);
        mv.addObject("feeditems", feedFetcher.getFeedItems(feed.getUrl()));
        return mv;
    }
	
	@RequestMapping(value="/feeds/{feedNumber}", method=RequestMethod.POST)
	public ModelAndView importFeedItems(@PathVariable("feedNumber") int feedNumber) {
        ModelAndView mv = new ModelAndView("feed");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());        
        
        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(feedNumber - 1);
        List<FeedItem> feedItems = feedFetcher.getFeedItems(feed.getUrl());
 
        reportPostService.postReports(feedItems, loggedInUserFilter.getLoggedInUser());
        
		return new ModelAndView(new RedirectView(urlBuilder.getHomepageUrl()));
    }
	
}
