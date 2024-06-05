package com.shopmmt.site.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopmmt.common.entity.Category;
import com.shopmmt.site.repositories.CategoryRepository;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTests {

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	public void testListEnabledCategories() {
		List<Category> categories = categoryRepository.findAllEnabled();
		categories.forEach(category -> {
			System.out.println(category.getName() + " (" + category.isEnabled() + ")");
		});
	}
	
	@Test
	public void testFindCategoryByName() {
		String name = "Áo thun";
		
		Category category = categoryRepository.findByNameEnabled(name);
		
		assertThat(category).isNotNull();
	}
}
