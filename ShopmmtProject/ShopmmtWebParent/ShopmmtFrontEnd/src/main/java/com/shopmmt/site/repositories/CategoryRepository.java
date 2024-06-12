package com.shopmmt.site.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Category;

@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	@Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
	public List<Category> findAllEnabled();
	
	@Query("SELECT c FROM Category c WHERE c.enabled = true AND c.name = ?1")
	public Category findByNameEnabled(String name);

	
	@Query("SELECT c FROM Category c WHERE c.enabled = true AND c.parent IS NULL ORDER BY c.name ASC")
	public List<Category> listRootParentCategories();
}
