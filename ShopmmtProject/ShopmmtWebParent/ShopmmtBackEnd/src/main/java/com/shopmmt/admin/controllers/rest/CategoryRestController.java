package com.shopmmt.admin.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopmmt.admin.services.CategoryService;

@RestController
public class CategoryRestController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/categories/check_category_name")
	public String checkDuplicateCategoryName(@Param("id") String id, @Param("name") String name) {
		return categoryService.isCategoryNameUnique(id, name) ? "OK" : "Duplicated";
	}
}
