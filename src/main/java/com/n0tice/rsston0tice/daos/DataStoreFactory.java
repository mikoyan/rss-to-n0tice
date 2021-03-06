package com.n0tice.rsston0tice.daos;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

@Component
public class DataStoreFactory {
	
    @Value("#{rsston0tice['mongoHost']}")
    private String mongoHost;
    
    @Value("#{rsston0tice['mongoDatabase']}")
    private String mongoDatabase;
        
	public DataStoreFactory() {
	}

	public DataStoreFactory(String mongoHost, String mongoDatabase) {
		this.mongoHost = mongoHost;
		this.mongoDatabase = mongoDatabase;
	}
	
	public Datastore getDatastore() throws UnknownHostException, MongoException {	
		final Morphia morphia = new Morphia();		
		final Mongo m = new Mongo(mongoHost);
		final Datastore dataStore = morphia.createDatastore(m, mongoDatabase);
		dataStore.ensureIndexes();
		return dataStore;
	}
	
}
