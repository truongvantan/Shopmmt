package com.shopmmt.admin.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Order;

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE CONCAT('#', o.id) LIKE %?1% OR "
			+ " CONCAT(o.lastName, ' ', o.firstName) LIKE %?1% OR"
			+ " o.firstName LIKE %?1% OR"
			+ " o.lastName LIKE %?1% OR o.phoneNumber LIKE %?1% OR"
			+ " o.addressLine1 LIKE %?1% OR o.addressLine2 LIKE %?1% OR"
			+ " o.postalCode LIKE %?1% OR o.city LIKE %?1% OR"
			+ " o.state LIKE %?1% OR"
			+ " o.paymentMethod LIKE %?1% OR o.status LIKE %?1% OR"
			+ " o.customer.firstName LIKE %?1% OR"
			+ " o.customer.lastName LIKE %?1%")
	public Page<Order> findAll(String keyword, Pageable pageable);

	public Long countById(Integer id);
	
	@Query("SELECT NEW com.shopmmt.common.entity.Order(o.id, o.orderTime, o.productCost,"
			+ " o.subtotal, o.total) FROM Order o WHERE"
			+ " o.orderTime BETWEEN ?1 AND ?2 ORDER BY o.orderTime ASC")
	public List<Order> findByOrderTimeBetween(Date startTime, Date endTime);
}
