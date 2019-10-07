package com.mr.nanke.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mr.nanke.cache.JedisUtil;
import com.mr.nanke.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService{

	@Autowired
	private JedisUtil.Keys jedisKeys;

	@Override
	public void removeFromCache(String key) {
		//获取到每个模块的以Key为前缀的，遍历删除
		Set<String> keySet = jedisKeys.keys(key + "*");
		for (String str : keySet) {
			jedisKeys.del(str);
		}
	}
	
}
