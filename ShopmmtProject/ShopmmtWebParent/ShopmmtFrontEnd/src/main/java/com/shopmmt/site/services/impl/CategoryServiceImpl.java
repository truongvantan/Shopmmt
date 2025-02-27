package com.shopmmt.site.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopmmt.common.entity.Category;
import com.shopmmt.common.exception.CategoryNotFoundException;
import com.shopmmt.site.repositories.CategoryRepository;
import com.shopmmt.site.services.CategoryService;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> listCategoriesWithoutChildren() {
		List<Category> listNoChildrenCategories = new ArrayList<Category>();

		List<Category> listEnabledCategories = categoryRepository.findAllEnabled();

		listEnabledCategories.forEach(category -> {
			Set<Category> children = category.getChildren();
			if (children == null || children.size() == 0) {
				listNoChildrenCategories.add(category);
			}
		});

		return listNoChildrenCategories;
	}

	@Override
	public Category getCategoryByName(String name) throws CategoryNotFoundException {
		Category category = categoryRepository.findByNameEnabled(name);
		
		if (category == null) {
			throw new CategoryNotFoundException("Could not find any categories with name " + name);
		}
		
		return category;
	}

	@Override
	public List<Category> getCategoryParents(Category child) {
		List<Category> listParents = new ArrayList<>();

		Category parent = child.getParent();

		while (parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}

		listParents.add(child);

		return listParents;
	}

	@Override
	public List<Category> listRootParentCategories() {
		return categoryRepository.listRootParentCategories();
	}

}
