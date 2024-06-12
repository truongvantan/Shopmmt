package com.shopmmt.common.exception;

public class OrderNotFoundException extends Exception {

	private static final long serialVersionUID = 3517907315214819581L;

	public OrderNotFoundException() {
		super();
	}
	
	public OrderNotFoundException(String message) {
		super(message);
	}

}
