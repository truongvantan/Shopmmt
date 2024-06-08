package com.shopmmt.site.services;

import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.enums.AuthenticationType;

public interface CustomerService {

	boolean isEmailUnique(String email);
	
	void registerCustomer(Customer customer);

	boolean validatePassword(String password, String confirmPassword);

	boolean verify(String code);
	
	void updateAuthenticationType(Customer customer, AuthenticationType type);

	Customer getCustomerByEmail(String email);

	void addNewCustomerUponOAuthLogin(String name, String email, AuthenticationType authenticationType);

	void update(Customer customer);

	String checkValidPassword(String newPassword, String confirmPassword);

}
