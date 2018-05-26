package com.simple.o2o.service;

public interface CacheService {
	/**
	 * 依据key前缀，删除匹配所有的key-value(redis的key删除)
	 * @param keyPrefix
	 */
	void removeFromCache(String keyPrefix);
}
