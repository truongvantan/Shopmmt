package com.shopmmt.site.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.entity.CartItem;
import com.shopmmt.site.pojo.CheckoutInfo;
import com.shopmmt.site.services.CheckoutService;

@Service("checkoutService")
public class CheckoutServiceImpl implements CheckoutService {
	
	@Override
	public CheckoutInfo prepareCheckout(List<CartItem> cartItems) {
		CheckoutInfo checkoutInfo = new CheckoutInfo();
		
		double productCost = calculateProductCost(cartItems);
		double productTotal = calculateProductTotal(cartItems);
		double shippingCostTotal = calculateShippingCost(cartItems);
		double paymentTotal = productTotal + shippingCostTotal;
		
		checkoutInfo.setProductCost(productCost);
		checkoutInfo.setProductTotal(productTotal);
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);
		
		checkoutInfo.setDeliverDays(ConstantsUtil.DELIVER_DAYS);
		
		return checkoutInfo;
	}
	
	private double calculateShippingCost(List<CartItem> cartItems) {
		double shippingCostTotal = 0.0D;
		
		for (CartItem item : cartItems) {
			item.setShippingCost(ConstantsUtil.SHIPPING_COST);
			
			shippingCostTotal += ConstantsUtil.SHIPPING_COST;
		}
		
		return shippingCostTotal;
	}
	
	private double calculateProductCost(List<CartItem> cartItems) {
		double cost = 0.0D;
		
		for (CartItem item : cartItems) {
			cost += item.getQuantity() * item.getProduct().getCost();
		}
		
		return cost;
	}
	
	private double calculateProductTotal(List<CartItem> cartItems) {
		double total = 0.0D;
		
		for (CartItem item : cartItems) {
			total += item.getSubtotal();
		}
		
		return total;
	}

}
