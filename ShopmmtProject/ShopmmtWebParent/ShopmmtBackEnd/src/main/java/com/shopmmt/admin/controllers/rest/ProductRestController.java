package com.shopmmt.admin.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopmmt.admin.services.ProductService;

@RestController
public class ProductRestController {

	@Autowired
	private ProductService productService;

	@PostMapping("/products/check_product_name")
	public String checkDuplicateProductName(@RequestParam(name = "id", required = false) String id,
			@RequestParam(name = "name", required = false) String name) {
		return productService.isProductNameUnique(id, name) ? "OK" : "Duplicated";
	}

	@PostMapping("/products/check_product_details")
	public String checkDuplicateProductDetails(@RequestParam(name = "product_id", required = false, defaultValue = "-1") String productId,
			@RequestParam(name = "productDetailNames", required = false) String jsonDetailNames,
			@RequestParam(name = "productDetailValues", required = false) String jsonDetailValues) {
		
		return productService.isProductDetailsUnique(productId, jsonDetailNames, jsonDetailValues) ? "OK" : "Duplicated";
	}
}
