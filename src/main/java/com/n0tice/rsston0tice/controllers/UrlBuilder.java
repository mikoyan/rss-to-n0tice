package com.n0tice.rsston0tice.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlBuilder {

	@Value(value="#{rsston0tice['url.prefix']}")
	private String prefix;
	
	public String getHomepageUrl() {
		return prefix + "/";
	}
	
	public String getLoginUrl() {
		return prefix + "/login";
	}
	
	public String getLogoutUrl() {
		return prefix + "/logout";
	}

}
