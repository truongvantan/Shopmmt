package com.shopmmt.site.services;

import java.util.List;

import com.shopmmt.common.entity.Address;
import com.shopmmt.common.entity.CartItem;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.entity.Order;
import com.shopmmt.common.enums.PaymentMethod;
import com.shopmmt.site.pojo.CheckoutInfo;

public interface OrderService {
	Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo);
}
