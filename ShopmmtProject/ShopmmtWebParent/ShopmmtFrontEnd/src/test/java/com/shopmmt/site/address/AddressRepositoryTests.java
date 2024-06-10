package com.shopmmt.site.address;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopmmt.common.entity.Address;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.site.repositories.AddressRepository;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Test
	public void testAddNewAdress() {
		Integer customerId = 14;
		
//		Address newAddress = new Address();
//		newAddress.setCustomer(new Customer(customerId));
//		newAddress.setFirstName("B");
//		newAddress.setLastName("Trần Thị");
//		newAddress.setPhoneNumber("0905-444-777");
//		newAddress.setAddressLine1("20 Hoàng Diệu");
//		newAddress.setAddressLine2("14 Huỳnh Thúc Kháng");
//		newAddress.setCity("Hà Nội");
//		newAddress.setState("Thanh Xuân");
//		newAddress.setPostalCode("11013");
		
		Address newAddress = new Address();
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setFirstName("B");
		newAddress.setLastName("Trần Thị");
		newAddress.setPhoneNumber("0905-444-777");
		newAddress.setAddressLine1("99 Tăng Bạt Hổ");
		newAddress.setCity("Hà Nội");
		newAddress.setState("Đống đa");
		newAddress.setPostalCode("999999");
		
		Address savedAddress = addressRepository.save(newAddress);
		
		assertThat(savedAddress).isNotNull();
		
		assertThat(savedAddress.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateAddress() {
		Integer addressId = 1;
		String phoneNumber = "0123456789";
		
		Address address = addressRepository.findById(addressId).get();
		address.setPhoneNumber(phoneNumber);

		Address updatedAddress = addressRepository.save(address);
		assertThat(updatedAddress.getPhoneNumber()).isEqualTo(phoneNumber);
		
//		Integer addressId = 2;
//		
//		Address address = addressRepository.findById(addressId).get();
//		address.setDefaultForShipping(true);
//
//		Address updatedAddress = addressRepository.save(address);
	}
}
