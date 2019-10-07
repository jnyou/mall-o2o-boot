package com.mr.nanke.service;

import java.io.IOException;
import java.util.List;


import com.mr.nanke.entity.HeadLine;


public interface HeadLineService {	
	//redis中的KEY
	public static final String HLLISTKEY = "headlinelist";
	/***
	 * 
	 * @param headLineCondition
	 * @return
	 * @throws IOException
	 */
	List<HeadLine> getHeadLine(HeadLine headLineCondition) throws IOException;
}
