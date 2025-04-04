package com.quizzbattle.ws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.DispatcherType;

@Configuration
public class WebSecurityConfiguration {
	protected static final String[] PUBLIC_URLS = { "/", "/users/save" };
	protected static final String[] AUTH_URLS = { "/users/**", "/friendship/**", "/games/**" };

	@Autowired
	private AuthenticationFilter authenticationFilter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, final UserDetailsService userDetailsService)
			throws Exception {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());

		return new ProviderManager(authenticationProvider);
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager
	// , final JwtAuthorizationFilter jwtAuthorizationFilter
	) throws Exception {

		/*
		 * return http.csrf(csrf -> csrf.disable())
		 * .authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
		 * .dispatcherTypeMatchers(DispatcherType.FORWARD,
		 * DispatcherType.ERROR).permitAll()
		 * .requestMatchers(PUBLIC_URLS).permitAll().requestMatchers(ADMIN_URLS).hasRole
		 * (User.ADMIN) .requestMatchers(USER_URLS).hasAnyRole(User.ADMIN,
		 * User.CLIENT).anyRequest().permitAll()) .sessionManagement(management ->
		 * management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		 * .addFilterAfter(authenticationFilter,
		 * UsernamePasswordAuthenticationFilter.class).build();
		 */
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
						.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
						.requestMatchers(PUBLIC_URLS).permitAll().requestMatchers(AUTH_URLS).authenticated()
						.anyRequest().permitAll())
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class).build();

	}
}
