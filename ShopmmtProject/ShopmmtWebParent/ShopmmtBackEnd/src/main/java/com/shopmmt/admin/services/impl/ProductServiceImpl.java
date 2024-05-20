package com.shopmmt.admin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopmmt.admin.repositories.ProductRepository;
import com.shopmmt.admin.services.ProductService;
import com.shopmmt.common.entity.Product;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<Product> listAll() {
		return productRepository.findAll();
	}
}
