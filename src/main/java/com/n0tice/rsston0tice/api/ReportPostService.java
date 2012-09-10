package com.n0tice.rsston0tice.api;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n0tice.api.client.N0ticeApi;
import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.exceptions.BadRequestException;
import com.n0tice.api.client.exceptions.NotAllowedException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.AccessToken;
import com.n0tice.rsston0tice.daos.AccessTokenDAO;
import com.n0tice.rsston0tice.daos.FeedItemHistoryDAO;
import com.n0tice.rsston0tice.feeds.FeedItemGuidService;
import com.n0tice.rsston0tice.model.FeedItem;
import com.n0tice.rsston0tice.model.UsersAccessToken;

@Component
public class ReportPostService {
	
	private static Logger log = Logger.getLogger(ReportPostService.class);

	private AccessTokenDAO accessTokenDAO;
	private N0ticeApiFactory n0ticeApiFactory;
	private FeedItemGuidService feedItemGuidService;
	private FeedItemHistoryDAO feedItemHistoryDAO;
	
	@Autowired
	public ReportPostService(AccessTokenDAO accessTokenDAO, N0ticeApiFactory n0ticeApiFactory, FeedItemGuidService feedItemGuidService, FeedItemHistoryDAO feedItemHistoryDAO) {
		this.accessTokenDAO = accessTokenDAO;
		this.n0ticeApiFactory = n0ticeApiFactory;
		this.feedItemGuidService = feedItemGuidService;
		this.feedItemHistoryDAO = feedItemHistoryDAO;
	}
	
	public void postReports(List<FeedItem> feedItems, String user, String noticeboard) {
		final UsersAccessToken accessToken = accessTokenDAO.getAccessTokenFor(user);
		final N0ticeApi n0ticeApi = n0ticeApiFactory.getAuthenticatedApi(new AccessToken(accessToken.getToken(), accessToken.getSecret()));
		
        for (FeedItem feedItem : feedItems) {
			if (feedItem.isGeoTagged()) {
				
				final String feedItemGuid = feedItemGuidService.getGuidFor(feedItem);
				if (!feedItemHistoryDAO.hasBeenImportedAlready(user, feedItemGuid, noticeboard)) {
					log.info("Importing item: " + feedItem.getTitle());
					try {
						n0ticeApi.postReport(feedItem.getTitle(), feedItem.getLatitude(), feedItem.getLongitude(), feedItem.getBody(), feedItem.getLink(), null, noticeboard, new DateTime(feedItem.getDate()));
						feedItemHistoryDAO.markAsImported(user, feedItemGuid, noticeboard);
					
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
				
				} else {
					log.info("Skipping previously imported item: " + feedItem.getTitle());
				}
			}
		}
	}
	
}
