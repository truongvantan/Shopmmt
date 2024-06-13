package com.shopmmt.site.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopmmt.site.config.oauth.OAuth2LoginSuccessHandler;
import com.shopmmt.site.services.impl.CustomerOAuth2UserService;
import com.shopmmt.site.services.impl.CustomerUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private CustomerOAuth2UserService customerOAuth2UserService;

	@Bean
	DatabaseLoginSuccessHandler databaseLoginHandler() {
		return new DatabaseLoginSuccessHandler();
	}

	@Bean
	OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
		return new OAuth2LoginSuccessHandler();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
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
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**", "/fontawesome/**", "/fonts/**",
						"/webfonts/**")
				.permitAll()

				.requestMatchers("/account_details", "/update_account_details", "/cart", "/address_book/**",
						"/checkout", "/place_order", "/process_paypal_order").authenticated()

				.anyRequest().permitAll())

				.formLogin(login -> login.loginPage("/login").usernameParameter("email")
						.successHandler(databaseLoginHandler()).permitAll())

				.oauth2Login(oauth2 -> oauth2.loginPage("/login")
						.userInfoEndpoint(userInfo -> userInfo.userService(customerOAuth2UserService))
						.successHandler(oAuth2LoginSuccessHandler()))

				.logout(logout -> logout.permitAll())
				.rememberMe(rem -> rem.key("AbcDefgHijKlmnOpqrs_1234567890").tokenValiditySeconds(7 * 24 * 60 * 60));

		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

		return http.build();
	}

//	@Bean
//	WebSecurityCustomizer webSecurityCustomizer() {
//		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**",
//				"/fontawesome/**", "/fonts/**", "/webfonts/**");
//	}

}
