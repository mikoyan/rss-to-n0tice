package com.n0tice.rsston0tice.model.forms;

import org.hibernate.validator.constraints.NotBlank;

public class EditFeedForm extends NewFeedForm {
	
    @NotBlank
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
