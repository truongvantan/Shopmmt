package com.shopmmt.admin.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopmmt.admin.services.CategoryService;

@RestController
public class CategoryRestController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/categories/check_category_name")
	public String checkDuplicateCategoryName(@RequestParam(name = "id", required = false) String id, @RequestParam(name = "name", required = false) String name) {
		return categoryService.isCategoryNameUnique(id, name) ? "OK" : "Duplicated";
	}
}
