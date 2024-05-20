package com.shopmmt.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopmmt.admin.repositories.ProductRepository;
import com.shopmmt.common.entity.Brand;
import com.shopmmt.common.entity.Category;
import com.shopmmt.common.entity.Product;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void testCreateProduct() {
//		Brand nikeBrand = testEntityManager.find(Brand.class, 1);
//		Category tshirt = testEntityManager.find(Category.class, 4);
//		
//		Product product = new Product();
//		product.setName("Áo Nike Dri-FIT Academy 21 TRAINING TOP CW6101-100");
//		product.setSortDescription("Áo Nike Dri-FIT Academy 21 Training Top (mã sản phẩm: CW6101-100) là một sản phẩm tuyệt vời của Nike dành cho việc tập luyện. Được thiết kế với công nghệ Dri-FIT tiên tiến, áo này có khả năng hút ẩm và thoát mồ hôi tốt, giúp bạn luôn khô ráo và thoải mái trong suốt quá trình tập luyện.");
//		product.setFullDescription("Áo Nike Dri-FIT Academy 21 Training Top (mã sản phẩm: CW6101-100) là một sản phẩm tuyệt vời của Nike dành cho việc tập luyện. Được thiết kế với công nghệ Dri-FIT tiên tiến, áo này có khả năng hút ẩm và thoát mồ hôi tốt, giúp bạn luôn khô ráo và thoải mái trong suốt quá trình tập luyện.");
//		product.setBrand(nikeBrand);
//		product.setCategory(tshirt);
//		product.setPrice(690_000);
//		product.setCreatedTime(new Date());
//		product.setUpdatedTime(new Date());
		
//		Brand adidasBrand = testEntityManager.find(Brand.class, 2);
//		Category sneaker = testEntityManager.find(Category.class, 15);
//		
//		Product product = new Product();
//		product.setName("Giày adidas NMD_R1 ‘Japan Heel – Metal Grey’ EF4261");
//		product.setSortDescription("adidas NMD_R1 “Japan Heel – Metal Grey” (EF4261) là một đôi giày thuộc dòng NMD_R1 đình đám, lấy cảm hứng từ văn hóa Nhật Bản, mang đến phong cách thời trang đường phố (streetwear) cá tính và thoải mái.");
//		product.setFullDescription("adidas NMD_R1 “Japan Heel – Metal Grey” (EF4261) là một đôi giày thuộc dòng NMD_R1 đình đám, lấy cảm hứng từ văn hóa Nhật Bản, mang đến phong cách thời trang đường phố (streetwear) cá tính và thoải mái.");
//		product.setBrand(adidasBrand);
//		product.setCategory(sneaker);
//		product.setCost(1_000_000);
//		product.setPrice(1_690_000);
//		product.setCreatedTime(new Date());
//		product.setUpdatedTime(new Date());
		
//		Brand newBalanceBrand = testEntityManager.find(Brand.class, 4);
//		Category shorts = testEntityManager.find(Category.class, 8);
//		
//		Product product = new Product();
//		product.setName("Quần Shorts New Balance Kl2 ‘Green Navy’ MS11593NGO");
//		product.setSortDescription("New Balance là một thương hiệu thời trang và giày thể thao từ Mỹ. Hơn 100 năm qua, New Balance luôn tìm hiểu nhu cầu của những vận động viên để đầu tư, thiết kế những sản phẩm phù hợp.");
//		product.setFullDescription("New Balance là một thương hiệu thời trang và giày thể thao từ Mỹ. Hơn 100 năm qua, New Balance luôn tìm hiểu nhu cầu của những vận động viên để đầu tư, thiết kế những sản phẩm phù hợp.");
//		product.setBrand(newBalanceBrand);
//		product.setCategory(shorts);
//		product.setCost(1_000_000);
//		product.setPrice(1_690_000);
//		product.setCreatedTime(new Date());
//		product.setUpdatedTime(new Date());
		
		Brand jordanBrand = testEntityManager.find(Brand.class, 4);
		Category jacket = testEntityManager.find(Category.class, 8);
		
		Product product = new Product();
		product.setName("Áo Sweatshirt Jordan ‘Flight Heritage Crew Neck’ DO2308-256");
		product.setSortDescription("Sort description Áo Sweatshirt Jordan ‘Flight Heritage Crew Neck’ DO2308-256");
		product.setFullDescription("Full description Áo Sweatshirt Jordan ‘Flight Heritage Crew Neck’ DO2308-256");
		product.setBrand(jordanBrand);
		product.setCategory(jacket);
		product.setCost(1_800_000);
		product.setPrice(2_190_000);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product savedProduct = productRepository.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllProducts() {
		List<Product> listProducts = productRepository.findAll();
		listProducts.forEach(product -> System.out.println(product));
	}
	
	@Test
	public void testFindProductById() {
		Product product = productRepository.findById(1).get();
		System.out.println(product);
		
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer id = 4;
		Product product = productRepository.findById(id).get();
		
		product.setCost(2_000_000);
		product.setPrice(2_390_000);
		
		productRepository.save(product);
		
		Product updatedProduct = testEntityManager.find(Product.class, id);
		
		assertThat(updatedProduct.getCost()).isEqualTo(2_000_000);
		assertThat(updatedProduct.getPrice()).isEqualTo(2_390_000);
		
	}
	
	@Test
	public void testDeleteProduct() {
		Integer id = 5;
		productRepository.deleteById(id);
		
		Optional<Product> result = productRepository.findById(id);
		
		assertThat(!result.isPresent());
	}
}
