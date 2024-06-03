package com.shopmmt.admin.exception;

public class ProductDetailDuplicateException extends Exception {
	private static final long serialVersionUID = 231496924428877760L;
	
	public ProductDetailDuplicateException() {
		
	}
	
	public ProductDetailDuplicateException(String message) {
		super(message);
	}

}
