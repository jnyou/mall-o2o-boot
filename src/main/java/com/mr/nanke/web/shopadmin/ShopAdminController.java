package com.mr.nanke.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("shopadmin")
/***
 * 主要用来解析路由并转发至相应的的html页面
 * @author 夏小颜
 *
 */
public class ShopAdminController {

	@RequestMapping("shopoperation")
	public String getInfo() {
		//转发到店铺注册/编辑页面
		return "shop/shopoperation";
	}
	
	@RequestMapping("shoplist")
	public String getShopList() {
		//转发到店铺列表页面
		return "shop/shoplist";
	}
	
	@RequestMapping("shopmanager")
	public String getShopManager() {
		//转发到店铺管理页面
		return "shop/shopmanager";
	}
	
	@RequestMapping("productcategorymanage")
	public String getProductCategoryManage() {
		//转发到商品类别管理页面
		return "shop/productcategorymanage";
	}
	
	@RequestMapping("productoperation")
	public String getProductOperationManage() {
		//转发到商品添加/编辑页面
		return "shop/productoperation";
	}
	
	@RequestMapping("productmanage")
	public String getProductManage() {
		//转发到商品列表页面
		return "shop/productmanage";
	}
}
