package com.n0tice.rsston0tice.signin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.n0tice.api.client.N0ticeApi;
import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.exceptions.BadRequestException;
import com.n0tice.api.client.exceptions.NotAllowedException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.User;
import com.n0tice.api.client.oauth.N0ticeOauthApi;
import com.n0tice.rsston0tice.api.N0ticeApiFactory;
import com.n0tice.rsston0tice.daos.AccessTokenDAO;

@Component
public class N0ticeOauthSigninHandler implements SigninHandler {

	private static Logger log = Logger.getLogger(N0ticeOauthSigninHandler.class);
	
	private static final String OAUTH_TOKEN = "oauth_token";
	private static final String OAUTH_VERIFIER = "oauth_verifier";

	private AccessTokenDAO accessTokenDAO;
	private N0ticeApiFactory n0ticeApiFactory;
	
	private OAuthService oauthService;

	private String apiUrl;
	private String consumerKey;	
	private String consumerSecret;	
	private String callBackUrl;
	
	private Map<String, Token> requestTokens;
	
	@Autowired
	public N0ticeOauthSigninHandler(AccessTokenDAO accessTokenDAO, N0ticeApiFactory n0ticeApiFactory,
			@Value(value = "#{rsston0tice['api.url']}") String apiUrl,
			@Value(value = "#{rsston0tice['consumer.key']}") String consumerKey,
			@Value(value = "#{rsston0tice['consumer.secret']}") String consumerSecret,
			@Value(value = "#{rsston0tice['callback.url']}") String callBackUrl) {
		this.accessTokenDAO = accessTokenDAO;
		this.n0ticeApiFactory = n0ticeApiFactory;
		this.apiUrl = apiUrl;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.callBackUrl = callBackUrl;
	
		oauthService = getOauthService();
		
		requestTokens = new HashMap<String, Token>();
	}
	
	@Override
	public ModelAndView getLoginView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			log.info("Getting request token");
			
			final Token requestToken = oauthService.getRequestToken();		
			if (requestToken != null) {
				log.info("Got request token: " + requestToken.getToken());
				requestTokens.put(requestToken.getToken(), requestToken);
				
				final String authorizeUrl = oauthService.getAuthorizationUrl(requestToken);		
				log.info("Redirecting user to authorize url: " + authorizeUrl);
				RedirectView redirectView = new RedirectView(authorizeUrl);
				return new ModelAndView(redirectView);
			}
			
		} catch (Exception e) {
			log.warn("Failed to obtain request token.", e);
		}
		return null;
	}
	
	@Override
	public String getExternalUserIdentifierFromCallbackRequest(HttpServletRequest request) {
		if (request.getParameter(OAUTH_TOKEN) != null && request.getParameter(OAUTH_VERIFIER) != null) {
			final String token = request.getParameter(OAUTH_TOKEN);
			final String verifier = request.getParameter(OAUTH_VERIFIER);
			log.info("Seen on request: oauth_token: " + token + ", oauth_verifier: " + verifier);
			
			log.info("Looking for request token: " + token);
			Token requestToken = requestTokens.get(token);
			if (requestToken != null) {
				log.info("Found local request token: " + requestToken.getToken());				
				log.info("Exchanging request token for access token");
				
				final Token accessToken = oauthService.getAccessToken(requestToken, new Verifier(verifier));				
				if (accessToken != null) {
					log.info("Got access token: '" + accessToken.getToken() + "', '" + accessToken.getSecret() + "'");
					requestTokens.remove(requestToken.getToken());
					
					log.info("Using access token to lookup user details using the n0tice api");					
					final N0ticeApi n0ticeApi = n0ticeApiFactory.getAuthenticatedApi(accessToken);
					
					try {
						final User user = n0ticeApi.verify();
						if (user != null) {
							log.info("External user is: " + user.getUsername());
							accessTokenDAO.storeAccessTokenForUser(user.getUsername(), accessToken);
							return user.getUsername();
						}
						
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParsingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (AuthorisationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotAllowedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadRequestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					log.warn("Failed up obtain user details for access token");
					return null;
					
				} else {
					log.warn("Could not exchange request token for access token: " + requestToken.getToken());
				}
				
			} else {
				log.warn("Could not find request token for: " + token);
			}		
		
		} else {
			log.error("oauth token or verifier missing from callback request");
		}
		return null;
	}
	
	private OAuthService getOauthService() {
		log.info("Building oauth service with consumer key and consumer secret: " + consumerKey + ":" + consumerSecret + ", callback url: " + callBackUrl);
		return new ServiceBuilder().provider(new N0ticeOauthApi(apiUrl)).apiKey(consumerKey).apiSecret(consumerSecret).callback(callBackUrl).build();
	}
	
}
