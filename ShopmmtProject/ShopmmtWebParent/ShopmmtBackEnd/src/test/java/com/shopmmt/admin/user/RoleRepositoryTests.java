package com.shopmmt.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopmmt.admin.repositories.RoleRepository;
import com.shopmmt.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "Quản lí hệ thống");
		Role savedRole = roleRepository.save(roleAdmin);

		assertThat(savedRole.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateRestRole() {
		Role roleSalesperson = new Role("Nhân viên bán hàng", "Quản lí giá sản phẩm, khách hàng, vận chuyển, báo cáo bán hàng");

		Role roleEditor = new Role("Nhân viên kho hàng", "Quản lí danh mục, thương hiệu, sản phẩm, bài viết và các tùy chọn");
		
		Role roleShipper = new Role("Nhân viên giao hàng", "Xem sản phẩm, xem và cập nhật trạng thái đơn hàng");
		
		Role roleAssistant = new Role("Cộng tác viên", "Quản lí câu hỏi và bình luận");
		
		roleRepository.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
	}
	
	@Test
	public void testGetAllRoles() {
		List<Role> roles = roleRepository.findAll();
		roles.forEach(role -> System.out.println(role.getName() + " - " + role.getDescription()));
	}
}
