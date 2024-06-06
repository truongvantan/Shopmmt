package com.shopmmt.site.setting;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.enums.SettingCategory;
import com.shopmmt.site.repositories.SettingRepository;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SettingRepositoryTests {
	
	@Autowired
	private SettingRepository settingRepository;
	
	@Test
	public void testFindByTwoCategories() {
		List<Setting> settings = settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
		settings.forEach(System.out::println);
	}
}
