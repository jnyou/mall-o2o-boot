package com.mr.nanke.service;

/**
 * 当改变了后台数据的时候移除redis中KEY
 * @author 夏小颜
 *
 */
public interface CacheService {

	void removeFromCache(String key);
}
