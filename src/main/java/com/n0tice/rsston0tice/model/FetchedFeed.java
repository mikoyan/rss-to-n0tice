package com.n0tice.rsston0tice.model;

import java.io.Serializable;
import java.util.List;

public class FetchedFeed implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final String feedName;
	private final List<FeedItem> feedItems;
	
	public FetchedFeed(String feedName, List<FeedItem> feedItems) {
		this.feedName = feedName;
		this.feedItems = feedItems;
	}
	
	public String getFeedName() {
		return feedName;
	}

	public List<FeedItem> getFeedItems() {
		return feedItems;
	}
	
}
