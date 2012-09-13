package com.n0tice.rsston0tice.model.forms;

import org.hibernate.validator.constraints.NotBlank;

public class NewFeedForm {

    @NotBlank
	private String url;
    
    private String noticeboard;
    
    private boolean scheduled;
    
	private Double latitude;
	private Double longitude;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getNoticeboard() {
		return noticeboard;
	}

	public void setNoticeboard(String noticeboard) {
		this.noticeboard = noticeboard;
	}

	public boolean isScheduled() {
		return scheduled;
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

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}
	
}
