package com.mr.nanke.web.shopadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mr.nanke.dto.ImageHolder;
import com.mr.nanke.dto.ShopExecution;
import com.mr.nanke.entity.Area;
import com.mr.nanke.entity.PersonInfo;
import com.mr.nanke.entity.Shop;
import com.mr.nanke.entity.ShopCategory;
import com.mr.nanke.enums.ShopStateEnum;
import com.mr.nanke.exceptions.ShopOperationException;
import com.mr.nanke.service.AreaService;
import com.mr.nanke.service.ShopCategoryService;
import com.mr.nanke.service.ShopService;
import com.mr.nanke.utils.CodeUtil;
import com.mr.nanke.utils.HttpServletRequestUtil;

@Controller
@RequestMapping("shopadmin")
public class ShopManagerController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;

	
	/*
	 * 判断是否登录 
	 */
	@RequestMapping("getmanagerinfo")
	@ResponseBody
	public Map<String, Object> getManagerInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获得id进行判断
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId <= 0) {
			//id没有 则从session中获取
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			//session中没有 则返回到列表页面
			if(currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
	
	
	
	/*
	 * 根据用户登录后能看到自己的信息及店铺信息 
	 */
	@RequestMapping("getshoplist")
	@ResponseBody
	public Map<String, Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
//		PersonInfo personInfo = new PersonInfo();
//		personInfo.setUserId(9L);
//		personInfo.setName("test");
//		request.getSession().setAttribute("user", personInfo);
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution shopList = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", shopList);     // 用户店铺
			//将店铺返给session中供权限判断
			request.getSession().setAttribute("shopList", shopList.getShopList());
			modelMap.put("user",user);  //用户个人信息
			modelMap.put("success", true);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping("getInfoById")
	@ResponseBody
	public Map<String, Object> getInfoById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1) {
			try {
				List<Area> areaList = areaService.getAreaList();
				Shop shop = shopService.queryShopById(shopId);
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}

	@RequestMapping("getshopinitinfo")
	@ResponseBody
	public Map<String, Object> getShopInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;

	}

	/***
	 * 注册
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("null")
	@RequestMapping("registershop")
	@ResponseBody
	public Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入验证码错误");
			return modelMap;
		}
		// 1.接受并转化相应的参数 ， 包括店铺信息和图片信息
		// 获取前台的参数
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		MultipartHttpServletRequest multipartRequest = null;
		// 处理图片
		CommonsMultipartFile shopImg = null;
		// 文件解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		// 2.注册店铺
		if (shop != null && shopImg != null) {
			try {
				PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
				shop.setOwner(user);
				// File shopImgFile = new File(PathUtil.getImgBasePath() +
				// ImageUtil.getRandomFileName());
				// try {
				// shopImgFile.createNewFile();
				// inputStreamToFile(shopImg.getInputStream(),shopImgFile);
				// } catch (IOException e) {
				// modelMap.put("success", false);
				// modelMap.put("errMsg", e.getMessage());
				// return modelMap;
				// }
				ShopExecution se;
				try {
					ImageHolder imageHolder = new ImageHolder();
					se = shopService.addShop(shop, imageHolder);
					if (se.getState() == ShopStateEnum.CHECK.getState()) {
						modelMap.put("success", true);
						// 若shop创建成功，则加入session中，作为权限使用
						@SuppressWarnings("unchecked")
						List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
						if (shopList == null && shopList.size() == 0) {
							shopList = new ArrayList<Shop>();
						}
						shopList.add(se.getShop());
						request.getSession().setAttribute("shopList", shopList);
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", se.getStateInfo());
					}
				} catch (ShopOperationException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.getMessage());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
		}
		// 3.返回结果
		return modelMap;
	}

	// 修改
	@RequestMapping("modifyshop")
	@ResponseBody
	public Map<String, Object> modifyshop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入验证码错误");
			return modelMap;
		}
		// 1.接受并转化相应的参数 ， 包括店铺信息和图片信息
		// 获取前台的参数
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		MultipartHttpServletRequest multipartRequest = null;
		// 处理图片
		CommonsMultipartFile shopImg = null;
		// 文件解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartRequest.getFile("shopImg");
		}
		// 2.修改店铺
		if (shop != null && shop.getShopId() != null) {
			try {
				ShopExecution se;
				ImageHolder imageHolder = new ImageHolder();
				if (shopImg == null) {
					se = shopService.updateShop(shop, null);
				} else {
					se = shopService.updateShop(shop, imageHolder);
				}
				if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺id");
			// 3.返回结果
			return modelMap;
		}
	}

	// 流转换 CommonsMultipartFile转换为File类型 使用了InputStream
	// private static void inputStreamToFile(InputStream ins, File file) {
	// FileOutputStream os = null;
	// try {
	// os = new FileOutputStream(file);
	// int bytesRead = 0; //定义读的数量
	// byte[] buffer = new byte[1024]; //一次读1024
	// if((bytesRead = ins.read(buffer)) != -1 ) {
	// os.write(buffer,0,bytesRead);
	// }
	//
	// } catch (Exception e) {
	// throw new RuntimeException("调用inputStreamToFile异常" + e.getMessage());
	// }finally {
	// try {
	// if(os != null) {
	// os.close();
	// }
	// if(ins != null) {
	// ins.close();
	// }
	// } catch (IOException e) {
	// throw new RuntimeException("inputStreamToFile关闭IO产生异常" + e.getMessage());
	// }
	// }
	// }
}
