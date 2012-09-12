package com.n0tice.rsston0tice.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlBuilder {

	private static final String UTF_8 = "UTF-8";
	
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

	public String getNewFeedUrl() {
		return prefix + "/feeds/new";
	}
	
	public String getFeedUrl(int i) {
		return prefix + "/feeds/" + i;
	}
	
	public String getTakeDownUrl(String n0ticeId, int feedNumber) {
		return prefix + "/takedown?id=" + encodeUrlParameter(n0ticeId) + "&feed=" + feedNumber;
	}

	private String encodeUrlParameter(String n0ticeId) {
		try {
			return URLEncoder.encode(n0ticeId, UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
}
