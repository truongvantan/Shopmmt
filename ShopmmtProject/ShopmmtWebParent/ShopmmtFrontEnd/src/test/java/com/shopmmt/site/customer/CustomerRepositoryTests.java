package com.shopmmt.site.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopmmt.common.entity.Customer;
import com.shopmmt.site.repositories.CustomerRepository;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void testCreateCustomer1() {
		Customer customer = new Customer();
		customer.setFirstName("A");
		customer.setLastName("Nguyễn Văn");
		customer.setPassword("@Nguyenvana123");
		customer.setEmail("nguyenva@gmail.com");
		customer.setPhoneNumber("0905-123-456");
		customer.setAddressLine1("193 Nguyễn Lương Bằng");
		customer.setCity("Đà Nẵng");
		customer.setState("Liên Chiểu");
		customer.setPostalCode("50217");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = customerRepository.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateCustomer2() {
		Customer customer = new Customer();
		customer.setFirstName("B");
		customer.setLastName("Trần Thị");
		customer.setPassword("@Tranthib123");
		customer.setEmail("tranthib@gmail.com");
		customer.setPhoneNumber("0905-444-777");
		customer.setAddressLine1("20 Hoàng Diệu");
		customer.setAddressLine2("14 Huỳnh Thúc Kháng");
		customer.setCity("Hà Nội");
		customer.setState("Thanh Xuân");
		customer.setPostalCode("11013");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = customerRepository.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void testListCustomers() {
		List<Customer> customers = customerRepository.findAll();
		customers.forEach(System.out::println);

		assertThat(customers).hasSizeGreaterThan(1);
	}

	@Test
	public void testGetCustomer() {
		Integer customerId = 2;
		Optional<Customer> findById = customerRepository.findById(customerId);

		assertThat(findById).isPresent();

		Customer customer = findById.get();
		System.out.println(customer);
	}

	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		String varificationCode = "code 123";

		Customer customer = customerRepository.findById(customerId).get();
		customer.setVerificationCode(varificationCode);
		customer.setEnabled(true);

		Customer updatedCustomer = customerRepository.save(customer);
		assertThat(updatedCustomer.getVerificationCode()).isEqualTo(varificationCode);
	}

	@Test
	public void testFindByEmail() {
		String email = "tranthib@gmail.com";
		Customer customer = customerRepository.findByEmail(email);

		assertThat(customer).isNotNull();
		System.out.println(customer);
	}

	@Test
	public void testFindByVerificationCode() {
		String code = "code 123";
		Customer customer = customerRepository.findByVerificationCode(code);

		assertThat(customer).isNotNull();
		System.out.println(customer);
	}

	@Test
	public void testEnableCustomer() {
		Integer customerId = 2;
		customerRepository.enable(customerId);

		Customer customer = customerRepository.findById(customerId).get();
		assertThat(customer.isEnabled()).isTrue();
	}
}
