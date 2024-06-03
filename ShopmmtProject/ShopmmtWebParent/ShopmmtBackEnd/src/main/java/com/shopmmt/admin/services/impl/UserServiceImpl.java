package com.shopmmt.admin.services.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopmmt.admin.exception.UserNotFoundException;
import com.shopmmt.admin.repositories.UserRepository;
import com.shopmmt.admin.services.UserService;
import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.dto.UserDTO;
import com.shopmmt.common.entity.User;

import jakarta.validation.Valid;

@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<User> listAll() {
		return userRepository.findAll();
	}

	@Override
	public User save(@Valid UserDTO userDTO) {
		if (userDTO.getId() != null) { // cập nhật
			User existingUser = userRepository.findById(userDTO.getId()).get();
			if ("".equals(userDTO.getPassword()) || userDTO.getPassword() == null
					|| userDTO.getPassword().equals(existingUser.getPassword())) {
				userDTO.setPassword(existingUser.getPassword());
			} else {
				encodePassword(userDTO);
			}

		} else { // thêm mới
			encodePassword(userDTO);
		}

		User user = new User(userDTO);
		return userRepository.save(user);
	}

	private void encodePassword(UserDTO userDTO) {
		String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
		userDTO.setPassword(encodedPassword);
	}

	@Override
	public boolean isEmailUnique(String id, String email) {
		if (email != null) {
			email = email.trim();
		}
		
		User userByEmail = userRepository.findByEmail(email);

		if (userByEmail == null) {
			return true;
		}

		boolean isCreatingNew = (id == null || "".equals(email));

		if (isCreatingNew) {
			if (userByEmail != null)
				return false;
		} else {
			if (userByEmail.getId() != Integer.valueOf(id)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public User findById(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}

	@Override
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}

		userRepository.deleteById(Integer.valueOf(id));
	}

	@Override
	public void updateUserEnabledStatus(Integer id, boolean enabled) throws UserNotFoundException {
		Long countById = userRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}

		userRepository.updateEnabledStatus(id, enabled);
	}

	@Override
	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ConstantsUtil.USER_PAGE_SIZE, sort);

		if (keyword != null) {
			return userRepository.findAll(keyword, pageable);
		}

		return userRepository.findAll(pageable);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public String validatePassword(String newPassword, String confirmPassword, String password) {
		if (("".equals(newPassword) || newPassword == null)
				&& ("".equals(confirmPassword) || confirmPassword == null)) {
			return "Valid new password";
		} else if (!newPassword.matches(ConstantsUtil.REGEX_PASSWORD)) {
			return "Invalid new password";
		} else if (!newPassword.equals(confirmPassword)) {
			return "Confirm password does not match";
		} else {
			return "Valid new password";
		}
	}

}
