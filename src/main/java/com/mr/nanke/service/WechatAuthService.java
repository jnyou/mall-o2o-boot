package com.mr.nanke.service;

import com.mr.nanke.dto.WechatAuthExecution;
import com.mr.nanke.entity.WechatAuth;
import com.mr.nanke.exceptions.WechatAuthOperationException;

public interface WechatAuthService {
	
	/**
	 * 通过openId查询对应本平台的微信账号
	 * @param openId
	 * @return
	 */
	WechatAuth queryWechatInfoByOpenId(String openId);
	
	/**
	 * 注册本平台的微信账号
	 * @param wechatAuth
	 * @return
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;
	
}
