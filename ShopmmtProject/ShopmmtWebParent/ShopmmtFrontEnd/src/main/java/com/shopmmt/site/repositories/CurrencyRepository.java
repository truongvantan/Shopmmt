package com.shopmmt.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Currency;

@Repository("currencyRepository")
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

}
