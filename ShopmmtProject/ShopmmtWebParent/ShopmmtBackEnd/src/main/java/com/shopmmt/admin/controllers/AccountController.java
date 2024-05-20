package com.shopmmt.admin.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shopmmt.admin.config.ShopmmtUserDetails;
import com.shopmmt.admin.services.UserService;
import com.shopmmt.admin.utils.FileUploadUtil;
import com.shopmmt.common.dto.UserDTO;
import com.shopmmt.common.entity.User;

import jakarta.validation.Valid;

@Controller
public class AccountController {

	@Autowired
	private UserService userService;

	@GetMapping("/account")
	public String showAccountDetails(@AuthenticationPrincipal ShopmmtUserDetails loggedUser, Model model) {
		String email = loggedUser.getUsername();
		User user = userService.findByEmail(email);

		UserDTO userDTO = new UserDTO(user);
		model.addAttribute("userDTO", userDTO);
		return "users/account_form";
	}

	@PostMapping("/account/update")
	public String editUser(@AuthenticationPrincipal ShopmmtUserDetails loggedUser, Model model,
			@RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@RequestParam(name = "newPassword", required = false) String newPassword,
			@RequestParam(name = "confirmPassword", required = false) String confirmPassword,
			@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding error");
			return "users/account_form";
		} else {
			if (!FileUploadUtil.isValidFileSize(multipartFile)) {
				model.addAttribute("errorPhoto", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");
				return "users/account_form";
			} else {
				String validateMessage = userService.validatePassword(newPassword, confirmPassword,
						userDTO.getPassword());
				if ("Invalid new password".equalsIgnoreCase(validateMessage)) {
					model.addAttribute("errorPassword",
							"Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 60 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.");
					return "users/account_form";
				} else if ("Confirm password does not match".equalsIgnoreCase(validateMessage)) {
					model.addAttribute("errorPassword", "Mật khẩu xác nhận không trùng khớp");
					return "users/account_form";
				} else {
					userDTO.setPassword(newPassword);
					if (!multipartFile.isEmpty()) {
						String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
						userDTO.setPhotos(fileName);
						User savedUser = userService.save(userDTO);
						String uploadDir = "user-photos/" + savedUser.getId();

						FileUploadUtil.cleanDir(uploadDir);
						FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
					} else {
						if ("".equals(userDTO.getPhotos()) || userDTO.getPhotos() == null) {
							userDTO.setPhotos(null);
						}
						userService.save(userDTO);
					}

					loggedUser.setFirstName(userDTO.getFirstName());
					loggedUser.setLastName(userDTO.getLastName());

					model.addAttribute("message", "Thông tin tài khoản của bạn đã được cập nhật thành công");

					return "users/account_form";
				}
			}

		}
	}
}
