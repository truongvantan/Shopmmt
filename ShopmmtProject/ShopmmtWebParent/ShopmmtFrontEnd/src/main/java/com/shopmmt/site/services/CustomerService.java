package com.shopmmt.site.services;

import com.shopmmt.common.entity.Customer;

public interface CustomerService {

	boolean isEmailUnique(String email);
	
	void registerCustomer(Customer customer);

	boolean validatePassword(String password, String confirmPassword);

}
