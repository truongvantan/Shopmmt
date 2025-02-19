package com.shopmmt.admin.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopmmt.admin.common.AmazonS3Util;
import com.shopmmt.admin.exception.UserNotFoundException;
import com.shopmmt.admin.exporter.UserCsvExporter;
import com.shopmmt.admin.exporter.UserExcelExporter;
import com.shopmmt.admin.exporter.UserPdfExporter;
import com.shopmmt.admin.services.RoleService;
import com.shopmmt.admin.services.UserService;
import com.shopmmt.admin.utils.FileUploadUtil;
import com.shopmmt.common.dto.UserDTO;
import com.shopmmt.common.entity.Role;
import com.shopmmt.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(model, 1, "id", "asc", null);
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) int pageNum,
			@RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
			@RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
			@RequestParam(name = "keyword", required = false) String keyword) {

		Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
		List<User> listUsers = page.getContent();

		String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "users/users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = roleService.listRoles();
		UserDTO userDTO = new UserDTO();
		userDTO.setEnabled(true);

		model.addAttribute("userDTO", userDTO);
		model.addAttribute("listRoles", listRoles);

		return "users/user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(Model model, @RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IOException {
		if (bindingResult.hasErrors()) {
			List<Role> listRoles = roleService.listRoles();
			model.addAttribute("listRoles", listRoles);
			return "users/user_form";
		} else {
			if (!FileUploadUtil.isValidFileSize(multipartFile)) {
				model.addAttribute("error", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");
				List<Role> listRoles = roleService.listRoles();
				model.addAttribute("listRoles", listRoles);

				return "users/user_form";
			} else {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					userDTO.setPhotos(fileName);
					User savedUser = userService.save(userDTO);

//					String uploadDir = "user-photos/" + savedUser.getId();
//
//					FileUploadUtil.cleanDir(uploadDir);
//					FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

					String uploadDir = "user-photos/" + savedUser.getId();

					AmazonS3Util.removeFolder(uploadDir);
					AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
				} else {
					if ("".equals(userDTO.getPhotos()) || userDTO.getPhotos() == null) {
						userDTO.setPhotos(null);
					}
					userService.save(userDTO);
				}

				redirectAttributes.addFlashAttribute("message", "Thêm mới người dùng thành công");

				return getRedirectURLtoAffectedUser(userDTO);
			}

		}
	}

	private String getRedirectURLtoAffectedUser(UserDTO userDTO) {
		String firstPathOfEmail = userDTO.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPathOfEmail;
	}

	@GetMapping("/users/showEdit/{id}")
	public String showEditUser(@PathVariable(name = "id", required = false) Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			User user = userService.findById(id);
			UserDTO userDTO = new UserDTO(user);
			List<Role> listRoles = roleService.listRoles();
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("userDTO", userDTO);

			return "users/user_edit_form";
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

			return "users/user_edit_form";
		} else {
			if (!FileUploadUtil.isValidFileSize(multipartFile)) {
				model.addAttribute("error", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");
				List<Role> listRoles = roleService.listRoles();
				model.addAttribute("listRoles", listRoles);
				model.addAttribute("id", userDTO.getId());

				return "users/user_edit_form";
			} else {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					userDTO.setPhotos(fileName);
					User savedUser = userService.save(userDTO);
					
//					String uploadDir = "user-photos/" + savedUser.getId();
//
//					FileUploadUtil.cleanDir(uploadDir);
//					FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

					String uploadDir = "user-photos/" + savedUser.getId();

					AmazonS3Util.removeFolder(uploadDir);
					AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
				} else {
					if ("".equals(userDTO.getPhotos()) || userDTO.getPhotos() == null) {
						userDTO.setPhotos(null);
					}
					userService.save(userDTO);
				}

				redirectAttributes.addFlashAttribute("message", "Cập nhật người dùng thành công");

				return getRedirectURLtoAffectedUser(userDTO);
			}

		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id", required = false) Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			userService.delete(id);
			
			String userPhotosDir = "user-photos/" + id;
			AmazonS3Util.removeFolder(userPhotosDir);
			
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
	public String updateUserEnabledStatus(@PathVariable(name = "id", required = false) Integer id,
			@PathVariable(name = "status", required = false) boolean enabled, RedirectAttributes redirectAttributes) {
		try {
			userService.updateUserEnabledStatus(id, enabled);
			String status = enabled ? "mở khóa" : "khóa";
			String message = "Đã " + status + " người dùng ID " + id;
			redirectAttributes.addFlashAttribute("message", message);

			return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + id;
		} catch (UserNotFoundException e) {
			if (e.getMessage().contains("Could not find any user with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng có ID " + id);
			}
			return "redirect:/users";
		}
	}

	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = userService.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = userService.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = userService.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}

}
