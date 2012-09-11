package com.n0tice.rsston0tice.daos;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.mongodb.MongoException;
import com.n0tice.rsston0tice.model.Feed;

@Component
public class FeedDAO {

	private Datastore datastore;
	
	@Autowired
	public FeedDAO(DataStoreFactory dataStoreFactory) throws UnknownHostException, MongoException {
		this.datastore = dataStoreFactory.getDatastore();
	}

	public List<Feed> getFeedsForUser(String username) {
		final Query<Feed> q = datastore.createQuery(Feed.class).
			field("user").equal(username);
		return q.asList();		
	}
	
	public List<Feed> getAllScheduledFeeds() {
		final Query<Feed> q = datastore.createQuery(Feed.class).
			field("scheduled").equal(true);
		return q.asList();
	}

	public void save(Feed feed) {
		datastore.save(feed);
	}
	
}
