package com.mr.nanke.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mr.nanke.dao.ShopDao;
import com.mr.nanke.dto.ImageHolder;
import com.mr.nanke.dto.ShopExecution;
import com.mr.nanke.entity.Shop;
import com.mr.nanke.enums.ShopStateEnum;
import com.mr.nanke.exceptions.ShopOperationException;
import com.mr.nanke.service.ShopService;
import com.mr.nanke.utils.ImageUtil;
import com.mr.nanke.utils.PageCalculator;
import com.mr.nanke.utils.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopDao shopDao;

	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder imageHolder) {
		// 空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		}
		try {
			// 给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			int effectNum = shopDao.insertShop(shop);
			if (effectNum <= 0) {
				throw new ShopOperationException("店铺创建失败...");
			} else {
				if (imageHolder.getThumbnailInputStream() != null) {
					// 将图片添加存入数据库
					try {
						addShopImg(shop, imageHolder);
					} catch (Exception e) {
						throw new ShopOperationException("添加图片失败 ：" + e.getMessage());
					}
					// 更新图片到数据库
					effectNum = shopDao.updateShop(shop);
					if (effectNum <= 0) {
						throw new ShopOperationException("更新图片失败 。。。");
					}

				}

			}
			// assertEquals(1, effectNum);
		} catch (Exception e) {
			throw new ShopOperationException("errMsg addShop.. " + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
		// TODO Auto-generated method stub
	}

	// 添加图片的方法
	private void addShopImg(Shop shop, ImageHolder imageHolder) {
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(imageHolder,dest);
		shop.setShopImg(shopImgAddr);

	}

	@Override
	public ShopExecution updateShop(Shop shop, ImageHolder imageHolder)
			throws ShopOperationException {
		// 非空判断
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		} else {
			try {
				// 1.判断是否需要处理图片
				if (imageHolder.getThumbnailInputStream() != null && imageHolder.getFileName() != null && !"".equals(imageHolder.getFileName())) {
					Shop shopId = shopDao.queryShopByShopId(shop.getShopId()); // 获得之前的shop图片
					if (shopId.getShopImg() != null) {
						ImageUtil.deletefileOrPath(shopId.getShopImg()); // 删除图片
					}

					addShopImg(shop, imageHolder);

				}
				// 2.对数据进行更新操作
				shop.setLastEditTime(new Date());
				int effectNum = shopDao.updateShop(shop);
				if (effectNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {
					shop = shopDao.queryShopByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new ShopOperationException("error");
			}
		}
	}

	@Override
	public Shop queryShopById(Long id) {
		return shopDao.queryShopByShopId(id);
	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int shopCount = shopDao.queryShopCount(shopCondition);
		ShopExecution shopExecution = new ShopExecution();
		if(shopList != null) {
			shopExecution.setCount(shopCount);
			shopExecution.setShopList(shopList);
		}else {
			shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return shopExecution;
	}

}
