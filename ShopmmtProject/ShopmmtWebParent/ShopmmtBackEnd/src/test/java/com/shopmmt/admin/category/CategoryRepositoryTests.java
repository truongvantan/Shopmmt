package com.shopmmt.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopmmt.admin.repositories.CategoryRepository;
import com.shopmmt.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	public void testCreateRootCategory() {
//		Category category = new Category("Áo nam");
//		Category savedCategory = categoryRepository.save(category);
		
//		Category category = new Category("Quần nam");
//		Category savedCategory = categoryRepository.save(category);
		
		Category category = new Category("Giày dép");
		Category savedCategory = categoryRepository.save(category);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSubCategory() {
//		Category parent = new Category(1);
//		Category shirt = new Category("Áo sơ mi", parent);
//		Category tshirt = new Category("Áo thun", parent);
//		Category jacket = new Category("Áo khoác", parent);
		
		Category parent = new Category(2);
		Category jeans = new Category("Quần jeans", parent);
		Category trousers = new Category("Quần tây", parent);
		Category shorts = new Category("Quần short", parent);
		
		categoryRepository.saveAll(List.of(jeans, trousers, shorts));
		
	}
	
	@Test
	public void testGetCategory() {
		Category category = categoryRepository.findById(2).get();
		System.out.println(category.getName());
		
		Set<Category> children = category.getChildren();
		
		for (Category subCategory : children) {
			System.out.println(subCategory.getName());
		}
		
		assertThat(children.size()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintTreeViewCategory() {
		Iterable<Category> categories = categoryRepository.findAll();
		
		for (Category category : categories) {
			if (category.getParent() == null) {
				System.out.println(category.getName());
				
				Set<Category> children = category.getChildren();
				for (Category subCategory : children) {
					System.out.printf("\t>%s\n", subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}	
	
	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		
		for (Category subCategory : children) {
			for (int i = 0; i < newSubLevel; i++) {
				System.out.printf("\t>");
			}
			
			System.out.println(subCategory.getName());
			
			printChildren(subCategory, newSubLevel);
		}
	}
	
	@Test
	public void testListCategories() {
		List<Category> listRootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
		for (Category c : listRootCategories) {
			System.out.println(c.getName());
		}
	}
}
