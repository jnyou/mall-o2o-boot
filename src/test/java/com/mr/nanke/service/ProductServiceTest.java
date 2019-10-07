package com.mr.nanke.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.dto.ImageHolder;
import com.mr.nanke.entity.Product;
import com.mr.nanke.entity.ProductCategory;
import com.mr.nanke.entity.Shop;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

	@Autowired
	private ProductService productService;
	
	@Test
	public void testProductService() throws Exception {
		Product product = new Product();
		product.setCreateTime(new Date());
		product.setImgAddr("test2");
		product.setEnableStatus(1);
		product.setLastEditTime(new Date());
		product.setNormalPrice("200");
		product.setPoint(1);
		product.setPriority(100);
		product.setProductDesc("清热解毒");
		product.setProductName("冰糖雪梨");
		product.setPromotionPrice("180");
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(11L);
		Shop shop = new Shop();
		shop.setShopId(20L);
		product.setShop(shop);
		product.setProductCategory(productCategory);
		
		File file = new File("C:/Users/YJN/Pictures/StudyPicture/ajax.png");
		InputStream inputStream = new FileInputStream(file);
		ImageHolder imageHolder = new ImageHolder(inputStream, file.getName());
		
		
		List<ImageHolder> imageHolders = new ArrayList<ImageHolder>();
		File file1 = new File("C:/Users/YJN/Pictures/StudyPicture/timg.jpg");
		InputStream inputStream1 = new FileInputStream(file1);
		ImageHolder imageHolder2 = new ImageHolder(inputStream1,file1.getName());
		
		File file2 = new File("C:/Users/YJN/Pictures/StudyPicture/ajax.png");
		InputStream inputStream2 = new FileInputStream(file2);
		ImageHolder imageHolder3 = new ImageHolder(inputStream2,file2.getName());
		imageHolders.add(imageHolder2);
		imageHolders.add(imageHolder3);
		
		productService.addProduct(product, imageHolder, imageHolders);
	}
}
