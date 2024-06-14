package com.shopmmt.admin.services.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopmmt.admin.repositories.OrderRepository;
import com.shopmmt.admin.services.OrderService;
import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.entity.Order;
import com.shopmmt.common.entity.OrderTrack;
import com.shopmmt.common.enums.OrderStatus;
import com.shopmmt.common.exception.OrderNotFoundException;

import jakarta.transaction.Transactional;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		
		Sort sort = null;
		
		if ("destination".equals(sortField)) {
			sort = Sort.by("state").and(Sort.by("city"));
		} else {
			sort = Sort.by(sortField);
		}
		
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, ConstantsUtil.ORDER_PAGE_SIZE, sort);
		
		if (keyword != null) {
			return orderRepository.findAll(keyword, pageable);
		}

		return orderRepository.findAll(pageable);
	}

	@Override
	public Order get(Integer id) throws OrderNotFoundException {
		try {
			return orderRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new OrderNotFoundException("Could not find any order with ID " + id);
		}
	}

	@Override
	public void delete(Integer id) throws OrderNotFoundException {
		Long count = orderRepository.countById(id);
		if (count == null || count == 0) {
			throw new OrderNotFoundException("Could not find any order with ID " + id); 
		}
		
		orderRepository.deleteById(id);
	}

	@Override
	public void save(Order orderInForm) {
		Order orderInDB = orderRepository.findById(orderInForm.getId()).get();
		orderInForm.setOrderTime(orderInDB.getOrderTime());
		orderInForm.setCustomer(orderInDB.getCustomer());
		
		orderRepository.save(orderInForm);
	}

	@Override
	public void updateStatus(Integer orderId, String status) {
		Order orderInDB = orderRepository.findById(orderId).get();
		OrderStatus statusToUpdate = OrderStatus.valueOf(status);
		
		if (!orderInDB.hasStatus(statusToUpdate)) {
			List<OrderTrack> orderTracks = orderInDB.getOrderTracks();
			
			OrderTrack track = new OrderTrack();
			track.setOrder(orderInDB);
			track.setStatus(statusToUpdate);
			track.setUpdatedTime(new Date());
			track.setNotes(statusToUpdate.label);
			
			orderTracks.add(track);
			
			orderInDB.setStatus(statusToUpdate);
			
			orderRepository.save(orderInDB);
		}
	}
	
	
}
