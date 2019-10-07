package com.mr.nanke.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.entity.LocalAuth;
import com.mr.nanke.entity.PersonInfo;
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthDaoTest{

	@Autowired
	private LocalAuthDao localAuthDao;
	
	@Test
	public void testInsertLocalAuth() {
		LocalAuth localAuth = new LocalAuth();
		localAuth.setCreateTime(new Date());
		localAuth.setLastEditTime(new Date());
		localAuth.setPassword("testPassword1");
		localAuth.setUsername("testUsername");
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(9L);
		localAuth.setPersonInfo(personInfo);
		
		int effectNum = localAuthDao.insertLocalAuth(localAuth);
		assertEquals(1, effectNum);
	}
	
	@Test
	public void testQueryLocalByUserNameAndPwd() {
		localAuthDao.queryLocalByUserNameAndPwd("testUsername", "testPassword1");
	}
	@Test
	public void testQueryLocalByUserId() {
		LocalAuth queryLocalByUserId = localAuthDao.queryLocalByUserId(11L);
		System.out.println(queryLocalByUserId);
	}
	@Test
	public void testUpdateLocalAuth() {
		LocalAuth localAuth = new LocalAuth();
		localAuth.setLastEditTime(new Date());
		localAuth.setPassword("testPassword");
		
		int effectNum = localAuthDao.updateLocalAuth(10L, "testUsername", "testPassword1", localAuth.getPassword(), localAuth.getLastEditTime());
		assertEquals(1, effectNum);
	}
	
}
