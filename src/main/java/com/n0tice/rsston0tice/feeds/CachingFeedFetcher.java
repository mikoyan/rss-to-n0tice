package com.n0tice.rsston0tice.feeds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.common.caching.CachingServiceWrapper;
import uk.co.eelpieconsulting.common.caching.MemcachedCache;

import com.n0tice.rsston0tice.model.FetchedFeed;

@Component
public class CachingFeedFetcher extends CachingServiceWrapper<String, FetchedFeed> {

	@Autowired
	public CachingFeedFetcher(FeedFetcher feedFetcher, MemcachedCache cache) {
		super(feedFetcher, cache);
	}

}
