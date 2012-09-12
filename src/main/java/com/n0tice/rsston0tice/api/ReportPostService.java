package com.n0tice.rsston0tice.api;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.common.http.HttpFetchException;
import uk.co.eelpieconsulting.common.http.HttpFetcher;

import com.n0tice.api.client.N0ticeApi;
import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.exceptions.BadRequestException;
import com.n0tice.api.client.exceptions.NotAllowedException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.ImageFile;
import com.n0tice.rsston0tice.daos.FeedItemHistoryDAO;
import com.n0tice.rsston0tice.feeds.FeedItemGuidService;
import com.n0tice.rsston0tice.model.FeedItem;

@Component
public class ReportPostService {
	
	private static Logger log = Logger.getLogger(ReportPostService.class);

	private N0ticeApiFactory n0ticeApiFactory;
	private FeedItemGuidService feedItemGuidService;
	private FeedItemHistoryDAO feedItemHistoryDAO;	
	private HttpFetcher httpFetcher;
	
	@Autowired
	public ReportPostService(N0ticeApiFactory n0ticeApiFactory, FeedItemGuidService feedItemGuidService, FeedItemHistoryDAO feedItemHistoryDAO) {
		this.n0ticeApiFactory = n0ticeApiFactory;
		this.feedItemGuidService = feedItemGuidService;
		this.feedItemHistoryDAO = feedItemHistoryDAO;
		httpFetcher = new HttpFetcher();
	}
	
	public int postReports(List<FeedItem> feedItems, String user, String noticeboard) {
		final N0ticeApi n0ticeApi = n0ticeApiFactory.getAuthenticatedApiFor(user);
		
		int importedCount = 0;
        for (FeedItem feedItem : feedItems) {
			if (feedItem.isGeoTagged()) {
				
				final String feedItemGuid = feedItemGuidService.getGuidFor(feedItem);
				if (!feedItemHistoryDAO.hasBeenImportedAlready(user, feedItemGuid, noticeboard)) {
					log.info("Importing item: " + feedItem.getTitle());
					try {
						final ImageFile imageFile = feedItem.getImageUrl() != null ? fetchRemoteImage(feedItem.getImageUrl()) : null;
						final Content postedReport = n0ticeApi.postReport(feedItem.getTitle(), feedItem.getLatitude(), feedItem.getLongitude(), feedItem.getBody(), feedItem.getLink(), imageFile, noticeboard, new DateTime(feedItem.getDate()));
						feedItemHistoryDAO.markAsImported(user, feedItemGuid, noticeboard, postedReport);
						importedCount++;
					
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
        
        return importedCount;
	}

	private ImageFile fetchRemoteImage(String imageUrl) {
		log.info("Fetching remote image from: " + imageUrl);
		try {
			return new ImageFile(httpFetcher.getBytes(imageUrl), "image.jpg");	// TODO extract image name from url.
		} catch (HttpFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
