package com.n0tice.rsston0tice.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("accessTokens")
public class UsersAccessToken {
	
	@Id
	private String user;
	
	private String token;
	private String secret;
	
	public UsersAccessToken() {
	}
	
	public UsersAccessToken(String user, String token, String secret) {
		this.user = user;
		this.token = token;
		this.secret = secret;
	}
	
	public String getUser() {
		return user;
	}

	public String getToken() {
		return token;
	}

	public String getSecret() {
		return secret;
	}

	@Override
	public String toString() {
		return "UsersAccessToken [user=" + user + ", token=" + token + ", secret=" + secret + "]";
	}
	
}
