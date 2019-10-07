package com.mr.nanke.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.entity.PersonInfo;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonInfoDaoTest  {
	
	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Test
	public void testPersonInfoById() {
		personInfoDao.queryPersonInfoById(8);
	}
	
	@Test
	public void testInsertPersonInfo() {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName("test");
		personInfo.setGender("1");
		personInfo.setProfileImg("http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKCWfIBicEwS3U0legxxQd5XFpZibBXVPyz0wphvvtaXqiblzQF2GqE28c7j8FGpuYqBCg1QRJThEzuw/0");
		personInfo.setCustomerFlag(1);
		personInfo.setShopOwnerFlag(1);
		personInfo.setAdminFlag(0);
		personInfo.setCreateTime(new Date());
		personInfo.setLastEditTime(new Date());
		personInfo.setEnableStatus(1);
		int effectNum = personInfoDao.insertPersonInfo(personInfo);
		assertEquals(effectNum, 1);
	}
	

}
