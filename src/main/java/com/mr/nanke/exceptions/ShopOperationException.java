package com.mr.nanke.exceptions;

public class ShopOperationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3637454868519892467L;
	
	public ShopOperationException(String errMsg) {
		super(errMsg);
	}
}
