package com.shopmmt.common.exception;

public class CustomerNotFoundException extends Exception {
	private static final long serialVersionUID = -201962312600002311L;

	public CustomerNotFoundException() {

	}

	public CustomerNotFoundException(String message) {
		super(message);
	}

}
