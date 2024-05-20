package com.shopmmt.admin.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Brand;

@Repository("brandRepository")
public interface BrandRepository extends JpaRepository<Brand, Integer> {

	Long countById(Integer id);

	Brand findByName(String name);

	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	Page<Brand> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	List<Brand> findAll();

}
