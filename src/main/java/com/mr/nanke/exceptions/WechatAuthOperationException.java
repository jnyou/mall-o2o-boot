package com.mr.nanke.exceptions;

public class WechatAuthOperationException extends RuntimeException{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4653346329986816424L;

	public WechatAuthOperationException(String errMsg) {
		super(errMsg);
	}
}
