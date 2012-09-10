package com.n0tice.rsston0tice.feeds;

import org.springframework.stereotype.Component;

import com.n0tice.rsston0tice.model.FeedItem;

@Component
public class FeedItemGuidService {

	public String getGuidFor(FeedItem feedItem) {
		return feedItem.getUri();
	}
	
}
