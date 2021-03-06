package com.n0tice.rsston0tice.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.n0tice.rsston0tice.daos.FeedDAO;
import com.n0tice.rsston0tice.model.Feed;
import com.n0tice.rsston0tice.services.BlockedUserService;
import com.n0tice.rsston0tice.services.FeedImportService;

@Component
public class ScheduledImportTask implements Runnable {

	private static Logger log = Logger.getLogger(ScheduledImportTask.class);
	
	private FeedDAO feedDAO;
	private BlockedUserService blockedUserService;
	private FeedImportService feedImportService;
	private TaskExecutor taskExecutor;
	
	@Autowired
	public ScheduledImportTask(FeedDAO feedDAO, BlockedUserService blockedUserService, FeedImportService feedImportService, TaskExecutor taskExecutor) {
		this.feedDAO = feedDAO;
		this.blockedUserService = blockedUserService;
		this.feedImportService = feedImportService;
		this.taskExecutor = taskExecutor;
	}
	
	public void run() {
		log.info("Starting scheduled import");
		final DateTime startTime = DateTime.now();

		final List<Feed> allScheduledFeeds = feedDAO.getAllScheduledFeeds();
		final int numberOfFeeds = allScheduledFeeds.size();
		log.info(numberOfFeeds + " scheduled feeds to import");
		int counter = 0;
		for (Feed feed : allScheduledFeeds) {
			counter++;
			log.info("Processing feed " + counter + "/" + numberOfFeeds);
			if (isBlockedUser(feed.getUser())) {
				log.warn("Skipping feed belonging to blocked user: " + feed.getTitle() + ", " + feed.getUser());
				
			} else {
				
				taskExecutor.execute(new ProcessFeedTask(feedImportService, feed));
			}
		}
		
		final Duration duration = new Duration(startTime.getMillis(), DateTime.now().getMillis());
		log.info("Finished scheduled import - imported " + numberOfFeeds + " in " + duration.toStandardSeconds().getSeconds() + " seconds");
	}
	
	private boolean isBlockedUser(String user) {
		return blockedUserService.isBlocked(user);
	}

	private class ProcessFeedTask implements Runnable {
		
		private Feed feed;
		private FeedImportService feedImportService;
		
		public ProcessFeedTask(FeedImportService feedImportService, Feed feed) {
			this.feedImportService = feedImportService;
			this.feed = feed;
		}
		
		public void run() {
			log.info("Processing feed: " + feed);
			feedImportService.importFeed(feed);
		}		
	} 
		
}
