package com.shopmmt.common.entity;

import java.util.Date;

import com.shopmmt.common.dto.CustomerAccountDetailDTO;
import com.shopmmt.common.dto.CustomerRegisterFormDTO;
import com.shopmmt.common.enums.AuthenticationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true, length = 128)
	private String email;

	@Column(nullable = false, length = 64)
	private String password;

	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@Column(name = "phone_number", nullable = false, length = 15)
	private String phoneNumber;

	@Column(name = "address_line_1", nullable = false, length = 64)
	private String addressLine1;

	@Column(name = "address_line_2", length = 64)
	private String addressLine2;

	@Column(nullable = false, length = 50)
	private String city;

	@Column(nullable = false, length = 50)
	private String state;

	@Column(name = "postal_code", nullable = false, length = 10)
	private String postalCode;

	@Column(name = "verification_code", length = 64)
	private String verificationCode;

	private boolean enabled;

	@Column(name = "created_time")
	private Date createdTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "authentication_type", length = 10)
	private AuthenticationType authenticationType;

	@Column(name = "reset_password_token", length = 64)
	private String resetPasswordToken;

	public Customer() {
		super();
	}

	public Customer(Integer id) {
		super();
		this.id = id;
	}

	public Customer(CustomerRegisterFormDTO customerDTO) {
		this.id = customerDTO.getId();
		this.email = customerDTO.getEmail();
		this.password = customerDTO.getPassword();
		this.firstName = customerDTO.getFirstName();
		this.lastName = customerDTO.getLastName();
		this.phoneNumber = customerDTO.getPhoneNumber();
		this.addressLine1 = customerDTO.getAddressLine1();
		this.addressLine2 = customerDTO.getAddressLine2();
		this.city = customerDTO.getCity();
		this.state = customerDTO.getState();
		this.postalCode = customerDTO.getPostalCode();
		this.authenticationType = customerDTO.getAuthenticationType();
	}

	public Customer(CustomerAccountDetailDTO customerDTO) {
		this.id = customerDTO.getId();
		this.email = customerDTO.getEmail();
		this.password = customerDTO.getPassword();
		this.firstName = customerDTO.getFirstName();
		this.lastName = customerDTO.getLastName();
		this.phoneNumber = customerDTO.getPhoneNumber();
		this.addressLine1 = customerDTO.getAddressLine1();
		this.addressLine2 = customerDTO.getAddressLine2();
		this.city = customerDTO.getCity();
		this.state = customerDTO.getState();
		this.postalCode = customerDTO.getPostalCode();
		this.authenticationType = customerDTO.getAuthenticationType();
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
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

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public String toString() {
		String address = lastName != null ? lastName : "";

		if (firstName != null && !firstName.isEmpty()) {
			address += " " + firstName;
		}

		if (addressLine1 != null && !addressLine1.isEmpty()) {
			address += ", " + addressLine1;
		}

		if (addressLine2 != null && !addressLine2.isEmpty()) {
			address += ", " + addressLine2;
		}

		if (state != null && !state.isEmpty()) {
			address += ", " + state;
		}

		if (city != null && !city.isEmpty()) {
			address += ", " + city;
		}

		if (postalCode != null && !postalCode.isEmpty()) {
			address += ". Mã bưu chính: " + postalCode;
		}

		if (phoneNumber != null && !phoneNumber.isEmpty()) {
			address += ". Số điện thoại: " + phoneNumber;
		}

		return address;
	}

	public String getFullName() {
		return lastName + " " + firstName;
	}

	@Transient
	public String getAddress() {
		String address = lastName != null ? lastName : "";

		if (firstName != null && !firstName.isEmpty()) {
			address += " " + firstName;
		}

		if (addressLine1 != null && !addressLine1.isEmpty()) {
			address += ", " + addressLine1;
		}

		if (addressLine2 != null && !addressLine2.isEmpty()) {
			address += ", " + addressLine2;
		}

		if (state != null && !state.isEmpty()) {
			address += ", " + state;
		}

		if (city != null && !city.isEmpty()) {
			address += ", " + city;
		}

		if (postalCode != null && !postalCode.isEmpty()) {
			address += ". Mã bưu chính: " + postalCode;
		}

		if (phoneNumber != null && !phoneNumber.isEmpty()) {
			address += ". Số điện thoại: " + phoneNumber;
		}

		return address;
	}

}
