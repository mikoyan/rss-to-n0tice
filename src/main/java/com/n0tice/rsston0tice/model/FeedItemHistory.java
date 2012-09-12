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

	private String n0ticeId;

	private String n0ticeWebUrl;
	
	public FeedItemHistory() {
	}
	
	public FeedItemHistory(String user, String guid, String noticeboard, Date date, String n0ticeId, String n0ticeWebUrl) {
		this.user = user;
		this.guid = guid;
		this.noticeboard = noticeboard;
		this.date = date;
		this.n0ticeId = n0ticeId;
		this.n0ticeWebUrl = n0ticeWebUrl;
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
	
	public String getN0ticeId() {
		return n0ticeId;
	}

	public String getN0ticeWebUrl() {
		return n0ticeWebUrl;
	}

	@Override
	public String toString() {
		return "FeedItemHistory [user=" + user + ", guid=" + guid
				+ ", noticeboard=" + noticeboard + ", date=" + date
				+ ", n0ticeId=" + n0ticeId + ", n0ticeWebUrl=" + n0ticeWebUrl
				+ "]";
	}
	
}
