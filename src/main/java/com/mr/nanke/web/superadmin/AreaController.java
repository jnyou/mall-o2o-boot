package com.mr.nanke.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mr.nanke.entity.Area;
import com.mr.nanke.service.AreaService;

@Controller
@RequestMapping("superadmin")
public class AreaController {
	//定义日志信息
	Logger logger = LoggerFactory.getLogger(AreaController.class);
	@Autowired
	private AreaService areaService;
	@RequestMapping("listarea")
	@ResponseBody
	public Map<String, Object> listArea(){
		logger.info("===start===");
		long startTime = System.currentTimeMillis();
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<Area> list = new ArrayList<Area>();
		try {
			list = areaService.getAreaList();
			modelMap.put("rows", list); //返回每个对象的信息
			modelMap.put("total", list.size()); //返回的总条数 
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false); //成功的话为false
			modelMap.put("errMsg",e.toString() ); //错误信息打印出来
		}
		logger.error("test errot...");
		long endTime = System.currentTimeMillis();
		logger.debug("costTime[{}ms]",endTime - startTime);
		logger.info("===end===");
		return modelMap;
	}
	
}
