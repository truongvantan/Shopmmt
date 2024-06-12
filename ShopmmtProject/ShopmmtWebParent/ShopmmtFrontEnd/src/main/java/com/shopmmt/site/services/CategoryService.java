package com.shopmmt.site.services;

import java.util.List;

import com.shopmmt.common.entity.Category;
import com.shopmmt.common.exception.CategoryNotFoundException;

public interface CategoryService {
	List<Category> listCategoriesWithoutChildren();
	
	Category getCategoryByName(String name) throws CategoryNotFoundException;
	
	List<Category> getCategoryParents(Category child);

	List<Category> listRootParentCategories();
}
