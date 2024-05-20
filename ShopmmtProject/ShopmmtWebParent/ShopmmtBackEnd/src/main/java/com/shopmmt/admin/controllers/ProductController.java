package com.shopmmt.admin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopmmt.admin.services.BrandService;
import com.shopmmt.admin.services.ProductService;
import com.shopmmt.common.dto.ProductDTO;
import com.shopmmt.common.entity.Brand;
import com.shopmmt.common.entity.Product;

import jakarta.validation.Valid;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts =  productService.listAll();
		model.addAttribute("listProducts", listProducts);
		
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.findAll();
		ProductDTO productDTO = new ProductDTO();
		
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("productDTO", productDTO);
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Model model, @Valid @ModelAttribute(name = "productDTO") ProductDTO productDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<Brand> listBrands = brandService.findAll();
			model.addAttribute("listBrands", listBrands);
			
			return "products/product_form";
		} else {
			return "redirect:/products";
		}
	}
}
