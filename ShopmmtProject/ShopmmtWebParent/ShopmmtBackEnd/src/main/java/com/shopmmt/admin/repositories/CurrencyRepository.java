package com.shopmmt.admin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Currency;

@Repository("currencyRepository")
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
	
	List<Currency> findAllByOrderByNameAsc();
}
