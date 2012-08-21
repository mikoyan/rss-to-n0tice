package com.n0tice.rsston0tice.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.n0tice.rsston0tice.model.Feed;

@Component
public class FeedDAO {

	private Map<String, List<Feed>> userFeeds;
	
	public FeedDAO() {
		userFeeds = new HashMap<String, List<Feed>>();
	}

	public List<Feed> getFeedsForUser(String username) {
		return userFeeds.get(username);
	}

	public void addNewFeedForUser(String user, Feed feed) {
		List<Feed> usersFeeds = userFeeds.get(user);
		if (usersFeeds == null) {
			usersFeeds = new ArrayList<Feed>();
		}
		
		usersFeeds.add(feed);
		userFeeds.put(user, usersFeeds);		
	}
	
}
