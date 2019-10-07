package com.mr.nanke.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mr.nanke.cache.JedisUtil;
import com.mr.nanke.dao.ShopCategoryDao;
import com.mr.nanke.entity.ShopCategory;
import com.mr.nanke.exceptions.ShopOperationException;
import com.mr.nanke.service.ShopCategoryService;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

	@Autowired
	private ShopCategoryDao shopCategoryDao;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	@Autowired
	private JedisUtil.Keys jedisKeys;

	@Override
	@Transactional
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
		// TODO Auto-generated method stub
		String key = SCLISTKEY;
		List<ShopCategory> shopCategoryList = null;
		ObjectMapper mapper = new ObjectMapper();
		// 根据前台传入的参数不一样，key获取到的内容也是不一样的，有三种状态，所有的headline，可用的headline（getEnableStatus为1），禁用的headline（getEnableStatus为0）
		if (shopCategoryCondition == null) {
			key = key + "_allfirstlevel"; //取出所有一级
		}else if(shopCategoryCondition != null && shopCategoryCondition.getParent() != null && shopCategoryCondition.getParent().getShopCategoryId() != null){
			//parent不为空则列出该大类别下面所有的子类别
			key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
		}else if(shopCategoryCondition != null) {
			//取出所有类别，不管属于哪个大类别下的全部显示
			key = key + "_allsecondlevel"; //取出所有二级
		}
		if (!jedisKeys.exists(key)) {
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
			String jsonString;
			try {
				//转换成String对象保存在redis中
				jsonString = mapper.writeValueAsString(shopCategoryList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				throw new ShopOperationException(e.getMessage());
			}
			//存入redis
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			try {
				shopCategoryList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				throw new ShopOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				throw new ShopOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw new ShopOperationException(e.getMessage());
			}
		}
		return shopCategoryList;
	}
	
}
