package com.shopmmt.common.entity;

import java.util.HashSet;
import java.util.Set;

import com.shopmmt.common.Constants;
import com.shopmmt.common.dto.CategoryDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	private String photos;

	private boolean enabled = Boolean.TRUE;

	@Column(name = "all_parent_ids", nullable = true)
	private String allParentIDs;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<Category>();

	public Category() {
		super();
	}

	public Category(Category category) {
		this.setId(category.getId());
		this.setName(category.getName());
		this.setPhotos(category.getPhotos());
		this.setEnabled(category.isEnabled());
		this.setHasChildren(category.getChildren().size() > 0);
	}

	public Category(Category category, String name) {
		this.setName(name);
		this.setId(category.getId());
		this.setPhotos(category.getPhotos());
		this.setEnabled(category.isEnabled());
		this.setHasChildren(category.getChildren().size() > 0);
	}

	public Category(Integer id) {
		super();
		this.id = id;
	}

	public Category(String name) {
		super();
		this.name = name;
	}

	public Category(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Category(String name, Category parent) {
		super();
		this.name = name;
		this.parent = parent;
	}

	public Category(Integer id, String name, String photos, boolean enabled, Category parent, Set<Category> children) {
		super();
		this.id = id;
		this.name = name;
		this.photos = photos;
		this.enabled = enabled;
		this.parent = parent;
		this.children = children;
	}

	public Category(CategoryDTO categoryDTO) {
		this.id = categoryDTO.getId();
		this.name = categoryDTO.getName();
		this.photos = categoryDTO.getPhotos();
		this.enabled = categoryDTO.isEnabled();
		this.parent = categoryDTO.getParent();
		this.children = categoryDTO.getChildren();
		this.allParentIDs = categoryDTO.getAllParentIDs();
	}

	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
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
		return "Category [id=" + id + ", name=" + name + ", enabled=" + enabled + "]";
	}

	@Transient
	public void convertFromDTO(CategoryDTO categoryDTO) {
		this.setName(categoryDTO.getName());
		this.setPhotos(categoryDTO.getPhotos());
		this.setEnabled(categoryDTO.isEnabled());
		this.setParent(categoryDTO.getParent());
		this.setChildren(categoryDTO.getChildren());
	}

	@Transient
	public String getPhotosImagePath() {
		if (this.id == null || this.photos == null) {
			return "/images/image-thumbnail.png";
		}
		return Constants.S3_BASE_URI + "/category-images/" + this.id + "/" + this.photos;
	}

	@Transient
	private boolean hasChildren;

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

}
