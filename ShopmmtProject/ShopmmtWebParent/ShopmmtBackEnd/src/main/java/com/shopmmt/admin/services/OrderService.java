package com.shopmmt.admin.services;

import org.springframework.data.domain.Page;

import com.shopmmt.common.entity.Order;
import com.shopmmt.common.exception.OrderNotFoundException;

public interface OrderService {
	
	 Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword);
	 
	 Order get(Integer id) throws OrderNotFoundException;

	void delete(Integer id) throws OrderNotFoundException;
}
