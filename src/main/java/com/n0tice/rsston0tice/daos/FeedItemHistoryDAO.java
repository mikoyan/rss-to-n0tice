package com.n0tice.rsston0tice.daos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class FeedItemHistoryDAO {

	private Map<String, Boolean> importedGuids;
	
	public FeedItemHistoryDAO() {
		importedGuids = new HashMap<String, Boolean>();
	}
	
	public void markAsImported(String user, String guid) {
		importedGuids.put(makeKeyFor(user, guid), true);		
	}

	private String makeKeyFor(String user, String guid) {
		return user + guid;
	}
	
	public boolean hasBeenImportedAlready(String user, String guid) {
		return importedGuids.get(makeKeyFor(user, guid)) != null;
	}

}
