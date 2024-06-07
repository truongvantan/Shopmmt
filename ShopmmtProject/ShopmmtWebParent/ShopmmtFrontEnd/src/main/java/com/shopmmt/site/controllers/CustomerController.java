package com.shopmmt.site.controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopmmt.common.dto.CustomerRegisterFormDTO;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.site.common.EmailSettingBag;
import com.shopmmt.site.common.Utility;
import com.shopmmt.site.services.CustomerService;
import com.shopmmt.site.services.SettingService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SettingService settingService;

	@GetMapping("/register")
	public String showRegisterForm(Model model) {

		CustomerRegisterFormDTO customerRegisterFormDTO = new CustomerRegisterFormDTO();

		model.addAttribute("pageTitle", "Đăng ký tài khoản");
		model.addAttribute("customer", customerRegisterFormDTO);

		return "register/register_form";
	}

	@PostMapping("/create_customer")
	public String createCustomer(Model model, HttpServletRequest request,
			@RequestParam(name = "confirmPassword", required = false) String confirmPassword,
			@Valid @ModelAttribute("customer") CustomerRegisterFormDTO customerRegisterFormDTO,
			BindingResult bindingResult) throws UnsupportedEncodingException, MessagingException {

		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result CustomerRegisterFormDTO");
			model.addAttribute("pageTitle", "Đăng ký tài khoản");

			return "register/register_form";
		} else {
			if (!customerService.validatePassword(customerRegisterFormDTO.getPassword(), confirmPassword)) {
				model.addAttribute("pageTitle", "Đăng ký tài khoản");
				model.addAttribute("errorConfirmPassword", "Mật khẩu xác nhận không trùng khớp!");
				
				return "register/register_form";
			} else {
				Customer customer = new Customer(customerRegisterFormDTO);
				
				customerService.registerCustomer(customer);
				sendVerificationEmail(request, customer);
				
				model.addAttribute("pageTitle", "Đăng ký tài khoản thành công!");
				
				return "/register/register_success";
			}
		}
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer)
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", customer.getFullName());

		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);
		
		message.setContent(content, "text/html; charset=utf-8");
		
		mailSender.send(message);

		System.out.println("to Address: " + toAddress);
		System.out.println("Verify URL: " + verifyURL);
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@RequestParam(name = "code", required = false) String code, Model model) {
		boolean verified = customerService.verify(code);
		if (verified) {
			model.addAttribute("pageTitle", "Xác minh tài khoản thành công!");
		} else {
			model.addAttribute("pageTitle", "Xác minh tài khoản thất bại!");
		}
		
		return "register/" + (verified ? "verify_success" : "verify_fail");
	}
}
