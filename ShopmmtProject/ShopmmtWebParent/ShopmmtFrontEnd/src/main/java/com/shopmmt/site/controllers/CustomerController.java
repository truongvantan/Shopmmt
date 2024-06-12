package com.shopmmt.site.controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopmmt.common.dto.CustomerAccountDetailDTO;
import com.shopmmt.common.dto.CustomerRegisterFormDTO;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.site.common.EmailSettingBag;
import com.shopmmt.site.common.Utility;
import com.shopmmt.site.config.oauth.CustomerOAuth2User;
import com.shopmmt.site.config.security.CustomerUserDetails;
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

	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCustomerByEmail(email);

		CustomerAccountDetailDTO customerAccountDetailDTO = new CustomerAccountDetailDTO(customer);
		
		model.addAttribute("customer", customerAccountDetailDTO);

		return "customers/account_edit_form";
	}

	@PostMapping("/update_account_details")
	public String updateAccountDetails(Model model,
			@RequestParam(name = "newPassword", required = false) String newPassword,
			@RequestParam(name = "confirmPassword", required = false) String confirmPassword,
			@Valid @ModelAttribute("customer") CustomerAccountDetailDTO customerAccountDetailDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		
		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result CustomerAccountDetailDTO");
			
			return "customers/account_edit_form";
		} else {
			
			String validateMessage = customerService.checkValidPassword(newPassword, confirmPassword);
			if ("Invalid new password".equalsIgnoreCase(validateMessage)) {
				model.addAttribute("errorNewPassword",
						"Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 60 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.");
				return "customers/account_edit_form";
			} else if ("Confirm password does not match".equalsIgnoreCase(validateMessage)) {
				model.addAttribute("errorConfirmPassword", "Mật khẩu xác nhận không trùng khớp");
				return "customers/account_edit_form";
			} else {
				Customer customer = new Customer(customerAccountDetailDTO);
				customer.setPassword(newPassword);
				
				customerService.update(customer);
				redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công.");

				updateNameForAuthenticatedCustomer(customer, request);
				
				String redirectOption = request.getParameter("redirect");
				String redirectURL = "redirect:/account_details";
				
				if ("address_book".equals(redirectOption)) {
					redirectURL = "redirect:/address_book";
				} else if ("cart".equals(redirectOption)) {
					redirectURL = "redirect:/cart";
				} else if ("checkout".equals(redirectOption)) {
					redirectURL = "redirect:/address_book?redirect=checkout";
				}
				
				return redirectURL;
				
			}
			
		}
	}

	private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
		Object principal = request.getUserPrincipal();

		if (principal instanceof UsernamePasswordAuthenticationToken
				|| principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			Customer authenticatedCustomer = userDetails.getCustomer();
			
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());

		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			String fullName = customer.getLastName() + " " + customer.getFirstName();
			oauth2User.setFullName(fullName);
		}
	}

	private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		CustomerUserDetails userDetails = null;
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		} else if (principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}

		return userDetails;
	}

}
