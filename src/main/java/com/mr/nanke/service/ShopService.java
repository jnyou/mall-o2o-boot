package com.mr.nanke.service;

import com.mr.nanke.dto.ImageHolder;
import com.mr.nanke.dto.ShopExecution;
import com.mr.nanke.entity.Shop;
import com.mr.nanke.exceptions.ShopOperationException;

public interface ShopService {
	ShopExecution getShopList(Shop shopCondition,int PageIndex , int PageSize);
	
	ShopExecution addShop(Shop shop,ImageHolder imageHolder) throws ShopOperationException;
	
	ShopExecution updateShop(Shop shop , ImageHolder imageHolder) throws ShopOperationException;
	
	Shop queryShopById(Long id);
}
