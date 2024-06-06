package com.shopmmt.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopmmt.admin.repositories.CurrencyRepository;
import com.shopmmt.common.entity.Currency;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Test
	public void testCreateCurrencies() {
		List<Currency> listCurrencies = Arrays.asList(
			new Currency("United States Dollar", "$", "USD"),
			new Currency("British Pound", "£", "GPB"),
			new Currency("Japanese Yen", "¥", "JPY"),
			new Currency("Euro", "€", "EUR"),
			new Currency("Vietnamese đồng ", "₫", "VND")
		);
		
		currencyRepository.saveAll(listCurrencies);
		
		Iterable<Currency> iterable = currencyRepository.findAll();
		
		assertThat(iterable).size().isEqualTo(5);
	}
	
	@Test
	public void testListAllOrderByNameAsc() {
		List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();
		
		currencies.forEach(System.out::println);
		
		assertThat(currencies.size()).isGreaterThan(0);
	}
}
