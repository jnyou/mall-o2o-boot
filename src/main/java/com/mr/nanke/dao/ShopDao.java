package com.mr.nanke.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mr.nanke.entity.Shop;

public interface ShopDao {
	/***
	 * 分页查询店铺 ， 可输入的条件有：店铺名（模糊） ，店铺状态，店铺类别，区域id，owner
	 * @param shopCondition  查询条件
	 * @param rowIndex		从第几条数据开始取
	 * @param pageSize		取几条
	 * @return
	 * 多个参数需要加上@Param
	 */
	
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);
	
	//根据条件查询出来数据的总条数
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
	
	/***
	 * 根据id查询商品信息
	 */
	Shop queryShopByShopId(Long id);
	
	/***
	 * 新增店铺
	 */
	int insertShop(Shop shop);
	
	/***
	 * 更新店铺信息
	 */
	int updateShop(Shop shop);
}
