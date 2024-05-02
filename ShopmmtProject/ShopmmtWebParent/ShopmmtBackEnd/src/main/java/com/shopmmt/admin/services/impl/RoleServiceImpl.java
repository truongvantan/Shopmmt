package com.shopmmt.admin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopmmt.admin.repositories.RoleRepository;
import com.shopmmt.admin.services.RoleService;
import com.shopmmt.common.entity.Role;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<Role> listRoles() {
		return roleRepository.findAll();
	}

}
