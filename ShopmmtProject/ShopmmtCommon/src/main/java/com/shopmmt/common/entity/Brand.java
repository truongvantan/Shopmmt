package com.shopmmt.common.entity;

import java.util.HashSet;
import java.util.Set;

import com.shopmmt.common.Constants;
import com.shopmmt.common.dto.BrandDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "brands")
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	private String logo;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "brands_categories", joinColumns = @JoinColumn(name = "brand_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<Category>();

	public Brand() {
		super();
	}

	public Brand(Integer id) {
		super();
		this.id = id;
	}

	public Brand(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Brand(String name) {
		super();
		this.name = name;
	}

	public Brand(Integer id, String name, String logo, Set<Category> categories) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.categories = categories;
	}
	
	public Brand(BrandDTO brandDTO) {
		this.id = brandDTO.getId();
		this.name = brandDTO.getName();
		this.logo = brandDTO.getLogo();
		this.categories = brandDTO.getCategories();
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
		return "Brand [id=" + id + ", name=" + name + "]";
	}
	
	@Transient
	public String getLogoPath() {
		if (this.id == null) return "/images/image-thumbnail.png";
		
		return Constants.S3_BASE_URI +  "/brand-logos/" + this.id + "/" + this.logo;		
	}

}
