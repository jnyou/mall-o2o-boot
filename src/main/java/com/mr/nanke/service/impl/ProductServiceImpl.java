package com.mr.nanke.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mr.nanke.dao.ProductDao;
import com.mr.nanke.dao.ProductImgDao;
import com.mr.nanke.dto.ImageHolder;
import com.mr.nanke.dto.ProductExecution;
import com.mr.nanke.entity.Product;
import com.mr.nanke.entity.ProductImg;
import com.mr.nanke.enums.ProductStateEnum;
import com.mr.nanke.exceptions.ProductOperationException;
import com.mr.nanke.service.ProductService;
import com.mr.nanke.utils.ImageUtil;
import com.mr.nanke.utils.PageCalculator;
import com.mr.nanke.utils.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductImgDao productImgDao;

	@Override
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolders)
			throws ProductOperationException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() > 0) {
			// 商品的默认属性设置
			product.setEnableStatus(1);
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 若商品的缩略图不为空
			if (imageHolder != null) {
				addProductImg(product, imageHolder);
			}
			// 添加商品信息
			try {
				int effectNum = productDao.insertProduct(product);
				if (effectNum <= 0) {
					throw new ProductOperationException("商品添加失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("商品信息添加失败：" + e.getMessage());
			}
			// 如果商品详情图不为空
			if (imageHolders != null && imageHolders.size() > 0) {
				addBatchProductImgs(product, imageHolders);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	// 保存缩略图方法
	private void addProductImg(Product product, ImageHolder imageHolder) {
		// 获取图片的全路径
		String targetPath = PathUtil.getShopImagePath(product.getShop().getShopId());
		// 调用图片处理的方法
		String thumbnail = ImageUtil.generateThumbnail(imageHolder, targetPath);
		// 设置进入到product对象中
		product.setImgAddr(thumbnail);
	}

	// 批量保存详情图方法
	private void addBatchProductImgs(Product product, List<ImageHolder> imageHolders) {
		// 获取图片的全路径
		String targetPath = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgs = new ArrayList<ProductImg>();
		for (ImageHolder holder : imageHolders) {
			String thumbnail = ImageUtil.generateNormalImg(holder, targetPath);
			ProductImg productImg = new ProductImg();
			productImg.setCreateTime(new Date());
			productImg.setImgAddr(thumbnail);
			productImg.setProductId(product.getProductId());
			productImgs.add(productImg);
		}
		if (productImgs != null && productImgs.size() > 0) {
			try {
				int batchProductImg = productImgDao.insertBatchProductImg(productImgs);
				if (batchProductImg <= 0) {
					throw new ProductOperationException("添加商品详情图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("添加商品详情图片失败" + e.getMessage());
			}
		}
	}

	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolders)
			throws ProductOperationException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() > 0) {
			// 商品的默认属性设置
			product.setLastEditTime(new Date());
			// 若商品的缩略图不为空
			if (imageHolder != null) {
				Product tempProduct = productDao.queryProductByProductId(product.getProductId());
				if (tempProduct.getImgAddr() != null) {
					ImageUtil.deletefileOrPath(tempProduct.getImgAddr());
				}
				addProductImg(product, imageHolder);
			}

			// 如果商品详情图不为空
			if (imageHolders != null && imageHolders.size() > 0) {
				deleteProductImgs(product.getProductId());
				addBatchProductImgs(product, imageHolders);
			}

			// 添加商品信息
			try {
				int effectNum = productDao.modifyProduct(product);
				if (effectNum <= 0) {
					throw new ProductOperationException("商品添加失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("商品信息添加失败：" + e.getMessage());
			}

			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	@Override
	public Product queryProductByProductId(Long productId) {
		// TODO Auto-generated method stub
		return productDao.queryProductByProductId(productId);
	}

	private void deleteProductImgs(long productId) {
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		for (ProductImg productImg : productImgList) {
			ImageUtil.deletefileOrPath(productImg.getImgAddr());
		}
		productImgDao.deleteProductImgByProductId(productId);
	}

	@Override
	public ProductExecution queryProductList(Product productCondition, int rowIndex, int pageSize) {
		int currentPageNum = PageCalculator.calculatorRowIndex(rowIndex, pageSize) ; //页数显示的条数
		List<Product> productList = productDao.queryProductList(productCondition, currentPageNum, pageSize);
		int count = productDao.queryConditionCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}
}
