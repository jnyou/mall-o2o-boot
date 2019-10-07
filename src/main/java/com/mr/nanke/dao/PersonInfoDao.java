package com.mr.nanke.dao;

import com.mr.nanke.entity.PersonInfo;

/**
 * 微信用户
 * @author 夏小颜
 *
 */
public interface PersonInfoDao {
	
	/**
	 * 通过用户的ID查询用户
	 * @param userId
	 * @return
	 */
	PersonInfo queryPersonInfoById(long userId);
	
	/**
	 * 添加用户信息
	 * @param personInfo
	 * @return
	 */
	int insertPersonInfo(PersonInfo personInfo);
}
