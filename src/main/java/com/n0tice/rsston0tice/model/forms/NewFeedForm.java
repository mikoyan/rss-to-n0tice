package com.n0tice.rsston0tice.model.forms;

import org.hibernate.validator.constraints.NotBlank;

public class NewFeedForm {

    @NotBlank
	private String url;
    
    private String noticeboard;

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
	
}
