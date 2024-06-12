package com.shopmmt.site.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopmmt.common.entity.CartItem;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.entity.Product;
import com.shopmmt.site.exceptions.ShoppingCartException;
import com.shopmmt.site.repositories.CartItemRepository;
import com.shopmmt.site.repositories.ProductRepository;
import com.shopmmt.site.services.ShoppingCartService;

import jakarta.transaction.Transactional;

@Service("shoppingCartService")
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		Product product = new Product(productId);

		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);

		if (cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;

			if (updatedQuantity > 5) {
				throw new ShoppingCartException(
						"Không thể thêm nhiều hơn " + quantity + " sản phẩm" + " vì đã có " + cartItem.getQuantity()
								+ " sản phẩm này trong giỏ hàng. Số lượng sản phẩm tối đa cho phép là 5.");
			}
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}

		cartItem.setQuantity(updatedQuantity);

		cartItemRepository.save(cartItem);

		return updatedQuantity;
	}

	@Override
	public List<CartItem> listCartItems(Customer customer) {
		return cartItemRepository.findByCustomer(customer);
	}

	@Override
	public double updateQuantity(Integer productId, Integer quantity, Customer customer) {
		cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
		
		Product product = productRepository.findById(productId).get();
		double subtotal = product.getDiscountPrice() * quantity;
		
		return subtotal;
	}

	@Override
	public void removeProduct(Integer productId, Customer customer) {
		cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productId);
	}

	@Override
	public void deleteByCustomer(Customer customer) {
		cartItemRepository.deleteByCustomer(customer.getId());
	}

}
