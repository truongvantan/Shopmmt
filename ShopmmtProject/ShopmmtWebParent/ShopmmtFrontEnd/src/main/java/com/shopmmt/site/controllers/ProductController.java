package com.shopmmt.site.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopmmt.common.entity.Category;
import com.shopmmt.common.entity.Product;
import com.shopmmt.common.exception.CategoryNotFoundException;
import com.shopmmt.common.exception.ProductNotFoundException;
import com.shopmmt.site.services.CategoryService;
import com.shopmmt.site.services.ProductService;

@Controller
public class ProductController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;

	@GetMapping("/c/{category_name}")
	public String viewCategoryFirstPage(Model model, @PathVariable("category_name") String name) {
		return viewCategoryByPage(name, 1, model);
	}

	@GetMapping("/c/{category_name}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_name") String name,
			@PathVariable("pageNum") int pageNum, Model model) {
		
		try {
			Category category = categoryService.getCategoryByName(name);
			
			List<Category> listCategoryParents = categoryService.getCategoryParents(category);
			
			Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
			List<Product> listProducts = pageProducts.getContent();
			
			model.addAttribute("totalPages", pageProducts.getTotalPages());
			model.addAttribute("totalItems", pageProducts.getTotalElements());
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("listProducts", listProducts);
			model.addAttribute("category", category);
			model.addAttribute("categoryName", name);
			
			return "products/products_by_category";
		} catch (CategoryNotFoundException e) {
			e.printStackTrace();
			return "error/404";
		}
		
	}
	
	@GetMapping("/p/{product_name}")
	public String viewProductDetail(Model model, @PathVariable("product_name") String name) {
		try {
			Product product = productService.getProductByName(name);
			List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());
			
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("product", product);
			
			return "products/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	
}
