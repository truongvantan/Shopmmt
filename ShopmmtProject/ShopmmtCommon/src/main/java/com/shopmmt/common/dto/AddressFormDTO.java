package com.shopmmt.common.dto;

import com.shopmmt.common.entity.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddressFormDTO {
	
	private Integer id;
	
	@NotBlank(message = "Vui lòng nhập tên.")
	@Size(max = 50, message = "Tối đa 50 ký tự.")
	private String firstName;
	
	@NotBlank(message = "Vui lòng nhập họ đệm.")
	@Size(max = 50, message = "Tối đa 50 ký tự.")
	private String lastName;
	
	@NotBlank(message = "Vui lòng nhập số điện thoại.")
	@Size(max = 15, message = "Tối đa 15 ký tự.")
	private String phoneNumber;
	
	@NotBlank(message = "Vui lòng nhập địa chỉ.")
	@Size(max = 64, message = "Tối đa 64 ký tự.")
	private String addressLine1;
	
	private String addressLine2;
	
	@NotBlank(message = "Vui lòng nhập Tỉnh/Thành phố.")
	@Size(max = 50, message = "Tối đa 50 ký tự.")
	private String city;
	
	@NotBlank(message = "Vui lòng nhập Quận/Huyện.")
	@Size(max = 50, message = "Tối đa 50 ký tự.")
	private String state;
	
	@NotBlank(message = "Vui lòng nhập mã bưu chính.")
	@Size(max = 10, message = "Tối đa 10 ký tự.")
	private String postalCode;
	
	private boolean defaultForShipping;

	public AddressFormDTO() {
		super();
	}
	
	public AddressFormDTO(Address address) {
		this.id = address.getId();
		this.firstName = address.getFirstName();
		this.lastName = address.getLastName();
		this.phoneNumber = address.getPhoneNumber();
		this.addressLine1 = address.getAddressLine1();
		this.addressLine2 = address.getAddressLine2();
		this.city = address.getCity();
		this.state = address.getState();
		this.postalCode = address.getPostalCode();
		this.defaultForShipping = address.isDefaultForShipping();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public boolean isDefaultForShipping() {
		return defaultForShipping;
	}

	public void setDefaultForShipping(boolean defaultForShipping) {
		this.defaultForShipping = defaultForShipping;
	}
	
	
}
