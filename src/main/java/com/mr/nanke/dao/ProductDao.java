package com.mr.nanke.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mr.nanke.entity.Product;

public interface ProductDao {
	
	int insertProduct(Product product);
	
	int modifyProduct(Product product);
	
	Product queryProductByProductId(Long productId);
	
	int updateProductCategoryToNull(long productCategoryId);
	
	/***
	 * 
	 * @param productCondition  分页条件
	 * @param rowIndex		 从第几页数据开始取
	 * @param pageSize		 每页显示的数量
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition") Product productCondition,@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);
	
	/***
	 * 查询符合条件的总数
	 * @param productCondition
	 * @return
	 */
	int queryConditionCount(@Param("productCondition") Product productCondition);
}
