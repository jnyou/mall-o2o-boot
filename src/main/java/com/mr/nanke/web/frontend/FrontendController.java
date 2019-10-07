package com.mr.nanke.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("frontend")
public class FrontendController {
	/***
	 * 首页路由
	 * @return
	 */

	@RequestMapping("index")
	public String getInfo() {
		//转发至首页
		return "frontend/index";
	}
	
	
	@RequestMapping("shoplist")
	public String getShopList() {
		//转发至商品列表
		return "frontend/shoplist";
	}
	
	@RequestMapping("shopdetail")
	public String getShopDetail() {
		//转发至商品详情页
		return "frontend/shopdetail";
	}
	
	@RequestMapping("productdetail")
	public String getProductDetail() {
		//转发至物品详情页
		return "frontend/productdetail";
	}
}
