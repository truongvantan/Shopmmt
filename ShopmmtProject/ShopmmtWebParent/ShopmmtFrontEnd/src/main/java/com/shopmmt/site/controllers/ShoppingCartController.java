package com.shopmmt.site.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopmmt.common.entity.CartItem;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.site.common.Utility;
import com.shopmmt.site.services.CustomerService;
import com.shopmmt.site.services.ShoppingCartService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
		
		double estimatedTotal = 0.0D;
		
		for (CartItem item : cartItems) {
			estimatedTotal += item.getSubtotal();
		}
		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);
		
		return "cart/shopping_cart";
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);				
		return customerService.getCustomerByEmail(email);
	}
}
