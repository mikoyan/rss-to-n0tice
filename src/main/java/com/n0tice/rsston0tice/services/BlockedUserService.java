package com.n0tice.rsston0tice.services;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class BlockedUserService {

	private Cache<String, Boolean> blockedUsers;

	public BlockedUserService() {
		blockedUsers = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build();
	}
	
	public boolean isBlocked(String user) {
		return blockedUsers.getIfPresent(user) != null;
	}

	public void blockUser(String user) {
		blockedUsers.put(user, true);		
	}

}
