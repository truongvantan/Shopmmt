package com.shopmmt.site.config.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopmmt.common.entity.Customer;

public class CustomerUserDetails implements UserDetails {
	private static final long serialVersionUID = -2796901750989145450L;
	
	private Customer customer;

	public CustomerUserDetails() {

	}

	public CustomerUserDetails(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return customer.isEnabled();
	}
	
	public String getFullName() {
		return customer.getLastName() + " " + customer.getFirstName();
	}

	public Customer getCustomer() {
		return this.customer;
	}

}
