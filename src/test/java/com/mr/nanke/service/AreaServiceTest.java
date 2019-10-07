package com.mr.nanke.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mr.nanke.entity.Area;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest {
	@Autowired
	private AreaService areaService;
	@Autowired
	private CacheService cacheService;
	@Test
	public void areaServiceTest() {
		List<Area> areas = areaService.getAreaList();
		assertEquals("东苑", areas.get(0).getAreaName()); //验证数据库第一行数据的名字是否为东苑
		cacheService.removeFromCache(areaService.AREALISTKRY); //测试删除key
		areas = areaService.getAreaList();
	}
}
