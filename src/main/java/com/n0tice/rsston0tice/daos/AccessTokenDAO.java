package com.n0tice.rsston0tice.daos;

import java.util.HashMap;
import java.util.Map;

import org.scribe.model.Token;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenDAO {

	Map<String, Token> accessTokens;
	
	public AccessTokenDAO() {
		accessTokens = new HashMap<String, Token>();
	}
	
	public void storeAccessTokenForUser(String user, Token accessToken) {
		accessTokens.put(user, accessToken);		
	}

	public Token getAccessTokenFor(String user) {
		return accessTokens.get(user);
	}
	
}
