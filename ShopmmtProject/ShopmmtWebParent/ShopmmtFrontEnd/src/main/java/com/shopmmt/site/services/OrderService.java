package com.shopmmt.site.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopmmt.common.entity.Address;
import com.shopmmt.common.entity.CartItem;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.entity.Order;
import com.shopmmt.common.enums.PaymentMethod;
import com.shopmmt.common.exception.OrderNotFoundException;
import com.shopmmt.site.pojo.CheckoutInfo;
import com.shopmmt.site.pojo.OrderReturnRequest;

public interface OrderService {
	Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo);
	
	Page<Order> listForCustomerByPage(Customer customer, int pageNum, 
			String sortField, String sortDir, String keyword);
	
	Order getOrder(Integer id, Customer customer);
	
	void setOrderReturnRequested(OrderReturnRequest request, Customer customer) 
			throws OrderNotFoundException;
}
