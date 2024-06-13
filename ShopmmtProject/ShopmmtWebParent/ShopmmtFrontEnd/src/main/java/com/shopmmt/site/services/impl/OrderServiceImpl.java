package com.shopmmt.site.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopmmt.common.entity.Address;
import com.shopmmt.common.entity.CartItem;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.entity.Order;
import com.shopmmt.common.entity.OrderDetail;
import com.shopmmt.common.entity.Product;
import com.shopmmt.common.enums.OrderStatus;
import com.shopmmt.common.enums.PaymentMethod;
import com.shopmmt.site.pojo.CheckoutInfo;
import com.shopmmt.site.repositories.OrderRepository;
import com.shopmmt.site.services.OrderService;

import jakarta.transaction.Transactional;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Order createOrder(Customer customer, Address address, List<CartItem> cartItems, PaymentMethod paymentMethod,
			CheckoutInfo checkoutInfo) {

		Order newOrder = new Order();
		newOrder.setOrderTime(new Date());
		
		if (PaymentMethod.PAYPAL.equals(paymentMethod)) {
			newOrder.setStatus(OrderStatus.PAID);
		} else {
			newOrder.setStatus(OrderStatus.NEW);
		}
		
		newOrder.setCustomer(customer);
		newOrder.setProductCost(checkoutInfo.getProductCost());
		newOrder.setSubtotal(checkoutInfo.getProductTotal());
		newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
		newOrder.setTax(0.0D);
		newOrder.setTotal(checkoutInfo.getPaymentTotal());
		newOrder.setPaymentMethod(paymentMethod);
		newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
		newOrder.setDeliverDate(checkoutInfo.getDeliverDate());

		if (address == null) {
			newOrder.copyAddressFromCustomer();
		} else {
			newOrder.copyShippingAddress(address);
		}

		Set<OrderDetail> orderDetails = newOrder.getOrderDetails();

		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();

			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrder(newOrder);
			orderDetail.setProduct(product);
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setUnitPrice(product.getDiscountPrice());
			orderDetail.setProductCost(product.getCost() * cartItem.getQuantity());
			orderDetail.setSubtotal(cartItem.getSubtotal());
			orderDetail.setShippingCost(cartItem.getShippingCost());

			orderDetails.add(orderDetail);
		}

		return orderRepository.save(newOrder);
	}
}
