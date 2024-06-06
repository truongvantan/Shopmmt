package com.shopmmt.admin.services;

import java.util.List;
import java.util.Optional;

import com.shopmmt.common.entity.Currency;

public interface CurrencyService {

	List<Currency> findAllByOrderByNameAsc();

	Optional<Currency> findById(Integer currencyId);

}
