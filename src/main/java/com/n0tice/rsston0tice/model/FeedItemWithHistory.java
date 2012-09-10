package com.n0tice.rsston0tice.model;

import java.util.Date;

public class FeedItemWithHistory {

	private final FeedItem feedItem;
	private final FeedItemHistory feedItemHistory;

	public FeedItemWithHistory(FeedItem feedItem, FeedItemHistory feedItemHistory) {
		this.feedItem = feedItem;
		this.feedItemHistory = feedItemHistory;
	}

	public String getTitle() {
		return feedItem.getTitle();
	}

	public String getLink() {
		return feedItem.getLink();
	}

	public Date getDate() {
		return feedItem.getDate();
	}

	public Double getLatitude() {
		return feedItem.getLatitude();
	}

	public Double getLongitude() {
		return feedItem.getLongitude();
	}

	public boolean isGeoTagged() {
		return feedItem.isGeoTagged();
	}

	public boolean hasHistory() {
		return feedItemHistory != null;
	}
	
}
