package com.n0tice.rsston0tice.api;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n0tice.api.client.N0ticeApi;
import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.exceptions.BadRequestException;
import com.n0tice.api.client.exceptions.NotAllowedException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.rsston0tice.daos.AccessTokenDAO;
import com.n0tice.rsston0tice.daos.FeedItemHistoryDAO;
import com.n0tice.rsston0tice.model.FeedItem;

@Component
public class ReportPostService {
	
	private static Logger log = Logger.getLogger(ReportPostService.class);

	private AccessTokenDAO accessTokenDAO;
	private N0ticeApiFactory n0ticeApiFactory;
	private FeedItemHistoryDAO feedItemHistoryDAO;
	
	@Autowired
	public ReportPostService(AccessTokenDAO accessTokenDAO, N0ticeApiFactory n0ticeApiFactory, FeedItemHistoryDAO feedItemHistoryDAO) {
		this.accessTokenDAO = accessTokenDAO;
		this.n0ticeApiFactory = n0ticeApiFactory;
		this.feedItemHistoryDAO = feedItemHistoryDAO;
	}
	
	public void postReports(List<FeedItem> feedItems, String user, String noticeboard) {
		final Token accessToken = accessTokenDAO.getAccessTokenFor(user);
		final N0ticeApi n0ticeApi = n0ticeApiFactory.getAuthenticatedApi(accessToken);
		
        for (FeedItem feedItem : feedItems) {
			if (feedItem.isGeoTagged()) {
				
				if (!feedItemHistoryDAO.hasBeenImportedAlready(user, getGuidFor(feedItem))) {
					log.info("Importing item: " + feedItem.getTitle());
					try {
						n0ticeApi.postReport(feedItem.getTitle(), feedItem.getLatitude(), feedItem.getLongitude(), feedItem.getBody(), feedItem.getLink(), null, noticeboard, new DateTime(feedItem.getDate()));
						feedItemHistoryDAO.markAsImported(user, getGuidFor(feedItem));
					
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

	private String getGuidFor(FeedItem feedItem) {
		return feedItem.getUri();
	}

}
