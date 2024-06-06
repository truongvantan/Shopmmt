package com.shopmmt.admin.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopmmt.admin.repositories.CurrencyRepository;
import com.shopmmt.admin.services.CurrencyService;
import com.shopmmt.common.entity.Currency;

@Service("currencyService")
public class CurrencyServiceImpl implements CurrencyService {
	
	@Autowired
	private CurrencyRepository currencyRepository;

	@Override
	public List<Currency> findAllByOrderByNameAsc() {
		// TODO Auto-generated method stub
		return currencyRepository.findAllByOrderByNameAsc();
	}

	@Override
	public Optional<Currency> findById(Integer currencyId) {
		return currencyRepository.findById(currencyId);
	}
}
