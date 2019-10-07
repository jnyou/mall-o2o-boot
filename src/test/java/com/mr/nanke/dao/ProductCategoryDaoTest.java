package com.mr.nanke.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.entity.ProductCategory;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryDaoTest  {

	@Autowired
	private ProductCategoryDao categoryDao;
	
	@Test
	public void testQueryProductCategoryList() {
		List<ProductCategory> productCategoryList = categoryDao.queryProductCategoryList(20L);
		System.out.println(productCategoryList.size());
	}
	
	@Test
	public void testBatchInsertProductCategory() {
		List<ProductCategory> list = new ArrayList<ProductCategory>();
		ProductCategory category1 = new ProductCategory();
		category1.setCreateTime(new Date());
		category1.setLastEditTime(new Date());
		category1.setPriority(20);
		category1.setProductCategoryDesc("新品1");
		category1.setProductCategoryName("测试类别1");
		category1.setShopId(30L);
		ProductCategory category2 = new ProductCategory();
		category2.setCreateTime(new Date());
		category2.setLastEditTime(new Date());
		category2.setPriority(25);
		category2.setProductCategoryDesc("新品2");
		category2.setProductCategoryName("测试类别2");
		category2.setShopId(30L);
		list.add(category1);
		list.add(category2);
		int batchInsertProductCategory = categoryDao.batchInsertProductCategory(list);
		assertEquals(2, batchInsertProductCategory);
	}
	
	
	@Test
	public void testDeleteProductCategory() {
		long shopId = 30L;
		List<ProductCategory> categoryList = categoryDao.queryProductCategoryList(shopId);
		for (ProductCategory productCategory : categoryList) {
			if("测试类别1".equals(productCategory.getProductCategoryName()) || "测试类别2".equals(productCategory.getProductCategoryName())) {
				int effectNum = categoryDao.deleteProductCategory(productCategory.getProductCategoryId(), shopId);
				assertEquals(1, effectNum);
			}
		}
	}
	
}
