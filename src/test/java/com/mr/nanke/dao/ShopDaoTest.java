package com.mr.nanke.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.entity.Area;
import com.mr.nanke.entity.PersonInfo;
import com.mr.nanke.entity.Shop;
import com.mr.nanke.entity.ShopCategory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest  {
	@Autowired
	private ShopDao shopDao;
	
	@Test
	public void testQueryShopListAndCount() {
		Shop shopCondition = new Shop();
		ShopCategory childCategory = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		parentCategory.setShopCategoryId(12L);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
		
		List<Shop> queryShopList = shopDao.queryShopList(shopCondition, 0, 5);
		
		int queryShopCount = shopDao.queryShopCount(shopCondition);
		System.out.println("返回的店铺的条数" + queryShopList.size() + "\n" + "返回的总条数" + queryShopCount);
	}
	
	@Test
	public void testQueryShopByShopId() {
		Long shopId = 15L;
		Shop shop = shopDao.queryShopByShopId(shopId);
		System.out.println(shop.getArea().getAreaId());
		System.out.println(shop.getArea().getAreaName());
	}
	
	@Test
	public void testInsertShop() {
		Shop shop = new Shop();;
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(8L);
		area.setAreaId(3L);
		shopCategory.setShopCategoryId(10L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺");
		shop.setShopAddr("test");
		shop.setShopDesc("test");
		shop.setShopImg("test");
		shop.setCreateTime(new Date());
		shop.setAdvice("审核中");
		shop.setEnableStatus(1);
		//影响的条数  看看是不是一条
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testUpdateShop() {
		Shop shop = new Shop();;
		shop.setShopId(30L);
		shop.setShopAddr("测试地址");
		shop.setShopDesc("测试描述");
		shop.setLastEditTime(new Date());
		//影响的条数  看看是不是一条
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}
	
}
