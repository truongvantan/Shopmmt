package com.shopmmt.site.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.enums.AuthenticationType;
import com.shopmmt.common.exception.CustomerNotFoundException;
import com.shopmmt.site.repositories.CustomerRepository;
import com.shopmmt.site.services.CustomerService;

import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;

@Service("customerService")
@Transactional
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
		customer.setAuthenticationType(AuthenticationType.DATABASE);

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

	@Override
	public boolean verify(String verificationCode) {
		Customer customer = customerRepository.findByVerificationCode(verificationCode);

		if (customer == null || customer.isEnabled()) {
			return false;
		} else {
			customerRepository.enable(customer.getId());
			return true;
		}
	}

	@Override
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if (!type.equals(customer.getAuthenticationType())) {
			customerRepository.updateAuthenticationType(customer.getId(), type);
		}
	}

	@Override
	public Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
	}

	@Override
	public void addNewCustomerUponOAuthLogin(String name, String email, AuthenticationType authenticationType) {
		Customer customer = new Customer();
		customer.setEmail(email);
		setName(name, customer);

		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setCity("");
		customer.setState("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");

		customerRepository.save(customer);
	}

	private void setName(String name, Customer customer) {
		String[] nameArray = name.split(" ");
		if (nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		} else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);

			String lastName = name.replaceFirst(firstName, "");
			customer.setLastName(lastName);
		}
	}

	@Override
	public void update(Customer customerInForm) {
		Customer customerInDB = customerRepository.findById(customerInForm.getId()).get();

		if (AuthenticationType.DATABASE.equals(customerInDB.getAuthenticationType())) {
			if (!customerInForm.getPassword().isEmpty()) {
				String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
				customerInForm.setPassword(encodedPassword);
			} else {
				customerInForm.setPassword(customerInDB.getPassword());
			}
		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}

		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());

		customerRepository.save(customerInForm);
	}

	@Override
	public String checkValidPassword(String newPassword, String confirmPassword) {
		if (("".equals(newPassword) || newPassword == null)
				&& ("".equals(confirmPassword) || confirmPassword == null)) {
			return "Valid new password";
		} else if (!newPassword.matches(ConstantsUtil.REGEX_PASSWORD)) {
			return "Invalid new password";
		} else if (!newPassword.equals(confirmPassword)) {
			return "Confirm password does not match";
		} else {
			return "Valid new password";
		}
	}

	@Override
	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByEmail(email);
		
		if (customer != null) {
			String token = RandomString.make(30);
			customer.setResetPasswordToken(token);
			customerRepository.save(customer);
			
			return token;
		} else {
			throw new CustomerNotFoundException("Could not find any customer with the email " + email);
		}
		
	}

	@Override
	public Customer getByResetPasswordToken(String token) {
		return customerRepository.findByResetPasswordToken(token);
	}

	@Override
	public String checkValidResetPassword(String newPassword, String confirmPassword) {
		if (newPassword == null || !newPassword.matches(ConstantsUtil.REGEX_PASSWORD)) {
			return "Invalid new password";
		} else if (!newPassword.equals(confirmPassword)) {
			return "Confirm password does not match";
		} else {
			return "Valid new password";
		}
	}

	@Override
	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByResetPasswordToken(token);
		
		if (customer == null) {
			throw new CustomerNotFoundException("No customer found: invalid token");
		}
		
		customer.setPassword(newPassword);
		customer.setResetPasswordToken(null);
		encodePassword(customer);
		
		customerRepository.save(customer);
	}
	
	
}
