package com.shopmmt.admin.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Category;

@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Category findByName(String name);
	
	@Query("SELECT c FROM Category c WHERE c.parent.id IS NUll")
	List<Category> findRootCategories(Sort sort);
	
	@Query("SELECT c FROM Category c WHERE c.parent.id IS NUll")
	Page<Category> findRootCategories(Pageable pageable);
	
	@Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
	Page<Category> search(String keyword , Pageable pageable);
	
	@Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	void updateEnabledStatus(Integer id, boolean enabled);

	Long countById(Integer id);
	
	

}
