package com.mr.nanke.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mr.nanke.dao.PersonInfoDao;
import com.mr.nanke.dao.WechatAuthDao;
import com.mr.nanke.dto.WechatAuthExecution;
import com.mr.nanke.entity.PersonInfo;
import com.mr.nanke.entity.WechatAuth;
import com.mr.nanke.enums.WechatAuthStateEnum;
import com.mr.nanke.exceptions.WechatAuthOperationException;
import com.mr.nanke.service.WechatAuthService;

@Service
public class WechatAuthServiceImpl implements WechatAuthService{

	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Autowired
	private WechatAuthDao wechatAuthDao;
	
	private static Logger log = LoggerFactory.getLogger(WechatAuthServiceImpl.class);
	/**
	 * 通过openId查询对应本平台的微信账号
	 * @param openId
	 * @return
	 */
	@Override
	public WechatAuth queryWechatInfoByOpenId(String openId) {
		return wechatAuthDao.queryWechatInfoByOpenId(openId);
	}
	/**
	 * 注册本平台的微信账号
	 * @param wechatAuth
	 * @return
	 */
	@Override
	@Transactional
	public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
		//空值判断
		if(wechatAuth == null) {
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			//设置创建时间
			wechatAuth.setCreateTime(new Date());
			//如果账号中有微信信息并且用户ID为空，则认为是第一次使用平台（且通过微信登陆）
			//则自动创建用户信息到平台中
			if(wechatAuth.getPersonInfo() != null && wechatAuth.getPersonInfo().getUserId() == null) {
				try {
					wechatAuth.getPersonInfo().setCreateTime(new Date());
					wechatAuth.getPersonInfo().setEnableStatus(1);
					PersonInfo personInfo=wechatAuth.getPersonInfo();
					int effeccNum = personInfoDao.insertPersonInfo(personInfo);
					wechatAuth.setPersonInfo(personInfo);
					if(effeccNum <=0) {
						throw new WechatAuthOperationException("用户信息创建失败");
					}
				}catch (Exception e) {
					log.error("insertPersonInfo create err" + e.getMessage());
					throw new WechatAuthOperationException("insertPersonInfo create err" + e.getMessage());
				}
			}
			//创建本平台微信账号
			int effeccNum = wechatAuthDao.insertWechatAuth(wechatAuth);
			if(effeccNum <= 0) {
				throw new WechatAuthOperationException("微信账号创建失败");
			}else {
				return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS,wechatAuth);
			}
		}catch (Exception e) {
			log.error("insertWechatAuth create err" + e.getMessage());
			throw new WechatAuthOperationException("insertWechatAuth create err" + e.getMessage());
		}
	}
}
