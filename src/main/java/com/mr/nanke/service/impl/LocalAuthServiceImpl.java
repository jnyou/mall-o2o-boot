package com.mr.nanke.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mr.nanke.dao.LocalAuthDao;
import com.mr.nanke.dto.LocalAuthExecution;
import com.mr.nanke.entity.LocalAuth;
import com.mr.nanke.enums.LocalAuthStateEnum;
import com.mr.nanke.exceptions.LocalAuthOperationException;
import com.mr.nanke.service.LocalAuthService;
import com.mr.nanke.utils.MD5;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

	@Autowired
	private LocalAuthDao localAuthDao;

	private static final Logger log = LoggerFactory.getLogger(LocalAuthServiceImpl.class);

	@Override
	public LocalAuth getLocalByUserNameAndPwd(String username, String password) {
		return localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMd5(password));
	}

	@Override
	public LocalAuth getLocalByUserId(long userId) {
		return localAuthDao.queryLocalByUserId(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		//空值判断
		if (localAuth == null || localAuth.getPassword() == null || localAuth.getUsername() == null
				|| localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		//此用户是否绑定过平台账号
		LocalAuth localByUserId = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
		if(localByUserId != null) {
			//不为空则绑定过，直接退出，保证userId唯一性
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		try {
			//如果没有绑定
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectNum = localAuthDao.insertLocalAuth(localAuth);
			if(effectNum <=0) {
				throw new LocalAuthOperationException("创建平台账号失败");
			}else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			}
		}catch (Exception e) {
			throw new LocalAuthOperationException("创建平台账号失败" + e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException {
		//非空判断，并判断新密码与旧密码是否相同
		if(username != null && password != null && userId != null && newPassword != null && !password.equals(newPassword)) {
			try {
				int effectNum = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password), MD5.getMd5(newPassword), new Date());
				if(effectNum <= 0) {
					throw new LocalAuthOperationException("密码修改失败");
				}else {
					return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
				}
			}catch (Exception e) {
				log.error("密码修改失败" + e.getMessage());
				throw new LocalAuthOperationException("密码修改失败" + e.getMessage());
			}
		}else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}

}
