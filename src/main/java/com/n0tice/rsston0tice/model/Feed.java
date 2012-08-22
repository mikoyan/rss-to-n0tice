package com.n0tice.rsston0tice.model;

public class Feed {

	private final String url;
	private final String noticeboard;


	public Feed(String url, String noticeboard) {
		this.url = url;
		this.noticeboard = noticeboard;
	}

	public String getUrl() {
		return url;
	}

	public String getNoticeboard() {
		return noticeboard;
	}

	@Override
	public String toString() {
		return "Feed [url=" + url + ", noticeboard=" + noticeboard + "]";
	}
	
}
