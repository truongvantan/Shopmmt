package com.shopmmt.common.exception;

public class ProductNotFoundException extends Exception {
	private static final long serialVersionUID = -8677438439489687898L;
	
	public ProductNotFoundException() {
		
	}
	
	public ProductNotFoundException(String message) {
		super(message);
	}

}
