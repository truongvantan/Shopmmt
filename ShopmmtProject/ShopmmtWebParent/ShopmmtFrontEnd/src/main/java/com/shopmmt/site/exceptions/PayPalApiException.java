package com.shopmmt.site.exceptions;

public class PayPalApiException extends Exception {

	private static final long serialVersionUID = 1980196313473886565L;

	public PayPalApiException() {
		super();
	}
	
	public PayPalApiException(String message) {
		super(message);
	}

}
