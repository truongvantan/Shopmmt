package com.shopmmt.site.services;

import org.springframework.data.domain.Page;

import com.shopmmt.common.entity.Product;
import com.shopmmt.common.exception.ProductNotFoundException;

public interface ProductService {

	Page<Product> listByCategory(int pageNum, Integer categoryId);
	
	Product getProductByName(String name) throws ProductNotFoundException;
	
	Page<Product> search(String keyword, int pageNum);

}
