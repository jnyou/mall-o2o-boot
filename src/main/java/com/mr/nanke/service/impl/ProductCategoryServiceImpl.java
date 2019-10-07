package com.mr.nanke.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mr.nanke.dao.ProductCategoryDao;
import com.mr.nanke.dao.ProductDao;
import com.mr.nanke.dto.ProductCategoryExecution;
import com.mr.nanke.entity.ProductCategory;
import com.mr.nanke.enums.ProductCategoryStateEnum;
import com.mr.nanke.exceptions.ProductCategoryOperationException;
import com.mr.nanke.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Autowired
	private ProductDao productDao;

	@Override
	public List<ProductCategory> getProductCategoryList(Long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategories)
			throws ProductCategoryOperationException {
		if (productCategories != null && productCategories.size() > 0) {
			try {
				int effectedNum = productCategoryDao.batchInsertProductCategory(productCategories);
				if (effectedNum < 0) {
					throw new ProductCategoryOperationException("店铺类别创建失败！");
				} else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new ProductCategoryOperationException("batchAddCategory error:" + e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}

	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// TODO 将此类别下的商品的类别id置为空，再删除该商品类别
		try {
			int effectNum = productDao.updateProductCategoryToNull(productCategoryId);
			if(effectNum <= 0) {
				throw new ProductCategoryOperationException("商品类别更新失败");
			}
		}catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory err" + e.getMessage());
		}
		try {
			int effectNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectNum <= 0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory err" + e.getMessage());
		}
	}

}
