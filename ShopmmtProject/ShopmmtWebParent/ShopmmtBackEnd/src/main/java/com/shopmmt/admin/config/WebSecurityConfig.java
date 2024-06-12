package com.shopmmt.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopmmt.admin.services.impl.ShopmmtUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	UserDetailsService userDetailsService() {
		return new ShopmmtUserDetailsService();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/users/**", "/settings/**").hasAuthority("Admin")
				.requestMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Nhân viên kho hàng")

				.requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Nhân viên kho hàng")

				.requestMatchers("/products/showEdit/**", "/products/edit/**", "/products/save",
						"/products/check_product_name", "/products/check_product_details")
				.hasAnyAuthority("Admin", "Nhân viên kho hàng", "Nhân viên bán hàng")

				.requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
				.hasAnyAuthority("Admin", "Nhân viên kho hàng", "Nhân viên bán hàng", "Nhân viên giao hàng")

				.requestMatchers("/products/**").hasAnyAuthority("Admin", "Nhân viên kho hàng")
				
				.requestMatchers("/customers/**", "/orders/**").hasAnyAuthority("Admin", "Nhân viên bán hàng")

				.requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**", "/fontawesome/**", "/fonts/**",
						"/webfonts/**")
				.permitAll()
				
				.anyRequest().authenticated())
		
				.formLogin(login -> login.loginPage("/login").usernameParameter("email").defaultSuccessUrl("/")
						.permitAll())
				.logout(logout -> logout.permitAll())
				.rememberMe(rem -> rem.key("AbcDefgHijKlmnOpqrs_1234567890").tokenValiditySeconds(7 * 24 * 60 * 60));

		return http.build();
	}

//	@Bean
//	WebSecurityCustomizer webSecurityCustomizer() {
//		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**",
//				"/fontawesome/**", "/fonts/**", "/webfonts/**");
//	}

}
