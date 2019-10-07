package com.mr.nanke.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.entity.HeadLine;
@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineDaoTest{
	
	@Autowired
	private HeadLineDao headLineDao;
	
	@Test
	public void headLineTest() {
		HeadLine headLineCondition = new HeadLine();
		headLineCondition.setEnableStatus(1);
		List<HeadLine> headLineList = headLineDao.queryHeadLineList(headLineCondition);
		assertEquals(4, headLineList.size());
	}
	
}
