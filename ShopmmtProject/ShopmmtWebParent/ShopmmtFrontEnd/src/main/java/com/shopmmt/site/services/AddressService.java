package com.shopmmt.site.services;

import java.util.List;

import com.shopmmt.common.entity.Address;
import com.shopmmt.common.entity.Customer;

public interface AddressService {
	
	List<Address> listAddressBook(Customer customer);
	
	void save(Address address);
	
	Address get(Integer addressId, Integer customerId);
	
	void delete(Integer addressId, Integer customerId);
	
	void setDefaultAddress(Integer defaultAddressId, Integer customerId);
}
