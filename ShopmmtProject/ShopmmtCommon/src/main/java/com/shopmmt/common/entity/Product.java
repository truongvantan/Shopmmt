package com.shopmmt.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.shopmmt.common.Constants;
import com.shopmmt.common.dto.ProductDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(name = "short_description", length = 512, nullable = false)
	private String shortDescription;

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

	@Column(name = "main_image", nullable = false)
	private String mainImage;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductImage> images = new HashSet<ProductImage>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductDetail> details = new ArrayList<ProductDetail>();

	public Product() {
		super();
	}

	public Product(Integer id) {
		super();
		this.id = id;
	}

	public Product(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.shortDescription = product.getShortDescription();
		this.fullDescription = product.getFullDescription();
		this.enabled = product.isEnabled();
		this.inStock = product.isInStock();
		this.cost = product.getCost();
		this.price = product.getPrice();
		this.discountPercent = product.getDiscountPercent();
		this.createdTime = product.getCreatedTime();
		this.updatedTime = product.getUpdatedTime();
		this.category = product.getCategory();
		this.brand = product.getBrand();
		this.mainImage = product.getMainImage();
		this.images = product.getImages();
		this.details = product.getDetails();
	}

	public Product(ProductDTO productDTO) {
		this.id = productDTO.getId();
		this.name = productDTO.getName();
		this.shortDescription = productDTO.getShortDescription();
		this.fullDescription = productDTO.getFullDescription();
		this.enabled = productDTO.isEnabled();
		this.inStock = productDTO.isInStock();
		this.cost = productDTO.getCost();
		this.price = productDTO.getPrice();
		this.discountPercent = productDTO.getDiscountPercent();
		this.createdTime = productDTO.getCreatedTime();
		this.updatedTime = productDTO.getUpdatedTime();
		this.category = productDTO.getCategory();
		this.brand = productDTO.getBrand();
		this.mainImage = productDTO.getMainImage();
		this.images = productDTO.getImages();
		this.details = productDTO.getDetails();
	}

	public List<ProductDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductDetail> details) {
		this.details = details;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public Set<ProductImage> getImages() {
		return images;
	}

	public void setImages(Set<ProductImage> images) {
		this.images = images;
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

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
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
		return "Product [id=" + id + ", name=" + name + "]";
	}

	public void addExtraImage(String imageName) {
		this.images.add(new ProductImage(imageName, this));
	}

	@Transient
	public String getMainImagePath() {
		if (id == null || mainImage == null) {
			return "/images/image-thumbnail.png";
		}

		return Constants.S3_BASE_URI + "/product-images/" + this.id + "/" + this.mainImage;
	}

	public void addDetail(String name, String value) {
		this.details.add(new ProductDetail(name, value, this));
	}

	public boolean containsImageName(String imageName) {
		Iterator<ProductImage> iterator = images.iterator();

		while (iterator.hasNext()) {
			ProductImage image = iterator.next();
			if (image.getName().equals(imageName)) {
				return true;
			}
		}

		return false;
	}

	public void addDetail(Integer id, String name, String value) {
		this.details.add(new ProductDetail(id, name, value, this));
	}

	@Transient
	public double getDiscountPrice() {
		if (discountPercent > 0) {
			return price * ((100 - discountPercent) / 100);
		}

		return this.price;
	}

	@Transient
	public String getShortName() {
		if (name.length() > 50) {
			return name.substring(0, 50).concat("...");
		}
		return name;
	}

}
