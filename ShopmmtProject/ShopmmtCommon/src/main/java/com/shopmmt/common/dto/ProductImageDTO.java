package com.shopmmt.common.dto;

import com.shopmmt.common.entity.Product;
import com.shopmmt.common.entity.ProductImage;

public class ProductImageDTO {
	private Integer id;
	private String name;
	private Product product;

	public ProductImageDTO() {
		super();
	}

	public ProductImageDTO(String name, Product product) {
		super();
		this.name = name;
		this.product = product;
	}
	
	public ProductImageDTO(ProductImage productImage) {
		this.id = productImage.getId();
		this.name = productImage.getName();
		this.product = productImage.getProduct();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
