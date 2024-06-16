package com.shopmmt.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopmmt.admin.repositories.OrderRepository;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.entity.Order;
import com.shopmmt.common.entity.OrderDetail;
import com.shopmmt.common.entity.OrderTrack;
import com.shopmmt.common.entity.Product;
import com.shopmmt.common.enums.OrderStatus;
import com.shopmmt.common.enums.PaymentMethod;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTests {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewOrderWithSingleProduct() {
		Customer customer = entityManager.find(Customer.class, 14);
		Product product = entityManager.find(Product.class, 50);

		Order mainOrder = new Order();
		mainOrder.setOrderTime(new Date());
		mainOrder.setCustomer(customer);
		mainOrder.copyAddressFromCustomer();

		mainOrder.setShippingCost(10000);
		mainOrder.setProductCost(product.getCost());
		mainOrder.setTax(0);
		mainOrder.setSubtotal(product.getPrice());
		mainOrder.setTotal(product.getPrice() + 10000);

		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setStatus(OrderStatus.NEW);
		mainOrder.setDeliverDate(new Date());
		mainOrder.setDeliverDays(1);

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(mainOrder);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(10000);
		orderDetail.setQuantity(1);
		orderDetail.setSubtotal(product.getPrice());
		orderDetail.setUnitPrice(product.getPrice());

		mainOrder.getOrderDetails().add(orderDetail);

		Order savedOrder = orderRepository.save(mainOrder);

		assertThat(savedOrder.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewOrderWithMultipleProducts() {
		Customer customer = entityManager.find(Customer.class, 14);
		Product product1 = entityManager.find(Product.class, 23);
		Product product2 = entityManager.find(Product.class, 32);
		
		Order mainOrder = new Order();
		mainOrder.setOrderTime(new Date());
		mainOrder.setCustomer(customer);
		mainOrder.copyAddressFromCustomer();
		
		OrderDetail orderDetail1 = new OrderDetail();
		orderDetail1.setProduct(product1);
		orderDetail1.setOrder(mainOrder);
		orderDetail1.setProductCost(product1.getCost());
		orderDetail1.setShippingCost(10000);
		orderDetail1.setQuantity(1);
		orderDetail1.setSubtotal(product1.getPrice());
		orderDetail1.setUnitPrice(product1.getPrice());
		
		OrderDetail orderDetail2 = new OrderDetail();
		orderDetail2.setProduct(product2);
		orderDetail2.setOrder(mainOrder);
		orderDetail2.setProductCost(product2.getCost());
		orderDetail2.setShippingCost(20000);
		orderDetail2.setQuantity(2);
		orderDetail2.setSubtotal(product2.getPrice() * 2);
		orderDetail2.setUnitPrice(product2.getPrice());
		
		mainOrder.getOrderDetails().add(orderDetail1);
		mainOrder.getOrderDetails().add(orderDetail2);
		
		mainOrder.setShippingCost(30000);
		mainOrder.setProductCost(product1.getCost() + product2.getCost());
		mainOrder.setTax(0);
		double subtotal = product1.getPrice() + product2.getPrice() * 2;
		mainOrder.setSubtotal(subtotal);
		mainOrder.setTotal(subtotal + 30000);
		
		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setStatus(OrderStatus.DELIVERED);
		mainOrder.setDeliverDate(new Date());
		mainOrder.setDeliverDays(2);
		
		Order savedOrder = orderRepository.save(mainOrder);		
		assertThat(savedOrder.getId()).isGreaterThan(0);		
	}
	
	@Test
	public void testListOrders() {
		Iterable<Order> orders = orderRepository.findAll();
		
		assertThat(orders).hasSizeGreaterThan(0);
		
		orders.forEach(System.out::println);
	}
	
	@Test
	public void testUpdateOrder() {
		Integer orderId = 2;
		Order order = orderRepository.findById(orderId).get();
		
		order.setStatus(OrderStatus.SHIPPING);
		order.setPaymentMethod(PaymentMethod.COD);
		order.setOrderTime(new Date());
		order.setDeliverDays(2);
		
		Order updatedOrder = orderRepository.save(order);
		
		assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.SHIPPING);
	}
	
	@Test
	public void testGetOrder() {
		Integer orderId = 2;
		Order order = orderRepository.findById(orderId).get();
		
		assertThat(order).isNotNull();
		System.out.println(order);
	}
	

	@Test
	public void testDeleteOrder() {
		Integer orderId = 2;
		orderRepository.deleteById(orderId);
		
		Optional<Order> result = orderRepository.findById(orderId);
		assertThat(result).isNotPresent();
	}
	
	@Test
	public void testUpdateOrderTracks() {
		Integer orderId = 15;
		Order order = orderRepository.findById(orderId).get();
		
		OrderTrack newTrack = new OrderTrack();
		newTrack.setOrder(order);
		newTrack.setUpdatedTime(new Date());
		newTrack.setStatus(OrderStatus.NEW);
		newTrack.setNotes(OrderStatus.NEW.label);

		OrderTrack processingTrack = new OrderTrack();
		processingTrack.setOrder(order);
		processingTrack.setUpdatedTime(new Date());
		processingTrack.setStatus(OrderStatus.PROCESSING);
		processingTrack.setNotes(OrderStatus.PROCESSING.label);
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		orderTracks.add(newTrack);
		orderTracks.add(processingTrack);
		
		Order updatedOrder = orderRepository.save(order);
		
		assertThat(updatedOrder.getOrderTracks()).hasSizeGreaterThan(1);
	}
	
	@Test
	public void testFindByOrderTimeBetween() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormatter.parse("2024-06-01");
		Date endTime = dateFormatter.parse("2024-06-30");
		
		List<Order> listOrders = orderRepository.findByOrderTimeBetween(startTime, endTime);
		
		assertThat(listOrders.size()).isGreaterThan(0);
		
		for (Order order : listOrders) {
			System.out.printf("%s | %s | %.2f | %.2f | %.2f \n", 
					order.getId(), order.getOrderTime(), order.getProductCost(), 
					order.getSubtotal(), order.getTotal());
		}
	}
}
