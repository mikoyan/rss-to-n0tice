package com.n0tice.rsston0tice.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n0tice.rsston0tice.daos.FeedDAO;
import com.n0tice.rsston0tice.feeds.FeedFetcher;
import com.n0tice.rsston0tice.model.Feed;
import com.n0tice.rsston0tice.model.FeedItem;

@Component
public class ScheduledImportTask implements Runnable {

	private static Logger log = Logger.getLogger(ScheduledImportTask.class);
	
	private FeedDAO feedDAO;
	private FeedFetcher feedFetcher;
	private ReportPostService reportPostService;
	
	@Autowired
	public ScheduledImportTask(FeedDAO feedDAO, FeedFetcher feedFetcher, ReportPostService reportPostService) {
		this.feedDAO = feedDAO;
		this.feedFetcher = feedFetcher;
		this.reportPostService = reportPostService;
	}
	
	public void run() {
		log.info("Starting scheduled import");

		final List<Feed> allScheduledFeeds = feedDAO.getAllScheduledFeeds();
		log.info(allScheduledFeeds.size() + " scheduled feeds to import");
		for (Feed feed : allScheduledFeeds) {
			importFeed(feed);
		}
	
		log.info("Finished scheduled import");
	}

	private void importFeed(Feed feed) {	// TODO push to shared service
		try {
			final List<FeedItem> feedItems = feedFetcher.getFeedItems(feed.getUrl());
			if (feedItems != null) {
				reportPostService.postReports(feedItems, feed.getUser(), feed.getNoticeboard());
			}			
		} catch (Exception e) {
			log.warn("Unexpected exception while importing feed: " + feed.toString(), e);
		}
	}
	
}
