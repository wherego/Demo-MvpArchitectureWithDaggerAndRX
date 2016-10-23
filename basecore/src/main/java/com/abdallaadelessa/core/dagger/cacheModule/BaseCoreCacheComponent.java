package com.abdallaadelessa.core.dagger.cacheModule;

import com.abdallaadelessa.core.dagger.cacheModule.cache.BaseCache;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Abdalla on 16/10/2016.
 */
@Singleton
@Component(modules = BaseCoreCacheModule.class)
public interface BaseCoreCacheComponent {
    BaseCache<String, String> getDiskCache();

    BaseCache<String, Object> getMemoryCache();
}