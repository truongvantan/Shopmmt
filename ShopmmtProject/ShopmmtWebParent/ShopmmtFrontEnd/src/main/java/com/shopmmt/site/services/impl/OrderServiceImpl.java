package com.shopmmt.site.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.entity.Address;
import com.shopmmt.common.entity.CartItem;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.entity.Order;
import com.shopmmt.common.entity.OrderDetail;
import com.shopmmt.common.entity.OrderTrack;
import com.shopmmt.common.entity.Product;
import com.shopmmt.common.enums.OrderStatus;
import com.shopmmt.common.enums.PaymentMethod;
import com.shopmmt.common.exception.OrderNotFoundException;
import com.shopmmt.site.pojo.CheckoutInfo;
import com.shopmmt.site.pojo.OrderReturnRequest;
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

		List<OrderTrack> orderTracks = newOrder.getOrderTracks();
		OrderTrack orderTrack = null;

		if (PaymentMethod.PAYPAL.equals(paymentMethod)) {
			newOrder.setStatus(OrderStatus.PAID);

			orderTrack = new OrderTrack();
			orderTrack.setOrder(newOrder);
			orderTrack.setStatus(OrderStatus.PAID);
			orderTrack.setUpdatedTime(new Date());
			orderTrack.setNotes(OrderStatus.PAID.label);

			orderTracks.add(orderTrack);

		} else {
			newOrder.setStatus(OrderStatus.NEW);

			orderTrack = new OrderTrack();
			orderTrack.setOrder(newOrder);
			orderTrack.setStatus(OrderStatus.NEW);
			orderTrack.setUpdatedTime(new Date());
			orderTrack.setNotes(OrderStatus.NEW.label);

			orderTracks.add(orderTrack);
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

	@Override
	public Page<Order> listForCustomerByPage(Customer customer, int pageNum, String sortField, String sortDir,
			String keyword) {

		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ConstantsUtil.ORDER_PAGE_SIZE, sort);

		if (keyword != null) {
			return orderRepository.findAll(keyword, customer.getId(), pageable);
		}

		return orderRepository.findAll(customer.getId(), pageable);
	}

	@Override
	public Order getOrder(Integer id, Customer customer) {
		return orderRepository.findByIdAndCustomer(id, customer);
	}

	@Override
	public void setOrderReturnRequested(OrderReturnRequest request, Customer customer) throws OrderNotFoundException {
		Order order = orderRepository.findByIdAndCustomer(request.getOrderId(), customer);
		if (order == null) {
			throw new OrderNotFoundException("Order ID " + request.getOrderId() + " not found");
		}

		if (order.isReturnRequested())
			return;

		OrderTrack track = new OrderTrack();
		track.setOrder(order);
		track.setUpdatedTime(new Date());
		track.setStatus(OrderStatus.RETURN_REQUESTED);
		

		String notes = "Lý do: " + request.getReason();
		if (!"".equals(request.getNote())) {
			notes += ". " + request.getNote();
		}

		track.setNotes(notes);

		order.getOrderTracks().add(track);
		order.setStatus(OrderStatus.RETURN_REQUESTED);

		orderRepository.save(order);
	}
}
