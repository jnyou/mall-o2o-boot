package com.mr.nanke.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mr.nanke.dao.PersonInfoDao;
import com.mr.nanke.entity.PersonInfo;
import com.mr.nanke.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService{

	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public PersonInfo getPersonInfoById(long userId) {
		return personInfoDao.queryPersonInfoById(userId);
	}
	
	
}
