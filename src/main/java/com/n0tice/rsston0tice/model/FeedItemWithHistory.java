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
	
	public Date getImportDate() {
		return feedItemHistory != null ? feedItemHistory.getDate() : null;
	}
	
	public String getImportNoticeboard() {
		return feedItemHistory != null ?feedItemHistory.getNoticeboard() : null;
	}
	
	public String getN0ticeId() {
		return feedItemHistory.getN0ticeId();
	}
	
	public String getN0ticeWebUrl() {
		return feedItemHistory.getN0ticeWebUrl();
	}
		
}
