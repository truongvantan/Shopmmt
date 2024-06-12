package com.shopmmt.admin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopmmt.admin.services.OrderService;
import com.shopmmt.admin.services.SettingService;
import com.shopmmt.common.entity.Order;
import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.exception.OrderNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private SettingService settingService;

	@GetMapping("/orders")
	public String listFirstPage(Model model, HttpServletRequest request) {
		return listByPage(model, request, 1, "orderTime", "desc", null);
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(Model model, HttpServletRequest request,
			@PathVariable(name = "pageNum", required = false) int pageNum,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword", required = false) String keyword) {

		String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

		Page<Order> pageOrders = orderService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Order> listOrders = pageOrders.getContent();

		model.addAttribute("listOrders", listOrders);

		model.addAttribute("totalPages", pageOrders.getTotalPages());
		model.addAttribute("totalItems", pageOrders.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", reverseSortDir);

		loadCurrencySetting(request);

		return "orders/orders";
	}

	private void loadCurrencySetting(HttpServletRequest request) {
		List<Setting> currencySettings = settingService.getCurrencySettings();

		for (Setting setting : currencySettings) {
			request.setAttribute(setting.getKey(), setting.getValue());
		}
	}

	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(@PathVariable(name = "id", required = false) Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		try {
			Order order = orderService.get(id);
			loadCurrencySetting(request);
			model.addAttribute("order", order);

			return "orders/order_details_modal";
		} catch (OrderNotFoundException e) {
			if (e.getMessage().contains("Could not find any order with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng có ID " + id);
			}
			return "redirect:/orders";
		}

	}
	
	@GetMapping("/orders/delete/{id}")
	public String deleteOrder(@PathVariable(name = "id", required = false) Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			orderService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Xóa đơn hàng ID " + id + " thành công.");
		} catch (OrderNotFoundException e) {
			if (e.getMessage().contains("Could not find any order with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng có ID " + id);
				
			}
		}
		
		return "redirect:/orders";
	}
}
