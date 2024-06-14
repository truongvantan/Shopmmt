package com.shopmmt.common.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.shopmmt.common.Constants;
import com.shopmmt.common.entity.Brand;
import com.shopmmt.common.entity.Category;
import com.shopmmt.common.entity.Product;
import com.shopmmt.common.entity.ProductDetail;
import com.shopmmt.common.entity.ProductImage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductDTO {

	private Integer id;

	@NotBlank(message = "Vui lòng nhập tên sản phẩm")
	@Size(max = 255, message = "Tối đa 255 ký tự.")
	private String name;

	@NotBlank(message = "Vui lòng nhập mô tả tổng quát")
	private String shortDescription;

	@NotBlank(message = "Vui lòng nhập mô tả chi tiết")
	private String fullDescription;

	private boolean enabled = Boolean.TRUE;
	private boolean inStock = Boolean.TRUE;
	private double cost;
	private double price;
	private float discountPercent;

	private Date createdTime;
	private Date updatedTime;

	private Category category;
	private Brand brand;

	private String mainImage;
	private Set<ProductImage> images = new HashSet<ProductImage>();
	private List<ProductDetail> details = new ArrayList<ProductDetail>();

	public ProductDTO() {
		super();
	}

	public ProductDTO(Product product) {
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
		return "ProductDTO [id=" + id + ", name=" + name + ", cost=" + cost + ", price=" + price + "]";
	}

	public void addExtraImage(String imageName) {
		this.images.add(new ProductImage(imageName, this));
	}

	public String getMainImagePath() {
		if (id == null || mainImage == null) {
			return "/images/image-thumbnail.png";
		}

		return Constants.S3_BASE_URI + "/product-images/" + this.id + "/" + this.mainImage;
	}

	public void convertFromProduct(Product product) {
		this.setId(product.getId());
		this.setName(product.getName());
		this.setShortDescription(product.getShortDescription());
		this.setFullDescription(product.getFullDescription());
		this.setEnabled(product.isEnabled());
		this.setInStock(product.isInStock());
		this.setCost(product.getCost());
		this.setPrice(product.getPrice());
		this.setDiscountPercent(product.getDiscountPercent());
		this.setCreatedTime(product.getCreatedTime());
		this.setUpdatedTime(product.getUpdatedTime());
		this.setCategory(product.getCategory());
		this.setBrand(product.getBrand());
		this.setMainImage(product.getMainImage());
		this.setImages(product.getImages());
		this.setDetails(product.getDetails());
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
	
	public double getDiscountPrice() {
		if (discountPercent > 0) {
			return price * ((100 - discountPercent) / 100);
		}
		
		return this.price;
	}
	
	public String getShortName() {
		if (name.length() > 50) {
			return name.substring(0, 50).concat("...");
		}
		return name;
	}

}
