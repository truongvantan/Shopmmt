package com.shopmmt.common.exception;

public class CategoryNotFoundException extends Exception {
	private static final long serialVersionUID = -7921353325514504573L;
	
	public CategoryNotFoundException() {
		
	}
	
	public CategoryNotFoundException(String message) {
		super(message);
	}
}
