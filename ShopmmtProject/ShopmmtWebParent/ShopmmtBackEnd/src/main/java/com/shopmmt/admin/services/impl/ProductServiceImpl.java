package com.shopmmt.admin.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopmmt.admin.exception.ProductDetailDuplicateException;
import com.shopmmt.admin.repositories.ProductDetailRepository;
import com.shopmmt.admin.repositories.ProductRepository;
import com.shopmmt.admin.services.ProductService;
import com.shopmmt.common.constants.ConstantsUtil;
import com.shopmmt.common.dto.ProductDTO;
import com.shopmmt.common.entity.Product;
import com.shopmmt.common.entity.ProductDetail;
import com.shopmmt.common.exception.ProductNotFoundException;

@Service("productService")
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductDetailRepository productDetailRepository;

	@Override
	public List<Product> listAll() {
		return productRepository.findAll();
	}

	@Override
	public Product save(ProductDTO productDTO) {
		if (productDTO.getId() == null) {
			productDTO.setCreatedTime(new Date());
		}

		productDTO.setUpdatedTime(new Date());

		Product product = new Product(productDTO);

		return productRepository.save(product);
	}

	@Override
	public boolean isProductNameUnique(String id, String name) {
		if (name != null) {
			name = name.trim();
		}

		boolean isCreatingNew = (id == null || "".equals(id));
		Product productByName = productRepository.findByName(name);

		if (isCreatingNew) {
			if (productByName != null) {
				return false;
			}
		} else {
			if (productByName != null && productByName.getId() != Integer.valueOf(id)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void updateProductEnabledStatus(Integer id, boolean enabled) throws ProductNotFoundException {
		Long countById = productRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}

		productRepository.updateEnabledStatus(id, enabled);
	}

	@Override
	public void delete(Integer id) throws ProductNotFoundException {
		Long countById = productRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}

		productRepository.deleteById(id);
	}

	@Override
	public Product save(Product product) throws ProductDetailDuplicateException {
		Product returnProduct = null;
		
		try {
			if (product.getId() == null) {
				product.setCreatedTime(new Date());
			}
			
			if(product.getMainImage() == null) {
				product.setMainImage("image-thumbnail.png");
			}

			product.setUpdatedTime(new Date());

			returnProduct =  productRepository.save(product);
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("Duplicate entry")) {
				throw new ProductDetailDuplicateException("Duplicate entry product detail");
			}
		}
		
		return returnProduct;
	}

	@Override
	public boolean isProductDetailsUnique(String productId, String jsonDetailNames, String jsonDetailValues) {
		List<String> listStringDetailNames = convertJsonStringToListString(jsonDetailNames);
		List<String> listStringDetailValues = convertJsonStringToListString(jsonDetailValues);
		
		boolean isCreatingNew = (productId == null || "-1".equals(productId));
		
		ProductDetail productDetailFirstByProductId = productDetailRepository.findFirstByProductId(Integer.valueOf(productId));
		
		Map<String, String> mapNameValue = new HashMap<String, String>();
		
		if (isCreatingNew) {
			if (productDetailFirstByProductId != null) {
				return false;
			}
			
			for (int count = 0; count < listStringDetailNames.size(); count++) {
				String name = listStringDetailNames.get(count);
				String value = listStringDetailValues.get(count);

				if (name != null && !"".equals(name) && value != null && !"".equals(value)) {
					name = name.trim().toLowerCase();
					value = value.trim().toLowerCase();
					
					if (value.equalsIgnoreCase(mapNameValue.get(name))) {
						return false;
					}
					
					mapNameValue.put(name, value);
					System.err.println(mapNameValue);
				}
			}
		} else {
			for (int count = 0; count < listStringDetailNames.size(); count++) {
				String name = listStringDetailNames.get(count);
				String value = listStringDetailValues.get(count);

				if (name != null && !"".equals(name) && value != null && !"".equals(value)) {
					name = name.trim().toLowerCase();
					value = value.trim().toLowerCase();
					ProductDetail productDetail = productDetailRepository
							.findByProductIdAndNameAndValue(Integer.valueOf(productId), name, value);
					if (productDetail != null) {
						return false;
					}
				}
			}
		}

		return true;
	}

	private List<String> convertJsonStringToListString(String jsonString) {
		List<String> listString = new ArrayList<String>();
		JSONArray jsonArray = new JSONArray(jsonString);

		for (int i = 0; i < jsonArray.length(); i++) {
			listString.add(jsonArray.getString(i));
		}

		return listString;
	}

	@Override
	public Product findById(Integer id) throws ProductNotFoundException {
		try {
			return productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}
	}

	@Override
	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId) {
		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ConstantsUtil.PRODUCT_PAGE_SIZE, sort);

		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				return productRepository.searchByCategory(categoryId, categoryIdMatch, keyword, pageable);
			}
			
			return productRepository.findAll(keyword, pageable);
		}
		
		if (categoryId != null && categoryId > 0) {
			String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
			return productRepository.findAllByCategory(categoryId, categoryIdMatch, pageable);
		}

		return productRepository.findAll(pageable);
	}

	@Override
	public void saveProductPrice(Product productForm) {
		Product productInDB = productRepository.findById(productForm.getId()).get();
		productInDB.setCost(productForm.getCost());
		productInDB.setPrice(productForm.getPrice());
		productInDB.setDiscountPercent(productForm.getDiscountPercent());
		
		productRepository.save(productInDB);
	}
}
