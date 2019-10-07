package com.mr.nanke.exceptions;

public class ProductOperationException extends RuntimeException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7806296358025755783L;

	public ProductOperationException(String errMsg) {
		super(errMsg);
	}
}
