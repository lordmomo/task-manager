package com.momo.task.manager.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class RefreshCache {
    @Autowired
    CacheManager cacheManager;
    // Method responsible for refreshing the cache
    public void refresh(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            // Clears all entries from the cache, effectively refreshing it
            cache.clear();
        }
    }
}
