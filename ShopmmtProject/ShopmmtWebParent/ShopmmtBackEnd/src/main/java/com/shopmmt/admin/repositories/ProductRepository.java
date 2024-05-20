package com.shopmmt.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
