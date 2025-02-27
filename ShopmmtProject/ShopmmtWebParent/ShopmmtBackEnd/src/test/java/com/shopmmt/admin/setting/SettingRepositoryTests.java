package com.shopmmt.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopmmt.admin.repositories.SettingRepository;
import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.enums.SettingCategory;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {

	@Autowired
	private SettingRepository settingRepository;

	@Test
	public void testCreateGeneralSettings() {
		Setting siteName = new Setting("SITE_NAME", "Shopmmt", SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO", "Shopmmt.png", SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2024 Shopmmt Ltd.", SettingCategory.GENERAL);

		settingRepository.saveAll(List.of(siteName, siteLogo, copyright));

		Iterable<Setting> iterable = settingRepository.findAll();

		assertThat(iterable).size().isGreaterThan(0);
	}

	@Test
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "đ", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "after", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);

		settingRepository.saveAll(
				List.of(currencyId, symbol, symbolPosition, decimalPointType, decimalDigits, thousandsPointType));
	}

	@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.GENERAL);

		settings.forEach(System.out::println);
	}

}
