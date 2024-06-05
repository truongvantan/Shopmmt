package com.shopmmt.admin.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.shopmmt.common.dto.CategoryDTO;
import com.shopmmt.common.dto.CategoryPageInfoDTO;
import com.shopmmt.common.entity.Category;
import com.shopmmt.common.exception.CategoryNotFoundException;

import jakarta.validation.Valid;

public interface CategoryService {

	List<Category> listByPage(CategoryPageInfoDTO categoryPageInfoDTO, int pageNum, String sortDir, String keyword);

	List<CategoryDTO> listCategoriesUsedInForm();

	boolean isCategoryNameUnique(String id, String name);

	Category save(@Valid CategoryDTO categoryDTO);

	Category findById(Integer id) throws CategoryNotFoundException;

	Category edit(@Valid CategoryDTO categoryDTO);

	void updateCategoryEnabledStatus(Integer id, boolean enabled) throws CategoryNotFoundException;

	void delete(Integer id) throws CategoryNotFoundException, DataIntegrityViolationException;
	
}
