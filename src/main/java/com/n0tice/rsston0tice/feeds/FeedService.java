package com.n0tice.rsston0tice.feeds;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n0tice.rsston0tice.model.FeedItem;
import com.n0tice.rsston0tice.model.FetchedFeed;

@Component
public class FeedService {

	private CachingFeedFetcher cachingFeedFetcher;

	@Autowired
	public FeedService(CachingFeedFetcher cachingFeedFetcher) {
		this.cachingFeedFetcher = cachingFeedFetcher;
	}
	
	public List<FeedItem> getFeedItems(String url) {
		final FetchedFeed fetchedFeed = cachingFeedFetcher.callService(url);
		if (fetchedFeed != null) {
			return fetchedFeed.getFeedItems();
		}
		return Collections.emptyList();
	}
	
	public String getFeedTitle(String url) {
		final FetchedFeed fetchedFeed = cachingFeedFetcher.callService(url);
		if (fetchedFeed != null) {
			return fetchedFeed.getFeedName();
		}
		return null;
	}
}
