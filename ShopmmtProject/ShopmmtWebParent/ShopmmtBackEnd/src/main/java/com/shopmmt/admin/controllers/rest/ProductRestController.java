package com.shopmmt.admin.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopmmt.admin.dto.ProductInfoOrderDTO;
import com.shopmmt.admin.services.ProductService;
import com.shopmmt.common.entity.Product;
import com.shopmmt.common.exception.ProductNotFoundException;

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
	
	@GetMapping("/products/get/{id}")
	public ProductInfoOrderDTO getProductInfo(@PathVariable(name = "id", required = false) Integer id) 
			throws ProductNotFoundException {
		Product product = productService.findById(id);
		return new ProductInfoOrderDTO(product.getName(), product.getMainImagePath(), 
				product.getDiscountPrice(), product.getCost());
	}
}
