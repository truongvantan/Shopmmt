package com.shopmmt.admin.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopmmt.admin.services.UserService;

@RestController
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/users/check_email")
	public String checkDuplicateEmail(@RequestParam(name = "id", required = false) String id, @RequestParam(name = "email", required = false) String email) {
		return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
}
