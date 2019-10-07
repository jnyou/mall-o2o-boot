package com.mr.nanke.service;

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
public class WechatAuthServiceTest {
	
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@Test
	public void testRegister() {
		WechatAuth wechatAuth = new WechatAuth();
		wechatAuth.setCreateTime(new Date());
		String openId = "zadsafsfa";
		wechatAuth.setOpenId(openId);
		PersonInfo personInfo = new PersonInfo();
		personInfo.setCreateTime(new Date());
		personInfo.setName("test");
		personInfo.setGender("1");
		personInfo.setProfileImg("http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKCWfIBicEwS3U0legxxQd5XFpZibBXVPyz0wphvvtaXqiblzQF2GqE28c7j8FGpuYqBCg1QRJThEzuw/0");
		personInfo.setCustomerFlag(1);
		personInfo.setShopOwnerFlag(1);
		personInfo.setAdminFlag(0);
		personInfo.setCreateTime(new Date());
		personInfo.setLastEditTime(new Date());
		personInfo.setEnableStatus(1);
		wechatAuth.setPersonInfo(personInfo);
		wechatAuthService.register(wechatAuth);
		WechatAuth queryWechatInfoByOpenId = wechatAuthService.queryWechatInfoByOpenId(openId);
		System.out.println(queryWechatInfoByOpenId.getPersonInfo().getName());
	}

}
