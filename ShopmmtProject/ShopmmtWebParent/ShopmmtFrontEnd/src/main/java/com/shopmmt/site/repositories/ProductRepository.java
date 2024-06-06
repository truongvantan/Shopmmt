package com.shopmmt.site.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%)"
			+ " ORDER BY p.name ASC")
	Page<Product> listByCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);

	Product findByName(String name);
	
	@Modifying
	@Query(value = "SELECT p.id, p.name , GROUP_CONCAT(DISTINCT pd.name ORDER BY pd.name SEPARATOR ',') AS product_detail_name "
			+ "FROM products p "
			+ "JOIN product_details pd ON p.id = pd.product_id "
			+ "GROUP BY p.name "
			+ "HAVING p.name = ?1", nativeQuery = true)
	List<Object[]> getProductDetailsByProductName(String name);
	
	@Query(value = "SELECT * FROM products WHERE enabled = true AND "
			+ "MATCH(name, short_description, full_description) AGAINST (?1 IN NATURAL LANGUAGE MODE)", 
			nativeQuery = true)
	public Page<Product> search(String keyword, Pageable pageable);

}
