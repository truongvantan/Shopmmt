package com.shopmmt.site.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.entity.Order;
import com.shopmmt.site.common.Utility;
import com.shopmmt.site.services.CustomerService;
import com.shopmmt.site.services.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private CustomerService customerService;

	@GetMapping("/orders")
	public String listFirstPage(Model model, HttpServletRequest request) {
		return listOrdersByPage(model, request, 1, "orderTime", "desc", null);
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listOrdersByPage(Model model, HttpServletRequest request,
			@PathVariable(name = "pageNum", required = false) int pageNum, String sortField, String sortDir,
			String orderKeyword) {
		Customer customer = getAuthenticatedCustomer(request);

		String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

		Page<Order> pageOrders = orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir,
				orderKeyword);
		List<Order> listOrders = pageOrders.getContent();

		model.addAttribute("listOrders", listOrders);

		model.addAttribute("totalPages", pageOrders.getTotalPages());
		model.addAttribute("totalItems", pageOrders.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("orderKeyword", orderKeyword);
		model.addAttribute("moduleURL", "/orders");

		return "orders/orders_customer";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}

	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(Model model, @PathVariable(name = "id", required = false) Integer id,
			HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);

		Order order = orderService.getOrder(id, customer);
		model.addAttribute("order", order);

		return "orders/order_details_modal";
	}
}
