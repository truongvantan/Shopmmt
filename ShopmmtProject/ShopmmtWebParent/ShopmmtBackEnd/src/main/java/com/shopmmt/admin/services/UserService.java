package com.shopmmt.admin.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopmmt.admin.exception.UserNotFoundException;
import com.shopmmt.common.dto.UserDTO;
import com.shopmmt.common.entity.User;

import jakarta.validation.Valid;

public interface UserService {
	List<User> listAll();

	User save(@Valid UserDTO userDTO);

	boolean isEmailUnique(String id, String email);

	User findById(String id) throws UserNotFoundException;

	void delete(String id) throws UserNotFoundException;

	void updateUserEnabledStatus(String id, boolean enabled) throws UserNotFoundException;

	Page<User> listByPage(int pageNum);
}
