package com.shopmmt.common.dto;

import com.shopmmt.common.constants.ConstantsUtil;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AccountDTO {

	@Pattern(regexp = ConstantsUtil.REGEX_EMAIL, message = "Email không đúng định dạng (vd: abc@gmail.com).")
	private String email;

//	@Pattern(regexp = ConstantsUtil.REGEX_PASSWORD, message = "Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 60 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.")
	@NotNull(message = "Vui lòng nhập mật khẩu")
	private String password;

	public AccountDTO() {
		super();
	}

	public AccountDTO(
			@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email không đúng định dạng (vd: abc@gmail.com).") String email,
			@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,60}$", message = "Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 60 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.") String password) {
		super();
		this.email = email;
		this.password = password;
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

	@Override
	public String toString() {
		return "AccountDTO [email=" + email + ", password=" + password + "]";
	}

}