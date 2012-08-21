package com.n0tice.rsston0tice.controllers;

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

import com.n0tice.rsston0tice.daos.FeedDAO;
import com.n0tice.rsston0tice.feeds.FeedFetcher;
import com.n0tice.rsston0tice.model.Feed;
import com.n0tice.rsston0tice.model.forms.NewFeedForm;

@Controller
public class FeedsController {
	
	private LoggedInUserFilter loggedInUserFilter;
	private FeedDAO feedDAO;
	private UrlBuilder urlBuilder;
	private FeedFetcher feedFetcher;
	
	@Autowired
	public FeedsController(LoggedInUserFilter loggedInUserFilter, FeedDAO feedDAO, UrlBuilder urlBuilder, FeedFetcher feedFetcher) {
		this.loggedInUserFilter = loggedInUserFilter;
		this.feedDAO = feedDAO;
		this.urlBuilder = urlBuilder;
		this.feedFetcher = feedFetcher;
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
	
	@RequestMapping("/feeds/{id}")
	public ModelAndView newFeed(@PathVariable("id") int id) {
        ModelAndView mv = new ModelAndView("feed");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());        
        
        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(id - 1);
		mv.addObject("feed", feed);
        mv.addObject("feeditems", feedFetcher.getFeedItems(feed.getUrl()));
        return mv;
    }
	
}
