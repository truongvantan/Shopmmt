package com.shopmmt.admin.services;

import org.springframework.data.domain.Page;

import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.exception.CustomerNotFoundException;

public interface CustomerService {

	Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword);

	void updateCustomerEnabledStatus(Integer id, boolean enabled);

	Customer get(Integer id) throws CustomerNotFoundException;

	boolean isEmailUnique(Integer id, String email);

	boolean validatePassword(String newPassword);

	void save(Customer customer);

	void delete(Integer id) throws CustomerNotFoundException;

}
