package com.shopmmt.admin.services.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopmmt.admin.exception.BrandNotFoundException;
import com.shopmmt.admin.repositories.BrandRepository;
import com.shopmmt.admin.services.BrandService;
import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.dto.BrandDTO;
import com.shopmmt.common.entity.Brand;
import com.shopmmt.common.validate.ValidateCommon;

import jakarta.validation.Valid;

@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandRepository brandRepository;
	
	@Override
	public List<Brand> findAll() {
		return brandRepository.findAll();
	}

	@Override
	public Brand save(@Valid BrandDTO brandDTO) {
		Brand brand = new Brand(brandDTO);
		return brandRepository.save(brand);
	}

	@Override
	public Brand findById(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
	}

	@Override
	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = brandRepository.countById(id);
		
		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
		
		brandRepository.deleteById(id);
	}

	@Override
	public boolean isBrandNameUnique(String id, String name) {
		if (name != null) {
			name = name.trim();
		}
		Brand brand = brandRepository.findByName(name);
		if (brand == null) {
			return true;
		}
		if (id == null) {
			if (brand != null) {
				return false;
			}
		} else {
			if (!ValidateCommon.isValidStringIntegerNumber(id)) {
				return false;
			} else {
				if (brand.getId() != Integer.valueOf(id)) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, ConstantsUtil.BRAND_PAGE_SIZE, sort);
		
		if (keyword != null) {
			return brandRepository.findAll(keyword, pageable);
		}
		
		return brandRepository.findAll(pageable);
	}
	
}
