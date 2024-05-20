package com.shopmmt.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopmmt.admin.repositories.UserRepository;
import com.shopmmt.common.entity.Role;
import com.shopmmt.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userTan = new User("hoasu11920@gmail.com", "vantan", "Tan", "Truong Van");
		userTan.addRole(roleAdmin);
		
		User savedUser = userRepository.save(userTan);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRoles() {
		User userPele = new User("pele@gmail.com", "pele", "Pele", "Edson Arantes");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userPele.addRole(roleEditor);
		userPele.addRole(roleAssistant);
		
		User savedUser = userRepository.save(userPele);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		List<User> listUsers = userRepository.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User user = userRepository.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User user = userRepository.findById(1).get();
		user.setEnabled(true);
		
		userRepository.save(user);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userPele = userRepository.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userPele.getRoles().remove(roleEditor);
		userPele.addRole(roleSalesperson);
		
		userRepository.save(userPele);
	}
	
	@Test
	public void testDeleteUser() {
		userRepository.deleteById(2);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "pele@gmail.com";
		User user =  userRepository.findByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = userRepository.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		userRepository.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 1;
		userRepository.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepository.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> {
			System.out.println(user);
		});
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testListSecondPage() {
		int pageNumber = 1;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepository.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> {
			System.out.println(user);
		});
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUser() {
		String keyword = "tan";
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepository.findAll(keyword, pageable);
		
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> {
			System.out.println(user);
		});
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}
	
	@Test
	public void listAllPhotoName() {
		List<Object[]> listAllPhotoName = userRepository.listAllPhotoName();
		System.out.println("size: " + listAllPhotoName.size());
		for (Object[] o : listAllPhotoName) {
			System.out.printf("photo = %s\n", o[0]);
		}
	}
}
