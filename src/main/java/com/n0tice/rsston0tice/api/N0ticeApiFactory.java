package com.n0tice.rsston0tice.api;

import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.n0tice.api.client.N0ticeApi;

@Component
public class N0ticeApiFactory {
	
	private String apiUrl;
	private String consumerKey;	
	private String consumerSecret;	
	
	@Autowired
	public N0ticeApiFactory(
		@Value(value = "#{rsston0tice['api.url']}") String apiUrl,
		@Value(value = "#{rsston0tice['consumer.key']}") String consumerKey,
		@Value(value = "#{rsston0tice['consumer.secret']}") String consumerSecret) {
			this.apiUrl = apiUrl;
			this.consumerKey = consumerKey;
			this.consumerSecret = consumerSecret;	
	}
	
	public N0ticeApi getAuthenticatedApi(Token accessToken) {
		return new N0ticeApi(apiUrl, consumerKey, consumerSecret, accessToken);	// TODO Shouldn't be using scribe classes on api interface
	}
	
	public N0ticeApi getReadOnlyApi() {
		return new N0ticeApi(apiUrl);
	}

}
