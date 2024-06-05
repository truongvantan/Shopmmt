package com.shopmmt.site.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopmmt.common.entity.Product;
import com.shopmmt.site.repositories.ProductRepository;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Test
	public void testFindByName() {
		String name = "Áo Golf Nike Polo DH0827-100";
		Product product = productRepository.findByName(name);
		
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testGetProductDetailName() {
		String name = "Áo Golf Nike Polo DH0827-100";
		List<Object[]> row = productRepository.getProductDetailsByProductName(name);
		String productAttibuteName = "";
		
		for (Object[] col : row) {
			productAttibuteName = (String)col[2];
		}
		System.out.println(productAttibuteName);
	}
}
