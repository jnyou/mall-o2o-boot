package com.mr.nanke.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.entity.PersonInfo;
import com.mr.nanke.entity.WechatAuth;
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthDaoTest {
	
	@Autowired
	private WechatAuthDao wechatAuthDao;
	
	@Test
	public void testqueryWechatInfoByOpenId() {
		String openId = "ovLbns-gxJHqC-UTPQKvgEuENl-E";
		wechatAuthDao.queryWechatInfoByOpenId(openId);
	}

	@Test
	public void testInsertWechatAuth() {
		WechatAuth wechatAuth = new WechatAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(12L);
		wechatAuth.setPersonInfo(personInfo);
		wechatAuth.setOpenId("test");
		wechatAuth.setCreateTime(new Date());
		int effeccNum = wechatAuthDao.insertWechatAuth(wechatAuth);
		assertEquals(effeccNum,1);
	}
}
