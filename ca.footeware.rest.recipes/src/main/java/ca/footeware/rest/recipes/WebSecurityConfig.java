/*******************************************************************************
 * Copyright (c) 2016 Footeware.ca
 *******************************************************************************/
package ca.footeware.rest.recipes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Specify the URLs to which the current user can access and those that are
 * forwarded to login.
 *
 * @author Footeware.ca
 */
@Configuration
public class WebSecurityConfig {

	/**
	 * A bean that configures HTTP security.
	 *
	 * @param http {@link HttpSecurity}
	 * @return {@link SecurityFilterChain}
	 * @throws Exception when the internet falls over.
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests().requestMatchers("/recipes/**").hasRole("USER").and().httpBasic();
		return http.build();
	}

	/**
	 * Use BCrypt for password encoding.
	 *
	 * @return {@link PasswordEncoder}
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 * Username and password.
	 *
	 * @return {@link InMemoryUserDetailsManager}
	 */
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.withUsername("craig").password(passwordEncoder().encode("chocolate")).roles("USER")
				.build();
		return new InMemoryUserDetailsManager(user);
	}
}