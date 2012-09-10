package com.n0tice.rsston0tice.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n0tice.rsston0tice.daos.FeedItemHistoryDAO;
import com.n0tice.rsston0tice.feeds.FeedItemGuidService;
import com.n0tice.rsston0tice.model.FeedItem;
import com.n0tice.rsston0tice.model.FeedItemHistory;
import com.n0tice.rsston0tice.model.FeedItemWithHistory;

@Component
public class FeedItemHistoryService {
	
	private FeedItemGuidService feedItemGuidService;
	private FeedItemHistoryDAO feedItemHistoryDAO;
	
	@Autowired
	public FeedItemHistoryService(FeedItemGuidService feedItemGuidService, FeedItemHistoryDAO feedItemHistoryDAO) {
		this.feedItemGuidService = feedItemGuidService;		
		this.feedItemHistoryDAO = feedItemHistoryDAO;
	}
	
	public List<FeedItemWithHistory> decorateFeedItemsWithHistory(List<FeedItem> feedItems, String user, String noticeboard) {
		final List<FeedItemWithHistory> feedItemsWithHistory = new ArrayList<FeedItemWithHistory>();
		for (FeedItem feedItem : feedItems) {
			final FeedItemHistory feedItemHistory = feedItemHistoryDAO.getHistoryFor(user, feedItemGuidService.getGuidFor(feedItem), noticeboard);
			feedItemsWithHistory.add(new FeedItemWithHistory(feedItem, feedItemHistory));
		}
		return feedItemsWithHistory;
	}

}
