package com.shopmmt.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopmmt.admin.services.impl.ShopmmtUserDetailsService;

@Configuration
public class WebSecurityConfig {

	@Bean
	UserDetailsService userDetailsService() {
		return new ShopmmtUserDetailsService();
	}

//	@Bean
//	SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
//				.formLogin(login -> login.loginPage("/login").usernameParameter("email").defaultSuccessUrl("/users").permitAll())
//				.logout(logout -> logout.logoutSuccessUrl("/").permitAll());
//		return http.build();
//	}

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
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/users").authenticated().anyRequest().permitAll())
				.formLogin(login -> login.loginPage("/login").usernameParameter("email").defaultSuccessUrl("/users").permitAll())
				.logout(logout -> logout.logoutSuccessUrl("/").permitAll());

		return http.build();
	}
	
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**", "/fontawesome/**", "/fonts/**", "/webfonts/**");
	}
}
