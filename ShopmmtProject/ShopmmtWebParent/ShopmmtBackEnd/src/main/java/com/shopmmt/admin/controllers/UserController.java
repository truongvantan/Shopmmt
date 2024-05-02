package com.shopmmt.admin.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopmmt.admin.exception.UserNotFoundException;
import com.shopmmt.admin.services.RoleService;
import com.shopmmt.admin.services.UserService;
import com.shopmmt.admin.utils.FileUploadUtil;
import com.shopmmt.common.dto.UserDTO;
import com.shopmmt.common.entity.Role;
import com.shopmmt.common.entity.User;

import jakarta.validation.Valid;

@Controller
@ControllerAdvice
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> listUsers = userService.listAll();
		model.addAttribute("listUsers", listUsers);

		return "users";
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) int pageNum) {
		Page<User> page = userService.listByPage(pageNum);
		List<User> listUsers = page.getContent();

		System.out.println("Page num = " + pageNum);
		System.out.println("Total elements = " + page.getTotalElements());
		System.out.println("Total pages = " + page.getTotalPages());
		
		model.addAttribute("listUsers", listUsers);
		return "users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = roleService.listRoles();
		UserDTO userDTO = new UserDTO();
		userDTO.setEnabled(true);

		model.addAttribute("userDTO", userDTO);
		model.addAttribute("listRoles", listRoles);

		return "user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(Model model, @RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IOException {
		if (bindingResult.hasErrors()) {
			List<Role> listRoles = roleService.listRoles();
			model.addAttribute("listRoles", listRoles);
			return "user_form";
		} else {
			if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				userDTO.setPhotos(fileName);
				User savedUser = userService.save(userDTO);
				String uploadDir = "user-photos/" + savedUser.getId();

				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
			redirectAttributes.addFlashAttribute("message", "Thêm mới người dùng thành công");

			return "redirect:/users";
		}
	}

	@GetMapping("/users/showEdit/{id}")
	public String showEditUser(@PathVariable(name = "id", required = false) String id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			User user = userService.findById(id);
			UserDTO userDTO = new UserDTO(user);
			List<Role> listRoles = roleService.listRoles();
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("userDTO", userDTO);

			return "user_edit_form";
		} catch (UserNotFoundException e) {
			if (e.getMessage().contains("Could not find any user with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng có ID " + id);
			}
			return "redirect:/users";
		}

	}

	@PostMapping("/users/edit")
	public String editUser(Model model, @RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IOException {
		if (bindingResult.hasErrors()) {
			List<Role> listRoles = roleService.listRoles();
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("id", userDTO.getId());

			return "user_edit_form";
		} else {
			if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				userDTO.setPhotos(fileName);
				User savedUser = userService.save(userDTO);
				String uploadDir = "user-photos/" + savedUser.getId();

				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} else {
				if ("".equals(userDTO.getPhotos())) {
					userDTO.setPhotos(null);
				}
				userService.save(userDTO);
			}
			redirectAttributes.addFlashAttribute("message", "Cập nhật người dùng thành công");

			return "redirect:/users";
		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id", required = false) String id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			userService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Xóa người dùng ID " + id + " thành công");
			return "redirect:/users";
		} catch (UserNotFoundException e) {
			if (e.getMessage().contains("Could not find any user with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng có ID " + id);
			}
			return "redirect:/users";
		}
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable(name = "id", required = false) String id,
			@PathVariable(name = "status", required = false) boolean enabled, RedirectAttributes redirectAttributes) {
		try {
			userService.updateUserEnabledStatus(id, enabled);
			String status = enabled ? "mở khóa" : "khóa";
			String message = "Đã " + status + " người dùng ID " + id;
			redirectAttributes.addFlashAttribute("message", message);

			return "redirect:/users";
		} catch (UserNotFoundException e) {
			if (e.getMessage().contains("Could not find any user with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng có ID " + id);
			}
			return "redirect:/users";
		}
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleFileUploadError(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("error", "Lỗi kích thước tệp vượt quá 1MB!");
		return "redirect:/users";
	}

}
