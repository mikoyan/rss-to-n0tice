package com.n0tice.rsston0tice.daos;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.mongodb.MongoException;
import com.n0tice.rsston0tice.model.UsersAccessToken;

@Component
public class AccessTokenDAO {
	
	private Datastore datastore;
	
	@Autowired
	public AccessTokenDAO(DataStoreFactory dataStoreFactory) throws UnknownHostException, MongoException {
		this.datastore = dataStoreFactory.getDatastore();
	}
	
	public void storeAccessTokenForUser(UsersAccessToken accessToken) {
		datastore.save(accessToken);
	}
	
	public UsersAccessToken getAccessTokenFor(String user) {
		final Query<UsersAccessToken> q = datastore.createQuery(UsersAccessToken.class).
				field("user").equal(user);
		return q.get();
	}
	
}
