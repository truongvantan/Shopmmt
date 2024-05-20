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

import com.shopmmt.admin.exception.BrandNotFoundException;
import com.shopmmt.admin.services.BrandService;
import com.shopmmt.admin.services.CategoryService;
import com.shopmmt.admin.utils.FileUploadUtil;
import com.shopmmt.common.dto.BrandDTO;
import com.shopmmt.common.dto.CategoryDTO;
import com.shopmmt.common.entity.Brand;

import jakarta.validation.Valid;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/brands")
	public String listFirstPage(Model model,
			@RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		return listByPage(model, 1, "id", sortDir, null);
	}

	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) int pageNum,
			@RequestParam("sortField") String sortField,
			@RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
			@RequestParam(name = "keyword", required = false) String keyword) {

		String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

		Page<Brand> pageBrand = brandService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Brand> listBrands = pageBrand.getContent();

		model.addAttribute("listBrands", listBrands);
		model.addAttribute("reverseSortDir", reverseSortDir);

		model.addAttribute("totalPages", pageBrand.getTotalPages());
		model.addAttribute("totalItems", pageBrand.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);

		return "brands/brands";
	}

	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();
		BrandDTO brandDTO = new BrandDTO();

		model.addAttribute("listCategoriesDTO", listCategoriesDTO);
		model.addAttribute("brandDTO", brandDTO);

		return "brands/brand_form";
	}

	@PostMapping("/brands/save")
	public String saveBrand(Model model, @RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute(name = "brandDTO") BrandDTO brandDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IOException {
		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result brandDTO");
			List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();
			model.addAttribute("listCategoriesDTO", listCategoriesDTO);

			return "brands/brand_form";
		} else {
			if (!FileUploadUtil.isValidFileSize(multipartFile)) {
				List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();
				model.addAttribute("listCategoriesDTO", listCategoriesDTO);
				model.addAttribute("error", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");

				return "brands/brand_form";
			} else {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					brandDTO.setLogo(fileName);

					Brand savedBrand = brandService.save(brandDTO);
					String uploadDir = "../brand-logos/" + savedBrand.getId();

					FileUploadUtil.cleanDir(uploadDir);
					FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				} else {
					if (brandDTO.getLogo() == null || "".equals(brandDTO.getLogo())) {
						brandDTO.setLogo(null);
					}
					brandService.save(brandDTO);
				}

				redirectAttributes.addFlashAttribute("message", "Thêm mới thương hiệu thành công");

				return "redirect:/brands";
			}
		}
	}

	@GetMapping("/brands/showEdit/{id}")
	public String showEdit(Model model, @PathVariable(name = "id", required = false) Integer id,
			RedirectAttributes redirectAttributes) {

		try {
			Brand brand = brandService.findById(id);
			BrandDTO brandDTO = new BrandDTO(brand);

			List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();

			model.addAttribute("listCategoriesDTO", listCategoriesDTO);
			model.addAttribute("brandDTO", brandDTO);

			return "brands/brand_edit_form";
		} catch (BrandNotFoundException e) {
			if (e.getMessage().contains("Could not find any brand with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy thương hiệu có ID " + id);
			}
			return "redirect:/brands";
		}

	}

	@PostMapping("/brands/edit")
	public String editBrand(Model model, @RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute("brandDTO") BrandDTO brandDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IOException {
		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result brandDTO");
			List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();
			model.addAttribute("listCategoriesDTO", listCategoriesDTO);
			model.addAttribute("id", brandDTO.getId());

			return "brands/brand_edit_form";
		} else {
			if (!FileUploadUtil.isValidFileSize(multipartFile)) {
				List<CategoryDTO> listCategoriesDTO = categoryService.listCategoriesUsedInForm();
				model.addAttribute("listCategoriesDTO", listCategoriesDTO);
				model.addAttribute("error", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");
				model.addAttribute("id", brandDTO.getId());

				return "brands/brand_edit_form";
			} else {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					brandDTO.setLogo(fileName);

					Brand savedBrand = brandService.save(brandDTO);
					String uploadDir = "../brand-logos/" + savedBrand.getId();

					FileUploadUtil.cleanDir(uploadDir);
					FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				} else {
					if (brandDTO.getLogo() == null || "".equals(brandDTO.getLogo())) {
						brandDTO.setLogo(null);
					}
					brandService.save(brandDTO);
				}

				redirectAttributes.addFlashAttribute("message", "Cập nhật thương hiệu thành công");

				return "redirect:/brands";
			}
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(Model model, @PathVariable(name = "id", required = false) Integer id,
			RedirectAttributes redirectAttributes) {
		try {
			brandService.delete(id);
			String brandDir = "../brand-logos/" + id;
			FileUploadUtil.removeDir(brandDir);

			redirectAttributes.addFlashAttribute("message", "Xóa thương hiệu ID " + id + " thành công");

			return "redirect:/brands";
		} catch (BrandNotFoundException e) {
			if (e.getMessage().contains("Could not find any brand with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy thương hiệu có ID " + id);
			}

			return "redirect:/brands";
		}
	}

}
