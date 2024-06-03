package com.shopmmt.admin.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import com.shopmmt.admin.exception.CategoryNotFoundException;
import com.shopmmt.admin.services.CategoryService;
import com.shopmmt.admin.utils.FileUploadUtil;
import com.shopmmt.common.dto.CategoryDTO;
import com.shopmmt.common.dto.CategoryPageInfoDTO;
import com.shopmmt.common.entity.Category;

import jakarta.validation.Valid;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public String listFirstPage(Model model,
			@RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		return listByPage(model, 1, sortDir, null);
	}

	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) int pageNum,
			@RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
			@RequestParam(name = "keyword", required = false) String keyword) {

		String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

		CategoryPageInfoDTO categoryPageInfoDTO = new CategoryPageInfoDTO();

		List<Category> listCategories = categoryService.listByPage(categoryPageInfoDTO, pageNum, sortDir, keyword);

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);

		model.addAttribute("totalPages", categoryPageInfoDTO.getTotalPages());
		model.addAttribute("totalItems", categoryPageInfoDTO.getTotalItems());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);

		return "categories/categories";
	}

	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		CategoryDTO categoryDTO = new CategoryDTO();
		List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();

		model.addAttribute("categoryDTO", categoryDTO);
		model.addAttribute("listCategoriesDTO", listCategoriesDTO);

		return "categories/category_form";
	}

	@PostMapping("/categories/save")
	public String saveCategory(Model model, @RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IOException {
		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result categoryDTO");
			List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();
			model.addAttribute("listCategoriesDTO", listCategoriesDTO);

			return "categories/category_form";
		} else {
			if (!FileUploadUtil.isValidFileSize(multipartFile)) {
				List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();
				model.addAttribute("listCategoriesDTO", listCategoriesDTO);
				model.addAttribute("error", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");

				return "categories/category_form";
			} else {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					categoryDTO.setPhotos(fileName);

					Category savedCategory = categoryService.save(categoryDTO);
					String uploadDir = "../category-images/" + savedCategory.getId();

					FileUploadUtil.cleanDir(uploadDir);
					FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				} else {
					if ("".equals(categoryDTO.getPhotos()) || categoryDTO.getPhotos() == null) {
						categoryDTO.setPhotos(null);
					}
					categoryService.save(categoryDTO);
				}

				redirectAttributes.addFlashAttribute("message", "Thêm mới danh mục thành công");

				return "redirect:/categories";
			}
		}
	}

	@GetMapping("categories/showEdit/{id}")
	public String showEditCategory(Model model, @PathVariable(name = "id", required = false) Integer id,
			RedirectAttributes redirectAttributes) {

		try {
			Category category = categoryService.findById(id);
			CategoryDTO categoryDTO = new CategoryDTO(category);

			List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();
			model.addAttribute("listCategoriesDTO", listCategoriesDTO);

			model.addAttribute("categoryDTO", categoryDTO);

			return "categories/category_edit_form";
		} catch (CategoryNotFoundException e) {
			if (e.getMessage().contains("Could not find any category with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục có ID " + id);
			}
			return "redirect:/categories";
		}
	}

	@PostMapping("/categories/edit")
	public String editCategory(Model model, @RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IOException {
		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result categoryDTO");
			List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();
			model.addAttribute("listCategoriesDTO", listCategoriesDTO);
			model.addAttribute("id", categoryDTO.getId());

			return "categories/category_edit_form";
		} else {
			if (!FileUploadUtil.isValidFileSize(multipartFile)) {
				List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();
				model.addAttribute("listCategoriesDTO", listCategoriesDTO);
				model.addAttribute("error", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");
				model.addAttribute("id", categoryDTO.getId());
				
				return "categories/category_edit_form";
			}
			if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				categoryDTO.setPhotos(fileName);

				Category savedCategory = categoryService.save(categoryDTO);
				String uploadDir = "../category-images/" + savedCategory.getId();

				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} else {
				if ("".equals(categoryDTO.getPhotos()) || categoryDTO.getPhotos() == null) {
					categoryDTO.setPhotos(null);
				}
				categoryService.save(categoryDTO);
			}

			redirectAttributes.addFlashAttribute("message", "Cập nhật danh mục thành công");

			return "redirect:/categories";
		}
	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable(name = "id", required = false) Integer id,
			@PathVariable(name = "status", required = false) boolean enabled, RedirectAttributes redirectAttributes) {
		try {
			categoryService.updateCategoryEnabledStatus(id, enabled);
			String status = enabled ? "mở khóa" : "khóa";
			String message = "Đã " + status + " danh mục ID " + id;
			redirectAttributes.addFlashAttribute("message", message);

			return "redirect:/categories";
		} catch (CategoryNotFoundException e) {
			if (e.getMessage().contains("Could not find any category with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục có ID " + id);
			}
			return "redirect:/categories";
		}
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id", required = false) Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			categoryService.delete(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);

			redirectAttributes.addFlashAttribute("message", "Xóa danh mục ID " + id + " thành công");

			return "redirect:/categories";
		} catch (CategoryNotFoundException e) {
			if (e.getMessage().contains("Could not find any category with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục có ID " + id);
			}

			return "redirect:/categories";
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("a foreign key constraint fails")) {
				redirectAttributes.addFlashAttribute("error", "Không thể xóa danh mục ID " + id + " vì danh mục này có liên kết dữ liệu đến sản phẩm hoặc thương hiệu khác!");
			}

			return "redirect:/categories";
		}
	}
}
