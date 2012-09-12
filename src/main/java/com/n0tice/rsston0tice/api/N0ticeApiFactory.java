package com.n0tice.rsston0tice.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.n0tice.api.client.N0ticeApi;
import com.n0tice.api.client.model.AccessToken;
import com.n0tice.rsston0tice.daos.AccessTokenDAO;
import com.n0tice.rsston0tice.model.UsersAccessToken;

@Component
public class N0ticeApiFactory {
	
	private String apiUrl;
	private String consumerKey;	
	private String consumerSecret;
	private AccessTokenDAO accessTokenDAO;
	
	@Autowired
	public N0ticeApiFactory(
		@Value(value = "#{rsston0tice['api.url']}") String apiUrl,
		@Value(value = "#{rsston0tice['consumer.key']}") String consumerKey,
		@Value(value = "#{rsston0tice['consumer.secret']}") String consumerSecret,
		AccessTokenDAO accessTokenDAO) {
			this.apiUrl = apiUrl;
			this.consumerKey = consumerKey;
			this.consumerSecret = consumerSecret;
			this.accessTokenDAO = accessTokenDAO;
	}
	
	public N0ticeApi getReadOnlyApi() {
		return new N0ticeApi(apiUrl);
	}
	
	public N0ticeApi getAuthenticatedApiFor(String user) {
		final UsersAccessToken accessToken = accessTokenDAO.getAccessTokenFor(user);	// TODO exception handling
		return new N0ticeApi(apiUrl, consumerKey, consumerSecret, new AccessToken(accessToken.getToken(), accessToken.getSecret()));
	}

	public N0ticeApi getAuthenticatedApi(AccessToken accessToken) {
		return new N0ticeApi(apiUrl, consumerKey, consumerSecret, new AccessToken(accessToken.getToken(), accessToken.getSecret()));
	}
	
}
