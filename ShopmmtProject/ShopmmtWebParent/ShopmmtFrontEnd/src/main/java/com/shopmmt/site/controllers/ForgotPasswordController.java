package com.shopmmt.site.controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.exception.CustomerNotFoundException;
import com.shopmmt.site.common.EmailSettingBag;
import com.shopmmt.site.common.Utility;
import com.shopmmt.site.services.CustomerService;
import com.shopmmt.site.services.SettingService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ForgotPasswordController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SettingService settingService;

	@GetMapping("/forgot_password")
	public String showForgotPasswordForm() {
		return "customers/forgot_password_form";
	}

	@PostMapping("/forgot_password")
	public String processRequestForm(Model model, @RequestParam(name = "email", required = false) String email,
			HttpServletRequest request) {
		try {
			String token = customerService.updateResetPasswordToken(email);
			String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;

			System.err.println("email: " + email);
			System.err.println("token: " + token);
			System.err.println("link: " + link);

			sendEmail(link, email);

			model.addAttribute("message", "Chúng tôi đã gửi liên kết đặt lại mật khẩu đến email: " + email
					+ ". Vui lòng kiểm tra hộp thoại email.");
		} catch (CustomerNotFoundException e) {
			if (e.getMessage().contains("Could not find any customer with the email")) {
				model.addAttribute("error", "Tài khoản có email là " + email + " chưa được đăng ký");
			}
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Hiện tại không thể gửi yêu cầu đến email " + email);
		}

		return "customers/forgot_password_form";
	}

	private void sendEmail(String link, String email) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

		String toAddress = email;
		String subject = "Yêu cầu đặt lại mật khẩu";

		String content = "<p>Xin chào,</p>"
				+ "<p>Bạn đã gửi yêu cầu đặt lại mật khẩu tại Shopmmt Ecommerce Website.</p>"
				+ "Vui lòng click vào đường link bên dưới để đặt lại mật khẩu của bạn:</p>" + "<p><a href=\"" + link
				+ "\">Đặt lại mật khẩu</a></p>" + "<br>"
				+ "<p>Hãy bỏ qua email này nếu bạn không yêu cầu đặt lại mật khẩu.</p>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);

		helper.setText(content, true);
		message.setContent(content, "text/html; charset=utf-8");

		mailSender.send(message);
	}

	@GetMapping("/reset_password")
	public String showResetForm(Model model, @RequestParam(name = "token", required = false) String token) {
		Customer customer = customerService.getByResetPasswordToken(token);

		if (customer != null) {
			model.addAttribute("token", token);
		} else {
			model.addAttribute("pageTitle", "Token không hợp lệ.");
			model.addAttribute("error", "Mã xác thực đặt lại mật khẩu không hợp lệ");

			return "message";
		}

		return "customers/reset_password_form";
	}

	@PostMapping("/reset_password")
	public String processResetForm(Model model, HttpServletRequest request,
			@RequestParam(name = "token", required = false) String token,
			@RequestParam(name = "newPassword", required = false) String newPassword,
			@RequestParam(name = "confirmPassword", required = false) String confirmPassword) {

		String validateMessage = customerService.checkValidResetPassword(newPassword, confirmPassword);

		if ("Invalid new password".equalsIgnoreCase(validateMessage)) {
			model.addAttribute("token", token);
			model.addAttribute("error",
					"Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 60 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.");

			return "customers/reset_password_form";
		} else if ("Confirm password does not match".equalsIgnoreCase(validateMessage)) {
			model.addAttribute("token", token);
			model.addAttribute("error", "Mật khẩu xác nhận không trùng khớp.");

			return "customers/reset_password_form";
		} else {
			try {
				customerService.updatePassword(token, newPassword);

				model.addAttribute("pageTitle", "Đặt lại mật khẩu");
				model.addAttribute("message", "Đặt lại mật khẩu thành công.");

			} catch (CustomerNotFoundException e) {
				if (e.getMessage().contains("No customer found: invalid token")) {
					model.addAttribute("pageTitle", "Token không hợp lệ");
					model.addAttribute("error", "Mã xác thực đặt lại mật khẩu không hợp lệ");
				}
			}

			return "message";
		}

	}
}
