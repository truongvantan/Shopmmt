package com.shopmmt.common.entity;

import java.util.Date;

import com.shopmmt.common.dto.ProductDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(name = "sort_description", length = 512, nullable = false)
	private String sortDescription;

	@Column(name = "full_description", length = 4096, nullable = false)
	private String fullDescription;

	private boolean enabled = Boolean.TRUE;

	@Column(name = "in_stock")
	private boolean inStock = Boolean.TRUE;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;

	private double cost;
	private double price;

	@Column(name = "discount_percent")
	private float discountPercent;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;

	public Product() {
		super();
	}
	
	public Product(ProductDTO productDTO) {
		this.id = productDTO.getId();
		this.name = productDTO.getName();
		this.sortDescription = productDTO.getSortDescription();
		this.fullDescription = productDTO.getFullDescription();
		this.enabled = productDTO.isEnabled();
		this.inStock = productDTO.isInStock();
		this.cost = productDTO.getCost();
		this.price = productDTO.getPrice();
		this.discountPercent = productDTO.getDiscountPercent();
		this.category = productDTO.getCategory();
		this.brand = productDTO.getBrand();
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
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

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", cost=" + cost + ", price=" + price + "]";
	}
	
	

}
