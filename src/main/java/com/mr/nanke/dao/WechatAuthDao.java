package com.mr.nanke.dao;

import com.mr.nanke.entity.WechatAuth;

public interface WechatAuthDao {
	
	/**
	 * 通过openId查询对应本平台的微信账号
	 * @param openId
	 * @return
	 */
	WechatAuth queryWechatInfoByOpenId(String openId);
	
	/**
	 * 添加微信账号到对应平台中
	 * @param wechatAuth
	 * @return
	 */
	int insertWechatAuth(WechatAuth wechatAuth);
}
