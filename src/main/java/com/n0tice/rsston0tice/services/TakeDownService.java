package com.n0tice.rsston0tice.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n0tice.api.client.N0ticeApi;
import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.exceptions.BadRequestException;
import com.n0tice.api.client.exceptions.N0ticeException;
import com.n0tice.api.client.exceptions.NotAllowedException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.rsston0tice.api.N0ticeApiFactory;
import com.n0tice.rsston0tice.api.ReportPostService;
import com.n0tice.rsston0tice.daos.FeedItemHistoryDAO;

@Component
public class TakeDownService {
	
	private static Logger log = Logger.getLogger(ReportPostService.class);
	
	private N0ticeApiFactory n0ticeApiFactory;
	private FeedItemHistoryDAO feedItemHistoryDAO;
	
	@Autowired	
	public TakeDownService(N0ticeApiFactory n0ticeApiFactory, FeedItemHistoryDAO feedItemHistoryDAO) {
		this.n0ticeApiFactory = n0ticeApiFactory;
		this.feedItemHistoryDAO = feedItemHistoryDAO;
	}
	
	public void takeDown(String user, String n0ticeId) {
		log.info("Taking down: " + n0ticeId);
		
		final N0ticeApi n0ticeApi = n0ticeApiFactory.getAuthenticatedApiFor(user);
		try {
			log.info("Calling n0tice api to delete: " + n0ticeId);
			n0ticeApi.deleteReport(n0ticeId);

			log.info("Purging import history for: " + n0ticeId);
			feedItemHistoryDAO.removeHistoryForNoticeId(n0ticeId);
			
		} catch (NotFoundException e) {
			log.warn(e);
		} catch (NotAllowedException e) {
			log.warn(e);
		} catch (AuthorisationException e) {
			log.warn(e);
		} catch (BadRequestException e) {
			log.warn(e);
		} catch (N0ticeException e) {
			log.warn(e);
		}
		
		log.info("Done");
	}

}
