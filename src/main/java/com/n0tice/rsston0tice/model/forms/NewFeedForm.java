package com.n0tice.rsston0tice.model.forms;

import org.hibernate.validator.constraints.NotBlank;

public class NewFeedForm {

    @NotBlank
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
