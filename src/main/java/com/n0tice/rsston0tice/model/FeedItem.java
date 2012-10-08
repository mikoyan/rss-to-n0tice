package com.n0tice.rsston0tice.model;

import java.io.Serializable;
import java.util.Date;

public class FeedItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private final String title;
	private final String uri;
	private final String body;
	private final String link;
	private final Date date;
	private final Double latitude;
	private final Double longitude;
	private final String imageUrl;

	public FeedItem(String title, String uri, String body, String link, Date date, Double latitude, Double longitude, String imageUrl) {
		this.title = title;
		this.uri = uri;
		this.body = body;
		this.link = link;
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public String getUri() {
		return uri;
	}
	
	public String getBody() {
		return body;
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
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public boolean isGeoTagged() {
		return latitude != null && longitude != null;
	}

	@Override
	public String toString() {
		return "FeedItem [body=" + body + ", date=" + date + ", imageUrl="
				+ imageUrl + ", latitude=" + latitude + ", link=" + link
				+ ", longitude=" + longitude + ", title=" + title + ", uri="
				+ uri + "]";
	}
	
}
