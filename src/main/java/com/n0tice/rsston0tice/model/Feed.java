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
	private Double latitude;
	private Double longitude;
	
	public Feed() {
	}
	
	public Feed(String user, String title, String url, String noticeboard, boolean scheduled, Double latitude, Double longitude) {
		this.user = user;
		this.title = title;
		this.url = url;
		this.noticeboard = noticeboard;
		this.scheduled = scheduled;
		this.latitude = latitude;
		this.longitude = longitude;
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
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public boolean isDefaultPosition() {
		return latitude != null && longitude != null;
	}
	
	@Override
	public String toString() {
		return "Feed [id=" + id + ", user=" + user + ", title=" + title
				+ ", url=" + url + ", noticeboard=" + noticeboard
				+ ", scheduled=" + scheduled + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}
	
}
