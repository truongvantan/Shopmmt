package com.shopmmt.common.dto;

import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.enums.AuthenticationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerRegisterFormDTO {
	private Integer id;

	@Pattern(regexp = ConstantsUtil.REGEX_EMAIL, message = "Email không đúng định dạng (vd: abc@gmail.com).")
	private String email;

	@Pattern(regexp = ConstantsUtil.REGEX_PASSWORD, message = "Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 60 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.")
	private String password;

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

	private AuthenticationType authenticationType;

	public CustomerRegisterFormDTO() {
		super();
	}

	public CustomerRegisterFormDTO(Customer customer) {
		this.id = customer.getId();
		this.email = customer.getEmail();
		this.password = customer.getPassword();
		this.firstName = customer.getFirstName();
		this.lastName = customer.getLastName();
		this.phoneNumber = customer.getPhoneNumber();
		this.addressLine1 = customer.getAddressLine1();
		this.addressLine2 = customer.getAddressLine2();
		this.city = customer.getCity();
		this.state = customer.getState();
		this.postalCode = customer.getPostalCode();
		this.authenticationType = customer.getAuthenticationType();
	}

	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	@Override
	public String toString() {
		return "CustomerRegisterFormDTO [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName="
				+ lastName + "]";
	}

	public String getFullName() {
		return lastName + " " + firstName;
	}

}
