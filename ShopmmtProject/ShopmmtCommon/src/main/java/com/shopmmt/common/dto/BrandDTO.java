package com.shopmmt.common.dto;

import java.util.HashSet;
import java.util.Set;

import com.shopmmt.common.Constants;
import com.shopmmt.common.entity.Brand;
import com.shopmmt.common.entity.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BrandDTO {
	private Integer id;

	@NotBlank(message = "Vui lòng nhập tên thương hiệu")
	@Size(max = 50, message = "Tối đa 50 ký tự.")
	private String name;

	private String logo;

	private Set<Category> categories = new HashSet<Category>();

	public BrandDTO() {
		super();
	}

	public BrandDTO(Integer id,
			@NotBlank(message = "Vui lòng nhập tên thương hiệu") @Size(max = 50, message = "Tối đa 50 ký tự.") String name,
			String logo, Set<Category> categories) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.categories = categories;
	}

	public BrandDTO(Brand brand) {
		this.id = brand.getId();
		this.name = brand.getName();
		this.logo = brand.getLogo();
		this.categories = brand.getCategories();
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "BrandDTO [id=" + id + ", name=" + name + "]";
	}
	
	public String getLogoPath() {
		if (this.id == null) return "/images/image-thumbnail.png";
		
		return Constants.S3_BASE_URI +  "/brand-logos/" + this.id + "/" + this.logo;		
	}

}
