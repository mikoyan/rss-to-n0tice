package com.n0tice.rsston0tice.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n0tice.rsston0tice.api.ReportPostService;
import com.n0tice.rsston0tice.feeds.FeedFetcher;
import com.n0tice.rsston0tice.model.Feed;
import com.n0tice.rsston0tice.model.FeedItem;

@Component
public class FeedImportService {
	
	private static Logger log = Logger.getLogger(FeedImportService.class);

	private FeedFetcher feedFetcher;
	private ReportPostService reportPostService;
		
	@Autowired
	public FeedImportService(FeedFetcher feedFetcher, ReportPostService reportPostService) {
		this.feedFetcher = feedFetcher;
		this.reportPostService = reportPostService;
	}
	
	public int importFeed(Feed feed) {
		log.info("Importing feed: " + feed.toString());
		try {
			final List<FeedItem> feedItems = feedFetcher.getFeedItems(feed.getUrl());
			if (feedItems != null) {
				return reportPostService.postReports(feedItems, feed.getUser(), feed.getNoticeboard());
			}			
		} catch (Exception e) {
			log.warn("Unexpected exception while importing feed: " + feed.toString(), e);
		}
		return 0;
	}
	
}
