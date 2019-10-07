package com.mr.nanke.service;


import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.mr.nanke.dto.LocalAuthExecution;
import com.mr.nanke.entity.LocalAuth;
import com.mr.nanke.exceptions.LocalAuthOperationException;

public interface LocalAuthService {
	
	/**
	 * 通过账号密码 供登陆
	 * @param username
	 * @param password
	 * @return
	 */
	LocalAuth getLocalByUserNameAndPwd(String username,String password);

	/**
	 * 通过用户id查询
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalByUserId(long userId);
	
	/***
	 * 绑定微信。生成平台账号
	 * @param localAuth
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;
	
	/**
	 * 修改平台的登陆密码
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution modifyLocalAuth(Long userId,String username,String password,String newPassword) throws LocalAuthOperationException;
	
}
