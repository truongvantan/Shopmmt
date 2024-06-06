package com.shopmmt.site.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopmmt.common.entity.Customer;
import com.shopmmt.site.repositories.CustomerRepository;
import com.shopmmt.site.services.CustomerService;

import net.bytebuddy.utility.RandomString;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepository.findByEmail(email);
		
		return customer == null;
	}

	@Override
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		
		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);
		
		customerRepository.save(customer);
	}
	
	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}

	@Override
	public boolean validatePassword(String password, String confirmPassword) {
		if (password == null || confirmPassword == null) {
			return false;
		}
		
		if ((password != null && confirmPassword != null) && (!password.equals(confirmPassword))) {
			return false;
		}
		
		return true;
	}
	
}
