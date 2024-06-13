package com.shopmmt.admin.dto;

public class ProductInfoOrderDTO {

	private String name;
	private String imagePath;
	private double price;
	private double cost;

	public ProductInfoOrderDTO() {
		super();
	}

	public ProductInfoOrderDTO(String name, String imagePath, double price, double cost) {
		this.name = name;
		this.imagePath = imagePath;
		this.price = price;
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

}
