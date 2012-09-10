package com.n0tice.rsston0tice.model;

import java.util.Date;

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

	private Date date;
	
	public FeedItemHistory() {
	}
	
	public FeedItemHistory(String user, String guid, String noticeboard, Date date) {
		this.user = user;
		this.guid = guid;
		this.noticeboard = noticeboard;
		this.date = date;
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

	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "FeedItemHistory [user=" + user + ", guid=" + guid + ", noticeboard=" + noticeboard + ", date=" + date + "]";
	}
	
}
