package com.mr.nanke.service;

import java.util.List;

import com.mr.nanke.dto.ImageHolder;
import com.mr.nanke.dto.ProductExecution;
import com.mr.nanke.entity.Product;
import com.mr.nanke.exceptions.ProductOperationException;

public interface ProductService {
	
	/***
	 * 
	 * @param product
	 * @param imageHolder   	缩略图
	 * @param imageHolders      详情图
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product,ImageHolder imageHolder,List<ImageHolder> imageHolders) throws ProductOperationException;
	/***
	 * 
	 * @param product
	 * @param imageHolder
	 * @param imageHolders
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product product,ImageHolder imageHolder,List<ImageHolder> imageHolders) throws ProductOperationException;


	Product queryProductByProductId(Long productId);
	/***
	 * 分页   条件：可根据商品名，状态，店铺id， 商品类别id
	 * @param productCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution queryProductList(Product productCondition, int rowIndex,int pageSize);
	
}