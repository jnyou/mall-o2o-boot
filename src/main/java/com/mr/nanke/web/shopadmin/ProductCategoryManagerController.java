package com.mr.nanke.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mr.nanke.dto.ProductCategoryExecution;
import com.mr.nanke.entity.ProductCategory;
import com.mr.nanke.entity.Shop;
import com.mr.nanke.enums.ProductCategoryStateEnum;
import com.mr.nanke.exceptions.ProductCategoryOperationException;
import com.mr.nanke.service.ProductCategoryService;

@Controller
@RequestMapping("shopadmin")
public class ProductCategoryManagerController {

	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping("removeproducatcategory")
	@ResponseBody
	public Map<String, Object> deleteProductCategory(Long productCategoryId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryId != null && productCategoryId > 0) {
			// 从session获取 返回一个shop对象
			Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
			// 获取session中的shopId
			ProductCategoryExecution deleteProductCategory = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
			try {
				if(deleteProductCategory.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", deleteProductCategory.getStateInfo());
				}
			}catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("success", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个商品类别！");
		}
		return modelMap;
	}

	@RequestMapping("addproductcategorys")
	@ResponseBody
	public Map<String, Object> addBatchProductCategory(@RequestBody List<ProductCategory> productCategories,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 给每一个添加的类别设置session中的shopId
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		for (ProductCategory productCategory : productCategories) {
			productCategory.setShopId(currentShop.getShopId());
		}

		if (productCategories != null && productCategories.size() > 0) {
			ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategories);
			try {
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("success", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个商品类别！");
		}

		return modelMap;

	}

	/***
	 * 获取店铺id下的店铺类编信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getproductcategorylist")
	@ResponseBody
	public Map<String, Object> getProductCategoryList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		try {
			if (currentShop != null && currentShop.getShopId() > 0) {
				List<ProductCategory> categoryList = productCategoryService
						.getProductCategoryList(currentShop.getShopId());
				modelMap.put("categoryList", categoryList);
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
			}
		} catch (Exception e) {
			modelMap.put("errMsg", e.getMessage());
			modelMap.put("success", false);
		}
		return modelMap;
	}

}
