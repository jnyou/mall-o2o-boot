package com.mr.nanke.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mr.nanke.dto.ImageHolder;
import com.mr.nanke.dto.ProductExecution;
import com.mr.nanke.entity.Product;
import com.mr.nanke.entity.ProductCategory;
import com.mr.nanke.entity.Shop;
import com.mr.nanke.enums.ProductStateEnum;
import com.mr.nanke.exceptions.ProductOperationException;
import com.mr.nanke.service.ProductCategoryService;
import com.mr.nanke.service.ProductService;
import com.mr.nanke.utils.CodeUtil;
import com.mr.nanke.utils.HttpServletRequestUtil;

@Controller
@RequestMapping("shopadmin")
public class ProductManagerController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	public static final int MAX_IMAGE_NUM = 6;
	
	@RequestMapping("getproductlistbypage")
	@ResponseBody
	public Map<String , Object> getProductListByPage(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取前台传过来的页码 
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取前台每页需要返回的商品信息数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//从当前session中获取shopId 
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		if(pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
			String productName = HttpServletRequestUtil.getString(request, "productName");
			
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			Product productCondition = compactProductCondition(currentShop.getShopId(), productCategoryId,productName);
			ProductExecution productList = productService.queryProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", productList);
			modelMap.put("count", productList.getCount());
			modelMap.put("success", true);
			
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId or pageSize or pageIndex");
		}
		
		return modelMap;
		
	}
	
	/****
	 * 
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		//若有自定条件。则加入进去
		if(productName != null) {
			productCondition.setProductName(productName);
		}
		if(productCategoryId != -1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		return productCondition;
	}


	@RequestMapping("getproductbyid")
	@ResponseBody
	private Map<String, Object> getProductById(@RequestParam Long productId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productId > -1) {
			Product product = productService.queryProductByProductId(productId);
			List<ProductCategory> productCategoryList = productCategoryService
					.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
	
	@RequestMapping("modifyproduct")
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//是商品编辑时调用还是商品上下架d的时候调用
		//若为前者，则会判断验证码，后者则跳过验证码判断
		boolean statusChange = HttpServletRequestUtil.getBoolean(request,
				"statusChange");
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//接受前端传过来的的参数的变量的初始化，包括商品，缩略图，详情图
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if(multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request,thumbnail,productImgList);
			}
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			String productStr = HttpServletRequestUtil.getString(request,"productStr");
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (product != null) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute(
						"currentShop");
				product.setShop(currentShop);
				ProductExecution pe = productService.modifyProduct(product,thumbnail, productImgList);
				if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;
	}
	
	@RequestMapping("addproduct")
	@ResponseBody
	public Map<String,Object> addProduct(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//校验验证码
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errMsg" ,"验证码错误");
			return modelMap;
		}
		//接受前端传过来的的参数的变量的初始化，包括商品，缩略图，详情图
		ObjectMapper objectMapper = new ObjectMapper();
		Product product = null;
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		MultipartHttpServletRequest multipartRequest = null;
		//缩略图
		ImageHolder imageHolder = null;
		//详情图
		List<ImageHolder> imageHolders = new ArrayList<ImageHolder>();
		//图片解析
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		try {
			//若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
			if(multipartResolver.isMultipart(request)) {
				multipartRequest = (MultipartHttpServletRequest) request;
				//取出缩略图并构建ImageHolder对象
				CommonsMultipartFile multipartFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
				imageHolder = new ImageHolder(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
				//取出详情图片并构建List<ImageHolder>对象 ，最多支持上传六张图片 MAX_IMAGE_NUM
				for (int i = 0; i < MAX_IMAGE_NUM; i++) {
					CommonsMultipartFile productImg = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
					if(productImg != null) {
						//若取出的第i个详情图文件流不为空，则加入详情图列表中
						imageHolder = new ImageHolder(productImg.getInputStream(), productImg.getOriginalFilename());
						imageHolders.add(imageHolder);
					}else {
						//取出的第i个详情图文件流为空，则终止
						break;
					}
				}
				
			}else {
				modelMap.put("success",false);
				modelMap.put("errMsg" ,"上传图片不能为空");
				return modelMap;
			}
		}catch (Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg" ,e.getMessage());
			return modelMap;
		}
		
		//获取前端的表单信息封装实体类
		try {
			product = objectMapper.readValue(productStr, Product.class);
		}catch (Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg" ,e.getMessage());
			return modelMap;
		}
		
		if(product != null && imageHolder != null && imageHolders.size() > 0) {
			try {
				//从session中获取shopId赋值给product中的shop_id，减少对前台数据的依赖
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				ProductExecution pe = productService.addProduct(product, imageHolder, imageHolders);
				if(pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success",true);
				}else {
					modelMap.put("success",false);
					modelMap.put("errMsg" ,pe.getStateInfo());
				}
			}catch (ProductOperationException e) {
				modelMap.put("success",false);
				modelMap.put("errMsg" ,e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg" ,"请输入商品信息");
		}
		
		return modelMap;
		
	}
	
	private ImageHolder handleImage(HttpServletRequest request,ImageHolder thumbnail,List<ImageHolder> imageHolders) throws IOException{
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		//取出缩略图并构建ImageHolder对象
		CommonsMultipartFile multipartFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		if(multipartFile!= null) {
			thumbnail = new ImageHolder(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
		}
		//取出详情图片并构建List<ImageHolder>对象 ，最多支持上传六张图片 MAX_IMAGE_NUM
		for (int i = 0; i < MAX_IMAGE_NUM; i++) {
			CommonsMultipartFile productImg = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
			if(productImg != null) {
				//若取出的第i个详情图文件流不为空，则加入详情图列表中
				thumbnail = new ImageHolder(productImg.getInputStream(), productImg.getOriginalFilename());
				imageHolders.add(thumbnail);
			}else {
				//取出的第i个详情图文件流为空，则终止
				break;
			}
		}
		return thumbnail;
		
	}
}
