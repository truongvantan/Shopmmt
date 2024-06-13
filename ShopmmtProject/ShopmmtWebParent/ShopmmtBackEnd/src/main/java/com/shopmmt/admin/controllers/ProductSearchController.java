package com.shopmmt.admin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopmmt.admin.services.ProductService;
import com.shopmmt.common.entity.Product;

@Controller
public class ProductSearchController {

	@Autowired
	private ProductService productService;

	@GetMapping("/orders/search_product")
	public String showSearchProductPage() {
		return "orders/search_product";
	}

	@PostMapping("/orders/search_product")
	public String searchProducts(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
		return searchProductsByPage(model, 1, "name", "asc", keyword);
//		return "redirect:/orders/search_product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
	}

	@GetMapping("/orders/search_product/page/{pageNum}")
	public String searchProductsByPage(Model model, @PathVariable(name = "pageNum", required = false) int pageNum,
			@RequestParam("sortField") String sortField,
			@RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
			@RequestParam(name = "keyword", required = false) String keyword) {

		Page<Product> pageProduct = productService.searchProducts(pageNum, sortField, sortDir, keyword);

		List<Product> listProducts = pageProduct.getContent();

		model.addAttribute("listProducts", listProducts);
		model.addAttribute("totalPages", pageProduct.getTotalPages());
		model.addAttribute("totalItems", pageProduct.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);

		return "orders/search_product";
	}
}
