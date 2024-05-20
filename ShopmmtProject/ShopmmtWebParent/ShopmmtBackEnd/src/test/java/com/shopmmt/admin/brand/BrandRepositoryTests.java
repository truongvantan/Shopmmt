package com.shopmmt.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopmmt.admin.repositories.BrandRepository;
import com.shopmmt.common.entity.Brand;
import com.shopmmt.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {

	@Autowired
	private BrandRepository brandRepository;

	@Test
	public void testCreateBrand1() {
		Category tshirt = new Category(4);
		Brand nike = new Brand("Nike");
		nike.getCategories().add(tshirt);

		Brand savedBrand = brandRepository.save(nike);

		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand2() {
		Category trousers = new Category(2);
		Category shoes = new Category(16);
		
		Brand adidas = new Brand("Adidas");
		adidas.getCategories().add(trousers);
		adidas.getCategories().add(shoes);
		
		Brand savedBrand = brandRepository.save(adidas);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand3() {
		Category accessories = new Category(12);
		Category jacket = new Category(5);
		
		Brand mlb = new Brand("MLB");
		mlb.getCategories().add(accessories);
		mlb.getCategories().add(jacket);
		
		Brand savedBrand = brandRepository.save(mlb);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAllBrands() {
		List<Brand> listBrands = brandRepository.findAll();
		
		listBrands.forEach(brand -> System.out.println(brand));
		
		assertThat(listBrands.size()).isGreaterThan(0);
	}
	
	@Test
	public void testGetById() {
		Brand brand = brandRepository.findById(1).get();
		System.out.println(brand);
		
		assertThat(brand.getName()).isEqualTo("Nike");
	}
	
	@Test
	public void testEditName() {
		String newName = "Major Leguage Baseball";
		Brand mlb = brandRepository.findById(3).get();
		mlb.setName(newName);
		
		Brand savedBrand = brandRepository.save(mlb);
		
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDelete() {
		Integer id = 3;
		brandRepository.deleteById(id);
		
		Optional<Brand> result = brandRepository.findById(id);
		
		assertThat(result.isEmpty());
	}
}
