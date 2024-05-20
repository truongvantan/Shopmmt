package com.shopmmt.common.dto;

public class CategoryPageInfoDTO {
	private int totalPages;
	private long totalItems;

	public CategoryPageInfoDTO() {
		super();
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}

}
