package com.shopmmt.site.services;

import java.util.List;

import com.shopmmt.common.entity.CartItem;
import com.shopmmt.site.pojo.CheckoutInfo;

public interface CheckoutService {
	
	CheckoutInfo prepareCheckout(List<CartItem> cartItems);
}
