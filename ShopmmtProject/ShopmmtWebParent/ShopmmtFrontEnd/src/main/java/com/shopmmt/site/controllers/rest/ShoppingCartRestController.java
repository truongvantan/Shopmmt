package com.shopmmt.site.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.exception.CustomerNotFoundException;
import com.shopmmt.site.common.Utility;
import com.shopmmt.site.exceptions.ShoppingCartException;
import com.shopmmt.site.services.CustomerService;
import com.shopmmt.site.services.ShoppingCartService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable(name = "productId", required = false) Integer productId,
			@PathVariable(name = "quantity", required = false) Integer quantity, HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = shoppingCartService.addProduct(productId, quantity, customer);
			
			return updatedQuantity + " sản phẩm này đã được thêm vào giỏ hàng.";
		} catch (CustomerNotFoundException e) {
			return "Bạn cần phải đăng nhập để thêm sản phẩm vào giỏ hàng.";
		} catch (ShoppingCartException ex) {
			return ex.getMessage();
		}
		
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) 
			throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
				
		return customerService.getCustomerByEmail(email);
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable(name = "productId", required = false) Integer productId,
			@PathVariable(name = "quantity", required = false) Integer quantity, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			double subtotal = shoppingCartService.updateQuantity(productId, quantity, customer);
			
			return String.valueOf(subtotal);
		} catch (CustomerNotFoundException ex) {
			return "Bạn cần đăng nhập để thay đổi số lượng sản phẩm.";
		}	
	}
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable(name = "productId", required = false) Integer productId,
			HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			shoppingCartService.removeProduct(productId, customer);
			
			return "Đã xóa sản phẩm trong giỏ hàng.";
			
		} catch (CustomerNotFoundException e) {
			return "Bạn cần đăng nhập để xóa sản phẩm khỏi giỏ hàng";
		}
	}
}
