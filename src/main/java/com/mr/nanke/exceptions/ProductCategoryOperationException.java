package com.mr.nanke.exceptions;

public class ProductCategoryOperationException extends RuntimeException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7806296358025755783L;

	public ProductCategoryOperationException(String errMsg) {
		super(errMsg);
	}
}
