package com.shopmmt.admin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleFileUploadError(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("error", "Lỗi kích thước tệp vượt quá 3MB!");
		return "redirect:/";
	}
}
