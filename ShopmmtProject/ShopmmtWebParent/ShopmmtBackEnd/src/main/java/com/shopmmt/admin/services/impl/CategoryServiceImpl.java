package com.shopmmt.admin.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopmmt.admin.repositories.CategoryRepository;
import com.shopmmt.admin.services.CategoryService;
import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.dto.CategoryDTO;
import com.shopmmt.common.dto.CategoryPageInfoDTO;
import com.shopmmt.common.entity.Category;
import com.shopmmt.common.exception.CategoryNotFoundException;

import jakarta.validation.Valid;

@Service("categoryService")
@Transactional(rollbackFor = Exception.class)
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> listByPage(CategoryPageInfoDTO categoryPageInfoDTO, int pageNum, String sortDir,
			String keyword) {
		Sort sort = Sort.by("name");
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ConstantsUtil.CATEGORY_PAGE_SIZE, sort);
		Page<Category> pageRootCategories = null;
		if (keyword != null) {
			pageRootCategories = categoryRepository.search(keyword, pageable);
		} else {
			pageRootCategories = categoryRepository.findRootCategories(pageable);
		}

		List<Category> rootCategories = pageRootCategories.getContent();

		categoryPageInfoDTO.setTotalPages(pageRootCategories.getTotalPages());
		categoryPageInfoDTO.setTotalItems(pageRootCategories.getTotalElements());

		if (keyword != null) {
			List<Category> searchCategories = pageRootCategories.getContent();

			for (Category category : searchCategories) {
				category.setHasChildren(category.getChildren().size() > 0);
			}

			return searchCategories;
		} else {
			return listCategoriesTree(rootCategories, sortDir);
		}

	}

	private List<Category> listCategoriesTree(List<Category> rootCategories, String sortDir) {
		List<Category> categoriesTree = new ArrayList<Category>();

		for (Category rootCategory : rootCategories) {
			categoriesTree.add(new Category(rootCategory));
			Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

			for (Category subCategory : children) {
				String name = ">>" + subCategory.getName();
				categoriesTree.add(new Category(subCategory, name));

				listSubCategoriesTree(categoriesTree, subCategory, 1, sortDir);
			}
		}

		return categoriesTree;
	}

	private void listSubCategoriesTree(List<Category> categoriesTree, Category parent, int subLevel, String sortDir) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += ">>";
			}
			name += subCategory.getName();

			categoriesTree.add(new Category(subCategory, name));

			listSubCategoriesTree(categoriesTree, subCategory, newSubLevel + 1, sortDir);
		}
	}

	@Override
	public List<CategoryDTO> listCategoriesUsedInForm() {
		List<CategoryDTO> listCategoriesUsedInForm = new ArrayList<CategoryDTO>();
		List<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				listCategoriesUsedInForm.add(new CategoryDTO(category));

				Set<Category> children = sortSubCategories(category.getChildren());
				for (Category subCategory : children) {
					String name = ">>" + subCategory.getName();
					listCategoriesUsedInForm.add(new CategoryDTO(subCategory.getId(), name));

					listSubCategoriesUsedInForm(listCategoriesUsedInForm, subCategory, 1);
				}
			}
		}

		return listCategoriesUsedInForm;
	}

	private void listSubCategoriesUsedInForm(List<CategoryDTO> listCategoriesUsedInForm, Category parent,
			int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += ">>";
			}
			name += subCategory.getName();

			listCategoriesUsedInForm.add(new CategoryDTO(subCategory.getId(), name));

			listSubCategoriesUsedInForm(listCategoriesUsedInForm, subCategory, newSubLevel);
		}
	}

	@Override
	public boolean isCategoryNameUnique(String id, String name) {
		if (name != null) {
			name = name.trim();
		}

		boolean isCreatingNew = (id == null || "".equals(id));

		Category categoryByName = categoryRepository.findByName(name);

		if (isCreatingNew) {
			if (categoryByName != null) {
				return false;
			}
		} else {
			if (categoryByName != null && categoryByName.getId() != Integer.valueOf(id)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Category save(@Valid CategoryDTO categoryDTO) {
		Category category = new Category(categoryDTO);
		Category parent = category.getParent();
		if (parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) + "-";
			category.setAllParentIDs(allParentIds);
		}
		
		return categoryRepository.save(category);
	}

	@Override
	public Category findById(Integer id) throws CategoryNotFoundException {
		try {
			return categoryRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
	}

	@Override
	public Category edit(@Valid CategoryDTO categoryDTO) {
		Category category = categoryRepository.findById(categoryDTO.getId()).get();
		category.convertFromDTO(categoryDTO);
		
		Category parent = category.getParent();
		if (parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) + "-";
			category.setAllParentIDs(allParentIds);
		}

		return categoryRepository.save(category);
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children, "asc");
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
		SortedSet<Category> sortedChildren = new TreeSet<Category>(new Comparator<Category>() {
			@Override
			public int compare(Category cat1, Category cat2) {
				if ("asc".equals(sortDir)) {
					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});

		sortedChildren.addAll(children);

		return sortedChildren;
	}

	@Override
	public void updateCategoryEnabledStatus(Integer id, boolean enabled) throws CategoryNotFoundException {
		Long countById = categoryRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}

		categoryRepository.updateEnabledStatus(id, enabled);
	}

	@Override
	public void delete(Integer id) throws CategoryNotFoundException, DataIntegrityViolationException {
		Long countById = categoryRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}

		try {
			categoryRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw e;
		}
	}

}
