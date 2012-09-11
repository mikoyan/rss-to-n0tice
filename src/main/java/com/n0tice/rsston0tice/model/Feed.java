package com.n0tice.rsston0tice.model;

import org.bson.types.ObjectId;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

@Entity("feeds")
public class Feed {
	
	@Id
	private ObjectId id;
	
	@Indexed
	private String user;
	private String title;
	private String url;
	private String noticeboard;
	private Boolean scheduled;
	
	public Feed() {
	}
	
	public Feed(String user, String title, String url, String noticeboard, boolean scheduled) {
		this.user = user;
		this.title = title;
		this.url = url;
		this.noticeboard = noticeboard;
		this.scheduled = scheduled;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public String getNoticeboard() {
		return noticeboard;
	}
	
	public boolean isScheduled() {
		return scheduled != null ? scheduled : false;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setNoticeboard(String noticeboard) {
		this.noticeboard = noticeboard;
	}

	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}

	@Override
	public String toString() {
		return "Feed [user=" + user + ", title=" + title + ", url=" + url
				+ ", noticeboard=" + noticeboard + ", scheduled=" + scheduled + "]";
	}
	
}
