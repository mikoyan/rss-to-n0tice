package com.n0tice.rsston0tice.daos;

import java.net.UnknownHostException;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.mongodb.MongoException;
import com.n0tice.api.client.model.Content;
import com.n0tice.rsston0tice.model.FeedItemHistory;

@Component
public class FeedItemHistoryDAO {

	private Datastore datastore;
	
	@Autowired
	public FeedItemHistoryDAO(DataStoreFactory dataStoreFactory) throws UnknownHostException, MongoException {
		this.datastore = dataStoreFactory.getDatastore();
	}
	
	public void markAsImported(String user, String guid, String noticeboard, Content postedReport) {
		datastore.save(new FeedItemHistory(user, guid, noticeboard, DateTime.now().toDate(), postedReport.getId(), postedReport.getWebUrl()));
	}
	
	public boolean hasBeenImportedAlready(String user, String guid) {
		final Query<FeedItemHistory> q = buildFeedItemHistoryQuery(user, guid);		
		return q.countAll() > 0;
	}
	
	public FeedItemHistory getHistoryFor(String user, String guid) {
		final Query<FeedItemHistory> q = datastore.createQuery(FeedItemHistory.class).
				field("guid").equal(guid).
				field("user").equal(user);
		
		final List<FeedItemHistory> asList = q.asList();
		if (q.asList().isEmpty()) {
			return null;
		}
		return asList.get(0);
	}
	
	private Query<FeedItemHistory> buildFeedItemHistoryQuery(String user, String guid) {
		return datastore.createQuery(FeedItemHistory.class).
				field("user").equal(user).
				field("guid").equal(guid);
	}

	public void removeHistoryForNoticeId(String n0ticeId) {
		final Query<FeedItemHistory> q = datastore.createQuery(FeedItemHistory.class).
				field("n0ticeId").equal(n0ticeId);
		datastore.delete(q);
	}

}
