package com.shopmmt.admin.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopmmt.admin.services.OrderService;
import com.shopmmt.admin.services.SettingService;
import com.shopmmt.common.entity.Order;
import com.shopmmt.common.entity.OrderDetail;
import com.shopmmt.common.entity.OrderTrack;
import com.shopmmt.common.entity.Product;
import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.enums.OrderStatus;
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
	
	@GetMapping("/orders/showEdit/{id}")
	public String editOrder(@PathVariable(name = "id", required = false) Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		try {
			Order order = orderService.get(id);
			
			
			model.addAttribute("id", id);
			model.addAttribute("order", order);
			
			return "orders/order_edit_form";
			
		} catch (OrderNotFoundException e) {
			if (e.getMessage().contains("Could not find any order with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng có ID " + id);
			}
			
			return "redirect:/orders";
		}
		
	}
	
	@PostMapping("/order/edit")
	public String editOrder(Order order, HttpServletRequest request, RedirectAttributes ra) {
		
		updateProductDetails(order, request);
		updateOrderTracks(order, request);

		orderService.save(order);		
		
		ra.addFlashAttribute("message", "Cập nhật đơn hàng ID " + order.getId() + " thành công.");
		
		return "redirect:/orders";
	}
	
	private void updateOrderTracks(Order order, HttpServletRequest request) {
		String[] trackIds = request.getParameterValues("trackId");
		String[] trackStatuses = request.getParameterValues("trackStatus");
		String[] trackDates = request.getParameterValues("trackDate");
		String[] trackNotes = request.getParameterValues("trackNotes");
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		
		for (int i = 0; i < trackIds.length; i++) {
			OrderTrack trackRecord = new OrderTrack();
			
			Integer trackId = Integer.parseInt(trackIds[i]);
			if (trackId > 0) {
				trackRecord.setId(trackId);
			}
			
			trackRecord.setOrder(order);
			trackRecord.setStatus(OrderStatus.valueOf(trackStatuses[i]));
			trackRecord.setNotes(trackNotes[i]);
			
			try {
				trackRecord.setUpdatedTime(dateFormatter.parse(trackDates[i]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			orderTracks.add(trackRecord);
		}
	}
	
	private void updateProductDetails(Order order, HttpServletRequest request) {
		String[] detailIds = request.getParameterValues("detailId");
		String[] productIds = request.getParameterValues("productId");
		String[] productPrices = request.getParameterValues("productPrice");
		String[] productDetailCosts = request.getParameterValues("productDetailCost");
		String[] quantities = request.getParameterValues("quantity");
		String[] productSubtotals = request.getParameterValues("productSubtotal");
		String[] productShipCosts = request.getParameterValues("productShipCost");
		
		Set<OrderDetail> orderDetails = order.getOrderDetails();
		
		for (int i = 0; i < detailIds.length; i++) {
			System.out.println("Detail ID: " + detailIds[i]);
			System.out.println("\t Prodouct ID: " + productIds[i]);
			System.out.println("\t Cost: " + productDetailCosts[i]);
			System.out.println("\t Quantity: " + quantities[i]);
			System.out.println("\t Subtotal: " + productSubtotals[i]);
			System.out.println("\t Ship cost: " + productShipCosts[i]);
			
			OrderDetail orderDetail = new OrderDetail();
			Integer detailId = Integer.parseInt(detailIds[i]);
			if (detailId > 0) {
				orderDetail.setId(detailId);
			}
			
			orderDetail.setOrder(order);
			orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
			orderDetail.setProductCost(Float.parseFloat(productDetailCosts[i]));
			orderDetail.setSubtotal(Float.parseFloat(productSubtotals[i]));
			orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
			orderDetail.setQuantity(Integer.parseInt(quantities[i]));
			orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));
			
			orderDetails.add(orderDetail);
			
		}
		
	}
}
