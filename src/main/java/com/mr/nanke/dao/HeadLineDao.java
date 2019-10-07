package com.mr.nanke.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mr.nanke.entity.HeadLine;

public interface HeadLineDao {
	/****
	 * 根据传入的查询条件（头条名查询条件）
	 * @param headLine
	 * @return
	 */
	
	List<HeadLine> queryHeadLineList(@Param("headLineCondition") HeadLine headLineCondition);

}
