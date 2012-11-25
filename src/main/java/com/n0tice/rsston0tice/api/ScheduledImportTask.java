package com.n0tice.rsston0tice.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n0tice.rsston0tice.daos.FeedDAO;
import com.n0tice.rsston0tice.model.Feed;
import com.n0tice.rsston0tice.services.FeedImportService;

@Component
public class ScheduledImportTask implements Runnable {

	private static Logger log = Logger.getLogger(ScheduledImportTask.class);
	
	private FeedDAO feedDAO;
	private FeedImportService feedImportService;
	
	@Autowired
	public ScheduledImportTask(FeedDAO feedDAO, FeedImportService feedImportService) {
		this.feedDAO = feedDAO;
		this.feedImportService = feedImportService;
	}
	
	public void run() {
		log.info("Starting scheduled import");
		final DateTime startTime = DateTime.now();

		final List<Feed> allScheduledFeeds = feedDAO.getAllScheduledFeeds();
		log.info(allScheduledFeeds.size() + " scheduled feeds to import");
		for (Feed feed : allScheduledFeeds) {
			feedImportService.importFeed(feed);
		}
		
		final Duration duration = new Duration(startTime.getMillis(), DateTime.now().getMillis());
		log.info("Finished scheduled import - imported " + allScheduledFeeds.size() + " in " + duration.toStandardSeconds() + " seconds");
	}
	
}
