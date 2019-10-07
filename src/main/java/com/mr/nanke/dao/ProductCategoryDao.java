package com.mr.nanke.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mr.nanke.entity.ProductCategory;

public interface ProductCategoryDao {

	/***
	 * 通过shopid查询店铺商品类别
	 * @param shopid
	 * @return
	 */
	List<ProductCategory> queryProductCategoryList(long shopid);
	
	/***
	 * 批量新增商品类别	返回被影响的条数
	 * @param productCategories
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategories);
	
	/***
	 * 根据商品类目的id及shopId删除商品类目列表
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId,@Param("shopId") long shopId);
	
}
