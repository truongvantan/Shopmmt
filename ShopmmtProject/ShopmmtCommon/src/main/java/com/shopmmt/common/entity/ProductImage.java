package com.shopmmt.common.entity;

import com.shopmmt.common.Constants;
import com.shopmmt.common.dto.ProductDTO;
import com.shopmmt.common.dto.ProductImageDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "product_images")
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductImage() {
		super();
	}
	
	public ProductImage(Integer id, String name, Product product) {
		this.id = id;
		this.name = name;
		this.product = product;
	}

	public ProductImage(String name, Product product) {
		this.name = name;
		this.product = product;
	}
	
	public ProductImage(String name, ProductDTO productDTO) {
		this.name = name;
		this.product = new Product(productDTO);
	}
	
	public ProductImage(ProductImageDTO productImageDTO) {
		this.id = productImageDTO.getId();
		this.name = productImageDTO.getName();
		this.product = productImageDTO.getProduct();
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
	
	@Transient
	public String getImagePath() {
		return Constants.S3_BASE_URI + "/product-images/" + product.getId() + "/extras/" + this.name;
	}
	
}
