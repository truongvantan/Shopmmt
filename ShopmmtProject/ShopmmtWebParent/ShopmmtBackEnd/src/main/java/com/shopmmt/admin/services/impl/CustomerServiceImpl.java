package com.shopmmt.admin.services.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopmmt.admin.repositories.CustomerRepository;
import com.shopmmt.admin.services.CustomerService;
import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.exception.CustomerNotFoundException;

import jakarta.transaction.Transactional;

@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, ConstantsUtil.CUSTOMERS_PAGE_SIZE, sort);
		
		if (keyword != null) {
			return customerRepository.findAll(keyword, pageable);
		}
		
		return customerRepository.findAll(pageable);
	}

	@Override
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepository.updateEnabledStatus(id, enabled);
	}

	@Override
	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not find any customer with ID " + id);
		}
	}

	@Override
	public boolean isEmailUnique(Integer id, String email) {
		if (email != null) {
			email = email.trim();
		}
		
		Customer existCustomer = customerRepository.findByEmail(email);

		if (existCustomer != null && existCustomer.getId() != id) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean validatePassword(String newPassword) {
		if (newPassword == null || "".equals(newPassword.trim())) {
			return true;
		} else if (!newPassword.matches(ConstantsUtil.REGEX_PASSWORD)) {
			return false;
		} else {
			return true;
		}
			
	}

	@Override
	public void save(Customer customer) {
		Customer customerInDB = customerRepository.findById(customer.getId()).get();
		
		if (!customer.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customer.getPassword());
			customer.setPassword(encodedPassword);			
		} else {
			customer.setPassword(customerInDB.getPassword());
		}
		
		customer.setEnabled(customerInDB.isEnabled());
		customer.setCreatedTime(customerInDB.getCreatedTime());
		customer.setVerificationCode(customerInDB.getVerificationCode());
		
		customerRepository.save(customer);
	}

	@Override
	public void delete(Integer id) throws CustomerNotFoundException {
		Long count = customerRepository.countById(id);
		if (count == null || count == 0) {
			throw new CustomerNotFoundException("Could not find any customer with ID " + id);
		}
		
		customerRepository.deleteById(id);
	}
	
}
