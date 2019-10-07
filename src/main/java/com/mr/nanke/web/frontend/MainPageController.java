package com.mr.nanke.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mr.nanke.entity.HeadLine;
import com.mr.nanke.entity.ShopCategory;
import com.mr.nanke.service.HeadLineService;
import com.mr.nanke.service.ShopCategoryService;

//主页的请求
@Controller
@RequestMapping("frontend")
public class MainPageController {
	
	@Autowired
	private HeadLineService headLineService;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	/***
	 * 初始化前端展示系统主页信息， 包括获取一级店铺类别列表以及头条列表
	 * @return
	 */
	@RequestMapping("getmainlistinfo")
	@ResponseBody
	public Map<String , Object> getMainListInfo(){
		Map<String , Object> modelMap = new HashMap<String, Object>();
		//获取一级店铺类别列表（即parent_id为空的类别）
		try {
			List<ShopCategory> categoryList = shopCategoryService.getShopCategoryList(null);
			modelMap.put("categoryList", categoryList);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		
		//设置状态为1的头条列表展示
		try {
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			List<HeadLine> headLines = headLineService.getHeadLine(headLineCondition);
			modelMap.put("headLines", headLines);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}

}
