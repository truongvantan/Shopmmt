package com.shopmmt.admin.dto;

public class CategoryBrandDTO {
	private Integer id;
	private String name;

	public CategoryBrandDTO() {
		super();
	}

	public CategoryBrandDTO(Integer id, String name) {
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

}
