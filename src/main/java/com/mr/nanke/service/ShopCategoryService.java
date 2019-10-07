package com.mr.nanke.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mr.nanke.entity.ShopCategory;

public interface ShopCategoryService {
	//redis中的KEY
	public static final String SCLISTKEY = "shopcategorylist";
	/**
	 * 
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);

	/**
	 * 
	 * @param area
	 * @return
	 */
	//AreaExecution addArea(Area area);

	/**
	 * 
	 * @param area
	 * @return
	 */
	//AreaExecution modifyArea(Area area);

	/**
	 * 
	 * @param areaId
	 * @return
	 */
	//AreaExecution removeArea(long areaId);

	/**
	 * 
	 * @param areaIdList
	 * @return
	 */
	//AreaExecution removeAreaList(List<Long> areaIdList);

}
