package com.shopmmt.site.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopmmt.site.services.CustomerService;

@RestController
public class CustomerRestController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/customers/check_unique_email")
	public String checkDuplicateEmail(@RequestParam(name = "email", required = false) String email) {
		return customerService.isEmailUnique(email) ? "OK" : "Duplicated";
	}
}
