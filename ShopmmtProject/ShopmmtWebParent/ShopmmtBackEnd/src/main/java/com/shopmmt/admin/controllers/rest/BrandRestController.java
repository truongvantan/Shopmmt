package com.shopmmt.admin.controllers.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopmmt.admin.dto.CategoryBrandDTO;
import com.shopmmt.admin.exception.BrandNotFoundException;
import com.shopmmt.admin.exception.BrandNotFoundRestException;
import com.shopmmt.admin.services.BrandService;
import com.shopmmt.common.entity.Brand;
import com.shopmmt.common.entity.Category;

@RestController
public class BrandRestController {
	
	@Autowired
	private BrandService brandService;
	
	@PostMapping("/brands/check_brand_name")
	public String checkDuplicateBrandName(@Param("id") String id, @Param("name") String name) {
		return brandService.isBrandNameUnique(id, name) ? "OK" : "Duplicated";
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryBrandDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException {
		List<CategoryBrandDTO> listCategories = new ArrayList<CategoryBrandDTO>();
		try {
			Brand brand = brandService.findById(brandId);
			Set<Category> categories = brand.getCategories();
			for (Category category : categories) {
				CategoryBrandDTO categoryBrandDTO = new CategoryBrandDTO(category.getId(), category.getName());
				listCategories.add(categoryBrandDTO);
			}
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
		return listCategories;
	}
}
