package com.shopmmt.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Role;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer>{

}
