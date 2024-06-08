package com.shopmmt.admin.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.shopmmt.admin.config.ShopmmtUserDetails;
import com.shopmmt.admin.exception.ProductDetailDuplicateException;
import com.shopmmt.admin.services.BrandService;
import com.shopmmt.admin.services.CategoryService;
import com.shopmmt.admin.services.ProductService;
import com.shopmmt.admin.utils.FileUploadUtil;
import com.shopmmt.common.dto.CategoryDTO;
import com.shopmmt.common.dto.ProductDTO;
import com.shopmmt.common.entity.Brand;
import com.shopmmt.common.entity.Product;
import com.shopmmt.common.entity.ProductImage;
import com.shopmmt.common.exception.ProductNotFoundException;
import com.shopmmt.common.validate.ValidateCommon;

import jakarta.validation.Valid;

@Controller
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(model, 1, "id", "asc", null, 0);
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) int pageNum,
			@RequestParam("sortField") String sortField,
			@RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "categoryId", required = false, defaultValue = "0") Integer categoryId) {

		String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

		Page<Product> pageProduct = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
		List<Product> listProducts = pageProduct.getContent();
		
		List<CategoryDTO> listCategories = categoryService.listCategoriesUsedInForm();
		
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
		}

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("reverseSortDir", reverseSortDir);

		model.addAttribute("totalPages", pageProduct.getTotalPages());
		model.addAttribute("totalItems", pageProduct.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);

		return "products/products";
	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.findAll();
		ProductDTO productDTO = new ProductDTO();

		model.addAttribute("listBrands", listBrands);
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Model model,
			@RequestParam(name = "fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(name = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@Valid @ModelAttribute(name = "productDTO") ProductDTO productDTO, BindingResult bindingResult,
			@AuthenticationPrincipal ShopmmtUserDetails loggedUser,
			RedirectAttributes redirectAttributes) throws IOException {
		
		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result productDTO");
			List<Brand> listBrands = brandService.findAll();
			Integer numberOfExistingExtraImages = productDTO.getImages().size();

			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			model.addAttribute("listBrands", listBrands);

			return "products/product_form";
		} else {
			if (!FileUploadUtil.isValidFileSize(mainImageMultipart)
					|| !FileUploadUtil.isValidExtraFileSize(extraImageMultiparts)) {
				List<Brand> listBrands = brandService.findAll();
				Integer numberOfExistingExtraImages = productDTO.getImages().size();

				model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
				model.addAttribute("listBrands", listBrands);
				model.addAttribute("error", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");

				return "products/product_form";
			} else {
				Product product = new Product(productDTO);
				setMainImageName(mainImageMultipart, product);
				setExistingExtraImageNames(imageIDs, imageNames, product);
				setNewExtraImageNames(extraImageMultiparts, product);
				setProductDetails(detailIDs, detailNames, detailValues, product);

				try {
					Product savedProduct = productService.save(product);

					saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
					deleteExtraImagesWeredRemovedOnForm(product);

					redirectAttributes.addFlashAttribute("message", "Thêm mới sản phẩm thành công");
				} catch (ProductDetailDuplicateException e) {
					if (e.getMessage().contains("Duplicate entry")) {
						redirectAttributes.addFlashAttribute("error",
								"Xuất hiện cặp [thuộc tính - giá trị] trùng lặp! Vui lòng kiểm tra lại.");
						return "redirect:/products/new";
					}
				}

				return "redirect:/products";
			}

		}
	}

	private void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
		if (detailNames == null || detailNames.length == 0) {
			return;
		}

		for (int count = 0; count < detailNames.length; count++) {
			System.err.println(
					"id: " + detailIDs[count] + ", name: " + detailNames[count] + ", value: " + detailValues[count]);
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = ValidateCommon.isValidStringIntegerNumber(detailIDs[count])
					? Integer.parseInt(detailIDs[count])
					: 0;

			if (name != null && !"".equals(name) && value != null && !"".equals(value)) {
				if (id != 0) {
					product.addDetail(id, name.trim().toUpperCase(), value.trim().toUpperCase());
				} else {
					product.addDetail(name.trim().toUpperCase(), value.trim().toUpperCase());
				}
			}
		}
	}

	private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}

	private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product savedProduct) throws IOException {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}

		if (extraImageMultiparts.length > 0) {
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";

			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) {
					continue;
				}

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}

	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable(name = "id", required = false) Integer id,
			@PathVariable(name = "status", required = false) boolean enabled, RedirectAttributes redirectAttributes) {
		try {
			productService.updateProductEnabledStatus(id, enabled);
			String status = enabled ? "mở khóa" : "khóa";
			String message = "Đã " + status + " sản phẩm ID " + id;
			redirectAttributes.addFlashAttribute("message", message);

			return "redirect:/products";
		} catch (ProductNotFoundException e) {
			if (e.getMessage().contains("Could not find any product with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm có ID " + id);
			}
			return "redirect:/products";
		}
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(Model model, @PathVariable(name = "id", required = false) Integer id,
			RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);

			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;

			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);

			redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm ID " + id + " thành công");

			return "redirect:/products";
		} catch (ProductNotFoundException e) {
			if (e.getMessage().contains("Could not find any product with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm có ID " + id);
			}

			return "redirect:/products";
		}
	}

	@GetMapping("/products/showEdit/{id}")
	public String showEditProduct(Model model, @PathVariable(name = "id", required = false) Integer id,
			RedirectAttributes redirectAttributes) {

		try {
			Product product = productService.findById(id);
			ProductDTO productDTO = new ProductDTO(product);

			List<Brand> listBrands = brandService.findAll();
			Integer numberOfExistingExtraImages = productDTO.getImages().size();

			model.addAttribute("listBrands", listBrands);
			model.addAttribute("productDTO", productDTO);
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/product_edit_form";
		} catch (ProductNotFoundException e) {
			if (e.getMessage().contains("Could not find any product with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm có ID " + id);
			}

			return "redirect:/products";
		}

	}

	@PostMapping("/products/edit")
	public String editProduct(Model model, @RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(name = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@Valid @ModelAttribute(name = "productDTO") ProductDTO productDTO, BindingResult bindingResult,
			@AuthenticationPrincipal ShopmmtUserDetails loggedUser,
			RedirectAttributes redirectAttributes) throws IOException {
		
		if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Nhân viên kho hàng")) {
			if (loggedUser.hasRole("Salesperson")) {
				Product product = new Product(productDTO);
				productService.saveProductPrice(product);
				
				redirectAttributes.addFlashAttribute("message", "Chỉnh sửa sản phẩm ID " + id + " thành công");
				
				return "redirect:/products";			
			}
		}
		
		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result productDTO");
			List<Brand> listBrands = brandService.findAll();
			Integer numberOfExistingExtraImages = productDTO.getImages().size();

			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("id", id);

			return "products/product_edit_form";
		} else {
			if (!FileUploadUtil.isValidFileSize(mainImageMultipart)
					|| !FileUploadUtil.isValidExtraFileSize(extraImageMultiparts)) {
				List<Brand> listBrands = brandService.findAll();
				Integer numberOfExistingExtraImages = productDTO.getImages().size();

				model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
				model.addAttribute("listBrands", listBrands);
				model.addAttribute("error", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");
				model.addAttribute("id", id);

				return "products/product_edit_form";
			} else {
				Product product = new Product(productDTO);
				setMainImageName(mainImageMultipart, product);
				setExistingExtraImageNames(imageIDs, imageNames, product);
				setNewExtraImageNames(extraImageMultiparts, product);
				setProductDetails(detailIDs, detailNames, detailValues, product);

				try {
					Product savedProduct = productService.save(product);

					saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
					deleteExtraImagesWeredRemovedOnForm(product);

					redirectAttributes.addFlashAttribute("message", "Chỉnh sửa sản phẩm ID " + id + " thành công");
				} catch (ProductDetailDuplicateException e) {
					if (e.getMessage().contains("Duplicate entry")) {
						redirectAttributes.addFlashAttribute("error",
								"Xuất hiện cặp [thuộc tính - giá trị] trùng lặp! Vui lòng kiểm tra lại.");
						return "redirect:/products/showEdit/" + id;
					}
				}

				return "redirect:/products";
			}
		}
	}

	private void deleteExtraImagesWeredRemovedOnForm(Product product) {
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);

		try {
			Files.list(dirPath).forEach(file -> {
				String filename = file.toFile().getName();

				if (!product.containsImageName(filename)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image: " + filename);

					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + filename);
					}
				}

			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
				}
			}
		}
	}

	private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
		if (imageIDs == null || imageIDs.length == 0) {
			return;
		}

		Set<ProductImage> images = new HashSet<ProductImage>();

		for (int count = 0; count < imageIDs.length; count++) {

			Integer id = ValidateCommon.isValidStringIntegerNumber(imageIDs[count]) ? Integer.parseInt(imageIDs[count])
					: 0;
			String name = imageNames[count];

			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);

	}

	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(Model model, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.findById(id);
			model.addAttribute("product", product);

			return "products/product_detail_modal";

		} catch (ProductNotFoundException e) {
			if (e.getMessage().contains("Could not find any product with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm có ID " + id);
			}

			return "redirect:/products";
		}
	}
}
