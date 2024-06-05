package com.shopmmt.site.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopmmt.common.entity.Category;
import com.shopmmt.site.services.CategoryService;

@Controller
public class MainController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("")
	public String viewHomePage(Model model) {
		List<Category> listCategories = categoryService.listCategoriesWithoutChildren();
		model.addAttribute("listCategories", listCategories);
		
		return "index";
	}
}
