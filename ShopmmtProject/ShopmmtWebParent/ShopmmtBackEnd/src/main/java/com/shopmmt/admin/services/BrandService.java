package com.shopmmt.admin.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import com.shopmmt.admin.exception.BrandNotFoundException;
import com.shopmmt.common.dto.BrandDTO;
import com.shopmmt.common.entity.Brand;

import jakarta.validation.Valid;

public interface BrandService {

	List<Brand> findAll();

	Brand save(@Valid BrandDTO brandDTO);

	Brand findById(Integer id) throws BrandNotFoundException;

	void delete(Integer id) throws BrandNotFoundException, DataIntegrityViolationException;

	boolean isBrandNameUnique(String id, String name);

	Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword);
	
}
