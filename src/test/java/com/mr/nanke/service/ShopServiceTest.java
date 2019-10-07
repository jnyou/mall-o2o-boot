package com.mr.nanke.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.dto.ImageHolder;
import com.mr.nanke.dto.ShopExecution;
import com.mr.nanke.entity.Area;
import com.mr.nanke.entity.PersonInfo;
import com.mr.nanke.entity.Shop;
import com.mr.nanke.entity.ShopCategory;
import com.mr.nanke.enums.ShopStateEnum;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {
	@Autowired
	private ShopService shopService;
	
	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		Area area = new Area();
		area.setAreaId(6L);
		shopCondition.setArea(area);
		ShopExecution shopList = shopService.getShopList(shopCondition, 1, 2);
		System.out.println("返回的店铺的条数++" + shopList.getShopList() + "\n" + "返回的总条数++" + shopList.getCount());
	}
	
	@Test
	public void testUpdateShop() throws FileNotFoundException {
		Shop shop = new Shop();
		shop.setShopId(35L);
		shop.setShopName("修改后店铺的名称");
		File shopImg = new File("C:/Users/YJN/Pictures/StudyPicture/ajax.png");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder(is, "ajax.png");
		ShopExecution updateShop = shopService.updateShop(shop, imageHolder);
		System.out.println(updateShop.getShop().getShopImg());
	}
	
	@Test
	public void testInsertShop() throws FileNotFoundException {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(8L);
		area.setAreaId(3L);
		shopCategory.setShopCategoryId(10L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺3");
		shop.setShopAddr("test3");
		shop.setShopDesc("test3");
		shop.setCreateTime(new Date());
		shop.setAdvice("审核中");
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		//影响的条数  看看是不是一条   ‪‪C:\Users\YJN\Pictures\StudyPicture\timg.jpg
		File shopImg = new File("C:/Users/YJN/Pictures/StudyPicture/timg.jpg");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder(is, shopImg.getName());
		ShopExecution se = shopService.addShop(shop, imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
	}
}
