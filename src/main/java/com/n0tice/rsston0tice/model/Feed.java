package com.n0tice.rsston0tice.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;

@Entity("feeds")
public class Feed {
	
	@Indexed
	private String user;
	private String title;
	private String url;
	private String noticeboard;
	
	public Feed() {
	}
	
	public Feed(String user, String title, String url, String noticeboard) {
		this.user = user;
		this.title = title;
		this.url = url;
		this.noticeboard = noticeboard;
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

	@Override
	public String toString() {
		return "Feed [user=" + user + ", title=" + title + ", url=" + url + ", noticeboard=" + noticeboard + "]";
	}
	
}
