package com.mr.nanke.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mr.nanke.entity.ShopCategory;

public interface ShopCategoryDao {
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition")ShopCategory shopCategoryCondition);
}
