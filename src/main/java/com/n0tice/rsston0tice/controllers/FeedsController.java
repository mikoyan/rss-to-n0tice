package com.n0tice.rsston0tice.controllers;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.rsston0tice.api.N0ticeApiFactory;
import com.n0tice.rsston0tice.daos.FeedDAO;
import com.n0tice.rsston0tice.feeds.FeedService;
import com.n0tice.rsston0tice.model.Feed;
import com.n0tice.rsston0tice.model.FeedItem;
import com.n0tice.rsston0tice.model.forms.EditFeedForm;
import com.n0tice.rsston0tice.model.forms.NewFeedForm;
import com.n0tice.rsston0tice.services.FeedImportService;
import com.n0tice.rsston0tice.services.FeedItemHistoryService;
import com.n0tice.rsston0tice.services.TakeDownService;

@Controller
public class FeedsController {
	
	private static Logger log = Logger.getLogger(FeedsController.class);
	
	private LoggedInUserFilter loggedInUserFilter;
	private FeedDAO feedDAO;
	private UrlBuilder urlBuilder;
	private FeedService feedService;
	private FeedItemHistoryService feedItemHistoryService;
	private N0ticeApiFactory n0ticeApiFactory;
	private FeedImportService feedImportService;
	private TakeDownService takeDownService;
	
	@Autowired
	public FeedsController(LoggedInUserFilter loggedInUserFilter, FeedDAO feedDAO, UrlBuilder urlBuilder, FeedService feedService, FeedItemHistoryService feedItemHistoryService, N0ticeApiFactory n0ticeApiFactory, FeedImportService feedImportService, TakeDownService takeDownService) {
		this.loggedInUserFilter = loggedInUserFilter;
		this.feedDAO = feedDAO;
		this.urlBuilder = urlBuilder;
		this.feedService = feedService;
		this.feedItemHistoryService = feedItemHistoryService;
		this.n0ticeApiFactory = n0ticeApiFactory;
		this.feedImportService = feedImportService;
		this.takeDownService = takeDownService;
	}
	
	@RequestMapping("/feeds/new")
	public ModelAndView newFeed(@ModelAttribute("feed") NewFeedForm feedForm) {
		if (loggedInUserFilter.getLoggedInUser() == null) {
			return homePageRedirect();
		}
		
        ModelAndView mv = new ModelAndView("newfeed");
        populateUsersNoticeboardList(mv);
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());
        return mv;
    }
	
	@RequestMapping(value="/feeds/new", method=RequestMethod.POST)
	public ModelAndView newconsumerSubmit(@Valid @ModelAttribute("feed") NewFeedForm feedForm, BindingResult result) throws Exception {
		if (loggedInUserFilter.getLoggedInUser() == null) {
			return homePageRedirect();
		}
		
		// TODO duplicate check
		
		if (!result.hasErrors()) {
			final Feed feed = new Feed(
					loggedInUserFilter.getLoggedInUser(),
					feedService.getFeedTitle(feedForm.getUrl()),
					feedForm.getUrl(),
					feedForm.getNoticeboard() != null && !feedForm.getNoticeboard().trim().isEmpty() ? feedForm.getNoticeboard() : null,
					feedForm.isScheduled(),
					feedForm.getLatitude(),
					feedForm.getLongitude());
			
			feedDAO.save(feed);	
			return homePageRedirect();
		}
		
		final ModelAndView mv = new ModelAndView("newfeed");
        populateUsersNoticeboardList(mv);
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());
        return mv;
	}
	
	@RequestMapping(value="/feeds/{feedNumber}/edit",  method=RequestMethod.GET)
	public ModelAndView editFeed(@PathVariable("feedNumber") int feedNumber) {
		if (loggedInUserFilter.getLoggedInUser() == null) {
			return homePageRedirect();
		}
		
		ModelAndView mv = new ModelAndView("editfeed");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());        
        
        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(feedNumber - 1);
		mv.addObject("feed", feed);
        populateUsersNoticeboardList(mv);
		return mv;
    }
	
	@RequestMapping(value="/feeds/{feedNumber}/delete",  method=RequestMethod.GET)
	public ModelAndView deleteFeedConfirm(@PathVariable("feedNumber") int feedNumber) {
		if (loggedInUserFilter.getLoggedInUser() == null) {
			return homePageRedirect();
		}
		
		ModelAndView mv = new ModelAndView("deletefeedconfirm");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());        
        
        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(feedNumber - 1);
		mv.addObject("feed", feed);
		mv.addObject("feedNumber", feedNumber);
		return mv;
    }

	@RequestMapping(value="/feeds/{feedNumber}/delete",  method=RequestMethod.POST)
	public ModelAndView deleteFeed(@PathVariable("feedNumber") int feedNumber) {
		if (loggedInUserFilter.getLoggedInUser() == null) {
			return homePageRedirect();
		}
		
        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(feedNumber - 1);
        if (feed != null) {
        	log.info("Deleting feed: " + feed);
        	feedDAO.delete(feed);
        }
        
		return homePageRedirect();
    }
	@RequestMapping(value="/feeds/{feedNumber}/edit", method=RequestMethod.POST)
	public ModelAndView updateFeed(@PathVariable("feedNumber") int feedNumber, @Valid @ModelAttribute("feed") EditFeedForm feedForm, BindingResult result) throws Exception {
		if (loggedInUserFilter.getLoggedInUser() == null) {
			return homePageRedirect();
		}

        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(feedNumber - 1);
        if (feed == null) {
        	log.warn("Unknown feed to updatel ignoring: " + feedNumber);
			return homePageRedirect();
        }
        
		if (!result.hasErrors()) {			
			feed.setTitle(feedForm.getTitle());
			feed.setUrl(feedForm.getUrl());
			feed.setNoticeboard(feedForm.getNoticeboard());
			feed.setScheduled(feedForm.isScheduled());
			feed.setLatitude(feedForm.getLatitude());
			feed.setLongitude(feedForm.getLongitude());
			
			feedDAO.save(feed);
			return homePageRedirect();
		}
		
		final ModelAndView mv = new ModelAndView("newfeed");
        populateUsersNoticeboardList(mv);
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());
        return mv;
	}
		
	@RequestMapping(value="/feeds/{feedNumber}",  method=RequestMethod.GET)
	public ModelAndView feedItems(@PathVariable("feedNumber") int feedNumber) {
		if (loggedInUserFilter.getLoggedInUser() == null) {
			return homePageRedirect();
		}
		
		ModelAndView mv = new ModelAndView("feed");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());        
        
        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(feedNumber - 1);
		mv.addObject("feed", feed);
		mv.addObject("feedNumber", feedNumber);
        List<FeedItem> feedItems = feedService.getFeedItems(feed.getUrl());
        if (feedItems != null) {
        	mv.addObject("feeditems", feedItemHistoryService.decorateFeedItemsWithHistory(feedItems, loggedInUserFilter.getLoggedInUser()));
        }
        return mv;
    }
		
	@RequestMapping(value="/feeds/{feedNumber}/import", method=RequestMethod.POST)
	public ModelAndView importFeedItems(@PathVariable("feedNumber") int feedNumber) {
		if (loggedInUserFilter.getLoggedInUser() == null) {
			return homePageRedirect();
		}
		
        final Feed feed = feedDAO.getFeedsForUser(loggedInUserFilter.getLoggedInUser()).get(feedNumber - 1);       
        final int importedCount = feedImportService.importFeed(feed);
        
        final ModelAndView mv = new ModelAndView("imported");
        mv.addObject("loggedInUsername", loggedInUserFilter.getLoggedInUser());
		mv.addObject("feed", feed);
		mv.addObject("feedNumber", feedNumber);
		mv.addObject("importedCount", importedCount);        
        return mv;
    }
	
	@RequestMapping(value="/takedown", method=RequestMethod.GET)	// TODO confirm screen
	public ModelAndView takeDown(@RequestParam(value="id", required=true) String id, @RequestParam(value="feed", required=true) int feed) {
		final String user = loggedInUserFilter.getLoggedInUser();
		if (user == null) {
			return homePageRedirect();
		}
		
		log.info("User " + user + " requested takedown of: " + id);
		takeDownService.takeDown(user, id);
		
		return feedPageRedirect(feed);
    }
	
	private void populateUsersNoticeboardList(ModelAndView mv) {
		try {
			mv.addObject("noticeboards", n0ticeApiFactory.getReadOnlyApi().noticeboards(loggedInUserFilter.getLoggedInUser()));
		} catch (NotFoundException e) {
			log.error(e);
		} catch (ParsingException e) {
			log.error(e);
		} catch (HttpFetchException e) {
			log.error(e);
		}
	}
	
	private ModelAndView homePageRedirect() {
		return new ModelAndView(new RedirectView(urlBuilder.getHomepageUrl()));
	}
	
	private ModelAndView feedPageRedirect(int feedNumber) {
		return new ModelAndView(new RedirectView(urlBuilder.getFeedUrl(feedNumber)));
	}
	
}
