package com.n0tice.rsston0tice.daos;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.mongodb.MongoException;
import com.n0tice.rsston0tice.model.FeedItemHistory;

@Component
public class FeedItemHistoryDAO {

	private Datastore datastore;
	
	@Autowired
	public FeedItemHistoryDAO(DataStoreFactory dataStoreFactory) throws UnknownHostException, MongoException {
		this.datastore = dataStoreFactory.getDatastore();
	}
	
	public void markAsImported(String user, String guid, String noticeboard) {
		datastore.save(new FeedItemHistory(user, guid, noticeboard));
	}
	
	public boolean hasBeenImportedAlready(String user, String guid, String noticeboard) {
		final Query<FeedItemHistory> q = buildFeedItemHistoryQuery(user, guid, noticeboard);		
		return q.countAll() > 0;
	}
	
	public FeedItemHistory getHistoryFor(String user, String guid, String noticeboard) {
		final Query<FeedItemHistory> q = datastore.createQuery(FeedItemHistory.class).
				field("user").equal(user).
				field("guid").equal(guid).
				field("noticeboard").equal(noticeboard);
		
		final List<FeedItemHistory> asList = q.asList();
		if (q.asList().isEmpty()) {
			return null;
		}
		return asList.get(0);
	}
	
	private Query<FeedItemHistory> buildFeedItemHistoryQuery(String user, String guid, String noticeboard) {
		return datastore.createQuery(FeedItemHistory.class).
				field("user").equal(user).
				field("guid").equal(guid).
				field("noticeboard").equal(noticeboard);
	}

}
