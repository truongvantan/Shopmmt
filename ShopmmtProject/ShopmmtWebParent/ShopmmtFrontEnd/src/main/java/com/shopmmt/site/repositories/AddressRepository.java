package com.shopmmt.site.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Address;
import com.shopmmt.common.entity.Customer;

@Repository("addressRepository")
public interface AddressRepository extends JpaRepository<Address, Integer> {
	
	List<Address> findByCustomer(Customer customer);
	
	@Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
	Address findByIdAndCustomer(Integer addressId, Integer customerId);
	
	@Query("DELETE FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
	@Modifying
	void deleteByIdAndCustomer(Integer addressId, Integer customerId);
	
	@Query("UPDATE Address a SET a.defaultForShipping = true WHERE a.id = ?1")
	@Modifying
	void setDefaultAddress(Integer id);
	
	@Query("UPDATE Address a SET a.defaultForShipping = false "
			+ "WHERE a.id != ?1 AND a.customer.id = ?2")
	@Modifying
	public void setNonDefaultForOthers(Integer defaultAddressId, Integer customerId);
}
