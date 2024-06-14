package com.shopmmt.common.dto;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.shopmmt.common.Constants;
import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.entity.Role;
import com.shopmmt.common.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {
	private Integer id;

	@Pattern(regexp = ConstantsUtil.REGEX_EMAIL, message = "Email không đúng định dạng (vd: abc@gmail.com).")
	private String email;

	@Pattern(regexp = ConstantsUtil.REGEX_PASSWORD, message = "Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 60 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.")
	private String password;

	@NotBlank(message = "Vui lòng nhập tên.")
	@Size(max = 50, message = "Tối đa 50 ký tự.")
	private String firstName;

	@NotBlank(message = "Vui lòng nhập họ đệm")
	@Size(max = 50, message = "Tối đa 50 ký tự.")
	private String lastName;

	private String photos;

	private boolean enabled;

	private Set<Role> roles = new HashSet<Role>();

	public UserDTO() {
		super();
	}

	public UserDTO(
			@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email không đúng định dạng (vd: abc@gmail.com).") String email,
			@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$", message = "Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 20 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.") String password,
			@NotBlank(message = "Vui lòng nhập tên.") @Size(max = 50, message = "Tối đa 50 ký tự.") String firstName,
			@NotBlank(message = "Vui lòng nhập họ đệm") @Size(max = 50, message = "Tối đa 50 ký tự.") String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public UserDTO(Integer id,
			@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email không đúng định dạng (vd: abc@gmail.com).") String email,
			@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$", message = "Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 20 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.") String password,
			@NotBlank(message = "Vui lòng nhập tên.") @Size(max = 50, message = "Tối đa 50 ký tự.") String firstName,
			@NotBlank(message = "Vui lòng nhập họ đệm") @Size(max = 50, message = "Tối đa 50 ký tự.") String lastName,
			String photos, boolean enabled, Set<Role> roles) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.photos = photos;
		this.enabled = enabled;
		this.roles = roles;
	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.photos = user.getPhotos();
		this.enabled = user.isEnabled();
		this.roles = user.getRoles();
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", email=" + email + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", photos=" + photos + ", enabled=" + enabled + ", roles=" + roles + "]";
	}
	
	public String getPhotosImagePath() {
		if (this.id == null || this.photos == null) {
			return "/images/default-user.png";
		}
		return Constants.S3_BASE_URI + "/user-photos/" + this.id + "/" + this.photos;
	}
	
	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();
		
		while (iterator.hasNext()) {
			Role role = iterator.next();
			if (role.getName().equals(roleName)) {
				return true;
			}
		}
		
		return false;
	}

}