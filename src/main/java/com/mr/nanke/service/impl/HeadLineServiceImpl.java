package com.mr.nanke.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mr.nanke.cache.JedisUtil;
import com.mr.nanke.dao.HeadLineDao;
import com.mr.nanke.entity.HeadLine;
import com.mr.nanke.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService{
	
	@Autowired
	private HeadLineDao headLineDao;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Override
	@Transactional
	public List<HeadLine> getHeadLine(HeadLine headLineCondition) throws IOException {
		List<HeadLine> headLineList = null;
		ObjectMapper mapper = new ObjectMapper();
		String key = HLLISTKEY;
		//根据前台传入的参数不一样，key获取到的内容也是不一样的，有三种状态，所有的headline，可用的headline（getEnableStatus为1），禁用的headline（getEnableStatus为0）
		if (headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		//判断
		if (!jedisKeys.exists(key)) {
			headLineList = headLineDao.queryHeadLineList(headLineCondition);
			String jsonString = mapper.writeValueAsString(headLineList);
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory()
					.constructParametricType(ArrayList.class, HeadLine.class);
			headLineList = mapper.readValue(jsonString, javaType);
		}
		return headLineList;
	}
	
	
	
}
