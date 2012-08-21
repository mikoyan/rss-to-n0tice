package com.n0tice.rsston0tice.model;

import java.util.Date;

public class FeedItem {

	private final String title;
	private final String uri;
	private final String link;
	private final Date date;
	private final Double latitude;
	private final Double longitude;

	public FeedItem(String title, String uri, String link, Date date, Double latitude, Double longitude) {
		this.title = title;
		this.uri = uri;
		this.link = link;
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getTitle() {
		return title;
	}

	public String getUri() {
		return uri;
	}

	public String getLink() {
		return link;
	}

	public Date getDate() {
		return date;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}
	
	public boolean isGeoTagged() {
		return latitude != null && longitude != null;
	}

	@Override
	public String toString() {
		return "FeedItem [title=" + title + ", uri=" + uri + ", link=" + link
				+ ", date=" + date + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}
	
}
