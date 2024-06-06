package com.shopmmt.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Customer;

@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Customer findByEmail(String email);

	Customer findByVerificationCode(String code);

	@Query("UPDATE Customer c SET c.enabled = true WHERE c.id = ?1")
	@Modifying
	public void enable(Integer id);
}
