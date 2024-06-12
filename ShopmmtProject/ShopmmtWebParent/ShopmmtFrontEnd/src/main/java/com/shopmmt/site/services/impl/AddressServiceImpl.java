package com.shopmmt.site.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopmmt.common.entity.Address;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.site.repositories.AddressRepository;
import com.shopmmt.site.services.AddressService;

import jakarta.transaction.Transactional;

@Service("addressService")
@Transactional
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public List<Address> listAddressBook(Customer customer) {
		return addressRepository.findByCustomer(customer);
	}

	@Override
	public void save(Address address) {
		addressRepository.save(address);
	}

	@Override
	public Address get(Integer addressId, Integer customerId) {
		return addressRepository.findByIdAndCustomer(addressId, customerId);
	}

	@Override
	public void delete(Integer addressId, Integer customerId) {
		addressRepository.deleteByIdAndCustomer(addressId, customerId);
	}

	@Override
	public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
		if (defaultAddressId > 0) {
			addressRepository.setDefaultAddress(defaultAddressId);
		}
		
		addressRepository.setNonDefaultForOthers(defaultAddressId, customerId);
	}

	@Override
	public Address getDefaultAddress(Customer customer) {
		return addressRepository.findDefaultByCustomer(customer.getId());
	}
}
