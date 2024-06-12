package com.shopmmt.site.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
//		List<Category> listCategories = categoryService.listCategoriesWithoutChildren();
		
		List<Category> listCategories = categoryService.listRootParentCategories();
		
		model.addAttribute("listCategories", listCategories);
		
		return "index";
	}
	
	@GetMapping("/login")
	public String showLoginPage() {
		// Ghi nhớ đăng nhập sau khi login
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		
		return "redirect:/";
	}
}
