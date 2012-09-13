package com.n0tice.rsston0tice.feeds;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.n0tice.rsston0tice.model.FeedItem;
import com.sun.syndication.feed.module.georss.GeoRSSModule;
import com.sun.syndication.feed.module.georss.GeoRSSUtils;
import com.sun.syndication.feed.module.mediarss.MediaEntryModuleImpl;
import com.sun.syndication.feed.module.mediarss.MediaModule;
import com.sun.syndication.feed.module.mediarss.types.MediaContent;
import com.sun.syndication.feed.module.mediarss.types.Metadata;
import com.sun.syndication.feed.module.mediarss.types.Reference;
import com.sun.syndication.feed.module.mediarss.types.Thumbnail;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;

@Component
public class FeedFetcher {
	
	private static Logger log = Logger.getLogger(FeedFetcher.class);
	
	public String getFeedTitle(String url) {
		final SyndFeed syndfeed = loadSyndFeedWithFeedFetcher(url);
    	if (syndfeed == null) {
    		log.warn("Could not load syndfeed from url: " + url + ". Returning empty list of items");
    		return null;
    	}
    	
    	return syndfeed.getTitle();
	}
	
	public List<FeedItem> getFeedItems(String url) {
		final List<FeedItem> feedItems = new ArrayList<FeedItem>();
    	final SyndFeed syndfeed = loadSyndFeedWithFeedFetcher(url);
    	if (syndfeed == null) {
    		log.warn("Could not load syndfeed from url: " + url + ". Returning empty list of items");
    		return null;
    	}
    	
        Iterator<SyndEntry> feedItemsIterator = syndfeed.getEntries().iterator();
        while (feedItemsIterator.hasNext()) {        	
        	SyndEntry syndEntry = (SyndEntry) feedItemsIterator.next();        	
        	
        	Double latitude = null;
        	Double longitude = null;        	
			final GeoRSSModule geoModule = (GeoRSSModule) GeoRSSUtils.getGeoRSS(syndEntry);
			if (geoModule != null && geoModule.getPosition() != null) {
				latitude = geoModule.getPosition().getLatitude();
				longitude = geoModule.getPosition().getLongitude();
				log.debug("Rss item '" + syndEntry.getTitle() + "' has position information: " + latitude + "," + longitude);
			}
        	
			final String imageUrl = extractImage(syndEntry);
			
        	final String body = syndEntry.getDescription() != null ? syndEntry.getDescription().getValue() : null;
			feedItems.add(new FeedItem(syndEntry.getTitle(), syndEntry.getUri(), body, syndEntry.getLink(), syndEntry.getPublishedDate(), latitude, longitude, imageUrl));
        }
        
        return feedItems;
	}
		
	private SyndFeed loadSyndFeedWithFeedFetcher(String feedUrl) {
		log.info("Loading SyndFeed from url: " + feedUrl);

		URL url;
		try {
			url = new URL(feedUrl);
			HttpURLFeedFetcher fetcher = new HttpURLFeedFetcher();
			SyndFeed feed = fetcher.retrieveFeed(url);
			return feed;
		} catch (Exception e) {
			log.warn("Error while fetching feed: " + e.getMessage());
		}
		return null;
	}
	
	private String extractImage(SyndEntry item) {
		final MediaEntryModuleImpl mediaModule = (MediaEntryModuleImpl) item.getModule(MediaModule.URI);
		if (mediaModule == null) {
			log.debug("No media module found for item: " + item.getTitle());
			return null;
		}
		
		final MediaContent[] mediaContents = mediaModule.getMediaContents();
		if (mediaContents.length > 0) {			
			MediaContent selectedMediaContent = null;
			for (int i = 0; i < mediaContents.length; i++) {
				MediaContent mediaContent = mediaContents[i];
				final boolean isImage = mediaContent.getType() != null && mediaContent.getType().equals("image/jpeg") && mediaContent.getReference() != null;
				if (isImage && isBetterThanCurrentlySelected(mediaContent, selectedMediaContent)) {
					selectedMediaContent = mediaContent;
				}
			}
			
			if (selectedMediaContent != null) {
				log.info("Took image reference from MediaContent: " + selectedMediaContent.getReference().toString());
				return selectedMediaContent.getReference().toString();
			}
		}

		log.info("No suitable media element image seen");
		return null;
	}

	private boolean isBetterThanCurrentlySelected(MediaContent mediaContent, MediaContent selectedMediaContent) {
		if (selectedMediaContent == null) {
			return true;
		}		
		return mediaContent.getWidth() > selectedMediaContent.getWidth();		
	}

}
