package com.mr.nanke.utils;

public class PageCalculator {

	/***
	 * 将页数转成每页显示的多条之后的条数
	 * @param PageIndex
	 * @param PageSize
	 */
	public static int calculatorRowIndex(int PageIndex, int PageSize) {
		
		return (PageIndex > 0) ? (PageIndex - 1) * PageSize : 0;
	}
}
