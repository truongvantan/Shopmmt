package com.shopmmt.site.services;

import java.util.List;

import com.shopmmt.common.entity.CartItem;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.site.exceptions.ShoppingCartException;

public interface ShoppingCartService {

	Integer addProduct(Integer productId, Integer quantity, Customer customer) 
			throws ShoppingCartException;
	
	List<CartItem> listCartItems(Customer customer);
	
	double updateQuantity(Integer productId, Integer quantity, Customer customer);
	
	void removeProduct(Integer productId, Customer customer);
	
	void deleteByCustomer(Customer customer);
}
