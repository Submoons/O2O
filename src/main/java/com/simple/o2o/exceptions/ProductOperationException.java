package com.simple.o2o.exceptions;

public class ProductOperationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1769901536171768830L;

	public ProductOperationException(String msg){
		super(msg);
	}
}
