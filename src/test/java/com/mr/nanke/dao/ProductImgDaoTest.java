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

import com.mr.nanke.entity.ProductImg;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductImgDaoTest  {
	
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	public void testBatchProductImgDao() {
		List<ProductImg> productImgs = new ArrayList<ProductImg>();
		ProductImg productImg1 = new ProductImg();
		productImg1.setCreateTime(new Date());
		productImg1.setImgAddr("图片1");
		productImg1.setImgDesc("冰雪莲图片");
		productImg1.setPriority(100);
		productImg1.setProductId(4L);
		ProductImg productImg2 = new ProductImg();
		productImg2.setCreateTime(new Date());
		productImg2.setImgAddr("图片2");
		productImg2.setImgDesc("芒果图片");
		productImg2.setPriority(100);
		productImg2.setProductId(4L);
		
		productImgs.add(productImg1);
		productImgs.add(productImg2);
		int effectNum = productImgDao.insertBatchProductImg(productImgs);
		assertEquals(2, effectNum);
	}
	
	@Test
	public void testQueryProductImgByProductIdDao() {
		ProductImg productImg2 = new ProductImg();
		productImg2.setProductId(4L);
		List<ProductImg> list = productImgDao.queryProductImgList(productImg2.getProductId());
		for (ProductImg productImg : list) {
			System.out.println(productImg);
		}
	}
	
	@Test
	public void testDeleteProductImgByProductIdDao() {
		ProductImg productImg2 = new ProductImg();
		productImg2.setProductId(4L);
		int effectNum = productImgDao.deleteProductImgByProductId(productImg2.getProductId());
		assertEquals(7, effectNum);
	}
}
