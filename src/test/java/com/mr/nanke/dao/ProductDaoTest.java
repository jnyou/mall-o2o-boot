package com.mr.nanke.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.entity.Product;
import com.mr.nanke.entity.ProductCategory;
import com.mr.nanke.entity.Shop;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductDaoTest{
	
	@Autowired
	private ProductDao productDao;
	
	@Test
	public void testQueryProductListDao() {
		Product productCondition = new Product();
		productCondition.setProductName("冰糖");
		productCondition.setEnableStatus(1);
		List<Product> productList = productDao.queryProductList(productCondition, 2, 2);
		System.out.println(productList.toString());
		
		productDao.queryConditionCount(productCondition);
	}
	
	@Test
	public void testAddProductDao() {
		Product product = new Product();
		product.setCreateTime(new Date());
		product.setImgAddr("test1");
		product.setEnableStatus(1);
		product.setLastEditTime(new Date());
		product.setNormalPrice("200");
		product.setPoint(1);
		product.setPriority(100);
		product.setProductDesc("清热解毒");
		product.setProductName("冰糖雪梨");
		product.setPromotionPrice("180");
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(9L);
		Shop shop = new Shop();
		shop.setShopId(15L);
		product.setShop(shop);
		product.setProductCategory(productCategory);
		productDao.insertProduct(product);
	}
	
	
	@Test
	public void testModifyProductDao() {
		Product product = new Product();
		product.setProductId(27L);
		product.setLastEditTime(new Date());
		product.setNormalPrice("50");
		product.setPriority(200);
		product.setProductDesc("味道好极了");
		product.setPromotionPrice("40");
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(14L);
		Shop shop = new Shop();
		shop.setShopId(30L);
		product.setShop(shop);
		product.setProductCategory(productCategory);
		productDao.modifyProduct(product);
	}
	
	@Test
	public void testFindProductByProductIdDao() {
		Product product = new Product();
		product.setProductId(27L);
		Product queryProductByProductId = productDao.queryProductByProductId(product.getProductId());
		System.out.println(queryProductByProductId);
	}
}
