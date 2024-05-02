package com.shopmmt.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

	Long countById(Integer id);

	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	void updateEnabledStatus(Integer id, boolean enabled);

}
