package com.mr.nanke.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mr.nanke.dto.ShopExecution;
import com.mr.nanke.entity.Area;
import com.mr.nanke.entity.Shop;
import com.mr.nanke.entity.ShopCategory;
import com.mr.nanke.service.AreaService;
import com.mr.nanke.service.ShopCategoryService;
import com.mr.nanke.service.ShopService;
import com.mr.nanke.utils.HttpServletRequestUtil;

@Controller
@RequestMapping("frontend")
public class ShopListController {

	@Autowired
	private ShopService shopService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private ShopCategoryService shopCategoryService;

	/***
	 * 返回商品列表页中的shopCategory列表（二级或者一级），以及区域列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("listshopspageinfo")
	@ResponseBody
	public Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 试从前台获取parentId
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		if (parentId != -1) {
			// 如果存在，则取出该一级下的二级shopcategory列表
			try {
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			// 如果不存在，则取出该一级下（用户在首页选择全部商店列表）
			try {
				shopCategoryList = shopCategoryService.getShopCategoryList(null);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		// 获取区域列表信息
		try {
			List<Area> areas = areaService.getAreaList();
			modelMap.put("areas", areas);
			modelMap.put("success", true);
			return modelMap;

		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	/***
	 * 获取指定查询条件下的店铺列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("listshops")
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 页码获取
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 每页显示条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 非空判断
		if ((pageIndex > -1) && (pageSize > -1)) {
			// 获取一级类别id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			// 获取特定二级类别ID
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			// 获取区域ID
			long areaId = HttpServletRequestUtil.getLong(request, "areaId");
			// 获取查询的名字
			String shopName = HttpServletRequestUtil.getString(request, "shopName");

			Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
			// 根据条件查询
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}

	/****
	 * 组合查询 并将查询的条件传入到ShopCondition
	 * 
	 * @param parentId
	 * @param shopCategoryId
	 * @param areaId
	 * @param shopName
	 * @return
	 */
	private Shop compactShopCondition4Search(long parentId, long shopCategoryId, long areaId, String shopName) {
		Shop ShopCondition = new Shop();
		if (parentId != -1) {
			// 查询某个一级下面的所有二级ShopCategory里面的店铺列表
			ShopCategory parent = new ShopCategory();
			ShopCategory twoLevel = new ShopCategory();
			parent.setShopCategoryId(parentId);
			twoLevel.setParent(parent);
			ShopCondition.setShopCategory(twoLevel);
		}

		if (shopCategoryId != -1L) {
			// 查询某个店铺下面的ShopCategory列表
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			ShopCondition.setShopCategory(shopCategory);
		}
		if (areaId != -1L) {
			// 查询当前areaId下面的ShopCategory列表
			Area area = new Area();
			area.setAreaId(areaId);
			ShopCondition.setArea(area);
		}
		// 根据shopName模糊查询出ShopCategory列表
		if (shopName != null) {
			ShopCondition.setShopName(shopName);
		}
		// 前端展示的都是审核通过的店铺
		ShopCondition.setEnableStatus(1);
		return ShopCondition;
	}

}
