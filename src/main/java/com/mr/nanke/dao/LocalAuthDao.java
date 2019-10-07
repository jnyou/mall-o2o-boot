package com.mr.nanke.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.mr.nanke.entity.LocalAuth;


public interface LocalAuthDao {

	/**
	 * 通过账号密码 供登陆
	 * @param username
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username,
			@Param("password") String password);

	/**
	 * 通过用户id查询
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalByUserId(@Param("userId") long userId);

	/**
	 * 添加平台账号
	 * @param localAuth
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);

	/**
	 * 修改密码，通过username,username,password
	 * @param localAuth
	 * @return
	 */
	int updateLocalAuth(@Param("userId") Long userId,
			@Param("username") String username,
			@Param("password") String password,
			@Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);
}
