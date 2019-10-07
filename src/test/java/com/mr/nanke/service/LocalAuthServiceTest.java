package com.mr.nanke.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.dto.LocalAuthExecution;
import com.mr.nanke.entity.LocalAuth;
import com.mr.nanke.entity.PersonInfo;
import com.mr.nanke.enums.WechatAuthStateEnum;
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthServiceTest  {

	@Autowired
	private LocalAuthService localAuthService;

	@Test
	public void testBindLocalAuth() {
		// 新增一条平台账号
		LocalAuth localAuth = new LocalAuth();
		localAuth.setUsername("testUsername");
		localAuth.setPassword("testPassword");
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(10L);
		localAuth.setPersonInfo(personInfo);
		LocalAuthExecution bindLocalAuth = localAuthService.bindLocalAuth(localAuth);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), bindLocalAuth.getState());
		localAuth = localAuthService.getLocalByUserId(personInfo.getUserId());
		System.out.println("用户昵称：" + localAuth.getPersonInfo().getName());
		System.out.println("用户密码" + localAuth.getPassword());
	}


	@Test
	public void testModifyLocalAuth() {
		long userId = 10;
		String username = "testUsername";
		String password = "testPassword";
		String newPassword ="new"+ password ;

		LocalAuthExecution modifyLocalAuth = localAuthService.modifyLocalAuth(userId, username, password, newPassword);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), modifyLocalAuth.getState());
		LocalAuth la = localAuthService.getLocalByUserNameAndPwd(username, newPassword);
		System.out.println(la.getPersonInfo().getName());
	}

}
