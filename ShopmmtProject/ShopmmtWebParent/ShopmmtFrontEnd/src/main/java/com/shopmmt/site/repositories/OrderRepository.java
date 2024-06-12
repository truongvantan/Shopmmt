package com.shopmmt.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Order;

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Integer> {
	

}
