package com.shopmmt.common.dto;

import java.util.HashSet;
import java.util.Set;

import com.shopmmt.common.entity.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {
	private Integer id;

	@NotBlank(message = "Vui lòng nhập tên danh mục")
	@Size(max = 50, message = "Tối đa 50 ký tự.")
	private String name;

	private String photos;

	private boolean enabled = Boolean.TRUE;

	private Category parent;
	private Set<Category> children = new HashSet<Category>();

	public CategoryDTO() {
		super();
	}

	public CategoryDTO(Integer id,
			@NotBlank(message = "Vui lòng nhập tên danh mục") @Size(max = 50, message = "Tối đa 50 ký tự.") String name,
			String photos, boolean enabled, Category parent, Set<Category> children) {
		super();
		this.id = id;
		this.name = name;
		this.photos = photos;
		this.enabled = enabled;
		this.parent = parent;
		this.children = children;
	}

	public CategoryDTO(Category category) {
		this.id = category.getId();
		this.name = category.getName();
		this.photos = category.getPhotos();
		this.enabled = category.isEnabled();
		this.parent = category.getParent();
		this.children = category.getChildren();
	}

	public CategoryDTO(Integer id,
			@NotBlank(message = "Vui lòng nhập tên danh mục") @Size(max = 50, message = "Tối đa 50 ký tự.") String name) {
		super();
		this.id = id;
		this.name = name;
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

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "CategoryDTO [id=" + id + ", name=" + name + ", photos=" + photos + ", enabled=" + enabled + "]";
	}
	
	public String getPhotosImagePath() {
		if (this.id == null || this.photos == null) {
			return "/images/image-thumbnail.png";
		}
		return "/category-images/" + this.id + "/" + this.photos;
	}
	
	private boolean hasChildren;

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

}
