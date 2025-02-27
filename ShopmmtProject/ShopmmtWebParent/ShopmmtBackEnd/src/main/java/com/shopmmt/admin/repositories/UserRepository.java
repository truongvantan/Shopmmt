package com.shopmmt.admin.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %?1%")
	Page<User> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT u.photos FROM User u GROUP BY u.photos")
	List<Object[]> listAllPhotoName();
	
}
