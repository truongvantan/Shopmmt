package com.shopmmt.admin.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopmmt.admin.services.OrderService;

@RestController
public class OrderRestController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/orders_shipper/update/{id}/{status}")
	public Response updateOrderStatus(@PathVariable(name = "id", required = false) Integer orderId,
			@PathVariable(name = "status", required = false) String status) {
		orderService.updateStatus(orderId, status);
		return new Response(orderId, status);
	}
}

class Response {
	private Integer orderId;
	private String status;
	
	public Response() {
		super();
	}

	public Response(Integer orderId, String status) {
		this.orderId = orderId;
		this.status = status;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
