package com.shopmmt.site.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.entity.Product;
import com.shopmmt.common.exception.ProductNotFoundException;
import com.shopmmt.site.repositories.ProductRepository;
import com.shopmmt.site.services.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Page<Product> listByCategory(int pageNum, Integer categoryId) {
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, ConstantsUtil.PRODUCT_PAGE_SIZE_CUSTOMER);

		return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
	}

	@Override
	public Product getProductByName(String name) throws ProductNotFoundException {
		Product product = productRepository.findByName(name);
		if (product == null) {
			throw new ProductNotFoundException("Could not find any product with name " + name);
		}
		
		return product;
	}

	@Override
	public Page<Product> search(String keyword, int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, ConstantsUtil.SEARCH_RESULTS_PER_PAGE);
		
		return productRepository.search(keyword, pageable);
	}

}
