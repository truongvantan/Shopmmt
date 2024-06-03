package com.shopmmt.admin.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopmmt.admin.exception.ProductDetailDuplicateException;
import com.shopmmt.admin.exception.ProductNotFoundException;
import com.shopmmt.common.dto.ProductDTO;
import com.shopmmt.common.entity.Product;

public interface ProductService {

	List<Product> listAll();
	
	Product save(ProductDTO productDTO);

	boolean isProductNameUnique(String id, String name);

	void updateProductEnabledStatus(Integer id, boolean enabled) throws ProductNotFoundException;

	void delete(Integer id) throws ProductNotFoundException;

	Product save(Product product) throws ProductDetailDuplicateException;

	boolean isProductDetailsUnique(String productId, String jsonDetailNames, String jsonDetailValues);

	Product findById(Integer id) throws ProductNotFoundException;
	
	Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId);

}
