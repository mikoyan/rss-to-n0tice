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

import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.rsston0tice.api.N0ticeApiFactory;
import com.n0tice.rsston0tice.api.ReportPostService;
import com.n0tice.rsston0tice.daos.FeedDAO;
import com.n0tice.rsston0tice.feeds.FeedFetcher;
import com.n0tice.rsston0tice.model.Feed;
import com.n0tice.rsston0tice.model.FeedItem;
import com.n0tice.rsston0tice.model.forms.NewFeedForm;
import com.n0tice.rsston0tice.services.FeedItemHistoryService;

@Controller
public class FeedsController {
		
	private LoggedInUserFilter loggedInUserFilter;
	private FeedDAO feedDAO;
	private UrlBuilder urlBuilder;
	private FeedFetcher feedFetcher;
	private FeedItemHistoryService feedItemHistoryService;
	private ReportPostService reportPostService;
	private N0ticeApiFactory n0ticeApiFactory;
	
	@Autowired
	public FeedsController(LoggedInUserFilter loggedInUserFilter, FeedDAO feedDAO, UrlBuilder urlBuilder, FeedFetcher feedFetcher, FeedItemHistoryService feedItemHistoryService, ReportPostService reportPostService, N0ticeApiFactory n0ticeApiFactory) {
		this.loggedInUserFilter = loggedInUserFilter;
		this.feedDAO = feedDAO;
		this.urlBuilder = urlBuilder;
		this.feedFetcher = feedFetcher;
		this.feedItemHistoryService = feedItemHistoryService;
		this.reportPostService = reportPostService;
		this.n0ticeApiFactory = n0ticeApiFactory;
	}
	
	@RequestMapping("/feeds/new")
	public ModelAndView newFeed(@ModelAttribute("feed") NewFeedForm feedForm) {
        ModelAndView mv = new ModelAndView("newfeed");
        populateUsersNoticeboardList(mv);
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());
        return mv;
    }
	
	@RequestMapping(value="/feeds/new", method=RequestMethod.POST)
	public ModelAndView newconsumerSubmit(@Valid @ModelAttribute("feed") NewFeedForm feedForm, BindingResult result) throws Exception {
		
		// TODO duplicate check
		
		if (!result.hasErrors()) {
			final Feed feed = new Feed(
					loggedInUserFilter.getLoggedInUser(),
					feedForm.getUrl(),
					feedForm.getNoticeboard() != null && !feedForm.getNoticeboard().trim().isEmpty() ? feedForm.getNoticeboard() : null);
			feedDAO.addNewFeedForUser(feed);	
			return new ModelAndView(new RedirectView(urlBuilder.getHomepageUrl()));
		}
		
		final ModelAndView mv = new ModelAndView("newfeed");
        populateUsersNoticeboardList(mv);
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());
        return mv;
	}
	
	@RequestMapping(value="/feeds/{feedNumber}",  method=RequestMethod.GET)
	public ModelAndView feedItems(@PathVariable("feedNumber") int feedNumber) {
        ModelAndView mv = new ModelAndView("feed");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());        
        
        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(feedNumber - 1);
		mv.addObject("feed", feed);
		mv.addObject("feedNumber", feedNumber);
        List<FeedItem> feedItems = feedFetcher.getFeedItems(feed.getUrl());
        if (feedItems != null) {
        	mv.addObject("feeditems", feedItemHistoryService.decorateFeedItemsWithHistory(feedItems, loggedInUserFilter.getLoggedInUser(), feed.getNoticeboard()));
        }
        return mv;
    }
	
	@RequestMapping(value="/feeds/{feedNumber}", method=RequestMethod.POST)
	public ModelAndView importFeedItems(@PathVariable("feedNumber") int feedNumber) {        
        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(feedNumber - 1);
        List<FeedItem> feedItems = feedFetcher.getFeedItems(feed.getUrl());
 
        final int importedCount = reportPostService.postReports(feedItems, loggedInUserFilter.getLoggedInUser(), feed.getNoticeboard());
        
        ModelAndView mv = new ModelAndView("imported");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());
		mv.addObject("feed", feed);
		mv.addObject("feedNumber", feedNumber);
		mv.addObject("importedCount", importedCount);        
        return mv;
    }
	
	private void populateUsersNoticeboardList(ModelAndView mv) {
		try {
			mv.addObject("noticeboards", n0ticeApiFactory.getReadOnlyApi().noticeboards(loggedInUserFilter.getLoggedInUser()));
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
