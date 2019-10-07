package com.mr.nanke.service;

import java.util.List;

import com.mr.nanke.dto.ProductCategoryExecution;
import com.mr.nanke.entity.ProductCategory;
import com.mr.nanke.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {

	List<ProductCategory> getProductCategoryList(Long shopId);
	
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategories) throws ProductCategoryOperationException;
	
	/***
	 * 将此类别下的商品的类别id置为空，再删除该商品类别
	 * @param ProductCategory
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId)throws ProductCategoryOperationException;
}
