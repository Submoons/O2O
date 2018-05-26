package com.simple.o2o.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.o2o.cache.JedisUtil;
import com.simple.o2o.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService{

	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Override
	public void removeFromCache(String keyPrefix) {
		Set<String> keySet = jedisKeys.keys(keyPrefix + "*");
		for(String key : keySet){
			jedisKeys.del(key);
		}
	}
	
}
