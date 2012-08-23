package com.n0tice.rsston0tice.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;

@Entity("feedItemHistory")
public class FeedItemHistory  {

	@Indexed
	private String user;
	
	@Indexed
	private String guid;

	@Indexed
	private String noticeboard;
	
	public FeedItemHistory() {
	}
	
	public FeedItemHistory(String user, String guid, String noticeboard) {
		this.user = user;
		this.guid = guid;
		this.noticeboard = noticeboard;
	}

	public String getUser() {
		return user;
	}

	public String getGuid() {
		return guid;
	}
	
	public String getNoticeboard() {
		return noticeboard;
	}

	@Override
	public String toString() {
		return "FeedItemHistory [user=" + user + ", guid=" + guid + ", noticeboard=" + noticeboard + "]";
	}
	
}
