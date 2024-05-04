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
import com.shopmmt.common.validate.ValidateCommon;

import jakarta.validation.Valid;

@Service("userService")
@Transactional
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
			
			if ("".equals(userDTO.getPassword()) || userDTO.getPassword() == null) {
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
		User user = userRepository.findByEmail(email);
		if (user == null) {
			return true;
		}
		if (id == null) {
			if (user != null) {
				return false;
			}
		} else {
			if (!ValidateCommon.isValidStringIntegerNumber(id)) {
				return false;
			} else {
				if (user.getId() != Integer.valueOf(id)) {
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public User findById(String id) throws UserNotFoundException {
		try {
			if (!ValidateCommon.isValidStringIntegerNumber(id)) {
				throw new UserNotFoundException("Could not find any user with ID " + id);
			} else {
				return userRepository.findById(Integer.valueOf(id)).get();
			}
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}

	@Override
	public void delete(String id) throws UserNotFoundException {
		if (!ValidateCommon.isValidStringIntegerNumber(id)) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		} else {
			Long countById = userRepository.countById(Integer.valueOf(id));
			if (countById == null || countById == 0) {
				throw new UserNotFoundException("Could not find any user with ID " + id);
			}
			userRepository.deleteById(Integer.valueOf(id));
		}
	}

	@Override
	public void updateUserEnabledStatus(String id, boolean enabled) throws UserNotFoundException {
		if (!ValidateCommon.isValidStringIntegerNumber(id)) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		} else {
			Long countById = userRepository.countById(Integer.valueOf(id));
			if (countById == null || countById == 0) {
				throw new UserNotFoundException("Could not find any user with ID " + id);
			}
			userRepository.updateEnabledStatus(Integer.valueOf(id), enabled);
		}
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
}