package com.shopmmt.site.services;

import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.enums.AuthenticationType;
import com.shopmmt.common.exception.CustomerNotFoundException;

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

	String updateResetPasswordToken(String email) throws CustomerNotFoundException;
	
	Customer getByResetPasswordToken(String token);

	String checkValidResetPassword(String newPassword, String confirmPassword);

	void updatePassword(String token, String newPassword) throws CustomerNotFoundException;

}
