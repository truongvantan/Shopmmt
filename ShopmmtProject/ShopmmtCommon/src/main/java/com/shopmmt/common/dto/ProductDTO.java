package com.shopmmt.common.dto;

import com.shopmmt.common.entity.Brand;
import com.shopmmt.common.entity.Category;
import com.shopmmt.common.entity.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductDTO {

	private Integer id;

	@NotBlank(message = "Vui lòng nhập tên sản phẩm")
	@Size(max = 255, message = "Tối đa 255 ký tự.")
	private String name;

	private String sortDescription;
	private String fullDescription;
	private boolean enabled = Boolean.TRUE;
	private boolean inStock = Boolean.TRUE;
	private double cost;
	private double price;
	private float discountPercent;
	private Category category;
	private Brand brand;

	public ProductDTO() {
		super();
	}
	
	public ProductDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.sortDescription = product.getSortDescription();
		this.fullDescription = product.getFullDescription();
		this.enabled = product.isEnabled();
		this.inStock = product.isInStock();
		this.cost = product.getCost();
		this.price = product.getPrice();
		this.discountPercent = product.getDiscountPercent();
		this.category = product.getCategory();
		this.brand = product.getBrand();
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

	public String getSortDescription() {
		return sortDescription;
	}

	public void setSortDescription(String sortDescription) {
		this.sortDescription = sortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

}
