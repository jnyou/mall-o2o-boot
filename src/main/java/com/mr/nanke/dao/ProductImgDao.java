package com.mr.nanke.dao;

import java.util.List;

import com.mr.nanke.entity.ProductImg;

public interface ProductImgDao {
	
	/***
	 * 批量添加商品图片
	 * @param productImgs
	 * @return
	 */
	int insertBatchProductImg(List<ProductImg> productImgs);
	
	/***
	 * 查询所有
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long productId);

	int deleteProductImgByProductId(long productId);
}
