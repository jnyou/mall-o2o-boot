package com.mr.nanke.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mr.nanke.dto.ProductExecution;
import com.mr.nanke.dto.ShopExecution;
import com.mr.nanke.entity.Product;
import com.mr.nanke.entity.ProductCategory;
import com.mr.nanke.entity.Shop;
import com.mr.nanke.service.ProductCategoryService;
import com.mr.nanke.service.ProductService;
import com.mr.nanke.service.ShopService;
import com.mr.nanke.utils.HttpServletRequestUtil;

@Controller
@RequestMapping("frontend")
public class ShopDetailController {
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	/***
	 * 获取店铺信息以及该店铺下面的商品类别列表
	 * @param request
	 * @return
	 */
	@RequestMapping("getlistdetailinfo")
	@ResponseBody
	public Map<String ,Object> getListDetailInfo(HttpServletRequest request){
		Map<String ,Object> modelMap = new HashMap<String, Object>();
		//获取前台传过来的店铺id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop = null;
		List<ProductCategory> productCategories = null;
		if(shopId != -1) {
			//根据店铺的ID获取该店铺信息
			shop = shopService.queryShopById(shopId);
			//获取该店铺下的商品类别列表
			productCategories = productCategoryService.getProductCategoryList(shopId);
			modelMap.put("shop", shop);
			modelMap.put("productCategories", productCategories);
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	/***
	 * 获取指定查询条件下的店铺分页显示所有商品列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getproductshoplist")
	@ResponseBody
	private Map<String, Object> getProductShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 页码获取
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 每页显示条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//获取前台传过来的店铺id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// 非空判断
		if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
			// 获取商品类别id
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			// 获取查询的商品名字
			String productName = HttpServletRequestUtil.getString(request, "productName");
			// 根据组合条件查询
			Product productCondition = compactShopCondition4Search(shopId,productCategoryId, productName);
			
			ProductExecution se = productService.queryProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", se.getProductList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}

	/***
	 * 组合条件查询封装
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactShopCondition4Search(long shopId,long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if(productCategoryId != -1L) {
			//查询某个商品类别下面的商品列表
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName != null) {
			//根据商品名字查询
			productCondition.setProductName(productName);
		}
		//只允许上架的商品显示
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
