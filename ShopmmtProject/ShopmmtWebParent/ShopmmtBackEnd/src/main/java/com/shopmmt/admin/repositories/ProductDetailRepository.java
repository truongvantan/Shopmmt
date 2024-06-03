package com.shopmmt.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.ProductDetail;

@Repository("productDetailRepository")
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
	ProductDetail findByProductIdAndNameAndValue(Integer productId, String name, String value);

	ProductDetail findFirstByProductId(Integer productId);
}
