package com.n0tice.rsston0tice.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.common.html.HtmlCleaner;
import uk.co.eelpieconsulting.common.http.HttpFetchException;
import uk.co.eelpieconsulting.common.http.HttpFetcher;

import com.n0tice.api.client.N0ticeApi;
import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.exceptions.BadRequestException;
import com.n0tice.api.client.exceptions.N0ticeException;
import com.n0tice.api.client.exceptions.NotAllowedException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.MediaFile;
import com.n0tice.rsston0tice.daos.FeedItemHistoryDAO;
import com.n0tice.rsston0tice.feeds.FeedItemGuidService;
import com.n0tice.rsston0tice.model.FeedItem;
import com.n0tice.rsston0tice.services.BlockedUserService;

@Component
public class ReportPostService {
	
	private static Logger log = Logger.getLogger(ReportPostService.class);

	private N0ticeApiFactory n0ticeApiFactory;
	private FeedItemGuidService feedItemGuidService;
	private FeedItemHistoryDAO feedItemHistoryDAO;
	private BlockedUserService blockedUserService;	

	private HttpFetcher httpFetcher;
	private HtmlCleaner htmlCleaner;
	
	@Autowired
	public ReportPostService(N0ticeApiFactory n0ticeApiFactory, FeedItemGuidService feedItemGuidService, FeedItemHistoryDAO feedItemHistoryDAO, BlockedUserService blockedUserService) {
		this.n0ticeApiFactory = n0ticeApiFactory;
		this.feedItemGuidService = feedItemGuidService;
		this.feedItemHistoryDAO = feedItemHistoryDAO;
		this.blockedUserService = blockedUserService;
		httpFetcher = new HttpFetcher();
		htmlCleaner = new HtmlCleaner();
	}
	
	public int postReports(List<FeedItem> feedItems, String user, String noticeboard, Double defaultLatitude, Double defaultLongitude) {
		final N0ticeApi n0ticeApi = n0ticeApiFactory.getAuthenticatedApiFor(user);
		
		int importedCount = 0;
        for (FeedItem feedItem : feedItems) {        	
        	Double latitude = defaultLatitude;
        	Double longitude = defaultLongitude;        	
        	if (feedItem.isGeoTagged()) {
        		latitude = feedItem.getLatitude();
        		longitude = feedItem.getLongitude();
        	}
        	
			final boolean isFeedItemPositioned = latitude != null && longitude != null;
			if (isFeedItemPositioned) {				
				final String feedItemGuid = feedItemGuidService.getGuidFor(feedItem);
				if (!feedItemHistoryDAO.hasBeenImportedAlready(user, feedItemGuid)) {
					log.info("Importing item: " + feedItem.getTitle());
					try {
						final MediaFile imageFile = feedItem.getImageUrl() != null ? fetchRemoteImage(feedItem.getImageUrl()) : null;
						final String body = makeBody(feedItem);
						final Content postedReport = n0ticeApi.postReport(feedItem.getTitle(), latitude, longitude, body, feedItem.getLink(), imageFile, null, noticeboard, new DateTime(feedItem.getDate()));
						feedItemHistoryDAO.markAsImported(user, feedItemGuid, noticeboard, postedReport);
						importedCount++;
					
					} catch (NotFoundException e) {
						log.warn(e);
					} catch (ParsingException e) {
						log.warn(e);
					} catch (AuthorisationException e) {
						log.warn("User failed to authenticate with the n0tice api; blocking: " + user);
						blockedUserService.blockUser(user);						
					} catch (NotAllowedException e) {
						log.warn(e);
					} catch (BadRequestException e) {
						log.warn(e);
					} catch (N0ticeException e) {
						log.warn(e);
					}
				
				} else {
					log.debug("Skipping previously imported item: " + feedItem.getTitle());
				}
			}
		}
        
        return importedCount;
	}

	private String makeBody(FeedItem feedItem) {
		return htmlCleaner.stripHtml(feedItem.getBody());
	}

	private MediaFile fetchRemoteImage(String imageUrl) {
		log.info("Fetching remote image from: " + imageUrl);
		try {			
			final MediaFile imageFile = new MediaFile(httpFetcher.getBytes(imageUrl), "image.jpg");
			log.info("Finished fetching image from: " + imageUrl);
			return imageFile;	// TODO extract image name from url.			
		} catch (HttpFetchException e) {
			log.warn(e);
		}
		return null;
	}
	
}
